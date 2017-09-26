package org.jboss.module.info.directives;

import java_cup.runtime.Symbol;
import java.util.List;
import org.jboss.parser.rules.sym;

/**
 * The uses directive specifies a service interface for which the current module may
 * discover service implementations via java.util.ServiceLoader .
 *
 * The uses module directive format is
 *    uses TypeName ;
 *       TypeName is a fully qualified class name.
 *
 * Created by rsearls on 6/12/17.
 */
public class UsesDirective extends ModuleDirective {

   @Override
   public void process (Symbol s) {

      switch (s.sym)
      {
         case sym.IDENTIFIER: {
            setName((String) s.value);
            break;
         }
      }
      return;
   }

   @Override
   public List<String> getModuleNameList() {
      // ignore
      return null;
   }

   @Override
   public void print() {
      System.out.println("USES " + getName() + ";");
   }
}
