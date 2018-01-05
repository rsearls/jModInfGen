package org.jboss.jmodinfgen.qdox;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by rsearls on 1/4/18.
 */
public class ClassPathProcessor {

   public TreeMap<String, Set<ArchiveStats>> process(String arg) {
      TreeMap<String, Set<ArchiveStats>> depository = new TreeMap<>();
      try
      {
         BufferedReader reader = new BufferedReader(new FileReader(arg));
         StringBuilder sb = new StringBuilder();
         String line = reader.readLine();
         while (line != null) {
            sb.append(line);
            line = reader.readLine();
         }

         List<String> archiveList = parseClassPath(sb.toString());
         TreeMap<String, ArchiveStats> results = archiveProcessor(archiveList);
         depository = createDepository(results);

      } catch (FileNotFoundException e) {
         System.out.println(e);
      } catch (IOException ioe) {
         System.out.println(ioe);
      }

      return depository;
   }

   private TreeMap<String, Set<ArchiveStats>> createDepository(TreeMap<String, ArchiveStats> archiveMap){
      TreeMap<String, Set<ArchiveStats>> masterMap = new TreeMap<>();

      for(Map.Entry<String,ArchiveStats> entry :archiveMap.entrySet()){
         ArchiveStats value = entry.getValue();
         for(String pkgName : value.getPkgList()) {
            if (masterMap.containsKey(pkgName)) {
               Set<ArchiveStats> as = masterMap.get(pkgName);
               as.add(value);
               /***
               // debug
               System.out.println("DUP: " + pkgName);
               for (ArchiveStats a : as)
               {
                  System.out.println("   - " + a.getJarName());
               }
               ***/
            } else {
               Set<ArchiveStats> as = new HashSet<>();
               as.add(value);
               masterMap.put(pkgName, as);
            }
         }
      }
      return masterMap;
   }

   private TreeMap<String, ArchiveStats> archiveProcessor(List<String> archiveList) {

      TreeMap<String, ArchiveStats> archivesDetails = new TreeMap<>();

      for(String a : archiveList) {
         try
         {
            ZipFile zipFile = new ZipFile(a);
            Enumeration<? extends ZipEntry> zEntries = zipFile.entries();

            int cnt = 0;
            TreeSet<String> dirNameList = new TreeSet<>();
            while(zEntries.hasMoreElements()) {
               ZipEntry entry = zEntries.nextElement();
               if (!entry.isDirectory()) {
                  String name = entry.getName();
                  if (name.endsWith(".class"))
                  {
                     int indx = name.lastIndexOf("/");
                     if (indx > -1)
                     {
                        String pkgName = name.substring(0, indx).replace("/", ".");
                        dirNameList.add(pkgName);
                        cnt++;
                     }
                  }
               }
            }
            /***
            // debug
            System.out.println("- " + a);
            for (String s : dirNameList) {
               System.out.println("  * " + s);
            }
            ***/
            archivesDetails.put(a, new ArchiveStats(a, dirNameList, cnt));

         } catch (IOException e) {
            System.out.println(e);
         }
      }
      return archivesDetails;
   }

   private List<String> parseClassPath(String line) {
      List<String> archiveList = new ArrayList<>();
      String[] cpArr = line.split(":");

      for(int i=0; i < cpArr.length; i++) {
         archiveList.add(cpArr[i]);
         //System.out.println(cpArr[i]);
      }

      return archiveList;
   }
}
