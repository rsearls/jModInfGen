package org.jboss.jmodinfgen.module.info.directives;

import java_cup.runtime.Symbol;

import java.util.List;

/**
 * Created by rsearls on 6/12/17.
 */
public abstract class ModuleDirective {
   private String name;

   public void setName(String name) {
      this.name = name;
   }

   public String getName() {
      return name;
   }

   public abstract List<String> getModuleNameList();
   public abstract void process(Symbol s) ;
   public abstract void print();
}
