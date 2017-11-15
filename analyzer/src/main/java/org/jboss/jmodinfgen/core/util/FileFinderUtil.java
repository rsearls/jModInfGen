package org.jboss.jmodinfgen.core.util;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class FileFinderUtil {

   public List<File> getModuleList(File rootDir) {
      List<File> results = moduleFileSearch(rootDir, new ModuleRootFilter());
      return results;
   }

   public File getDotFile(File rootDir) {
      File f = singleFileSearch(rootDir, new DotFileFilter());
      return f;
   }

   public List<File> getServicesList(File rootDir) {
      List<File> results = fileSearch(rootDir, new ServicesFileFilter());
      return results;
   }

   public File getModuleInfoFile (File rootDir) {
      File f = singleFileSearch(rootDir, new ModuleInfoFilter());
      return f;
   }

   /**
    *
    * @param parentDir
    * @param filter
    * @return
    */
   private List<File> moduleFileSearch (File parentDir, FileFilter filter) {
      List<File> results = new ArrayList<File>();
      for(File currentItem : parentDir.listFiles(filter)){
         if (currentItem.getName().toLowerCase().equals("target")) {
            results.add(parentDir);
         } else  {
            results.addAll(moduleFileSearch(currentItem, filter));
         }
      }
      return results;
   }

   /**
    *
    * @param parentDir
    * @param filter
    * @return
    */
   private List<File> fileSearch (File parentDir, FileFilter filter) {
      List<File> results = new ArrayList<File>();
      for(File currentItem : parentDir.listFiles(filter)){
         if(currentItem.isDirectory()){
            results.addAll(fileSearch(currentItem, filter));
         }
         else{
            results.add(currentItem);
         }
      }
      return results;
   }


   /**
    *
    * @param parentDir
    * @param filter
    * @return
    */
   private File singleFileSearch (File parentDir, FileFilter filter) {
      File result = null;
      for(File currentItem : parentDir.listFiles(filter)){
         if(currentItem.isDirectory()){
            result = singleFileSearch(currentItem, filter);
            if (result != null) {
               return result;
            }
         }
         else {
            result = currentItem;
            break;
         }
      }
      return result;
   }



   private static final String SERVICES_PATH_FRAG = File.separator + "target"
           + File.separator + "classes"
           + File.separator + "META-INF"
           + File.separator + "services";

   private class ModuleRootFilter implements FileFilter {
      public boolean accept(File file) {
         return file.isDirectory() && !file.getName().toLowerCase().equals("src");
      }
   }

   private class DotFileFilter implements FileFilter {
      public boolean accept(File file) {
         return file.isDirectory() || file.getName().toLowerCase().equals("classes.dot");
      }
   }

   private class ServicesFileFilter implements FileFilter {
      public boolean accept(File file) {
         return file.isDirectory() ||
                 (!file.getName().endsWith(".java") &&
                         file.getParent().endsWith(SERVICES_PATH_FRAG));
      }
   }

   private class ModuleInfoFilter implements FileFilter {
      public boolean accept(File file) {
         return file.isDirectory() || file.getName().toLowerCase().equals("module-info.java");
      }
   }
}
