package org.jboss.core.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rsearls on 10/11/17.
 */
public class ServicesModel {

   private List<String> servicesList = new ArrayList<>();
   private File serviceFile;

   public List<String> getServicesList() {
      return servicesList;
   }

   public void setServicesList(List<String> servicesList) {
      this.servicesList = servicesList;
   }

   public File getServiceFile() {
      return serviceFile;
   }

   public void setServiceFile(File serviceFile) {
      this.serviceFile = serviceFile;
   }

   public String getServiceFileName() {
      if(serviceFile == null) {
         return "no services";
      }
      return serviceFile.getName();
   }
}
