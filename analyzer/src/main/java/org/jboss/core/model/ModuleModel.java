package org.jboss.core.model;

import org.jboss.core.util.Unification;
import org.jboss.module.info.directives.ModuleInfoDeclaration;

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
   private ModuleInfoDeclaration moduleInfoModel;
   private Unification unification;

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

}
