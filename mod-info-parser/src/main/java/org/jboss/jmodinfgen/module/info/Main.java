package org.jboss.jmodinfgen.module.info;

import org.jboss.jmodinfgen.module.info.directives.ModuleInfoDeclaration;
import org.jboss.jmodinfgen.module.info.ui.ArgumentValidator;
import org.jboss.jmodinfgen.module.info.ui.ArgumentException;

import java.io.File;

/**
 * Created by rsearls on 6/28/17.
 */
public class Main {

   public static void main(String args[]) {

      try {
         ArgumentValidator.validate(args);

         Main main = new Main();
         main.processInput(new File(args[0]));
      } catch (ArgumentException e) {
         System.out.println(e);
         System.exit(1);
      }

   }


   public void processInput(File file) {

      try {
         ModuleInfoDeclaration mInfoDecl = new ModuleInfoDeclaration();
         mInfoDecl.parse(file);
         mInfoDecl.print();
      }
      catch (Exception e) {
         e.printStackTrace(System.out);
         System.exit(1);
      }
   }
}
