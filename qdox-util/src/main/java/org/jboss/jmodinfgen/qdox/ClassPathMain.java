package org.jboss.jmodinfgen.qdox;

import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * https://maven.apache.org/plugins/maven-dependency-plugin/
 * mvn dependency:build-classpath -Dmdep.outputFile=classpath.txt
 *
 * Created by rsearls on 1/4/18.
 */
public class ClassPathMain {
   public static void main(String [] args) {
      args = new String[2];
      args[0] = "/home/rsearls/j1/Resteasy/resteasy-client/classpath.txt";
      args[1] = "/home/rsearls/j1/Resteasy/resteasy-client/src/main/java";

      args[0] = "/home/rsearls/j1/Resteasy/resteasy-jaxrs/classpath.txt";
      args[1] = "/home/rsearls/j1/Resteasy/resteasy-jaxrs/src/main/java";

      ClassPathProcessor cpp = new ClassPathProcessor();
      TreeMap<String, Set<ArchiveStats>> depository = cpp.process(args[0]);

      SourceTreeImports worker = new SourceTreeImports();
      TreeMap<String,AtomicInteger> tabulatedImportPkgs = worker.process(args[1]);

      TreeSet<ArchiveStats> accumlatedStats =worker.evaluate(tabulatedImportPkgs, depository);
      Report report = new Report();
      report.report(args[1], accumlatedStats);

   }
}
