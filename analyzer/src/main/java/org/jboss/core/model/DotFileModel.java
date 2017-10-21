package org.jboss.core.model;

import java.io.File;
import java.util.Date;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by rsearls on 10/11/17.
 */
public class DotFileModel {

   // package java and its subpackages lang and io are associated with the java.base module
   // and do not need to be declared.
   public static TreeSet<String> stndJDK = new TreeSet<String>();
   static {
      stndJDK.add("java.lang");
      stndJDK.add("java.io");
   }

   // Packages in this module. Potential exports
   private TreeMap<String,TreeSet<String>> internalPackages = new TreeMap<String,TreeSet<String>>();

   // Packages this module requires
   private TreeMap<String,TreeSet<String>> externalPackages = new TreeMap<String,TreeSet<String>>();


   private TreeSet<String> oldRequiredModuleNames = new TreeSet<>();

   private TreeMap<String,TreeSet<String>> requiredModuleNames = new TreeMap<String,TreeSet<String>>();

   private File dotFile = null;
   private File moduleDir = null;
   private String moduleName = null;


   public void setModuleName(String moduleName) {
      this.moduleName = moduleName;
   }

   public String getModuleName() {

      if (moduleName == null) {
         if (moduleDir == null) {
            moduleName = "module-name-unknown";
         } else {
            moduleName = moduleDir.getName();
         }
      }
      return moduleName;
   }

   public File getModuleDir() {
      return moduleDir;
   }

   public void setModuleDir(File moduleDir) {
      this.moduleDir = moduleDir;
   }

   public TreeMap<String, TreeSet<String>> getInternalPackages() {
      return internalPackages;
   }

   public void setInternalPackages(TreeMap<String, TreeSet<String>> internalPackages) {
      this.internalPackages = internalPackages;
   }

   public TreeMap<String, TreeSet<String>> getExternalPackages() {
      return externalPackages;
   }

   public void setExternalPackages(TreeMap<String, TreeSet<String>> externalPackages) {
      this.externalPackages = externalPackages;
   }

   public TreeSet<String> getOldRequiredModuleNames() {
      return oldRequiredModuleNames;
   }

   public void setOldRequiredModuleNames(TreeSet<String> oldRequiredModuleNames) {
      this.oldRequiredModuleNames = oldRequiredModuleNames;
   }

   public TreeMap<String, TreeSet<String>> getRequiredModuleNames() {
      return requiredModuleNames;
   }

   public void setRequiredModuleNames(TreeMap<String, TreeSet<String>> requiredModuleNames) {
      this.requiredModuleNames = requiredModuleNames;
   }

   public File getDotFile() {
      return dotFile;
   }

   public void setDotFile(File dotFile) {
      this.dotFile = dotFile;
   }

   /** -------------------------------------------------- **/

   public String internalPackageLookup (String s) {
      return internalPackages.containsKey(s)? s : null ;
   }


   /**
    * Register external modules that reference this package.
    * @param key
    * @param modName
    */
   public boolean registerInternalPackageDependency (String key, String modName) {
      boolean isSet = false;

      if (internalPackages.containsKey(key)) {
         TreeSet<String> valueSet = internalPackages.get(key);
         valueSet.add(modName);
         isSet = true;
      }
      return isSet;
   }


   public void registerExternalPackageDependency(String key, String modName) {

      if (externalPackages.containsKey(key)) {
         TreeSet<String> values = externalPackages.get(key);
         values.add(modName);
      } else {
         TreeSet<String> values = new TreeSet<>();
         values.add(modName);
         externalPackages.put(key, values);
      }

   }


   public void oldRegisterRequiredModuleName(String key) {
      oldRequiredModuleNames.add(key);
   }

   public void registerRequiredModuleName(String key, String pkgName) {

      TreeSet<String> values = requiredModuleNames.get(key);
      if (values == null) {
         values = new TreeSet<>();
         requiredModuleNames.put(key, values);
      }
      if (pkgName != null) {
         values.add(pkgName);
      }
   }

   // todo fix this
   public TreeMap<String,TreeSet<String>> XgetRequiredModuleNames() {
      return requiredModuleNames;
   }

}
