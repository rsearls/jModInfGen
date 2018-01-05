package org.jboss.jmodinfgen.qdox;

import org.jboss.jmodinfgen.core.util.FileFinderUtil;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by rsearls on 1/4/18.
 */
public class Main {

   public static void main(String[] args){
      Main main = new Main();
      main.stepOne("/home/rsearls/j1/Resteasy/");
   }

   public void stepOne(String rootDir) {

      File inDir = new File("/home/rsearls/j1/Resteasy/");
      FileFinderUtil ffUtil = new FileFinderUtil();
      List<File> rawModules = ffUtil.getModuleList(inDir);
      System.out.println("module_name\t\tarchive_referenced\t\tpercent_usage\t\timport_cnt\t\tclass_cnt");
      for (File f : rawModules) {
         //System.out.println(f.getAbsolutePath());
         File srcDir = new File(f, "/src/main/java");
         File cpFile = new File(f,"classpath.txt");
         if (srcDir.exists()){
            if (cpFile.exists()) {
               process(f.getName(), srcDir, cpFile);
            }
         }
      }
   }

   public void process(String modName, File srcDir, File cpFile){

      ClassPathProcessor cpp = new ClassPathProcessor();
      TreeMap<String, Set<ArchiveStats>> depository = cpp.process(cpFile.getAbsolutePath());

      SourceTreeImports worker = new SourceTreeImports();
      TreeMap<String,AtomicInteger> tabulatedImportPkgs = worker.process(srcDir.getAbsolutePath());

      TreeSet<ArchiveStats> accumlatedStats =worker.evaluate(tabulatedImportPkgs, depository);
      Report report = new Report();
      report.reportThree(modName, accumlatedStats);

   }
}
