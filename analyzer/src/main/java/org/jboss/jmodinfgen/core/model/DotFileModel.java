package org.jboss.jmodinfgen.core.model;

import java.io.File;
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

   // external packages for which a moduleName has not been found but which are still needed.
   private TreeSet<String> extPackages = new TreeSet<>();

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

   public void setModuleDir(File moduleDir) {
      this.moduleDir = moduleDir;
   }

   public TreeMap<String, TreeSet<String>> getInternalPackages() {
      return internalPackages;
   }

   public TreeMap<String, TreeSet<String>> getExternalPackages() {
      return externalPackages;
   }

   public void setExtPackages(TreeSet<String> extPackages) {
      this.extPackages.addAll(extPackages);
   }

   public TreeSet<String> getExtPackages() {
      return extPackages;
   }

   public TreeMap<String, TreeSet<String>> getRequiredModuleNames() {
      return requiredModuleNames;
   }

   public void setDotFile(File dotFile) {
      this.dotFile = dotFile;
   }

   /** -------------------------------------------------- **/

   public String internalPackageLookup (String s) {
      return internalPackages.containsKey(s)? s : null ;
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

}
