package org.jboss.core;

import org.jboss.core.util.Depository;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by rsearls on 10/30/17.
 */
public class DepositoryTest {

   private static File propertyFile;

   private static final String ORG_APACHE_CXF_ANNOTATIONS = "org.apache.cxf.annotations";
   private static final String ORG_APACHE_CXF_BINDING = "org.apache.cxf.binding";
   private static final String ORG_APACHE_CXF_BUS ="org.apache.cxf.bus";
   private static final String MY_APACHE_CXF = "my.apache.cxf";
   private static final String NOT_MY_APACHE_CXF = "not.my.apache.cxf";

   private static List<String> modulenameControl = new ArrayList<>();
   static {
      modulenameControl.add(MY_APACHE_CXF);
      modulenameControl.add(NOT_MY_APACHE_CXF);
   };

   @BeforeClass
   static public void beforeClass() {

      propertyFile = new File("./src/test/resources/filefinder/client/apacheMappings.properties");
      if (!propertyFile.canRead())
      {
         throw new IllegalArgumentException("File not found: " + propertyFile.getAbsolutePath());
      }
   }

   @Test
   public void fileLoadTest() throws Exception {
      Depository.loadFile(propertyFile);
      String m = Depository.getModuleName(ORG_APACHE_CXF_ANNOTATIONS);
      Assert.assertEquals(MY_APACHE_CXF, m);
      Assert.assertEquals(MY_APACHE_CXF,
              Depository.getModuleName(ORG_APACHE_CXF_BINDING));
   }

   @Test
   public void registerTest()  throws Exception {
      Depository.register(ORG_APACHE_CXF_BUS, MY_APACHE_CXF);
      Assert.assertEquals(MY_APACHE_CXF,
              Depository.getModuleName(ORG_APACHE_CXF_BUS));

   }

   @Test
   public void duplicatePackageTest()  throws Exception {
      Depository.register(ORG_APACHE_CXF_BUS, NOT_MY_APACHE_CXF);
      Assert.assertEquals(1, Depository.getDuplicatePackageList().size());

      Assert.assertTrue(Depository.getDuplicatePackageList().keySet().contains(ORG_APACHE_CXF_BUS));

      for (Map.Entry<String, List<String>> entry :
              Depository.getDuplicatePackageList().entrySet())
      {
         for (String s : entry.getValue())
         {
            if (modulenameControl.contains(s))
            {
                  modulenameControl.remove(s);
            }
         }
      }

      Assert.assertEquals(0, modulenameControl.size());
   }
}
