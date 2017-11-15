package org.jboss.jmodinfgen.core.writer;

import org.jboss.jmodinfgen.core.model.ModuleModel;
import org.jboss.jmodinfgen.core.util.Depository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by rsearls on 10/22/17.
 */
public class SummaryReportWriter {

   private Depository depository;

   private final String genFilename = "jModInfGen-Summary-Report.txt";

   private List<ModuleModel> mModelList = new ArrayList<>();
   private List<File> dotFileList = new ArrayList<>();
   private List<File> noDotFileList = new ArrayList<>();
   private List<File> moduleInfoList = new ArrayList<>();
   private List<File> writtenFileList = new ArrayList<>();
   private List<File> servicesFileList = new ArrayList<>();
   private boolean verbose = false;

   public SummaryReportWriter(Depository depository) {
      if (depository == null) {
         throw new IllegalArgumentException("Depository can not be null");
      }
      this.depository = depository;
   }

   public boolean isVerbose() {
      return verbose;
   }

   public void setVerbose (boolean flag) {
      verbose = flag;
   }

   public void write(List<ModuleModel> mModelList) {
      this.mModelList = mModelList;
      init(mModelList);

      final String dir = System.getProperty("user.dir");
      File modFile = new File(dir, genFilename);

      try (PrintWriter pWriter = new PrintWriter(modFile))
      {
         pWriter.print(header());
         pWriter.print(toStringModuleModels());
         pWriter.print(toStringModuleInfos());
         pWriter.print(toStringDotFiles());
         pWriter.print(toStringServicesFiles());
         pWriter.print(toStringWrittenFiles());
         pWriter.print("\n");
         pWriter.print(toStringDuplicatePackages());

      } catch (FileNotFoundException e)
      {
         System.out.println(e);
      }
      System.out.println("Summary Report written to " + genFilename);
   }

   public void print(List<ModuleModel> mModelList) {
      this.mModelList = mModelList;
      init(mModelList);

      System.out.print(header());
      System.out.print(toStringModuleModels());
      System.out.print(toStringModuleInfos());
      System.out.print(toStringDotFiles());
      System.out.print(toStringServicesFiles());
      System.out.print(toStringWrittenFiles());
      System.out.println();
      System.out.print(toStringDuplicatePackages());
   }

   /**
    * public String toString() {
    * <p>
    * }
    **/
   private void init(List<ModuleModel> mModelList) {
      dotFileList.clear();
      noDotFileList.clear();
      moduleInfoList.clear();
      writtenFileList.clear();
      servicesFileList.clear();

      for (ModuleModel mModel : mModelList)
      {

         if (mModel.getDotFile() == null)
         {
            noDotFileList.add(mModel.getRootDir());
         } else
         {
            dotFileList.add(mModel.getDotFile());
         }

         if (mModel.getModuleInfoFile() != null)
         {
            moduleInfoList.add(mModel.getModuleInfoFile());
         }

         if (mModel.getWrittenFile() != null)
         {
            writtenFileList.add(mModel.getWrittenFile());
         }

         servicesFileList.addAll(mModel.getServicesFileList());
      }

   }

   private String header() {
      StringBuilder sb = new StringBuilder();
      Date date = new Date();
      sb.append("  " + date + "\n");
      sb.append("  jModInfoGet Summary Report\n\n");
      return sb.toString();
   }

   private final String twoSpaces = "";
   private final String tab = "\t";

   private String toStringModuleModels() {
      StringBuilder sb = new StringBuilder();
      sb.append(twoSpaces + mModelList.size() + " root directories processed\n");
      if (verbose)
      {
         for (ModuleModel m : mModelList)
         {
            sb.append(tab + m.getRootDir().getAbsolutePath() + "\n");
         }
      }
      return sb.toString();
   }

   private String toStringModuleInfos() {
      StringBuilder sb = new StringBuilder();
      sb.append(twoSpaces + moduleInfoList.size() + " module-info.java files found\n");
      if (verbose)
      {
         for (File f : moduleInfoList)
         {
            sb.append(tab + f.getAbsolutePath() + "\n");
         }
      }
      return sb.toString();
   }

   private String toStringDotFiles() {
      StringBuilder sb = new StringBuilder();
      sb.append(twoSpaces + dotFileList.size() + " classes.dot files found\n");
      if (verbose)
      {
         for (File f : dotFileList)
         {
            sb.append(tab + f.getAbsolutePath() + "\n");
         }

         sb.append(twoSpaces + noDotFileList.size() + " directories without classes.dot files\n");
         for (File nf : noDotFileList)
         {
            sb.append(tab + nf.getAbsolutePath() + "\n");
         }
      }
      return sb.toString();
   }

   private String toStringServicesFiles() {
      StringBuilder sb = new StringBuilder();
      sb.append(twoSpaces + servicesFileList.size() + " service provider files found\n");
      if (verbose)
      {
         for (File f : servicesFileList)
         {
            sb.append(tab + f.getAbsolutePath() + "\n");
         }
      }
      return sb.toString();
   }

   private String toStringWrittenFiles() {
      StringBuilder sb = new StringBuilder();
      sb.append(twoSpaces + writtenFileList.size() + " generated-module-info.java files written\n");
      if (verbose)
      {
         for (File f : writtenFileList)
         {
            sb.append(tab + f.getAbsolutePath() + "\n");
         }
      }
      return sb.toString();
   }


   private String toStringDuplicatePackages() {

      StringBuilder sb = new StringBuilder();
      int cnt = depository.getDuplicatePackageList().size();

      sb.append("  *** Duplicate Package Names ***\n");

      if (cnt == 0) {
         sb.append("  " + cnt + " duplicate package names found\n");
      } else {
         sb.append("  " + cnt + " duplicate package names found in the following modules.\n");
         for (Map.Entry<String, List<String>> entry : depository.getDuplicatePackageList().entrySet())
         {
            sb.append("  package: " + entry.getKey() + " in modules\n");
            for (String moduleName : entry.getValue())
            {
               sb.append("\t" + moduleName + "\n");
            }
         }
      }

      return sb.toString();
   }
}
