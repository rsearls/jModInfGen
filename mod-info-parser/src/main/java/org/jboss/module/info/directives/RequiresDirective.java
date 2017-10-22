package org.jboss.module.info.directives;

import java_cup.runtime.Symbol;

import java.util.ArrayList;
import java.util.List;
import org.jboss.parser.rules.sym;

/**
 * The exports module directive format is
 *    requires {RequiresModifier} ModuleName ;
 *       RequiresModifier is either "transitive" or "static"
 *       ModuleName is a fully qualified module name;
 * Created by rsearls on 6/12/17.
 */
public class RequiresDirective extends ModuleDirective {

   protected List<String> referencingPackages = new ArrayList<>();
   private String modifier = "";

   @Override
   public void process(Symbol s) {

      switch (s.sym)
      {
         case sym.IDENTIFIER: {
            setName((String) s.value);
            break;
         }
         case sym.STATIC: {
            modifier = "static";
            break;
         }
         case sym.TRANSITIVE: {
            modifier = "transitive";
            break;
         }
      }
      return;
   }

   @Override
   public List<String> getModuleNameList() {
      return referencingPackages;
   }

   @Override
   public void print() {
      System.out.println(this.toString());
   }

   public String toString() {
      return "requires " + modifier + " " + getName() + ";";
   }


   public void printReferencedPackagesComment() {
      System.out.printf(this.toString());
   }

   public String toStringReferencedPackagesComment() {
      StringBuilder sb = new StringBuilder();
      if (!getModuleNameList().isEmpty())
      {
         for (String pkgName : getModuleNameList())
         {
            sb.append("\t\t//" + pkgName + "  - package referenced\n");
         }
      }
      return sb.toString();
   }
}
