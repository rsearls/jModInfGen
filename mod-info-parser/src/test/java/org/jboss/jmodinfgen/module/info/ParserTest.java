package org.jboss.jmodinfgen.module.info;

import java.io.File;

import org.jboss.jmodinfgen.module.info.directives.ModuleInfoDeclaration;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ParserTest {

   private static File inFile;

   @BeforeClass
   static public void beforeClass()
   {
      inFile = new File("./target/test-classes/module-info.java");
      if (!inFile.canRead()) {
         throw new IllegalArgumentException("File not found: " + inFile.getAbsolutePath());
      }
   }

   @Test
   public void parseTest() throws Exception {

      ModuleInfoDeclaration mInfoDecl = new ModuleInfoDeclaration();
      mInfoDecl.parse(inFile);

      Assert.assertEquals("my.other.ModuleStmtTest", mInfoDecl.getName());
      Assert.assertEquals("exports: ", 3, mInfoDecl.getExportsMap().size());
      Assert.assertEquals("requires: ", 4, mInfoDecl.getRequiresMap().size());
      Assert.assertEquals("provides: ", 2, mInfoDecl.getProvidesMap().size());
      Assert.assertEquals("uses: ", 1, mInfoDecl.getUsesMap().size());
      Assert.assertEquals("opens: ", 2, mInfoDecl.getOpensMap().size());
   }
}