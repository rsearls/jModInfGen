package org.jboss.core;

import org.jboss.core.model.DotFileModel;
import org.jboss.core.model.ModuleModel;
import org.jboss.core.parser.DotFileParser;
import org.jboss.core.util.Analyzer;
import org.jboss.core.util.Depository;
import org.jboss.core.util.FileFinderUtil;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rsearls on 10/30/17.
 */
public class AnalyzerTest {

   private static File inDir;

   @BeforeClass
   static public void beforeClass() {
      inDir = new File("./src/test/resources/filefinder/client");
      if (!inDir.canRead())
      {
         throw new IllegalArgumentException("File not found: " + inDir.getAbsolutePath());
      }
   }

   @Test
   public void analyzeTest() throws Exception {

      FileFinderUtil ffUtil = new FileFinderUtil();
      List<File> rawModules = ffUtil.getModuleList(inDir);
      List<ModuleModel> mModelList = new ArrayList<>();
      File file = rawModules.get(0);

      ModuleModel mm = new ModuleModel();
      mModelList.add(mm);
      mm.setRootDir(file);
      mm.setDotFile(ffUtil.getDotFile(file));

      DotFileParser dotParser = new DotFileParser();

      File f = mm.getDotFile();
      DotFileModel dotFileModel = dotParser.parse(f);
      Assert.assertNotNull(dotFileModel);
      dotFileModel.setModuleDir(mm.getRootDir());
      mm.setDotFileModel(dotFileModel);


      Analyzer analyzer = new Analyzer(mModelList, new Depository());
      analyzer.analyze();

      Assert.assertEquals(65, dotFileModel.getExtPackages().size());
   }
}
