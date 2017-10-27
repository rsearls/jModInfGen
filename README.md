# jModInfGen
Tool for generating a module-info file from existing java sources.


Version 1.0.0.SNAPSHOT  <date>

## Overview
The jModInfGen project provides a utility to assist in the construction of a JDK 9
module-info.java file for your code.  This tool uses the output from the JDK's
"Java [static] class dependency analyzer", jdeps[1] utility and any existing
module-info file in the project to generate a module-info.java file.

This tool does not require JDK 9 in order to run.  It is built and runs with JDK 8.

## Dependencies
jModInfGen requires the presents of classes.dot file(s) that jdeps generates.  jdeps
can be run manually or with the org.apache.maven.plugins:maven-jdeps-plugin.

## Limitations
It is likely this tool will not be able to identify all the module names of all the
packages reported by jdeps.  jModInfGen makes the best effort to identify external module names.
It reports unknown packages in the generated module.info file for the user to address.

The `export` directive allows the declared package to be exported only to a set of
specifically-named modules, and to no others.  This restriction can not be determined
by this tool.  The generated file may need to be editied by the user.

The `uses` directive allows the programmer to identify a service residing in an external
module that is used in this module.  This is another element this tool can not detect.
The user may need to provide edits to the generated module-info file.

The `open` directive allows all of the non-public elements of the declared package to be accessed
by this module.  This is another element this tool can not detect.  The user may need to
provide edits to the generated module-info file.

The `requires` directive has optional qualifiers `transitive` and `static`.  This is information
that can not be detected by this tool and may required user editing of the generated file.


## Project Structure
- Project module, `mod-info-parser` uses lexical analyzer JFlex and LALR Parser Generator for Java
java_cup in extracting data from each module-info.java file.  This tool does not process
module-info.class files as this would require JDK9.  Any executable jar is provided as
part of the build.

- Project module, `analyzer` processes the data written to `classes.dot` by utility `jdeps` and
a module-info.java file found in the project directory.  It generates a module-info file
of the analyzed data.  An executable jar is provided as part of the build.

- Project `maven-plugin` provides a plugin to be used on multi-module maven projects.  It
will identify cross module dependences.  This plugin relies on the output from
org.apache.maven.plugins:maven-jdeps-plugin.  The plugin must be configured to generate
`classes.dot` files.


## Related Tools
Currently there are 2 other known 3rd party projects related to addressing 
JSR 376: "The Java Platform Module System" in Project Jigsaw[2], 
`maven-jdeps-plugin` [3] and `moditect` [4].

* The `maven-jdeps-plugin` uses the JDK's jdeps tool to analyze the classes in each
maven module of a project.  The plugin can run on each module of a multi-module 
project but it does not perform dependency analysis across modules.   (jModInfGen
when pointed to the root directory of a multi-module maven project does perform 
a cross module analysis of the jdeps analysis files and generates references to 
the inter-project modules.)

* `Moditect` is a maven plugin.  It generates a module-info.java file for given artifacts
for Maven dependencies or local JAR files in the project.  The module directives can
be declared within elements of the plugin.

moditect and jModInfGen differ in the following ways,
- moditect requires JDK 9 in order to run.  Your app can't make use of this tool
      to generate module-info files until it can build with JDK 9.  jModInfGen will
      run using JDK 8.
- moditect allows the project to retain pre-defined module directives in the
      pom.xml.  The focus of jModInfGen is to initially generate the module directives.
      

## References
1. https://docs.oracle.com/javase/8/docs/technotes/tools/unix/jdeps.html
2. http://openjdk.java.net/projects/jigsaw/spec/sotms/
3. https://maven.apache.org/plugins/maven-jdeps-plugin/
4. https://github.com/moditect/moditect


