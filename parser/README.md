This is a utility that parses and retains for ref the statements of a module-info.java file.

This utility does NOT check for correctness of the format or contents of the file.

The utility uses the JFlex lexical analyzer (http://jflex.de/) and LALR Parser Generator for Java
(java_cup) (http://www2.cs.tum.edu/projects/cup/)

Two Jar archives are produced.
    - Archive module-info-directives-util-1.0.jar is the traditional reference archive.
    - Archive module-info-directives-util-1.0.one-jar.jar is an executable Jar that
            allows you to use Main.  It can be run with the following cmd.
                java -jar module-info-directives-util-1.0.one-jar.jar <INPUT_FILE>
                example
                    java -jar target/module-info-directives-util-1.0.one-jar.jar src/main/resources/ModuleStmtTest.txt

