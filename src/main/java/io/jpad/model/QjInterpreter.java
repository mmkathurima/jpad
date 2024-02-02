package io.jpad.model;


import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Locale;


class QjInterpreter {
    public static String classOutputFolder = "tt";


    public static boolean compile(Iterable<? extends JavaFileObject> files) {

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();


        MyDiagnosticListener c = new MyDiagnosticListener();

        StandardJavaFileManager fileManager = compiler.getStandardFileManager(c, Locale.ENGLISH, null);


        Iterable<String> options = Arrays.asList("-d", classOutputFolder);

        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, c, options, null, files);

        return task.call().booleanValue();

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


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\model\QjInterpreter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */