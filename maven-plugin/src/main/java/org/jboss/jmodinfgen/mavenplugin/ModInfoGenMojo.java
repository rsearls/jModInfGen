package org.jboss.jmodinfgen.mavenplugin;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.jboss.core.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

// when using "requiresDirectInvocation = true" DO NOT define a LifecyclePhase with the plugin
// "aggregator = true; means the goal with get executed only once in the build

@Mojo(name = "generate-module-info", aggregator = true, requiresDirectInvocation = true)
public class ModInfoGenMojo extends AbstractMojo {

   @Parameter( defaultValue = "${project}", readonly = true, required = true )
   private MavenProject project;

   @Parameter( defaultValue = "${session}", readonly = true, required = true )
   private MavenSession session;

   @Parameter(readonly = true, property = "jModInfGen.propertyFile")
   private File propertyFile;

   @Parameter( defaultValue = "false", property = "jModInfGen.verbose" )
   private boolean verbose;

   @Parameter( defaultValue = "false", property = "jModInfGen.report" )
   private boolean report;

   @Override
   public void execute() throws MojoExecutionException, MojoFailureException {

      String[] args = processConfigSettings();
      try
      {
         Main.main(args);
      } catch (Exception e)
      {
         throw new MojoExecutionException(e.getMessage());
      }
   }

   private String[] processConfigSettings() {
      List<String> argsList = new ArrayList<>();
      argsList.add("--inputDirectory");
      argsList.add(project.getBasedir().getAbsolutePath());

      if (report) {
         argsList.add("--report");
      }
      if (verbose) {
         argsList.add("--verbose");
      }
      if (propertyFile != null) {
         argsList.add("--file");
         argsList.add(propertyFile.getAbsolutePath());
      }
      String[] args = argsList.toArray(new String[argsList.size()]);
      return args;
   }
}