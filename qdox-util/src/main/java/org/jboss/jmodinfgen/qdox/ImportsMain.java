package org.jboss.jmodinfgen.qdox;

/**
 * Created by rsearls on 1/2/18.
 */
public class ImportsMain {

   public static void main(String [] args) {
      args = new String[1];
      args[0] = "/home/rsearls/j1/Resteasy/resteasy-client/src/main/java";
      SourceTreeImports worker = new SourceTreeImports();
      worker.process(args[0]);
   }
}
