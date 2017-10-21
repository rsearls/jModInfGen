package org.jboss.core;

import com.beust.jcommander.JCommander;
import org.jboss.core.model.DotFileModel;
import org.jboss.core.model.ModuleModel;
import org.jboss.core.model.ServicesModel;
import org.jboss.core.parser.DotFileParser;
import org.jboss.core.parser.ServiceFileParser;
import org.jboss.core.ui.CmdLineArgs;
import org.jboss.core.util.Depository;
import org.jboss.core.util.DumpIt;
import org.jboss.core.util.FileFinderUtil;
import org.jboss.core.util.Analyzer;
import org.jboss.core.util.Unification;
import org.jboss.core.writer.ModuleInfoWriter;
import org.jboss.module.info.directives.ModuleInfoDeclaration;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by rsearls on 10/11/17.
 */
public class Main
{
   public static void main(String[] args) throws Exception {

      CmdLineArgs cmdLineArgs = new CmdLineArgs();
      new JCommander( cmdLineArgs, args );

      File inDir = cmdLineArgs.getInputDirectory().toFile();

      // test only // Depository d = new Depository();

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
         if (dotFileModel != null) {
            dotFileModel.setModuleDir(mm.getRootDir());
         }
         mm.setDotFileModel(dotFileModel);

         for(File sFile : mm.getServicesFileList()) {
            ServicesModel sModel = serviceFileParser.parse(sFile);
            mm.setServicesModel(sModel);
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
      analyzer.duplicatePackageNameCheck();  // todo this may be obsolete.

/***
      ModuleInfoWriter writer = new ModuleInfoWriter();
      for(ModuleModel mModel: mModelList) {
         if (mModel.getDotFileModel() != null) {
            writer.writeFile(mModel);
         }
      }
***/
      for(ModuleModel mModel: mModelList) {
         Unification unification = new Unification(mModel);
         unification.process();
         mModel.setUnification(unification);
         unification.print();
      }

      //todo add existing module-info file parse
      //todo compare/merge data
      //todo print tmp file.

   }

}
