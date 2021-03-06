package org.jboss.jmodinfgen.core.util;

import org.jboss.jmodinfgen.core.model.DotFileModel;
import org.jboss.jmodinfgen.core.model.ModuleModel;
import org.jboss.jmodinfgen.core.model.ServicesModel;
import org.jboss.jmodinfgen.module.info.directives.ExportsDirective;
import org.jboss.jmodinfgen.module.info.directives.ModuleDirective;
import org.jboss.jmodinfgen.module.info.directives.ModuleInfoDeclaration;
import org.jboss.jmodinfgen.module.info.directives.ProvidesDirective;
import org.jboss.jmodinfgen.module.info.directives.RequiresDirective;

import java.util.ArrayList;
import java.util.HashSet;
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
   private boolean verbose = false;

   public Unification(ModuleModel mModel) {
      this.mModel = mModel;
   }

   public void setVerbose(boolean flag) {
      verbose = flag;
   }

   public void process() {
      final ModuleInfoDeclaration mInfoDecl = mModel.getModuleInfoModel();
      final DotFileModel dFileModel = mModel.getDotFileModel();

      processExports(mInfoDecl, dFileModel);
      processRequires(mInfoDecl, dFileModel);
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

   private void processRequires(ModuleInfoDeclaration mInfoDecl, DotFileModel dFileModel) {

      if (dFileModel != null) {

         Set<String> modInfKeys = (mInfoDecl == null) ? new HashSet() : mInfoDecl.getRequiresMap().keySet();

         for (Map.Entry<String, TreeSet<String>> entry : dFileModel.getRequiredModuleNames().entrySet())
         {
            String moduleName = entry.getKey();
            if (modInfKeys.contains(moduleName)) {
               // existing stmt from module-info file takes precedence because it might have a modifier set
               RequiresDirective eDirective = (RequiresDirective)mInfoDecl.getRequiresMap().get(moduleName);
               requiresMap.put(moduleName, eDirective);
            } else
            {
               if (!dFileModel.getModuleName().equals(moduleName))
               {
                  RequiresDirective eDirective = new RequiresDirective();
                  eDirective.setName(moduleName);
                  if (entry.getValue() != null)
                  {
                     eDirective.getModuleNameList().addAll(entry.getValue());
                  }
                  requiresMap.put(moduleName, eDirective);
               }
            }
         }
         unresolveRequiresPackageNamesList.addAll(dFileModel.getExtPackages());
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

   public String getModuleInfoStart () {

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

      return "module " + moduleName + " {\n" ;
   }

   public String getModuleInfoEnd() {
      return "\n}";
   }

   public String getExportsDeclarations() {
      StringBuilder sb = new StringBuilder();
      for (ModuleDirective m : exportsMap.values())
      {
         sb.append(m.toString() + "\n");
      }
      return sb.toString();
   }

   public String getRequiresDeclarations() {
      StringBuilder sb = new StringBuilder();
      for (RequiresDirective m : requiresMap.values())
      {
         sb.append(m.toString() + "\n");
         if (verbose)
         {
            sb.append(m.toStringReferencedPackagesComment());
         }
      }
      return sb.toString();
   }

   public String getOpensDeclarations() {
      StringBuilder sb = new StringBuilder();
      if (mModel.getModuleInfoModel() != null)
      {
         for (ModuleDirective m : mModel.getModuleInfoModel().getOpensMap().values())
         {
            sb.append(m.toString() + "\n");
         }
      }
      return sb.toString();
   }

   public String getUsesDeclarations() {
      StringBuilder sb = new StringBuilder();
      if (mModel.getModuleInfoModel() != null)
      {
         for (ModuleDirective m : mModel.getModuleInfoModel().getUsesMap().values())
         {
            sb.append(m.toString() +"\n");
         }
      }
      return sb.toString();
   }

   public String getProvidesDeclarations() {
      StringBuilder sb = new StringBuilder();
      for (ModuleDirective m : providesMap.values())
      {
         sb.append(m.toString() + "\n");
      }
      return sb.toString();
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


      System.out.println(toStringUnrsolvedRequiredPackages());
      System.out.println("}");
   }

   public String toStringUnrsolvedRequiredPackages() {
      StringBuilder sb = new StringBuilder();
      if (!unresolveRequiresPackageNamesList.isEmpty())
      {
         sb.append("\n/**\n");
         sb.append("\tThe module names for these packages are unknown.\n");
         sb.append("\tUser intervention may be needed.\n");
         for (String pkgName : unresolveRequiresPackageNamesList)
         {
            sb.append("\t\trequires " + pkgName + ";\n");
         }
         sb.append("**/\n");
      }
      return sb.toString();
   }
}
