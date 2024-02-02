package io.jpad.model;


import com.google.common.collect.Lists;

import java.io.File;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ClassFinder {
    private static final Logger log = Logger.getLogger(ClassFinder.class.getName());


    public static <T> List<Class<T>> findClasses(final Predicate<String> filter, final Class<T> interfaceWanted) {

        final List<Class<T>> instances = Lists.newArrayList();


        findClasses(new Visitor<String>() {

            public boolean visit(String t) {

                if (filter.test(t)) {

                    try {

                        Class<?> cls = Class.forName(t);

                        if (interfaceWanted.isAssignableFrom(cls)) {

                            instances.add((Class<T>) cls);

                        }

                    } catch (ClassNotFoundException | NoClassDefFoundError e) {

                        ClassFinder.log.log(Level.WARNING, "problem loading plugin", e);

                    }

                }

                return true;

            }

        });

        return instances;

    }


    public static void findClasses(Visitor<String> visitor) {

        String classpath = System.getProperty("java.class.path");

        String[] paths = classpath.split(System.getProperty("path.separator"));


        String javaHome = System.getProperty("java.home");

        File file = new File(javaHome + File.separator + "lib");

        if (file.exists()) {

            findClasses(file, file, true, visitor);

        }


        for (String path : paths) {

            file = new File(path);

            if (file.exists()) {

                findClasses(file, file, true, visitor);

            }

        }

    }


    private static boolean findClasses(File root, File file, boolean includeJars, Visitor<String> visitor) {

        if (file.isDirectory()) {

            for (File child : file.listFiles()) {

                if (!findClasses(root, child, includeJars, visitor)) {

                    return false;

                }

            }


        } else if (file.getName().toLowerCase().endsWith(".jar") && includeJars) {

            JarFile jar = null;

            try {

                jar = new JarFile(file);

            } catch (Exception ex) {
            }


            if (jar != null) {

                Enumeration<JarEntry> entries = jar.entries();

                while (entries.hasMoreElements()) {

                    JarEntry entry = entries.nextElement();

                    String name = entry.getName();

                    int extIndex = name.lastIndexOf(".class");

                    if (extIndex > 0 &&
                            !visitor.visit(name.substring(0, extIndex).replace("/", "."))) {

                        return false;

                    }

                }


            }


        } else return !file.getName().toLowerCase().endsWith(".class") ||
                visitor.visit(createClassName(root, file));


        return true;

    }


    private static String createClassName(File root, File file) {

        StringBuffer sb = new StringBuffer();

        String fileName = file.getName();

        sb.append(fileName, 0, fileName.lastIndexOf(".class"));

        file = file.getParentFile();

        while (file != null && !file.equals(root)) {

            sb.insert(0, '.').insert(0, file.getName());

            file = file.getParentFile();

        }

        return sb.toString();

    }

}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\model\ClassFinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */