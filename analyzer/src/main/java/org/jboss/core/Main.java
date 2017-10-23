package org.jboss.core;

import com.beust.jcommander.JCommander;
import org.jboss.core.model.DotFileModel;
import org.jboss.core.model.ModuleModel;
import org.jboss.core.model.ServicesModel;
import org.jboss.core.parser.DotFileParser;
import org.jboss.core.parser.ServiceFileParser;
import org.jboss.core.ui.CmdLineArgs;
import org.jboss.core.util.Analyzer;
import org.jboss.core.util.Depository;
import org.jboss.core.util.DumpIt;
import org.jboss.core.util.FileFinderUtil;
import org.jboss.core.util.Unification;
import org.jboss.core.writer.ModuleInfoWrite;
import org.jboss.core.writer.SummaryReportWriter;
import org.jboss.module.info.directives.ModuleInfoDeclaration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by rsearls on 10/11/17.
 */
public class Main
{
   public static void main(String[] args) throws Exception {

      // check cmd-line args
      CmdLineArgs cmdLineArgs = new CmdLineArgs();
      new JCommander( cmdLineArgs, args );

      File inDir = cmdLineArgs.getInputDirectory().toFile();

      // Provide user help
      if (cmdLineArgs.isHelp()) {
         System.out.print(cmdLineArgs.toStringHelp());
         System.exit(1);
      }

      // load user provided properties file
      if (cmdLineArgs.getPropertiesFile() != null) {
         Depository.loadFile(cmdLineArgs.getPropertiesFile().toFile());
      }

      // Collect all the files to be analyzed
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
         //DumpIt.dumpModel(mm); // debug
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

      Analyzer analyzer = new Analyzer(mModelList);
      analyzer.analyze();

      //Merge data from jdeps report and any existing module-info file.
      for(ModuleModel mModel: mModelList) {
         Unification unification = new Unification(mModel);
         unification.process();
         mModel.setUnification(unification);
         //unification.print(); // debug
      }

      ModuleInfoWrite writer = new ModuleInfoWrite();
      for(ModuleModel mModel: mModelList) {
         //writer.printFile(mModel);
         writer.writeFile(mModel);
      }

      SummaryReportWriter sReportWriter = new SummaryReportWriter();
      sReportWriter.setVerbose(cmdLineArgs.isVerbose());
      if (cmdLineArgs.isReport()) {
         sReportWriter.write(mModelList);
      } else {
         sReportWriter.print(mModelList);
      }
   }

}
