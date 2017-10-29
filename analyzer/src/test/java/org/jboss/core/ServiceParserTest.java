package org.jboss.core;

import org.jboss.core.model.ServicesModel;
import org.jboss.core.parser.ServiceFileParser;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;

/**
 * Created by rsearls on 10/29/17.
 */
public class ServiceParserTest {

   private static File serviceFile;
   private final String filenameControl = "org.jboss.wsf.spi.management.StackConfigFactory";
   private final String serviceControl = "org.jboss.wsf.stack.cxf.config.CXFStackConfigFactory";

   @BeforeClass
   static public void beforeClass() {

      serviceFile = new File("./src/test/resources/filefinder/server/target/classes/META-INF/services/org.jboss.wsf.spi.management.StackConfigFactory");
      if (!serviceFile.canRead())
      {
         throw new IllegalArgumentException("File not found: " + serviceFile.getAbsolutePath());
      }
   }

   @Test
   public void parserTest() {
      ServiceFileParser serviceFileParser = new ServiceFileParser();
      ServicesModel sModel = serviceFileParser.parse(serviceFile);

      Assert.assertNotNull(sModel);
      Assert.assertEquals(1, sModel.getServicesList().size());
      Assert.assertEquals(filenameControl, sModel.getServiceFile().getName());
      Assert.assertEquals(serviceControl, sModel.getServicesList().get(0));
   }

}
