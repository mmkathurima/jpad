package io.jpad.model;


import javax.tools.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


class MyInterpreter {
    private static final String classOutputFolder = "tt";


    private static JavaFileObject getJavaFileObject() {

        StringBuilder contents = new StringBuilder("package math;public class Calculator {   public void testAdd() {     System.out.println(200+300);   }   } ");


        JavaFileObject so = null;


        try {

            so = new InMemoryJavaFileObject("math.Calculator", contents.toString());

        } catch (Exception exception) {

            exception.printStackTrace();

        }

        return so;

    }


    public static void compile(Iterable<? extends JavaFileObject> files) {

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();


        MyDiagnosticListener c = new MyDiagnosticListener();

        StandardJavaFileManager fileManager = compiler.getStandardFileManager(c, Locale.ENGLISH, null);


        Iterable<String> options = Arrays.asList("-d", classOutputFolder);

        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, c, options, null, files);


        Boolean result = task.call();

    }


    public static void runIt(String classname, String methodName) {

        File file = new File(classOutputFolder);


        try {
            URL url = file.toURL();

            URL[] urls = {url};


            ClassLoader loader = new URLClassLoader(urls);


            Class<?> thisClass = loader.loadClass(classname);


            Class[] params = new Class[0];

            Object[] paramsObj = new Object[0];

            Object instance = thisClass.newInstance();

            Method thisMethod = thisClass.getDeclaredMethod(methodName, params);


            thisMethod.invoke(instance, paramsObj);
        } catch (MalformedURLException e) {
        } catch (ClassNotFoundException e) {
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }


    public static void main(String[] args) throws Exception {

        InputStreamReader isr = new InputStreamReader(System.in);

        BufferedReader br = new BufferedReader(isr);


        File f = new File(classOutputFolder);

        f.mkdirs();


        String query = "";


        do {

            if (!query.trim().startsWith("/")) {


                try {


                    String PRE = "package com.timestored; import static com.timestored.Jkdb.*; import java.util.Arrays;public class Testy {  \tpublic static void p(Object o) {\t\t\tString s = (o instanceof double[]) ? Arrays.toString((double[])o) \t\t\t\t: (o instanceof int[]) ? Arrays.toString((int[])o)\t\t\t\t: (o instanceof boolean[]) ? Arrays.toString((boolean[])o)\t\t\t\t: o.toString(); \tif(s.length()>80) { s = s.substring(0,80) + \"...\"; };   System.out.println(s);}public void run() {      p(";


                    String POST = "); } }";


                    String classStr = PRE + query.replace(";", ",").replace("[", "(").replace("]", ")").replace("=", "equal").replace("?", "choose") + POST;


                    if (query.trim().length() > 0) {

                        JavaFileObject file = new InMemoryJavaFileObject("com.timestored.Testy", classStr);


                        deleteDir(f);

                        f.mkdirs();

                        compile(List.of(file));


                        runIt("com.timestored.Testy", "run");

                    }


                } catch (Exception exception) {

                    exception.printStackTrace();

                }

            }

            System.out.print("q)");

            query = br.readLine();


        }
        while (!query.equalsIgnoreCase("\\\\"));

    }


    public static boolean deleteDir(File dir) {

        if (dir.isDirectory()) {

            String[] children = dir.list();

            for (int i = 0; i < children.length; i++) {

                boolean success = deleteDir(new File(dir, children[i]));

                if (!success) {

                    return false;

                }

            }

        }


        return dir.delete();

    }


    public static class MyDiagnosticListener
            implements DiagnosticListener<JavaFileObject> {

        public void report(Diagnostic<? extends JavaFileObject> diagnostic) {

            System.out.println("Message->" + diagnostic
                    .getMessage(Locale.ENGLISH));

        }

    }


    public static class InMemoryJavaFileObject
            extends SimpleJavaFileObject {
        private String contents = null;


        public InMemoryJavaFileObject(String className, String contents) throws Exception {

            super(URI.create("string:///" + className.replace('.', '/') + JavaFileObject.Kind.SOURCE.extension), JavaFileObject.Kind.SOURCE);


            this.contents = contents;

        }


        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {

            return this.contents;

        }

    }

}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\model\MyInterpreter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */