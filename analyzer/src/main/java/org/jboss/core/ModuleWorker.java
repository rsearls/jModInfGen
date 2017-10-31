package org.jboss.core;

import java.io.File;

import org.jboss.core.model.DotFileModel;
import org.jboss.core.model.ModuleModel;
import org.jboss.core.model.ServicesModel;
import org.jboss.core.parser.DotFileParser;
import org.jboss.core.parser.ServiceFileParser;
import org.jboss.core.util.FileFinderUtil;
import org.jboss.module.info.directives.ModuleInfoDeclaration;

import java.util.concurrent.Callable;

/**
 * Created by rsearls on 10/31/17.
 */
public class ModuleWorker implements Callable<ModuleModel> {

   private File file;

   public ModuleWorker(File file) {
      this.file = file;
   }

   public ModuleModel call() {
      return process();
   }

   public ModuleModel process() {

      ModuleModel mm = new ModuleModel();

      // retrieve files of interest to process
      FileFinderUtil ffUtil = new FileFinderUtil();
      mm.setRootDir(file);
      mm.setDotFile(ffUtil.getDotFile(file));
      mm.setServicesFileList(ffUtil.getServicesList(file));
      mm.setModuleInfoFile(ffUtil.getModuleInfoFile(file));

      DotFileParser dotParser = new DotFileParser();
      ServiceFileParser serviceFileParser = new ServiceFileParser();

      // process contents of jdeps output
      File f = mm.getDotFile();
      DotFileModel dotFileModel = dotParser.parse(f);
      if (dotFileModel != null) {
         dotFileModel.setModuleDir(mm.getRootDir());
      }
      mm.setDotFileModel(dotFileModel);

      // process project service files
      for(File sFile : mm.getServicesFileList()) {
         ServicesModel sModel = serviceFileParser.parse(sFile);
         mm.getServicesModelList().add(sModel);
      }

      // process project module-info file
      if (mm.getModuleInfoFile() != null) {
         ModuleInfoDeclaration mInfoDecl = new ModuleInfoDeclaration();
         mInfoDecl.parse(mm.getModuleInfoFile());
         mm.setModuleInfoModel(mInfoDecl);

         if (dotFileModel != null) {
            dotFileModel.setModuleName(mInfoDecl.getName());
         }
      }
      return mm;
   }
}
