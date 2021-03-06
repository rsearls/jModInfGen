package org.jboss.jmodinfgen.module.info.directives;

/**
 * The opens module directive format is
 *    opens PackageName [ to ModuleName { , ModuleName}] ;
 *       PackageName is a fully qualified package name.
 *       ModuleName  is a fully qualified module name.
 *
 * This is fundamentally the save as the "exports" statement.
 *
 * Created by rsearls on 6/12/17.
 */
public class OpensDirective extends ExportsDirective {

   @Override
   public void print() {
      System.out.println(this.toString());
   }

   public String toString() {

      StringBuilder sbuf = new StringBuilder();
      sbuf.append("opens " + getName());
      if (isTO) {
         sbuf.append(" to \n");
         for (String s: moduleNameList) {
            sbuf.append("\t\t" + s + ",\n");
         }
         sbuf.delete(sbuf.lastIndexOf(","), sbuf.length());
      }
      sbuf.append(";");
      return sbuf.toString();
   }
}
