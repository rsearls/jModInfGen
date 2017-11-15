package org.jboss.jmodinfgen.module.info;

import org.jboss.jmodinfgen.module.info.directives.ExportsDirective;
import org.jboss.jmodinfgen.module.info.directives.ModuleDirective;
import org.jboss.jmodinfgen.module.info.directives.ModuleInfoDeclaration;
import org.jboss.jmodinfgen.module.info.directives.OpensDirective;
import org.jboss.jmodinfgen.module.info.directives.ProvidesDirective;
import org.jboss.jmodinfgen.module.info.directives.RequiresDirective;
import org.jboss.jmodinfgen.module.info.directives.UsesDirective;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DirectivesTest {

   private static ModuleInfoDeclaration mInfoDecl;

   private static List<String> exportsControl = new ArrayList<>();
   static {
      exportsControl.add("exports one.org.my.test.pkg to \n\t\tdodge.dart,\n\t\tchevy.truck;");
      exportsControl.add("exports oneOne.org.my.test.pkg;");
      exportsControl.add("exports mypkg;");
   };

   private static List<String> requiresControl = new ArrayList<>();
   static {
      requiresControl.add("requires two.org.my.test.pkg;");
      requiresControl.add("requires three.org.my.test.pkg;");
      requiresControl.add("requires transitive six.org.my.test.pkg;");
      requiresControl.add("requires static seven.org.my.test.pkg;");
   };

   private static List<String> providesControl = new ArrayList<>();
   static {
      providesControl.add("provides fiveOne.org.test.pkg with \n\t\tgreen.beans;");
      providesControl.add("provides five.org.test.pkg with \n\t\tgreen.beans,\n\t\torange.carrots,\n\t\tred.potatoes;");
   };


   private static List<String> usesControl = new ArrayList<>();
   static {
      usesControl.add("uses tooth.brush;");
   };


   private static List<String> opensControl = new ArrayList<>();
   static {
      opensControl.add("opens four.org.test.pkg to \n\t\tford.escort;");
      opensControl.add("opens eight.org.test.pkg;");
   };

   @BeforeClass
   static public void beforeClass()
   {
      File inFile = new File("./target/test-classes/module-info.java");
      if (!inFile.canRead()) {
         throw new IllegalArgumentException("File not found: " + inFile.getAbsolutePath());
      }

      mInfoDecl = new ModuleInfoDeclaration();
      mInfoDecl.parse(inFile);
   }


   @Test
   public void exportsDirectiveTest() throws Exception {

      Assert.assertEquals(exportsControl.size(), mInfoDecl.getExportsMap().size());

      List<String> controlGroup = new ArrayList<>();
      controlGroup.addAll(exportsControl);

      for (Map.Entry<String, ModuleDirective> entry: mInfoDecl.getExportsMap().entrySet()) {
         String key = entry.getKey();
         ExportsDirective value = (ExportsDirective)entry.getValue();

         if (controlGroup.contains(value.toString())) {
            controlGroup.remove(value.toString());
         }
      }

      Assert.assertEquals(0, controlGroup.size());
   }

   @Test
   public void requiresDirectiveTest() throws Exception {

      Assert.assertEquals(requiresControl.size(), mInfoDecl.getRequiresMap().size());

      List<String> controlGroup = new ArrayList<>();
      controlGroup.addAll(requiresControl);

      for (Map.Entry<String, ModuleDirective> entry: mInfoDecl.getRequiresMap().entrySet()) {
         String key = entry.getKey();
         RequiresDirective value = (RequiresDirective)entry.getValue();

         if (controlGroup.contains(value.toString())) {
            controlGroup.remove(value.toString());
         }
      }

      Assert.assertEquals(0, controlGroup.size());
   }

   @Test
   public void providesDirectiveTest() throws Exception {

      Assert.assertEquals(providesControl.size(), mInfoDecl.getProvidesMap().size());

      List<String> controlGroup = new ArrayList<>();
      controlGroup.addAll(providesControl);

      for (Map.Entry<String, ModuleDirective> entry: mInfoDecl.getProvidesMap().entrySet()) {
         String key = entry.getKey();
         ProvidesDirective value = (ProvidesDirective)entry.getValue();

         if (controlGroup.contains(value.toString())) {
            controlGroup.remove(value.toString());
         }
      }

      Assert.assertEquals(0, controlGroup.size());
   }

   @Test
   public void usersDirectiveTest() throws Exception {

      Assert.assertEquals(usesControl.size(), mInfoDecl.getUsesMap().size());

      List<String> controlGroup = new ArrayList<>();
      controlGroup.addAll(usesControl);

      for (Map.Entry<String, ModuleDirective> entry: mInfoDecl.getUsesMap().entrySet()) {
         String key = entry.getKey();
         UsesDirective value = (UsesDirective)entry.getValue();

         if (controlGroup.contains(value.toString())) {
            controlGroup.remove(value.toString());
         }
      }

      Assert.assertEquals(0, controlGroup.size());
   }

   @Test
   public void opensDirectiveTest() throws Exception {

      Assert.assertEquals(opensControl.size(), mInfoDecl.getOpensMap().size());

      List<String> controlGroup = new ArrayList<>();
      controlGroup.addAll(opensControl);

      for (Map.Entry<String, ModuleDirective> entry: mInfoDecl.getOpensMap().entrySet()) {
         String key = entry.getKey();
         OpensDirective value = (OpensDirective)entry.getValue();

         if (controlGroup.contains(value.toString())) {
            controlGroup.remove(value.toString());
         }
      }

      Assert.assertEquals(0, controlGroup.size());
   }
}