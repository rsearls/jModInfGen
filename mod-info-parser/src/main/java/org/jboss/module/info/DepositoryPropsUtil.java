package org.jboss.module.info;

import org.jboss.module.info.directives.ModuleInfoDeclaration;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rsearls on 10/19/17.
 */
public class DepositoryPropsUtil {

   public static void main (String [] argv) {

      String arg = "/home/rsearls/d5/jdk-9-181/jdk-9/src/";

      DepositoryPropsUtil dpu = new DepositoryPropsUtil();

      LinkedHashMap<String, String> propertiesMap = new LinkedHashMap<>();
      List<File> fList = dpu.getModuleInfoFileList(new File(arg));
      for(File f : fList) {
         ModuleInfoDeclaration mid = new ModuleInfoDeclaration();
         mid.parse(f);
         String moduleName = mid.getName();
         for (String s : mid.getExportPkgNames()) {
            //System.out.format("%s:%s\n", s, moduleName);
            propertiesMap.put(s, moduleName);
         }
      }

      // dump map
      for(Map.Entry<String,String> entry: propertiesMap.entrySet() ) {
         System.out.format("%s:%s\n", entry.getKey(), entry.getValue());
      }

   }


   public List<File> getModuleInfoFileList(File rootDir) {
      List<File> results = moduleFileSearch(rootDir, new ModuleInfoFilter());
      return results;
   }

   private List<File> moduleFileSearch (File parentDir, FileFilter filter) {
      List<File> results = new ArrayList<File>();
      for(File currentItem : parentDir.listFiles(filter)){
         if(currentItem.isDirectory()){
            results.addAll(moduleFileSearch(currentItem, filter));
         }
         else{
            results.add(currentItem);
         }
      }
      return results;
   }

   private class ModuleInfoFilter implements FileFilter {
      public boolean accept(File file) {
         return file.isDirectory() || file.getName().toLowerCase().equals("module-info.java");
      }
   }
}
