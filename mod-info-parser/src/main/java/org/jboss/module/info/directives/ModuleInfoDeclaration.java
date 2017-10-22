package org.jboss.module.info.directives;

import java.io.FileReader;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import java_cup.runtime.Symbol;
import org.jboss.parser.rules.Scanner;
import org.jboss.parser.rules.sym;

/**
 * This class represents the contents of a (jdk 9) module-info class.
 * It reads the (.java) text file and gleans all the module directives
 * provided.
 *
 * Created by rsearls on 6/9/17.
 */
public class ModuleInfoDeclaration extends ModuleDirective {

   private boolean isDebug = false;
   private LinkedHashMap<String, ModuleDirective> exportsMap = new LinkedHashMap<>();
   private LinkedHashMap<String, ModuleDirective> requiresMap = new LinkedHashMap<>();
   private LinkedHashMap<String, ModuleDirective> opensMap = new LinkedHashMap<>();
   private LinkedHashMap<String, ModuleDirective> usesMap = new LinkedHashMap<>();
   private LinkedHashMap<String, ModuleDirective> providesMap = new LinkedHashMap<>();

   public void parse (File file) {
      try {
         Scanner scanner = new Scanner(new FileReader(file));

         ModuleDirective mDirective = null;
         LinkedHashMap<String, ModuleDirective> curTreeMap = null;
         Symbol s;
         do {
            //s = scanner.debug_next_token();
            s = scanner.next_token();
            switch (s.sym)
            {
               case org.jboss.parser.rules.sym.IDENTIFIER:
               {
                  mDirective.process(s);
                  break;
               }
               case sym.REQUIRES:
               {
                  mDirective = new RequiresDirective();
                  curTreeMap = requiresMap;
                  break;
               }
               case sym.EXPORTS:
               {
                  mDirective = new ExportsDirective();
                  curTreeMap = exportsMap;
                  break;
               }
               case sym.OPENS:
               {
                  mDirective = new OpensDirective();
                  curTreeMap = opensMap;
                  break;
               }
               case sym.USES: {
                  mDirective = new UsesDirective();
                  curTreeMap = usesMap;
                  break;
               }
               case sym.PROVIDES: {
                  mDirective = new ProvidesDirective();
                  curTreeMap = providesMap;
                  break;
               }
               case sym.MODULE:
               {
                  mDirective = this;
                  break;
               }
               case sym.SEMICOLON: {
                  if (curTreeMap != null) {
                     curTreeMap.put(mDirective.getName(), mDirective);
                     curTreeMap = null;
                  }
               }
               default:
               {
                  mDirective.process(s);
                  break;
               }

            }

         } while (s.sym != sym.EOF);

         //System.out.println("No errors.");
         if (isDebug)
         {
            print();
         }
      }
      catch (Exception e) {
         e.printStackTrace(System.out);
         System.exit(1);
      }
   }

   @Override
   public void process (Symbol s) {

      switch (s.sym) {
         case sym.IDENTIFIER: {
            setName((String) s.value);
         }
      }
      return;
   }

   @Override
   public List<String> getModuleNameList() {
      // ignore
      return null;
   }

   public LinkedHashMap<String, ModuleDirective> getExportsMap() {
     return this.exportsMap;
   }


   public LinkedHashMap<String, ModuleDirective> getRequiresMap() {
      return this.requiresMap;
   }

   public LinkedHashMap<String, ModuleDirective> getProvidesMap() {
      return this.providesMap;
   }

   public LinkedHashMap<String, ModuleDirective> getOpensMap() {
      return this.opensMap;
   }

   public LinkedHashMap<String, ModuleDirective> getUsesMap() {
      return this.usesMap;
   }

   public void setDebug(boolean flag) {
      this.isDebug = flag;
   }

   /*
    * Only provide package names globally available and not restricted to a
    * declared set of components
    */
   public List<String> getExportPkgNames() {
      List<String> results = new ArrayList<>();
      for (Map.Entry<String, ModuleDirective> entry : exportsMap.entrySet()) {
         if ( !((ExportsDirective)entry.getValue()).isTo()) {
            results.add(entry.getKey());
         }
      }
      return results;
   }

   @Override
   public void print() {

      System.out.printf("MODULE: %s\n",getName());

      for (ModuleDirective m: exportsMap.values()) {
         m.print();
      }

      for (ModuleDirective m: requiresMap.values()) {
         m.print();
      }

      for (ModuleDirective m: opensMap.values()) {
         m.print();
      }

      for (ModuleDirective m: usesMap.values()) {
         m.print();
      }

      for (ModuleDirective m: providesMap.values()) {
         m.print();
      }
   }
}
