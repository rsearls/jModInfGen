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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

   public Unification(ModuleModel mModel) {
      this.mModel = mModel;
   }

   public void process() {
      final ModuleInfoDeclaration mInfoDecl = mModel.getModuleInfoModel();
      final DotFileModel dFileModel = mModel.getDotFileModel();

      processExports(mInfoDecl, dFileModel);
      processRequires(dFileModel);
      processProvides(mModel.getServicesModelList());
   }

   private void processExports(ModuleInfoDeclaration mInfoDecl, DotFileModel dFileModel) {

      if (mInfoDecl != null && dFileModel != null)
      {
         // Any exports listed in the pre-existing module-info file and is not found in the list
         // of current exports from the jdeps analysis are obsolete and are no longer retained.
         // Any packages listed in the jdeps analysis but not on the module-info list are new
         // and are added to the list.
         Set<String> dotKeys = dFileModel.getInternalPackages().keySet();
         for (Map.Entry<String, ModuleDirective> entry : mInfoDecl.getExportsMap().entrySet())
         {
            if (dotKeys.contains(entry.getKey()))
            {
               exportsMap.put(entry.getKey(), (ExportsDirective) entry.getValue());
            }
         }

         Set<String> exportKeys = exportsMap.keySet();
         for (Map.Entry<String, TreeSet<String>> entry : dFileModel.getInternalPackages().entrySet())
         {
            if (!exportKeys.contains(entry.getKey()))
            {
               String key = entry.getKey();
               ExportsDirective eDirective = new ExportsDirective();
               eDirective.setName(key);
               exportsMap.put(key, eDirective);
            }
         }
      } else if (mInfoDecl == null && dFileModel != null)
      {
         // There is no module-info file.  The are packages that are to be exported from the module.
         for (Map.Entry<String, TreeSet<String>> entry : dFileModel.getInternalPackages().entrySet())
         {
            String key = entry.getKey();
            ExportsDirective eDirective = new ExportsDirective();
            eDirective.setName(key);
            exportsMap.put(key, eDirective);
         }
      } else if (mInfoDecl != null && dFileModel == null)
      {
         // no packages are being exported from this module.  Any exports from the pre-existing
         // module-info file are obsolete.  Do not list them.
      }
   }

   private void processRequires(DotFileModel dFileModel) {

      if (dFileModel != null) {
         for (Map.Entry<String, TreeSet<String>> entry : dFileModel.getRequiredModuleNames().entrySet())
         {
            String moduleName = entry.getKey();
            RequiresDirective eDirective = new RequiresDirective();
            eDirective.setName(moduleName);
            if (entry.getValue() != null)
            {
               eDirective.getModuleNameList().addAll(entry.getValue());
            }
            requiresMap.put(moduleName, eDirective);
         }
         unresolveRequiresPackageNamesList.addAll(dFileModel.getExpPackages());
      }
   }


   private void processProvides(List<ServicesModel> servicesModelList) {

      if (servicesModelList != null)
      {
         for (ServicesModel sModel : servicesModelList)
         {
            providesMap.put(sModel.getServiceFileName(), sModel);
         }
      }
   }


   public void print() {

      String moduleName = null;
      if (mModel.getModuleInfoModel() != null)
      {
         moduleName = mModel.getModuleInfoModel().getName();
      } else if (mModel.getDotFileModel() != null)
      {
         moduleName = mModel.getDotFileModel().getModuleName();
      }
      if (moduleName == null)
      {
         moduleName = mModel.getRootDir().getName();
      }
      if (moduleName == null)
      {
         moduleName = "unknown-Module-Name";
      }

      System.out.println("\n----");
      System.out.printf("MODULE %s {\n", moduleName);

      System.out.println();
      for (ModuleDirective m : exportsMap.values())
      {
         m.print();
      }

      System.out.println();
      for (RequiresDirective m : requiresMap.values())
      {
         m.print();
         m.printReferencedPackagesComment();
      }

      System.out.println();
      printUnrsolvedRequiredPackages();

      if (mModel.getModuleInfoModel() != null)
      {
         System.out.println();
         for (ModuleDirective m : mModel.getModuleInfoModel().getOpensMap().values())
         {
            m.print();
         }

         System.out.println();
         for (ModuleDirective m : mModel.getModuleInfoModel().getUsesMap().values())
         {
            m.print();
         }
      }

      System.out.println();
      for (ModuleDirective m : providesMap.values())
      {
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
