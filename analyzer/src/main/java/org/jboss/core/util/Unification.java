package org.jboss.core.util;

import org.jboss.core.model.DotFileModel;
import org.jboss.core.model.ModuleModel;
import org.jboss.core.model.ServicesModel;
import org.jboss.module.info.directives.ExportsDirective;
import org.jboss.module.info.directives.ModuleDirective;
import org.jboss.module.info.directives.ModuleInfoDeclaration;
import org.jboss.module.info.directives.ProvidesDirective;
import org.jboss.module.info.directives.RequiresDirective;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * Created by rsearls on 10/20/17.
 */
public class Unification {

   private LinkedHashMap<String, ExportsDirective> exportsMap = new LinkedHashMap<>();
   private LinkedHashMap<String, RequiresDirective> requiresMap = new LinkedHashMap<>();
   private LinkedHashMap<String, ProvidesDirective> providesMap = new LinkedHashMap<>();
   private List<String> unresolveRequiresPackageNamesList = new ArrayList<>();

   private final ModuleModel mModel;
   private final Depository depository = new Depository();

   public Unification(ModuleModel mModel) {
      this.mModel = mModel;
   }

   public void process() {
      final ModuleInfoDeclaration mInfoDecl = mModel.getModuleInfoModel();
      final DotFileModel dFileModel = mModel.getDotFileModel();

      processExports(mInfoDecl, dFileModel);
      processRequires(mInfoDecl, dFileModel);
      //processProvides(mInfoDecl, mModel.getServicesModel());
   }

   // todo how to flag new ref and removed references ENUM??
   private void processExports(ModuleInfoDeclaration mInfoDecl, DotFileModel dFileModel) {
      String moduleName = null;

      if (mInfoDecl != null)
      {
         moduleName = mInfoDecl.getName();
         // clone master
         for (Map.Entry<String, ModuleDirective> entry : mInfoDecl.getExportsMap().entrySet())
         {
            exportsMap.put(entry.getKey(), (ExportsDirective) entry.getValue());
         }
      }

      if (dFileModel != null)
      {
         if (moduleName == null || moduleName.isEmpty())
         {
            moduleName = dFileModel.getModuleName();
         }

         HashSet<String> moduleKeys = new HashSet<>();
         moduleKeys.addAll(exportsMap.keySet());
         // check dotFileModel exports against the module-info export list
         for (Map.Entry<String, TreeSet<String>> entry : dFileModel.getInternalPackages().entrySet())
         {
            if (!moduleKeys.contains(entry.getKey()))
            {
               String key = entry.getKey();
               ExportsDirective eDirective = new ExportsDirective();
               eDirective.setName(key);
               exportsMap.put(key, eDirective);
            }
         }
      }
   }

   private void processRequires(ModuleInfoDeclaration mInfoDecl, DotFileModel dFileModel) {

      if (mInfoDecl != null)
      {
         // clone master
         for (Map.Entry<String, ModuleDirective> entry : mInfoDecl.getRequiresMap().entrySet())
         {
            requiresMap.put(entry.getKey(), (RequiresDirective) entry.getValue());
         }
      }

      if (dFileModel != null)
      {
         HashSet<String> moduleKeys = new HashSet<>();
         moduleKeys.addAll(requiresMap.keySet());
         // check dotFileModel requires against the module-info export list
         for (Map.Entry<String, TreeSet<String>> entry : dFileModel.getRequiredModuleNames().entrySet())
         {
            String pkgName = entry.getKey();
            String moduleName = depository.getModuleName(pkgName);
            if (moduleName == null)
            {
               unresolveRequiresPackageNamesList.add(pkgName);
            } else
            {
               if (!moduleKeys.contains(moduleName))
               {
                  RequiresDirective eDirective = new RequiresDirective();
                  eDirective.setName(pkgName);
                  requiresMap.put(pkgName, eDirective);
               }
            }
         }
      }
   }


   private void processProvides(ModuleInfoDeclaration mInfoDecl, ServicesModel servicesModel) {

      if (mInfoDecl != null)
      {
         // clone master
         for (Map.Entry<String, ModuleDirective> entry : mInfoDecl.getProvidesMap().entrySet())
         {
            providesMap.put(entry.getKey(), (ProvidesDirective) entry.getValue());
         }
      }

      if (servicesModel != null)
      {
         HashSet<String> moduleKeys = new HashSet<>();
         moduleKeys.addAll(providesMap.keySet());
         /***
          // check dotFileModel exports against the module-info export list
          for (Map.Entry<String, TreeSet<String>> entry : dFileModel.getRequiredModuleNames().entrySet())
          {
          if (!moduleKeys.contains(entry.getKey()))
          {

          } else {
          System.out.format("providesMap contains %s\n", entry.getKey());
          }
          }
          ***/
      }
   }


   public void print() {

      String moduleName = null;
      if (mModel.getModuleInfoModel() != null) {
         moduleName = mModel.getModuleInfoModel().getName();
      } else  if (mModel.getDotFileModel() != null) {
         moduleName = mModel.getDotFileModel().getModuleName();
      }
      if (moduleName == null) {
         moduleName = mModel.getRootDir().getName();
      }
      if (moduleName == null) {
         moduleName = "unknown-Module-Name";
      }

      System.out.printf("MODULE %s {\n",moduleName);

      for (ModuleDirective m: exportsMap.values()) {
         m.print();
      }

      for (ModuleDirective m: requiresMap.values()) {
         m.print();
      }

      printUnrsolvedRequiredPackages();

      if (mModel.getModuleInfoModel() != null)
      {
         for (ModuleDirective m : mModel.getModuleInfoModel().getOpensMap().values())
         {
            m.print();
         }

         for (ModuleDirective m : mModel.getModuleInfoModel().getUsesMap().values())
         {
            m.print();
         }
      }

      for (ModuleDirective m: providesMap.values()) {
         m.print();
      }

      System.out.println("}");
   }

   private void printUnrsolvedRequiredPackages() {

      if (!unresolveRequiresPackageNamesList.isEmpty())
      {
         System.out.println("/**");
         System.out.println("\tThe module names for these packages are unknown.");
         System.out.println("\tUser intervention is needed.");
         for (String pkgName : unresolveRequiresPackageNamesList)
         {
            System.out.printf("\t\tREQUIRES %s;\n", pkgName);
         }
         System.out.println("**/");
      }
   }
}
