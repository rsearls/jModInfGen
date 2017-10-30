/* Existing data to be merged with analyzed data.
 */
module my.mod.service {
   exports org.jboss.wsf.stack.cxf to my.mod.client;
   opens org.jboss.wsf.stack.cxf.transport to
           my.mod.client,
           not.my.mod.client;
   opens org.jboss.wsf.stack.cxf.management;
   uses some.database.driver;
   requires  static java.xml.ws;
   requires  transitive java.xml;
}
