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
           description = "Directory from which to start analysis",
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

   @Parameter(
           names = { "-f", "--file" },
           description = "A java properties file that contains package name to module name mappings",
           converter = PathConverter.class,
           validateWith = FileValidator.class
   )
   private Path propertiesFile;

   public Path getPropertiesFile() {
      return propertiesFile;
   }

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

   @Parameter(
           names = "--help",
           description = "Print command-line options"
   )
   boolean help = false;

   public boolean isHelp() {
      return help;
   }

   private final String twoSpaces = "  ";

   public String toStringHelp() {
      StringBuilder sb = new StringBuilder();
      sb.append("jModInfoGen command-line options\n");
      sb.append(twoSpaces + "-d --inputDirectory   Path to the directory from which to start analysis.\n");
      sb.append(twoSpaces + "                      Default directory is the one from which jModInfGen is run.\n");

      sb.append(twoSpaces + "-f --file   Path to a java properties file that contains package name to module name mappings.\n");
      sb.append(twoSpaces + "            The file format is <qualified package name>:<module name>\n");
      sb.append(twoSpaces + "            (e.g. java.applet:java.desktop\n");
      sb.append(twoSpaces + "                  javax.jws:java.xml.ws\n");
      sb.append(twoSpaces + "                  org.omg.CosNaming:java.corba  )\n");

      sb.append(twoSpaces + "--help   Print the command-line options\n");
      sb.append(twoSpaces + "-r --report   Write the Summary Report to a file rather than the console.\n");
      sb.append(twoSpaces + "-v --verbose  The Summary Report will contains a detailed list of files found and processed.\n");

      return sb.toString();
   }
}
