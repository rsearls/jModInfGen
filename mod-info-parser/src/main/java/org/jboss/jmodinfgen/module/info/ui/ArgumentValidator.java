package org.jboss.jmodinfgen.module.info.ui;

import java.io.File;

/**
 * Created by rsearls on 10/19/17.
 */
public class ArgumentValidator {

   public static void validate(String args[]) throws ArgumentException {

      if (args.length == 0)
      {
         throw new ArgumentException("Input file required.  None provided.");

      } else
      {
         File f = new File(args[0]);
         if (!f.canRead())
         {
            throw new ArgumentException("ERROR: Not readable: " + f.getAbsolutePath());
         } else if (!f.isFile())
         {
            throw new ArgumentException("ERROR: Not a file: " + f.getAbsolutePath());
         }
      }
   }
}
