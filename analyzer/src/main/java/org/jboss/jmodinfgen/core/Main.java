package org.jboss.jmodinfgen.core;

import com.beust.jcommander.JCommander;
import org.jboss.jmodinfgen.core.model.ModuleModel;
import org.jboss.jmodinfgen.core.ui.CmdLineArgs;
import org.jboss.jmodinfgen.core.util.Analyzer;
import org.jboss.jmodinfgen.core.util.Depository;
import org.jboss.jmodinfgen.core.util.FileFinderUtil;
import org.jboss.jmodinfgen.core.util.Unification;
import org.jboss.jmodinfgen.core.writer.ModuleInfoWrite;
import org.jboss.jmodinfgen.core.writer.SummaryReportWriter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by rsearls on 10/11/17.
 */
public class Main
{
   public static void main(String[] args) throws Exception {

      Depository depository = new Depository();

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
         depository.loadFile(cmdLineArgs.getPropertiesFile().toFile());
      }


      ExecutorService pool = Executors.newCachedThreadPool();
      List<Future<ModuleModel>> mmFutureList = new ArrayList<>();

      FileFinderUtil ffUtil = new FileFinderUtil();
      List<File> rawModules = ffUtil.getModuleList(inDir);
      for(File file : rawModules) {
         ModuleWorker mWorker = new ModuleWorker(file);
         mmFutureList.add(pool.submit(mWorker));
      }

      List<ModuleModel> mModelList = new ArrayList<>();
      for (Future<ModuleModel> fmm : mmFutureList) {
         mModelList.add(fmm.get());
      }

      Analyzer analyzer = new Analyzer(mModelList, depository);
      analyzer.analyze();

      //Merge data from jdeps report and any existing module-info file.
      for(ModuleModel mModel: mModelList) {
         Unification unification = new Unification(mModel);
         unification.setVerbose(cmdLineArgs.isVerbose());
         unification.process();
         mModel.setUnification(unification);
         //unification.print(); // debug
      }

      ModuleInfoWrite writer = new ModuleInfoWrite();
      for(ModuleModel mModel: mModelList) {
         //writer.printFile(mModel);
         writer.writeFile(mModel);
      }

      SummaryReportWriter sReportWriter = new SummaryReportWriter(depository);
      sReportWriter.setVerbose(cmdLineArgs.isVerbose());
      if (cmdLineArgs.isReport()) {
         sReportWriter.write(mModelList);
      } else {
         sReportWriter.print(mModelList);
      }
   }

}
