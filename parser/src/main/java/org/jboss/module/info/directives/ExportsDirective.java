package org.jboss.module.info.directives;

import java_cup.runtime.Symbol;
import org.jboss.parser.rules.sym;

import java.util.ArrayList;
import java.util.List;
import org.jboss.parser.rules.sym;

/**
 * The exports directive specifies the name of a package to be exported by the current
 * module. This makes public and protected types in the package, and their public
 * and protected members, be accessible to code in other modules
 *
 * The to clause directs that the public and protected types in the package,
 * and their public and protected members, are accessible solely to code
 * in the modules specified in the to clause.
 *
 * The exports module directive format is
 *    exports PackageName [ to ModuleName { , ModuleName}] ;
 *       PackageName is a fully qualified package name.
 *       ModuleName  is a fully qualified module name.
 *
 * Created by rsearls on 6/12/17.
 */
public class ExportsDirective extends ModuleDirective {

   protected List<String> moduleNameList = new ArrayList<>();
   protected boolean isTO = false;    // flag that proposition "to" is active

   @Override
   public void process(Symbol s) {

      switch (s.sym)
      {
         case sym.IDENTIFIER:
         {
            if (isTO) {
               moduleNameList.add((String)s.value);
            }
            else {
               setName((String)s.value);
            }
            break;
         }
         case sym.TO: {
            isTO = true;
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
   public List<String> getModuleNameList() {
      return moduleNameList;
   }

   @Override
   public void print() {
      StringBuilder sbuf = new StringBuilder();
      sbuf.append("EXPORTS " + getName());
      if (isTO) {
         sbuf.append(" TO \n");
         for (String s: moduleNameList) {
            sbuf.append("\t\t" + s + ",\n");
         }
         sbuf.delete(sbuf.lastIndexOf(","), sbuf.length());
      }
      sbuf.append(";");
   System.out.println(sbuf.toString());
   }
}
