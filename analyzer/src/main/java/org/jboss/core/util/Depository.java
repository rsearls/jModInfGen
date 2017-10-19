package org.jboss.core.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

/**
 * Temp utility for getting module names of packages outside Resteasy.
 * TODO replace with lookup utility in future
 *
 * Created by rsearls on 10/18/17.
 */
public class Depository
{

   private static HashMap<String, String> thirdPartyPackageSet = new HashMap<String, String>();
   static {
      thirdPartyPackageSet.put("java.io","java.base");
      thirdPartyPackageSet.put("java.lang","java.base");
      thirdPartyPackageSet.put("java.lang.annotation","java.base");
      thirdPartyPackageSet.put("java.lang.management","java.base");
      thirdPartyPackageSet.put("java.lang.ref","java.base");
      thirdPartyPackageSet.put("java.lang.reflect","java.base");
      thirdPartyPackageSet.put("java.math","java.base");
      thirdPartyPackageSet.put("java.net","java.base");
      thirdPartyPackageSet.put("java.nio","java.base");
      thirdPartyPackageSet.put("java.nio.channels","java.base");
      thirdPartyPackageSet.put("java.nio.charset","java.base");
      thirdPartyPackageSet.put("java.security","java.base");
      thirdPartyPackageSet.put("java.text","java.base");
      thirdPartyPackageSet.put("java.util","java.base");
      thirdPartyPackageSet.put("java.util.concurrent","java.base");
      thirdPartyPackageSet.put("java.util.concurrent.atomic","java.base");
      thirdPartyPackageSet.put("java.util.logging","java.base");
      thirdPartyPackageSet.put("java.util.regex","java.base");
      thirdPartyPackageSet.put("javax.activation","java.activation");
      thirdPartyPackageSet.put("java.awt.image","java.desktop");
      thirdPartyPackageSet.put("java.beans","java.desktop");
      thirdPartyPackageSet.put("javax.imageio","java.desktop");
      thirdPartyPackageSet.put("javax.imageio.metadata","java.desktop");
      thirdPartyPackageSet.put("javax.imageio.plugins.jpeg","java.desktop");
      thirdPartyPackageSet.put("javax.imageio.stream","java.desktop");
      thirdPartyPackageSet.put("javax.naming","java.naming");
      thirdPartyPackageSet.put("javax.xml","java.xml");
      thirdPartyPackageSet.put("javax.xml.parsers","java.xml");
      thirdPartyPackageSet.put("javax.xml.transform","java.xml");
      thirdPartyPackageSet.put("javax.xml.transform.dom","java.xml");
      thirdPartyPackageSet.put("javax.xml.transform.sax","java.xml");
      thirdPartyPackageSet.put("javax.xml.transform.stream","java.xml");
      thirdPartyPackageSet.put("org.w3c.dom","java.xml");
      thirdPartyPackageSet.put("org.xml.sax","java.xml");
      thirdPartyPackageSet.put("javax.xml.bind","java.xml.bind");
      thirdPartyPackageSet.put("javax.xml.bind.annotation","java.xml.bind");
      thirdPartyPackageSet.put("javax.xml.bind.attachment","java.xml.bind");
   }

   public static String getModuleName (String pkg) {
      return thirdPartyPackageSet.get(pkg);
   }

   public static boolean isJavaBaseModule (String pkg) {
      String value = thirdPartyPackageSet.get(pkg);
      if (value == null) {
         return false;
      }
      return "java.base".equals(thirdPartyPackageSet.get(pkg));
   }

   // todo replace other code.
   public static void testPropertiesFileRead() {

      HashMap<String, String> TMPthirdPartyPackageSet = new HashMap<String, String>();
      Properties properties = new Properties();
      try
      {
         File f = new File("src/com/jboss/core/DepositoryData.properties");
         String s = f.getAbsolutePath();
         InputStream input = new FileInputStream("src/com/jboss/core/DepositoryData.properties");
         properties.load(input);
         Set<String> keys = properties.stringPropertyNames();

         for (String k : keys) {
            TMPthirdPartyPackageSet.put(k, properties.getProperty(k));
            System.out.format("prop: %s    %s  \n", k, properties.getProperty(k));
         }
         String waitHere = "";
      } catch(FileNotFoundException e) {
         System.out.println(e);
      } catch (IOException ioe) {
         System.out.println(ioe);
      }
   }
}
