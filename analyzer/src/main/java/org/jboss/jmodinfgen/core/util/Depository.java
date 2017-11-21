package org.jboss.jmodinfgen.core.util;

import java.io.File;
import java.io.FileInputStream;
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

   private HashMap<String, List<String>> duplicatePackageList = new HashMap<>();
   private HashMap<String, String> pkgModuleMapping = new HashMap<String, String>();

   public Depository() {

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
   public void loadFile(File file) {
      try
      {
         Properties properties = new Properties();
         InputStream input = new FileInputStream(file);

         properties.load(input);
         Set<String> keys = properties.stringPropertyNames();

         for (String k : keys)
         {
            pkgModuleMapping.put(k, properties.getProperty(k));
         }
      } catch (IOException ioe)
      {
         System.out.println(ioe);
      }
   }

   public String getModuleName(String pkg) {
      return pkgModuleMapping.get(pkg);
   }

   /**
    * @param pkgName
    * @param moduleName
    */
   public void register(String pkgName, String moduleName) {

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

   public HashMap<String, List<String>> getDuplicatePackageList() {
      return duplicatePackageList;
   }
}
