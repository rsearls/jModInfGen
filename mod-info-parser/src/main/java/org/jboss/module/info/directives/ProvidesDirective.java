package org.jboss.module.info.directives;

import java_cup.runtime.Symbol;
import org.jboss.parser.rules.sym;

/**
 * The provides directive specifies a service interface for which the
 * with clause specifies one or more possible service implementations to
 * java.util.ServiceLoader .
 *
 * The provides module directive format is
 *    provides TypeName with TypeName { , TypeName} ;
 *       TypeName is a fully qualified class name.
 *
 * Created by rsearls on 6/12/17.
 */
public class ProvidesDirective extends ExportsDirective {

   protected boolean isWITH = false;    // flag that proposition "to" is active

   @Override
   public void process(Symbol s) {

      switch (s.sym)
      {
         case sym.IDENTIFIER:
         {
            if (isWITH) {
               getModuleNameList().add((String)s.value);
            }
            else {
               setName((String)s.value);
            }
            break;
         }
         case sym.WITH: {
            isWITH = true;
            break;
         }
         default:
         {
            break;
         }
      }
      return;
   }

   @Override
   public void print() {
      StringBuilder sbuf = new StringBuilder();
      sbuf.append("PROVIDES " + getName());
      if (isWITH) {
         sbuf.append(" WITH \n");
         for (String s: moduleNameList) {
            sbuf.append("\t\t" + s + ",\n");
         }
         sbuf.delete(sbuf.lastIndexOf(","), sbuf.length());
      }
      sbuf.append(";");
      System.out.println(sbuf.toString());
   }
}
