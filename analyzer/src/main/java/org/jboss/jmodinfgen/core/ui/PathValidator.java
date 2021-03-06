package org.jboss.jmodinfgen.core.ui;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import java.io.File;

/**
 * Created by rsearls on 10/19/17.
 */
public class PathValidator implements IParameterValidator {

   public void validate(String name, String value)
           throws ParameterException {

      if (value != null) {
         File f = new File(value);
         if (!f.canRead()) {
            throw new ParameterException("Directory does not exist or can't be read: " + f.getAbsolutePath());
         } else if (!f.isDirectory()) {
            throw new ParameterException("Not a directory: " + f.getAbsolutePath());
         }
      }
   }
}
