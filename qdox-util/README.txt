How to generate data.
(https://maven.apache.org/plugins/maven-dependency-plugin/)

- From the cmd-line at the root dir of the project run
        mvn dependency:build-classpath -Dmdep.outputFile=classpath.txt

 - edit one of the Main classes and set the root path.
 - cut an paste the console output into some file.
 - import data into LibraOffice spreadsheet.

