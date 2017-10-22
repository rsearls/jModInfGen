package org.jboss.core.ui;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.PathConverter;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by rsearls on 10/19/17.
 */
public class CmdLineArgs {
   @Parameter(
           names = { "-d", "--inputDirectory" },
           description = "Directory from which to start anlaysis",
           converter = PathConverter.class,
           validateWith = PathValidator.class
   )
   private Path inputDirectory;

   @Parameter(
           names = { "-v", "--verbose" },
           description = "Summary Report provides detailed list of files found and processed."
   )
   private boolean verbose = false;

   public boolean isVerbose() {
      return verbose;
   }

   @Parameter(
           names = { "-r", "--report" },
           description = "Summary Report written to a file rather than the console"
   )
   private boolean report = false;

   public boolean isReport() {
      return report;
   }

   public Path getInputDirectory() {

      if (inputDirectory == null) {
         final String dir = System.getProperty("user.dir");
         inputDirectory = Paths.get(dir);
         PathValidator pv = new PathValidator();
         pv.validate("-d", dir);
      }

      return inputDirectory;
   }
}
