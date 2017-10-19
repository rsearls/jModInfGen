package org.jboss.core;

import org.jboss.core.model.DotFileModel;
import org.jboss.core.model.ModuleModel;
import org.jboss.core.model.ServicesModel;
import org.jboss.core.parser.DotFileParser;
import org.jboss.core.parser.ServiceFileParser;
import org.jboss.core.util.DumpIt;
import org.jboss.core.util.FileFinderUtil;
import org.jboss.core.util.Analyzer;
import org.jboss.core.writer.ModuleInfoWriter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rsearls on 10/11/17.
 */
public class Main
{
   public static void main(String[] args) {
      String arg = "";
      arg = "/home/rsearls/j1/Resteasy/resteasy-jaxrs/dot/resteasy-jaxrs-3.0.23.Final-SNAPSHOT.jar.dot";
      arg = "/home/rsearls/j1/Resteasy";

      // test only //Depository.testPropertiesFileRead();

      if (arg == null || arg.length() == 0) {
         System.out.println ("Absolute path to root dir required.");
         System.exit(1);
      }

      FileFinderUtil ffUtil = new FileFinderUtil();
      List<File> rawModules = ffUtil.getModuleList(new File(arg));
      List<ModuleModel> mModelList = new ArrayList<>();
      for(File file : rawModules) {
         ModuleModel mm = new ModuleModel();
         mModelList.add(mm);
         mm.setRootDir(file);
         mm.setDotFile(ffUtil.getDotFile(file));
         mm.setServicesFileList(ffUtil.getServicesList(file));
         mm.setModuleInfoFile(ffUtil.getModuleInfoFile(file));
         DumpIt.dumpModel(mm); // debug
      }

      DotFileParser dotParser = new DotFileParser();
      ServiceFileParser serviceFileParser = new ServiceFileParser();

      for (ModuleModel mm : mModelList) {
         File f = mm.getDotFile();
         DotFileModel dotFileModel = dotParser.parse(f);
         if (dotFileModel != null)
         {
            dotFileModel.setModuleDir(mm.getRootDir());
         }
         mm.setDotFileModel(dotFileModel);

         for(File sFile : mm.getServicesFileList()) {
            ServicesModel sModel = serviceFileParser.parse(sFile);
            mm.setServicesModel(sModel);
         }
      }

      Analyzer analyzer = new Analyzer(mModelList);
      analyzer.analyze();
      analyzer.duplicatePackageNameCheck();


      ModuleInfoWriter writer = new ModuleInfoWriter();
      for(ModuleModel mModel: mModelList) {
         if (mModel.getDotFileModel() != null) {
            writer.writeFile(mModel);
         }
      }

      //todo add existing module-info file parse
      //todo compare/merge data
      //todo print tmp file.
   }

}
