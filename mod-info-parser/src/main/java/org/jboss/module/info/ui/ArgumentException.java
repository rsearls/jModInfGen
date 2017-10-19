package org.jboss.module.info.ui;

/**
 * Created by rsearls on 10/19/17.
 */
public class ArgumentException extends RuntimeException {

   public ArgumentException(String msg) {
      // don't print stacktrace
      super(msg, null, true, false);
   }
}
