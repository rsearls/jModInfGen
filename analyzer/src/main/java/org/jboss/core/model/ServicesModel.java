package org.jboss.core.model;

import java_cup.runtime.Symbol;
import org.jboss.module.info.directives.ProvidesDirective;

import java.io.File;
import java.util.List;

/**
 * Created by rsearls on 10/11/17.
 */
public class ServicesModel extends ProvidesDirective {

   private File serviceFile;

   @Override
   public void process(Symbol s) {
      // disable parsing
   }

   public List<String> getServicesList() {
      return getModuleNameList();
   }

   public File getServiceFile() {
      return serviceFile;
   }

   public void setServiceFile(File serviceFile) {
      this.serviceFile = serviceFile;
   }

   public String getServiceFileName() {
      if(serviceFile == null) {
         setName("no services");
      } else {
         setName(serviceFile.getName());
      }
      return getName();
   }
}
