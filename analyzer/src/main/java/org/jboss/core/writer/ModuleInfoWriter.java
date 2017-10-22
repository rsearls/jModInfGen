package org.jboss.core.writer;

import org.jboss.core.model.DotFileModel;
import org.jboss.core.model.ModuleModel;
import org.jboss.core.model.ServicesModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by rsearls on 10/13/17.
 */
public class ModuleInfoWriter
{

   public void writeFile(ModuleModel mModel) {
      if (mModel.getDotFile() != null) {
         File dotFileModel = mModel.getDotFile();

         try
         {
            String path = dotFileModel.getParentFile().getCanonicalPath();
            File modFile = new File(path, "sample-module-info.java");
            System.out.println("## path: " + modFile.getCanonicalPath());
            String contents = fileContents(mModel);
            System.out.println(contents);
            writeFile(modFile, contents);
         } catch (IOException e)
         {
            System.out.println(e);
         }
      }
   }

   /***********
   public void writeFile (DotFileModel dotFileModel) {
      if (dotFileModel != null)
      {
         File file = dotFileModel.getDotFile();
         try
         {
            String path = file.getParentFile().getCanonicalPath();
            File modFile = new File(path, "sample-module-info.java");
            System.out.println("## path: " + modFile.getCanonicalPath());
            String contents = fileContents(dotFileModel);
            System.out.println(contents);
            writeFile(modFile, contents);
         } catch (IOException e)
         {
            System.out.println(e);
         }
      }
   }
*********/

   private String fileContents(ModuleModel mModel) {
      DotFileModel dotFileModel = mModel.getDotFileModel();
      StringBuilder sb = new StringBuilder();
      sb.append("module " + dotFileModel.getModuleName());
      //sb.append("\n");
      sb.append(" {\n");

      for(String s: dotFileModel.getInternalPackages().keySet()) {
         sb.append("\texports " + s + ";\n");
      }

      sb.append("\n");
/***
      for (Map.Entry<String, TreeSet<String>> entry: dotFileModel.XgetRequiredModuleNames().entrySet()) {
         Set<String> values = entry.getValue();
         String key = entry.getKey();

         if (values.isEmpty()) {
            sb.append("\t//requires " + key + ";\t\t// module name TBD\n");
         } else {
            sb.append("\trequires " + key + ";\n");
            for(String v: values) {
               sb.append("\t\t\t// " + v + ";\n");
            }
         }
      }
***/
/**
      // List services provided
      ServicesModel servicesModel = mModel.getServicesModel();
      if (servicesModel != null) {
         if (!servicesModel.getServicesList().isEmpty()) {
            String name = servicesModel.getServiceFile().getName();
            int indx = name.indexOf("$");
            if (indx > -1) {
               name = name.substring(0, indx -1);
            }
            sb.append("\tprovides " + name + " with\n");

            int cnt = servicesModel.getServicesList().size();
            int i = 1;
            for (String s : servicesModel.getServicesList()) {
               sb.append("\t\t" + s);
               if (i == cnt)
               {
                  sb.append(";");
               } else
               {
                  sb.append(",");
               }
               sb.append("\n");
               i++;
            }
         }
      }
**/
      sb.append("}");
      sb.append("\n");

      return sb.toString();
   }

/**********
   public String fileContents(DotFileModel dotFileModel) {
      StringBuilder sb = new StringBuilder();
      sb.append("module " + dotFileModel.getModuleName());
      sb.append("\n");
      sb.append("{\n");

      for(String s: dotFileModel.getInternalPackages().keySet()) {
         sb.append("\texports " + s + ";\n");
      }

      sb.append("\n");

      for (Map.Entry<String, TreeSet<String>> entry: dotFileModel.XgetRequiredModuleNames().entrySet()) {
         Set<String> values = entry.getValue();
         String key = entry.getKey();

         if (values.isEmpty()) {
            sb.append("\t//requires " + key + ";\t\t// module name TBD\n");
         } else {
            sb.append("\trequires " + key + ";\n");
            for(String v: values) {
               sb.append("\t\t\t// " + v + ";\n");
            }
         }
      }

      sb.append("}");
      sb.append("\n");

      return sb.toString();
   }
************/
   private void writeFile(File modFile, String contents) {

      try (PrintWriter pWriter = new PrintWriter(modFile))
      {
         pWriter.print(contents);
         System.out.println(modFile.getCanonicalFile() + " written");
      } catch (FileNotFoundException e) {
         System.out.println(e);
      } catch (IOException ioe) {
         System.out.println(ioe);
      }

   }

}
