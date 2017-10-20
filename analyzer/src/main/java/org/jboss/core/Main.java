package org.jboss.core;

import com.beust.jcommander.JCommander;
import org.jboss.core.model.DotFileModel;
import org.jboss.core.model.ModuleModel;
import org.jboss.core.model.ServicesModel;
import org.jboss.core.parser.DotFileParser;
import org.jboss.core.parser.ServiceFileParser;
import org.jboss.core.ui.CmdLineArgs;
import org.jboss.core.util.DumpIt;
import org.jboss.core.util.FileFinderUtil;
import org.jboss.core.util.Analyzer;
import org.jboss.core.writer.ModuleInfoWriter;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rsearls on 10/11/17.
 */
public class Main
{
   public static void main(String[] args) throws Exception {

      CmdLineArgs cmdLineArgs = new CmdLineArgs();
      new JCommander( cmdLineArgs, args );

      File inDir = cmdLineArgs.getInputdirectory().toFile();
      //String arg = "";
      //arg = "/home/rsearls/j1/Resteasy/resteasy-jaxrs/dot/resteasy-jaxrs-3.0.23.Final-SNAPSHOT.jar.dot";
      //arg = "/home/rsearls/j1/Resteasy";

      // test only //Depository.testPropertiesFileRead();

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
