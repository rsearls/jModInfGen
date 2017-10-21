package org.jboss.core.util;

import org.jboss.core.model.DotFileModel;
import org.jboss.core.model.ModuleModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by rsearls on 10/13/17.
 */
public class Analyzer
{

   private List<DotFileModel> dotModelList = null;

   public Analyzer( List<ModuleModel> mModelList) {
      dotModelList = new ArrayList<>();

      // create master ref list
      for(ModuleModel mModel: mModelList) {
         DotFileModel dotFileModel = mModel.getDotFileModel();
         if (dotFileModel != null) {
            dotModelList.add(dotFileModel);
         }
      }

   }

   public void analyze() {

      // check all references against each other
      for(DotFileModel dFile: dotModelList) {
         analyzeExternalPackages(dFile, dotModelList);
         DumpIt.dumpExternalPackages(dFile.getModuleName(), dFile.getExternalPackages());
         DumpIt.dumpInternalPackages(dFile.getModuleName(), dFile.getInternalPackages());
      }
   }


   private void analyzeExternalPackages(DotFileModel dFile, List<DotFileModel> dotFileList) {

      TreeSet<String> extPackages = checkDepository(dFile);
      TreeSet<String> resolvedPackages = new TreeSet<>();

      if (!extPackages.isEmpty()) {
         for (DotFileModel d : dotFileList) {
            if (d != dFile) { // skip self
               for (String pkgName : extPackages) {
                  String pkg = d.internalPackageLookup(pkgName);
                  if (pkg != null) {
                     dFile.registerRequiredModuleName(d.getModuleName(), pkgName);
                     dFile.oldRegisterRequiredModuleName(d.getModuleName() + "; // " + pkgName);
                     d.registerInternalPackageDependency(pkgName, dFile.getModuleName());
                     dFile.registerExternalPackageDependency(pkgName, d.getModuleName());
                     resolvedPackages.add(pkgName);
                  }
               }
            }
         }

         cleanupResolvedPackages(extPackages, resolvedPackages, dFile);
         depositoryRegistation(dFile);
      }
   }


   /**
    * Register the modules internal packages and module name in the depository
    * @param dFile
    */
   private void depositoryRegistation(DotFileModel dFile) {
      String moduleName = dFile.getModuleName();
      for (String pkgName : dFile.getInternalPackages().keySet()) {
         Depository.register(pkgName, moduleName);
      }
   }

   /**
    * Resolve JDK package module mappings.  Remove successfully identified
    * packages from the list to be processed.
    *
    * @param dFile
    * @return
    */
   private TreeSet<String> checkDepository (DotFileModel dFile) {

      TreeSet<String> extPackages = new TreeSet<>();
      extPackages.addAll(dFile.getExternalPackages().keySet());

      // get 3rd party pkgs out of the way
      for (String pkgName: dFile.getExternalPackages().keySet()) {
         String moduleName = Depository.getModuleName(pkgName);
         if (moduleName != null) {
            dFile.registerRequiredModuleName(moduleName, pkgName);
            extPackages.remove(pkgName);
         }
      }
      return extPackages;
   }

   /**
    * Register 3rd party packages that need to be resolved.
    *
    * @param extPackages
    * @param resolvedPackages
    * @param dFile
    */
   private void cleanupResolvedPackages(TreeSet<String> extPackages,
                                        TreeSet<String> resolvedPackages,
                                        DotFileModel dFile) {

      for (String key: resolvedPackages) {
         extPackages.remove(key);
      }
      for (String key: dFile.getInternalPackages().keySet()) {
         extPackages.remove(key);
      }
      // register packages remaining to be resolved
      for (String pkgName : extPackages) {
         dFile.registerRequiredModuleName(pkgName, null);
      }
   }


   /******************** ------------------------------- *******************************/
   private TreeMap<String,TreeSet<String>> dupPackageNameMap = new TreeMap<String,TreeSet<String>>();

   /**
    * Check for duplicate packagename across all modules
    */
   public void duplicatePackageNameCheck() {
      List<DotFileModel> checklist = new ArrayList<>();
      checklist.addAll(dotModelList);

      for (DotFileModel d: dotModelList) {
         Set<String> keySet = d.getInternalPackages().keySet();
         for (DotFileModel checkD: checklist) {
            if (d != checkD) {
               for (String key: keySet) {
                  String s = checkD.internalPackageLookup(key);
                  if (s != null) {
                     if (dupPackageNameMap.containsKey(s)){
                        TreeSet<String> value = dupPackageNameMap.get(s);
                        value.add(checkD.getModuleName());
                     } else {
                        TreeSet<String> t = new TreeSet<String>();
                        t.add(checkD.getModuleName());
                        dupPackageNameMap.put(s, t);
                     }
                  }
               }
            }
         }
      }

      dumpDuplicatePackageNames();
   }


   private void dumpDuplicatePackageNames() {
      System.out.println("##-- duplicate package name among modules --##");
      System.out.println("##--        violates module rules         --##");
      for(Map.Entry<String, TreeSet<String>> entry: dupPackageNameMap.entrySet()) {
         System.out.printf("\t-- %s \n", entry.getKey());
         for(String v: entry.getValue()) {
            System.out.printf("\t\t-- module: %s \n", v);
         }
      }
   }
}
