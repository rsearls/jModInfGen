package org.jboss.jmodinfgen.core.model;

import org.jboss.jmodinfgen.core.util.Unification;
import org.jboss.jmodinfgen.module.info.directives.ModuleInfoDeclaration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rsearls on 10/11/17.
 */
public class ModuleModel {

   private File rootDir;
   private File dotFile;
   private List<File> servicesFileList = new ArrayList<>();
   private File moduleInfoFile;

   private DotFileModel dotFileModel;
   private List<ServicesModel> servicesModelList = new ArrayList<>();
   private ModuleInfoDeclaration moduleInfoModel;
   private Unification unification;
   private File writtenFile;

   public DotFileModel getDotFileModel() {
      return dotFileModel;
   }

   public void setDotFileModel(DotFileModel dotFileModel) {
      this.dotFileModel = dotFileModel;
   }

   public List<ServicesModel> getServicesModelList() {
      return servicesModelList;
   }

   public ModuleInfoDeclaration getModuleInfoModel() {
      return moduleInfoModel;
   }

   public void setModuleInfoModel(ModuleInfoDeclaration moduleInfoModel) {
      this.moduleInfoModel = moduleInfoModel;
   }

   public File getRootDir() {
      return rootDir;
   }

   public void setRootDir(File rootDir) {
      this.rootDir = rootDir;
   }

   public File getDotFile() {
      return dotFile;
   }

   public void setDotFile(File dotFile) {
      this.dotFile = dotFile;
   }

   public List<File> getServicesFileList() {
      return servicesFileList;
   }

   public void setServicesFileList(List<File> servicesFileList) {
      this.servicesFileList = servicesFileList;
   }

   public File getModuleInfoFile() {
      return moduleInfoFile;
   }

   public void setModuleInfoFile(File moduleInfoFile) {
      this.moduleInfoFile = moduleInfoFile;
   }

   public Unification getUnification() {
      return unification;
   }

   public void setUnification(Unification unification) {
      this.unification = unification;
   }

   public File getWrittenFile() {
      return writtenFile;
   }

   public void setWrittenFile(File writtenFile) {
      this.writtenFile = writtenFile;
   }

}
