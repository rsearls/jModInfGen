package org.jboss.jmodinfgen.qdox;

import java.text.DecimalFormat;
import java.util.TreeSet;

/**
 * Created by rsearls on 1/4/18.
 */
public class Report {

   public void report(String title, TreeSet<ArchiveStats> accumlatedStats){

      // report
      System.out.println("--- "+ title +" ---");
      for(ArchiveStats as : accumlatedStats) {
         System.out.println(as.getShortName() + "\t\t" + as.getPercent()
                 + "\t\t[ " + as.getRefSum() + " / " + as.getClassCnt() + " ]" );
      }
   }

   public void reportTwo(String title, TreeSet<ArchiveStats> accumlatedStats){

      // report
      System.out.println("module\t\tarchive\t\tpercent\t\tref cnt\t\tclass cnt");
      for(ArchiveStats as : accumlatedStats) {
         System.out.println(title + "\t\t"
                 + as.getShortName() + "\t\t"
                 + as.getPercent() + "\t\t"
                 + as.getRefSum()
                 + "\t\t" + as.getClassCnt() );
      }
   }

   public void reportThree(String title, TreeSet<ArchiveStats> accumlatedStats){

      for(ArchiveStats as : accumlatedStats) {
         DecimalFormat df = new DecimalFormat("##.##");
         String formatted = df.format(as.getPercent());

         System.out.println(title + "\t\t"
                 + as.getShortName() + "\t\t"
                 + formatted + "\t\t"
                 + as.getRefSum()
                 + "\t\t" + as.getClassCnt() );
      }
   }
}
