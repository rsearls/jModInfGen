package org.jboss.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 *
 * Created by rsearls on 10/18/17.
 */
public class Depository {

   private static HashMap<String, List<String>> duplicatePackageList = new HashMap<>();
   private static HashMap<String, String> pkgModuleMapping = new HashMap<String, String>();

   static
   {
      try
      {
         // initial information is packages and module mappings of JDK
         Properties properties = new Properties();
         InputStream input = Depository.class.getClassLoader()
                 .getResourceAsStream("DepositoryData.properties");

         properties.load(input);
         Set<String> keys = properties.stringPropertyNames();

         for (String k : keys)
         {
            pkgModuleMapping.put(k, properties.getProperty(k));
            //System.out.format("prop: %s    %s  \n", k, properties.getProperty(k));
         }
      } catch (IOException ioe)
      {
         System.out.println(ioe);
      }
   }


   public static String getModuleName(String pkg) {
      return pkgModuleMapping.get(pkg);
   }

   public static boolean isJavaBaseModule(String pkg) {
      String value = pkgModuleMapping.get(pkg);
      if (value == null)
      {
         return false;
      }
      return "java.base".equals(pkgModuleMapping.get(pkg));
   }

   /**
    * @param pkgName
    * @param moduleName
    */
   public static void register(String pkgName, String moduleName) {

      String value = pkgModuleMapping.get(pkgName);
      if (value == null)
      {
         pkgModuleMapping.put(pkgName, moduleName);
      } else
      {
         List<String> dupValues = duplicatePackageList.get(pkgName);
         if (dupValues == null)
         {
            dupValues = new ArrayList<String>();
            dupValues.add(value);
            duplicatePackageList.put(pkgName, dupValues);
         }
         dupValues.add(moduleName);
      }
   }

   public static HashMap<String, List<String>> getDuplicatePackageList() {
      return duplicatePackageList;
   }
}
