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
