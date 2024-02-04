package com.timestored.misc;

import com.google.common.collect.Lists;

import java.beans.ConstructorProperties;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

public class DynamicClassLoader {
    private static final Logger log = Logger.getLogger(DynamicClassLoader.class.getName());

    public static <T> List<T> loadInstances(File dir, Class<T> interfaceWanted, boolean onlyLoadFirstMatch) {
        log.info("Searching for plugins in folder: " + dir.getAbsolutePath());
        InstanceFinderVisitor<T> v = new InstanceFinderVisitor<>(interfaceWanted, onlyLoadFirstMatch);
        try {
            visitJarFileJavaClasses(dir, v);
        } catch (IOException e) {
            log.warning("Problem while searching for plugins" + e);
        }
        return v.instances;
    }

    private static void visitJarFileJavaClasses(File dir, Visitor<ClassDetails> visitor) throws IOException {
        if (!dir.exists()) {
            return;
        }
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".jar");
            }
        };

        String[] children = dir.list(filter);
        if (children != null)
            for (String s : children) {
                String filename = dir.getAbsolutePath() + "/" + s;
                log.info("Looking for plugins inside: " + filename);

                URL url = new URL("jar:file:" + filename + "/!/");
                JarURLConnection conn = (JarURLConnection) url.openConnection();
                JarFile jarFile = conn.getJarFile();

                Enumeration<JarEntry> e = jarFile.entries();
                while (e.hasMoreElements()) {
                    JarEntry entry = e.nextElement();
                    String name = entry.getName();

                    String externalName = name.substring(0, name.indexOf('.')).replace('/', '.');
                    if (!entry.isDirectory() && name.endsWith(".class") && !visitor.visit(new ClassDetails(externalName, url)))
                        return;
                }
            }
    }

    private static class InstanceFinderVisitor<T>
            implements Visitor<ClassDetails> {
        private final Class<T> interfaceWanted;

        private final boolean stopAtFirstMatch;

        private final List<T> instances;

        @ConstructorProperties({"interfaceWanted", "stopAtFirstMatch"})
        public InstanceFinderVisitor(Class<T> interfaceWanted, boolean stopAtFirstMatch) {
            this.instances = Lists.newArrayList();
            this.interfaceWanted = interfaceWanted;
            this.stopAtFirstMatch = stopAtFirstMatch;
        }

        public List<T> getInstances() {
            return this.instances;
        }

        public boolean visit(DynamicClassLoader.ClassDetails classDetails) {

            try {
                URLClassLoader loader = new URLClassLoader(new URL[]{classDetails.getClassUrl()});

                try {
                    Class<?> c = loader.loadClass(classDetails.className);
                    Class[] interfaces = c.getInterfaces();
                    for (Class anInterface : interfaces) {
                        if (this.interfaceWanted == anInterface) {
                            log.info("Found Plugin with correct interface: " + classDetails.className);
                            Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                            addURL.setAccessible(true);
                            ClassLoader cl = ClassLoader.getSystemClassLoader();
                            addURL.invoke(cl, classDetails.getClassUrl());
                            Object o = c.newInstance();
                            this.instances.add((T) o);
                            return !this.stopAtFirstMatch;
                        }
                    }
                } catch (NoClassDefFoundError | VerifyError | IllegalAccessError e) {
                }
            } catch (IllegalAccessException | ClassNotFoundException | InstantiationException |
                     InvocationTargetException | IllegalArgumentException | SecurityException | NoSuchMethodException e) {
            }

            return true;
        }
    }

    private static class ClassDetails {
        private final String className;
        private final URL classUrl;

        @ConstructorProperties({"className", "classUrl"})
        public ClassDetails(String className, URL classUrl) {
            this.className = className;
            this.classUrl = classUrl;
        }

        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof ClassDetails)) return false;
            ClassDetails other = (ClassDetails) o;
            if (!other.canEqual(this)) return false;
            if (!Objects.equals(this.className, other.className))
                return false;
            return Objects.equals(this.classUrl, other.classUrl);
        }

        protected boolean canEqual(Object other) {
            return other instanceof ClassDetails;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            Object $className = this.className;
            result = result * 59 + (($className == null) ? 0 : $className.hashCode());
            Object $classUrl = this.classUrl;
            return result * 59 + (($classUrl == null) ? 0 : $classUrl.hashCode());
        }

        public String toString() {
            return "DynamicClassLoader.ClassDetails(className=" + this.className + ", classUrl=" + this.classUrl + ")";
        }

        public String getClassName() {
            return this.className;
        }

        public URL getClassUrl() {
            return this.classUrl;
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\misc\DynamicClassLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */