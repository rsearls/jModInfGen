package org.jboss.core;

import org.jboss.core.model.DotFileModel;
import org.jboss.core.model.ModuleModel;
import org.jboss.core.model.ServicesModel;
import org.jboss.core.parser.DotFileParser;
import org.jboss.core.parser.ServiceFileParser;
import org.jboss.core.util.Analyzer;
import org.jboss.core.util.FileFinderUtil;
import org.jboss.core.util.Depository;
import org.jboss.core.util.Unification;
import org.jboss.module.info.directives.ModuleInfoDeclaration;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rsearls on 10/30/17.
 */
public class UnificationTest {

   private static final String openStmtControl = "opens org.jboss.wsf.stack.cxf.transport to \n\t\tmy.mod.client,\n\t\tnot.my.mod.client;\nopens org.jboss.wsf.stack.cxf.management;\n";
   private static final String exportsControl = "exports org.jboss.wsf.stack.cxf to \n\t\tmy.mod.client;";
   private static final String requiresControl = "requires transitive java.xml;";
   private static final String usesControl = "uses some.database.driver;\n";
   private static final String moduleNameControl = "my.mod.service";
   private static File inDir;

   @BeforeClass
   static public void beforeClass() {
      inDir = new File("./src/test/resources/filefinder");
      if (!inDir.canRead())
      {
         throw new IllegalArgumentException("File not found: " + inDir.getAbsolutePath());
      }
   }

   @Test
   public void contentsTest() throws Exception {

      Unification testUnification = setup();

      Assert.assertNotNull(testUnification);
      Assert.assertTrue(testUnification.getModuleInfoStart().contains(moduleNameControl));
      Assert.assertEquals(openStmtControl, testUnification.getOpensDeclarations());
      Assert.assertEquals(usesControl, testUnification.getUsesDeclarations());
      Assert.assertTrue(testUnification.getExportsDeclarations().contains(exportsControl));
      Assert.assertTrue(testUnification.getRequiresDeclarations().contains(requiresControl));
   }

   private Unification setup() {

      FileFinderUtil ffUtil = new FileFinderUtil();
      List<File> rawModules = ffUtil.getModuleList(inDir);
      List<ModuleModel> mModelList = new ArrayList<>();
      for(File file : rawModules) {
         ModuleModel mm = new ModuleModel();
         mModelList.add(mm);
         mm.setRootDir(file);
         mm.setDotFile(ffUtil.getDotFile(file));
         mm.setServicesFileList(ffUtil.getServicesList(file));
         mm.setModuleInfoFile(ffUtil.getModuleInfoFile(file));
      }

      DotFileParser dotParser = new DotFileParser();
      ServiceFileParser serviceFileParser = new ServiceFileParser();

      for (ModuleModel mm : mModelList) {
         File f = mm.getDotFile();
         DotFileModel dotFileModel = dotParser.parse(f);
         if (dotFileModel != null) {
            dotFileModel.setModuleDir(mm.getRootDir());
         }
         mm.setDotFileModel(dotFileModel);

         for(File sFile : mm.getServicesFileList()) {
            ServicesModel sModel = serviceFileParser.parse(sFile);
            mm.getServicesModelList().add(sModel);
         }

         if (mm.getModuleInfoFile() != null) {
            ModuleInfoDeclaration mInfoDecl = new ModuleInfoDeclaration();
            mInfoDecl.parse(mm.getModuleInfoFile());
            mm.setModuleInfoModel(mInfoDecl);

            if (dotFileModel != null) {
               dotFileModel.setModuleName(mInfoDecl.getName());
            }
         }
      }

      Analyzer analyzer = new Analyzer(mModelList, new Depository());
      analyzer.analyze();

      Unification testUnification = null;
      for(ModuleModel mModel: mModelList) {
         Unification unification = new Unification(mModel);
         unification.process();
         mModel.setUnification(unification);
         if ("server".equals(mModel.getRootDir().getName())) {
            testUnification = unification;
         }
      }

      return testUnification;
   }
}
