package org.jboss.core.util;

import org.jboss.core.model.ModuleModel;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by rsearls on 10/11/17.
 */
public class DumpIt {

   public static void dumpModel (ModuleModel mm) {
      System.out.format("--root: %s \n",
              (mm.getRootDir() != null)? mm.getRootDir().getAbsolutePath() : "NULL");
      System.out.format("\tdot: %s \n",
              (mm.getDotFile() != null)? mm.getDotFile().getAbsolutePath() : "NULL");
      System.out.format("\tinfo: %s \n",
              (mm.getModuleInfoFile() != null)? mm.getModuleInfoFile().getAbsolutePath() : "NULL");
      for(File f : mm.getServicesFileList()) {
         System.out.format("\tservice: %s \n", f.getAbsolutePath());
      }
      System.out.println();
   }

   public static void dumpExternalPackages(String moduleName, TreeMap<String, TreeSet<String>> externalPackages) {
      System.out.printf("-- summary (%s) --\n", moduleName);
      System.out.println("-- external packages ref by other modules --");
      //for (String s: externalPackages.keySet()) {
      //   System.out.println(s);
      //}

      for(Map.Entry<String,TreeSet<String>> entry: externalPackages.entrySet()) {
         System.out.println(entry.getKey());
         for (String value: entry.getValue()) {
            System.out.printf("\t%s (requires this pkg)\n", value);
         }
      }
   }


   public static void dumpInternalPackages(String moduleName, TreeMap<String, TreeSet<String>> internalPackages) {
      System.out.printf("-- summary (%s) --\n", moduleName);
      System.out.println("-- internal package reference by other modules list --");
      //for (String s: externalPackages.keySet()) {
      //   System.out.println(s);
      //}

      for(Map.Entry<String,TreeSet<String>> entry: internalPackages.entrySet()) {
         System.out.println(entry.getKey());
         for (String value: entry.getValue()) {
            System.out.printf("\t%s (requires this pkg)\n", value);
         }
      }
   }
}
