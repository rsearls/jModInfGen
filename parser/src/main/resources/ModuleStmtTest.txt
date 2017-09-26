module my.other.ModuleStmtTest {

 exports one.org.my.test.pkg to dodge.dart, chevy.truck; // comment test

 exports oneOne.org.my.test.pkg;
 exports mypkg;

 requires two.org.my.test.pkg;
 requires three.org.my.test.pkg; // comment test

 opens four.org.test.pkg to ford.escort;
 opens eight.org.test.pkg;

  /**/
 uses tooth.brush;
 provides fiveOne.org.test.pkg with green.beans;
 provides five.org.test.pkg with green.beans, orange.carrots,
        red.potatoes;
/**/
 requires transitive six.org.my.test.pkg;
 requires static seven.org.my.test.pkg;

}

