package org.jboss.jmodinfgen.qdox;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaSource;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by rsearls on 1/2/18.
 */
public class SourceTreeImports {

   public TreeMap<String,AtomicInteger> process(String arg) {

      JavaProjectBuilder builder = new JavaProjectBuilder();
      builder.addSourceTree(new File(arg));
      Collection<JavaSource> jSrcList = builder.getSources();

      TreeSet<String> sourceTreePkgList = createUniquePkgList(jSrcList);
      TreeMap<String, AtomicInteger> externalImportMap = new TreeMap<>();

      for (JavaSource jSrc : jSrcList)
      {
         //System.out.println("- src:  " + jSrc.getURL().toString() + "  class cnt: " + jSrc.getClasses().size());
         JavaClass jClazz = jSrc.getClasses().get(0);

         //System.out.println("- " + jClazz.getFullyQualifiedName());

         processImports(jSrc.getImports(), sourceTreePkgList, externalImportMap);
      }
      //TreeMap<String,AtomicInteger> tabulatedImportPkgs = tabulateImportNames(externalImportMap);
      TreeMap<String,AtomicInteger> tabulatedImportPkgs = tabulateImportNamesTwo(externalImportMap);
      //dumpMap(externalImportMap, "---- external imports count ----");

      //-dumpMap(tabulatedImportPkgs, "#### parent pkg count ####");
      return tabulatedImportPkgs;
   }

   /**
    *
    * @param importList
    * @param sourceTreePkgList
    * @param externalImportMap
    */
   private void processImports(List<String> importList,
                               TreeSet<String> sourceTreePkgList,
                               TreeMap<String, AtomicInteger> externalImportMap) {

      for(String importName : importList) {

         String s = importName;
         int indx = importName.lastIndexOf(".");
         if (indx > -1) {
            s = importName.substring(0, indx);
         }

         // Filter out packages in this source tree and jdk packages
         // Retaining external packages
         if (!sourceTreePkgList.contains(s)) {
            if (!importName.startsWith("java.") && !importName.startsWith("javax."))
            {
               //System.out.println("  imp: " + importName);
               registerImport(importName, externalImportMap);
            }
         }
      }
   }

   /**
    * Unique pkgname of module's source classes.
    * @param jSrcList
    * @return
    */
   private TreeSet<String> createUniquePkgList(Collection<JavaSource> jSrcList) {
      TreeSet<String> uniqueList = new TreeSet<String>();
      //System.out.println("--- Package List ---");
      for (JavaSource jSrc : jSrcList)
      {
         if (!uniqueList.contains(jSrc.getPackageName()))
         {
            uniqueList.add(jSrc.getPackageName());
            //System.out.println("-- " + jSrc.getPackageName());
         }
      }
      //System.out.println("--- ---------- ---");
      return uniqueList;
   }

   /**
    *
    * @param importName
    * @param map
    */
   private void registerImport(String importName, TreeMap<String,AtomicInteger> map) {

      // record import; count dup references
      AtomicInteger value = map.get(importName);
      if (value == null) {
         map.put(importName, new AtomicInteger(1));
      } else {
         value.incrementAndGet();
      }
   }

   private TreeMap<String,AtomicInteger> tabulateImportNamesTwo(TreeMap<String, AtomicInteger> importsMap){

      TreeMap<String,AtomicInteger> subPartsMap = new TreeMap<>();

      for(String importName: importsMap.keySet()) {
         int indx = importName.lastIndexOf(".");
         if (indx > -1){
            String importPkg = importName.substring(0, indx);
            registerImport(importPkg, subPartsMap);
         }
      }
      return subPartsMap;
   }

   /**
    * Count each sub part of a package name.
    * @param importsMap
    */
   /** todo OBSOLETE **/
   private TreeMap<String,AtomicInteger> tabulateImportNames(TreeMap<String, AtomicInteger> importsMap){

      TreeMap<String,AtomicInteger> subPartsMap = new TreeMap<>();

      for(String importName: importsMap.keySet()) {
         String[] pkgSubParts = importName.split("\\.");
         StringBuilder sb = new StringBuilder();
         int cnt = pkgSubParts.length -1;

         for (int i=0; i < cnt; i++) {
            sb.append(pkgSubParts[i]);
            registerImport(sb.toString(), subPartsMap);
            if (i < cnt-1)
            {
               sb.append(".");
            }
         }
      }
      return subPartsMap;
   }

   private void dumpMap(TreeMap<String, AtomicInteger> importsCntMap, String header) {
      System.out.println(header);
      for (Map.Entry<String, AtomicInteger> entry : importsCntMap.entrySet())
      {
         System.out.println(entry.getKey() + "  cnt: " + entry.getValue().get());
      }
      System.out.println("--------------------------------");
   }


   /**
    *
    * @param tabulatedImportPkgs
    * @param depository
    */
   public TreeSet<ArchiveStats> evaluate(TreeMap<String,AtomicInteger>tabulatedImportPkgs,
                        TreeMap<String, Set<ArchiveStats>> depository) {

      TreeSet<ArchiveStats> accumlatedInfo = new TreeSet<>();
      for (Map.Entry<String,AtomicInteger> entry : tabulatedImportPkgs.entrySet())
      {
         String key = entry.getKey();
         if (depository.containsKey(key)) {
            AtomicInteger value = entry.getValue();

            for(ArchiveStats as : depository.get(key)) {
               as.add(value.get());
               accumlatedInfo.add(as);
            }
         }
      }
/***
      for(ArchiveStats as : accumlatedInfo) {
         System.out.println(as.getShortName() + "\t\t" + as.getPercent()
                 + "\t\t[ " + as.getRefSum() + " / " + as.getClassCnt() + " ]" );
      }
***/
      return accumlatedInfo;
   }
}
