package org.jboss.jmodinfgen.core.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.jmodinfgen.core.model.DotFileModel;

/**
 * Created by rsearls on 10/11/17.
 */
public class DotFileParser
{

   private final static String NOT_FOUND = "(not found)";
   private Pattern quotationMarks = Pattern.compile("([\"'])(\\\\?.)*?\\1");

   public DotFileModel parse( File file) {
      DotFileModel dotFileModel = null;
      if (file != null) {
         dotFileModel = new DotFileModel();
         dotFileModel.setDotFile(file);

         TreeMap<String,TreeSet<String>> internalPackages = dotFileModel.getInternalPackages();
         TreeMap<String,TreeSet<String>> externalPackages = dotFileModel.getExternalPackages();

         // process line-by-line
         try (Scanner fileScan = new Scanner(file).useDelimiter("\\n"))
         {

            // todo fix this
            // 1st stmt is digraph with jar name
            if (fileScan.hasNext()) {
               String line = fileScan.next();
            }

            while (fileScan.hasNext()) {
               String line = fileScan.next();

               try (Scanner s = new Scanner(line))
               {
                  // get text in quotation marks
                  quotationMarks = Pattern.compile("([\"'])(\\\\?.)*?\\1");
                  Matcher m = quotationMarks.matcher(line);
                  if (m.find()) {
                     // internal package
                     String exportPkg = rmQuotes(m.group());
                     if (internalPackages.get(exportPkg) == null)
                     {
                        internalPackages.put(exportPkg, new TreeSet<String>());
                     }

                     if (m.find()) {
                        // external package ref by internal classes
                        String requiredPkg = rmQuotes(m.group());
                        String dText = parseDependencyText(requiredPkg, internalPackages);
                        if (dText != null) {
                           if (externalPackages.get(dText) == null)
                           {
                              externalPackages.put(dText, new TreeSet<String>());
                           }
                        }
                     }
                  }

               } catch (IllegalStateException se) {
                  // ignore
                  System.out.println(se);
               }
            }
         } catch (FileNotFoundException e) {
            System.out.println(e);
         }
      }
      return dotFileModel;
   }


   /**
    * sift out the package names of the dependencies.
    * @param d
    * @return
    */
   private String parseDependencyText(String d, TreeMap<String,TreeSet<String>> internalPackages) {
      String dText = null;
      if (d.endsWith(NOT_FOUND)) {
         // a 3rd party package
         String[] strs = d.split(" ");
         dText = strs[0].trim();
      }
      else if (!DotFileModel.stndJDK.contains(d)) {
         String[] tokens = d.split("\\s");
         if (tokens.length == 1) {
            dText = tokens[0];
         } else {
            // skip packages provided by this module.
            if (!internalPackages.containsKey(tokens[0])) {
               dText = tokens[0];
            }
         }
      }
      return dText;

   }

   private String rmQuotes(String txt) {
      return txt.substring(1, txt.length()-1);
   }
}
