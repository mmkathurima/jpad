package io.jpad;

import org.junit.Assert;
import org.junit.Test;


public class JpadHeaderTest {
    private final String messyDoc = "       /**    \r\n    *    @title      tittle tattle    \r\n   *     @url     http://jpad.io/example/j/tittle-tattle     \r\n    *    @tags    java,kdb,tags\r\n   *    @description     How to do stuff in a cool way.    \r\n */";


    private final String perfectDoc = "/**\r\n * @title tittle tattle\r\n * @url http://jpad.io/example/j/tittle-tattle\r\n * @tags java,kdb,tags\r\n * @description How to do stuff in a cool way.\r\n */";


    @Test
    public void testPerfectFormat() {
        checkDE(JpadHeader.extract(this.perfectDoc));
    }


    @Test
    public void testMessyFormat() {
        checkDE(JpadHeader.extract(this.messyDoc));
    }


    @Test
    public void testInvalidDocs() {
        JpadHeader.extract("");
        JpadHeader.extract("/***");
        JpadHeader.extract("/**");
        JpadHeader.extract("/*");
    }


    @Test
    public void testAsComment() {
        String[] docs = {this.perfectDoc, this.messyDoc};

        for (String d : docs) {
            JpadHeader j = JpadHeader.extract(this.perfectDoc);
            String c = j.getAsComment();
            JpadHeader k = JpadHeader.extract(c);
            System.out.println("c = " + c);
            System.out.println("j = " + j);
            System.out.println("k = " + k);
            Assert.assertEquals(j, k);
        }
    }


    @Test
    public void testCompactUrlOnly() {
        checkURL("/** @url http://jpad.io/example/j/tittle-tattle */");
        checkURL("/*http://jpad.io/example/j/tittle-tattle*/");
        checkURL("/*****http://jpad.io/example/j/tittle-tattle********/");
        checkURL("/* **http://jpad.io/example/j/tittle-tattle**** **/");
        checkURL("/* **   http://jpad.io/example/j/tittle-tattle   **** **/");
    }

    private void checkURL(String doc) {
        JpadHeader jpde = JpadHeader.extract(doc);
        Assert.assertEquals("http://jpad.io/example/j/tittle-tattle", jpde.getUrl());
        Assert.assertEquals("tittle-tattle", jpde.getTitle());
    }

    private void checkDE(JpadHeader jpde) {
        Assert.assertEquals("tittle tattle", jpde.getTitle());
        Assert.assertEquals("http://jpad.io/example/j/tittle-tattle", jpde.getUrl());
        Assert.assertEquals("java,kdb,tags", jpde.getTags());
        Assert.assertEquals("How to do stuff in a cool way.", jpde.getDescription());
    }


    @Test
    public void testReplaceComment() {
        String partialDoc = "/**\n * @title tittle tattle\n * @url http://jpad.io/example/j/tittle-tattle\n */";


        JpadHeader j = JpadHeader.extract(partialDoc);
        String actual = j.replaceComment("2+2");
        Assert.assertEquals(partialDoc + "\n" + "2+2", actual);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\JpadHeaderTest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */