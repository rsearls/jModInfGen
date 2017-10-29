package org.jboss.core;

import org.jboss.core.model.ModuleModel;
import org.jboss.core.util.FileFinderUtil;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by rsearls on 10/29/17.
 */
public class FileFinderTest {

   private static File inDir;

   private static List<String> dirnameControl = new ArrayList<>();
   static {
      dirnameControl.add("/testsuite");
      dirnameControl.add("/client");
      dirnameControl.add("/server");
   };

   private static LinkedHashMap<String, ModuleModelControl> mmControlMap = new LinkedHashMap<>();

   @BeforeClass
   static public void beforeClass()
   {
      inDir = new File("./src/test/resources/filefinder");
      if (!inDir.canRead()) {
         throw new IllegalArgumentException("File not found: " + inDir.getAbsolutePath());
      }

      ModuleModelControl m1 = new ModuleModelControl(inDir.getAbsolutePath(),
              "/testsuite",null,null,null);
      List<String> m2Services = new ArrayList<>();
      m2Services.add("/client/target/classes/META-INF/services/javax.xml.soap.SOAPConnectionFactory");
      ModuleModelControl m2 = new ModuleModelControl(inDir.getAbsolutePath(),
              "/client", "/client/target/gen-jdeps/classes.dot",
              "/client/src/module-info.java", m2Services );
      List<String> m3Services = new ArrayList<>();
      m3Services.add("/server/target/classes/META-INF/services/org.jboss.wsf.spi.management.StackConfigFactory");
      ModuleModelControl m3 = new ModuleModelControl(inDir.getAbsolutePath(),
              "/server", "/server/target/gen-jdeps/classes.dot",
              "/server/src/module-info.java", m3Services );
      mmControlMap.put("/testsuite", m1);
      mmControlMap.put("/client", m2);
      mmControlMap.put("/server",m3);
   }

   @Test
   public void rootDirsTest() throws Exception {
      FileFinderUtil ffUtil = new FileFinderUtil();
      List<File> rawModules = ffUtil.getModuleList(inDir);

      Assert.assertEquals(rawModules.size(), dirnameControl.size());

      int dirPrefixCnt = inDir.getAbsolutePath().length();
      List<String> inFilenames = new ArrayList<>();
      for (File f : rawModules) {
         inFilenames.add(f.getAbsolutePath().substring(dirPrefixCnt));
      }

      for(String s : inFilenames) {
         if (dirnameControl.contains(s)) {
            dirnameControl.remove(s);
         }
      }

      Assert.assertEquals(0, dirnameControl.size());
   }

   @Test
   public void filesTest() throws Exception {

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

      int dirPrefixCnt = inDir.getAbsolutePath().length();
      for (ModuleModel mm : mModelList) {
         String key = mm.getRootDir().getAbsolutePath().substring(dirPrefixCnt);
         ModuleModelControl mmControl = mmControlMap.get(key);
         Assert.assertNotNull("Unknown model for " + key, mmControl);
         mmControl.assertEquals(mm);
      }
   }

   private static class ModuleModelControl {
      private String dirPrefix = null;
      private String rootDir = null;
      private String dotFile = null;
      private String moduleInfoFile = null;
      private List<String> servicesFileList = new ArrayList<>();

      public ModuleModelControl(String dirPrefix,
                                String rootDir,
                                String dotFile,
                                String moduleInfoFile,
                                List<String> servicesFileList) {
         this.dirPrefix = dirPrefix;
         this.rootDir = rootDir;
         this.dotFile = dotFile;
         this.moduleInfoFile = moduleInfoFile;
         if (servicesFileList != null)
         {
            this.servicesFileList = servicesFileList;
         }
      }

      public void assertEquals(ModuleModel model) throws Exception {

         int prefixCnt = dirPrefix.length();
         if (dotFile == null && model.getDotFile() == null) {
            // match is OK
         } else
            if (model.getDotFile() != null)
         {
            Assert.assertEquals("Correct classes.dot file not found", dotFile,
                    model.getDotFile().getAbsolutePath().substring(prefixCnt));
         }

         if (moduleInfoFile == null && model.getModuleInfoFile() == null) {
            // match is OK
         } else
            if (model.getModuleInfoFile() != null) {
            Assert.assertEquals("Correct module-info file not found" , moduleInfoFile,
                    model.getModuleInfoFile().getAbsolutePath().substring(prefixCnt));
         }

         Assert.assertEquals("Different services list count.", servicesFileList.size(),
                 model.getServicesFileList().size());

         for(File f : model.getServicesFileList()) {
            if (servicesFileList.contains(f.getAbsolutePath().substring(prefixCnt))){
               servicesFileList.remove(f.getAbsolutePath().substring(prefixCnt));
            }
         }

         Assert.assertEquals(0, servicesFileList.size());
      }
   }
}
