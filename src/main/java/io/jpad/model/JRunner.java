package io.jpad.model;


import javax.tools.JavaFileObject;
import java.io.File;
import java.util.List;


class JRunner {

    public static void main(String query) throws Exception {

        File f = new File(QjInterpreter.classOutputFolder);

        f.mkdirs();


        String PRE = "package com.timestored; import static com.timestored.JRunner.*; import java.util.Arrays;public class Testy {  \tpublic static void p(Object o) {\t\t\tString s = (o instanceof double[]) ? Arrays.toString((double[])o) \t\t\t\t: (o instanceof int[]) ? Arrays.toString((int[])o)\t\t\t\t: (o instanceof boolean[]) ? Arrays.toString((boolean[])o)\t\t\t\t: o.toString(); \tif(s.length()>80) { s = s.substring(0,80) + \"...\"; };   System.out.println(s);}public void run() {      p(";


        String POST = "); } }";


        String classStr = PRE + query + POST;

        try {

            if (query.trim().length() > 0) {

                JavaFileObject file = new MyInterpreter.InMemoryJavaFileObject("com.timestored.JRunner", classStr);


                MyInterpreter.deleteDir(f);

                f.mkdirs();

                MyInterpreter.compile(List.of(file));


                MyInterpreter.runIt("com.timestored.Testy", "run");

            }

        } catch (Exception exception) {

            exception.printStackTrace();

        }

    }

}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\model\JRunner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */