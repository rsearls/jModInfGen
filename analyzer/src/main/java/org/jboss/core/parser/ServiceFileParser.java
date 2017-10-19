package org.jboss.core.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import org.jboss.core.model.ServicesModel;

/**
 * Created by rsearls on 10/11/17.
 */
public class ServiceFileParser
{

   public ServicesModel parse( File file) {
      ServicesModel servicesModel = null;
      if (file != null) {
         servicesModel = new ServicesModel();
         servicesModel.setServiceFile(file);
         List<String> servicesList = servicesModel.getServicesList();

         try (Scanner fileScan = new Scanner(file).useDelimiter("\\n"))
         {
            while (fileScan.hasNext()) {
               String line = fileScan.next().trim();
               if (!line.startsWith("#") && !line.isEmpty()) {
                  servicesList.add(line);
               }
            }

         } catch (FileNotFoundException e) {
            System.out.println(e);
         }
      }
      return servicesModel;
   }
}
