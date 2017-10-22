package org.jboss.core.util;

import org.jboss.core.model.DotFileModel;
import org.jboss.core.model.ModuleModel;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by rsearls on 10/13/17.
 */
public class Analyzer
{
   private List<DotFileModel> dotModelList = new ArrayList<>();

   public Analyzer( List<ModuleModel> mModelList) {

      // create master ref list
      for(ModuleModel mModel: mModelList) {
         DotFileModel dotFileModel = mModel.getDotFileModel();
         if (dotFileModel != null) {
            dotModelList.add(dotFileModel);
         }
      }

   }

   public void analyze() {

      for(DotFileModel dFile: dotModelList) {
         depositoryRegistation(dFile);
      }

      // check all references against each other
      for(DotFileModel dFile: dotModelList) {
         TreeSet<String> extPackages = checkDepository(dFile);
         dFile.setExtPackages(extPackages);
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
}
