# jModInfGen
Tool for generating a module-info file from existing java sources.


Version 1.0.0.alpha1  <date>

The jModInfGen project provides a utility to assist in the construction of a JDK 9
module for your code.  This tool uses the output from the JDK's "Java [static] 
class dependency analyzer", jdeps[1] utility to generate a module-info.java file. 

This tool does not require JDK 9 in order to run.  It is built with JDK 8 and
will run in JDK 8. 

It is likely this tool will not be able to identify all the module names of all the
packages reported by jdeps.  jModInfGen makes the best effort and flags, unknown 
packages in comments for the user to address.

  

Currently there are 2 other known 3rd party projects related to addressing 
JSR 376: "The Java Platform Module System" in Project Jigsaw[2], 
maven-jdeps-plugin [3] and moditect [4].

The maven-jdeps-plugin uses the JDK's jdeps tool to analyze the classes in each
maven module of a project.  The plugin can run on each module of a multi-module 
project but it does not perform dependency analysis across modules.   jModInfGen
when pointed to the root directory of a multi-module maven project does perform 
a cross module analysis of the jdeps analysis files and generates references to 
the inter-project modules.

Utility moditect is provided as a maven plugin.  It generates a module-info.java 
file for given artifacts for Maven dependencies or local JAR files in the project.
This tool allows projects to retain a archives module-info stmts in the pom.xml file.
A module-info file written on every build iteration.

moditect and jModInfGen differ in the following ways,
  - moditect requires JDK 9 in order to run.  Your app can't make use of this tool
      to generate module-info files until it can build with JDK 9.  jModInfGen will
      run using JDK 8.
  - moditect allows the project to retain the module-info information in the
      pom.xml.  Because jModInfGen is unlikely to map all references this tool
      might be better used to collect the initial declarations and use them
      in the creation of the moditect pom settings.
      

References
-[1] https://docs.oracle.com/javase/8/docs/technotes/tools/unix/jdeps.html
-[2] http://openjdk.java.net/projects/jigsaw/spec/sotms/
-[3] https://maven.apache.org/plugins/maven-jdeps-plugin/
-[4] https://github.com/moditect/moditect


