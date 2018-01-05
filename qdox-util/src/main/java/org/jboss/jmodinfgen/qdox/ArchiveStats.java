package org.jboss.jmodinfgen.qdox;

import java.util.TreeSet;
import java.io.File;
import java.lang.Comparable;

/**
 * Created by rsearls on 1/4/18.
 */
public class ArchiveStats implements Comparable<ArchiveStats> {
   private String fullJarName;
   private String jarName;
   private TreeSet<String> pkgNameList = new TreeSet<>();
   private int classCnt=0;
   private int totalRefs = 0;

   public ArchiveStats(String jarName, TreeSet<String> pkgNameList, int classCnt){
      this.fullJarName = jarName;
      this.pkgNameList = pkgNameList;
      this.classCnt = classCnt;

      File f = new File(fullJarName);
      this.jarName = f.getName();
   }

   public String getShortName(){
      return jarName;
   }

   public String getJarName() {
      return fullJarName;
   }

   public TreeSet<String> getPkgList() {
      return pkgNameList;
   }

   public int getClassCnt() {
      return classCnt;
   }

   public void add(int cnt) {
      totalRefs = totalRefs + cnt;
   }

   public float getRefSum() {
      float f = totalRefs;
      return f;
   }

   public float getPercent() {
      float tf = totalRefs;
      float cc = classCnt;
      return (tf * 100.0f)/cc;
   }

   public int compareTo(ArchiveStats other) {
      return other.getShortName().compareTo(this.getShortName());
   }
}
