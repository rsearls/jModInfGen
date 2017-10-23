package org.jboss.core.ui;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import java.io.File;

/**
 * Created by rsearls on 10/22/17.
 */
public class FileValidator  implements IParameterValidator {

   public void validate(String name, String value)
           throws ParameterException {

      if (value != null) {
         File f = new File(value);
         if (!f.canRead()) {
            throw new ParameterException("File does not exist or can't be read: " + f.getAbsolutePath());
         } else if (!f.isFile()) {
            throw new ParameterException("Not a File: " + f.getAbsolutePath());
         }
      }
   }
}
