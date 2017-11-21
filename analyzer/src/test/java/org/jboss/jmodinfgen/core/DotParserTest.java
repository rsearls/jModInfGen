package org.jboss.jmodinfgen.core;

import org.jboss.jmodinfgen.core.model.DotFileModel;
import org.jboss.jmodinfgen.core.parser.DotFileParser;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rsearls on 10/29/17.
 */
public class DotParserTest {

   private static File dotFile;

   private static List<String> internalPkgControl = new ArrayList<>();
   static {
      internalPkgControl.add("org.jboss.wsf.stack.cxf");
      internalPkgControl.add("org.jboss.wsf.stack.cxf.client");
      internalPkgControl.add("org.jboss.wsf.stack.cxf.client.configuration");
      internalPkgControl.add("org.jboss.wsf.stack.cxf.client.injection");
      internalPkgControl.add("org.jboss.wsf.stack.cxf.client.serviceref");
      internalPkgControl.add("org.jboss.wsf.stack.cxf.extensions.addressing.map");
      internalPkgControl.add("org.jboss.wsf.stack.cxf.extensions.policy");
      internalPkgControl.add("org.jboss.wsf.stack.cxf.extensions.security");
      internalPkgControl.add("org.jboss.wsf.stack.cxf.saaj");
      internalPkgControl.add("org.jboss.wsf.stack.cxf.tools");
   }

   @BeforeClass
   static public void beforeClass() {

      dotFile = new File("./src/test/resources/filefinder/client/target/gen-jdeps/classes.dot");
      if (!dotFile.canRead())
      {
         throw new IllegalArgumentException("File not found: " + dotFile.getAbsolutePath());
      }
   }

   @Test
   public void parserTest() {

      DotFileParser dotParser = new DotFileParser();
      DotFileModel dotFileModel = dotParser.parse(dotFile);

      Assert.assertEquals(dotFileModel.getInternalPackages().keySet().size(), internalPkgControl.size());

      for (String s : dotFileModel.getInternalPackages().keySet()) {
         if (internalPkgControl.contains(s)) {
            internalPkgControl.remove(s);
         }
      }

      Assert.assertEquals(0, internalPkgControl.size());
      Assert.assertEquals("External package count is wrong",92, dotFileModel.getExternalPackages().size());
   }
}
