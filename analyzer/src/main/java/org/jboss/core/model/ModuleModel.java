package org.jboss.core.model;

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
   private ServicesModel servicesModel;
   private ModuleInfoModel moduleInfoModel;

   public DotFileModel getDotFileModel() {
      return dotFileModel;
   }

   public void setDotFileModel(DotFileModel dotFileModel) {
      this.dotFileModel = dotFileModel;
   }

   public ServicesModel getServicesModel() {
      return servicesModel;
   }

   public void setServicesModel(ServicesModel servicesModel) {
      this.servicesModel = servicesModel;
   }

   public ModuleInfoModel getModuleInfoModel() {
      return moduleInfoModel;
   }

   public void setModuleInfoModel(ModuleInfoModel moduleInfoModel) {
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
}
