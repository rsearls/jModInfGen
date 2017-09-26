package org.jboss.module.info;

import org.jboss.module.info.directives.ModuleInfoDeclaration;

import java.io.File;

/**
 * Created by rsearls on 6/28/17.
 */
public class Main {

   private static boolean debug_flag = false;
   private static final String HELP_MSG =
           "A relative or absolute path to a module-info formated file is required.";

   public static void main(String argv[]) {
      Main jp = new Main();
      // todo check input for -debug flag
      jp.processInput(jp.inputCheck(argv[0]));
   }


   public void processInput(File file) {

      try {
         ModuleInfoDeclaration mInfoDecl = new ModuleInfoDeclaration();
         //mInfoDecl.setDebug(debug_flag);
         mInfoDecl.parse(file);
         mInfoDecl.print();
      }
      catch (Exception e) {
         e.printStackTrace(System.out);
         System.exit(1);
      }
   }

   public File inputCheck(String fileName) {
      if (fileName == null || fileName.isEmpty()) {
         System.out.println("The name of the input file is missing.");
         System.out.println(HELP_MSG);
         System.exit(1);
      }

      File f = new File(fileName);
      if (!f.exists()) {
         System.out.printf("File, %s was not found.", fileName);
         System.out.println(HELP_MSG);
         System.exit(1);
      } else if (!f.isFile()) {
         System.out.printf("%s is not a file.", fileName);
         System.out.println(HELP_MSG);
         System.exit(1);
      } else if (!f.canRead()) {
         System.out.printf("%s does not have read permission.", fileName);
         System.exit(1);
      }
      return f;
   }

}
