package io.jpad;

import org.fife.rsta.ac.java.PackageMapNode;
import org.fife.rsta.ac.java.buildpath.LibraryInfo;
import org.fife.rsta.ac.java.classreader.ClassFile;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Mapping of class names to <code>ClassFile</code>s. This information is
 * cached even though it's also cached at the <code>JarReader</code> level
 * because the class definitions are effectively immutable since they're on
 * the classpath. This allows you to theoretically share a single
 * <code>ClasspathLibraryInfo</code> across several different jar managers.
 */
public class JDK9ClasspathLibraryInfo extends LibraryInfo {
    private final Map<String, ClassFile> classNameToClassFile;
    private final Map<String, String> classNameToFullyQualified;
    private final Map<String, String> classNameToModule;
    private final Path pathToJrt;

    /**
     * Constructor.
     * <p>
     * This may be <code>null</code>.
     */
    public JDK9ClasspathLibraryInfo() {
        this.setSourceLocation(null);
        this.classNameToClassFile = new HashMap<>();
        this.classNameToFullyQualified = new HashMap<>();
        this.classNameToModule = new HashMap<>();

        Path pathToJRE = Paths.get(System.getProperty("java.home"));
        this.pathToJrt = pathToJRE.resolve("lib").resolve("jrt-fs.jar");

        // read path entries and cache them
        if (Files.exists(this.pathToJrt)) {
            try (URLClassLoader loader = new URLClassLoader(new URL[]{this.pathToJrt.toUri().toURL()});
                 FileSystem fs = FileSystems.newFileSystem(URI.create("jrt:/"),
                         Collections.emptyMap(),
                         loader);
                 Stream<Path> ls = Files.list(fs.getPath("/modules"))) {
                List<Path> paths = ls.collect(Collectors.toList());
                this.cacheFiles(paths);
            } catch (Exception ex) {
                System.err.println("Error loading JDK9+ module information" + ex);
            }
        }
    }

    private void cacheFiles(List<Path> paths) throws IOException {
        for (Path p : paths) {
            if (Files.isDirectory(p)) {
                try (Stream<Path> ls = Files.list(p)) {
                    this.cacheFiles(ls.collect(Collectors.toList()));
                }
            } else {
                if (p.toString().endsWith(".class")) {
                    StringBuilder className = new StringBuilder();
                    StringBuilder fqName = new StringBuilder();
                    // index 0 is the modules dir, index 1 is the module name (eg. java.base), so we start at index 2
                    for (int i = 2; i < p.getNameCount(); i++) {
                        if (className.length() > 0) {
                            className.append("/");
                            fqName.append(".");
                        }
                        className.append(p.getName(i));
                        if (!p.getName(i).toString().endsWith(".class"))
                            fqName.append(p.getName(i));
                        else
                            fqName.append(p.getName(i).toString(), 0, p.getName(i).toString().lastIndexOf(".class"));
                    }

                    this.classNameToModule.put(className.toString(), p.getName(1).toString());
                    String entryName = className.toString();
                    this.classNameToClassFile.put(entryName, null);
                    this.classNameToFullyQualified.put(entryName, fqName.toString());
                }
            }
        }
    }

    public int compareTo(@NotNull LibraryInfo o) {
        if (o == this)
            return 0;
        int res = -1;

        if (o instanceof JDK9ClasspathLibraryInfo) {
            JDK9ClasspathLibraryInfo other = (JDK9ClasspathLibraryInfo) o;
            res = this.classNameToClassFile.size() - other.classNameToClassFile.size();
            if (res == 0) {
                for (String key : this.classNameToClassFile.keySet()) {
                    if (!other.classNameToClassFile.containsKey(key)) {
                        res = -1;
                        break;
                    }
                }
            }
        }

        return res;
    }

    @Override
    public ClassFile createClassFile(String entryName) throws IOException {
        // System.out.println("Create class file for entry: " + entryName);
        // NOTE: entryName always ends in ".class", so our map must account
        // for this.
        ClassFile cf = null;
        if (this.classNameToClassFile.containsKey(entryName)) {
            cf = this.classNameToClassFile.get(entryName);
            if (cf == null) {
                cf = this.createClassFileImpl(entryName);
                this.classNameToClassFile.put(entryName, cf);
            }
        }
        return cf;
    }

    private ClassFile createClassFileImpl(String res) {
        // System.out.println("Create class file IMPL for entry: " + res);

        ClassFile cf = null;
        // need to get the binary form

        String module = this.classNameToModule.get(res);

        if (!module.trim().isEmpty()) {
            try (URLClassLoader loader = new URLClassLoader(new URL[]{this.pathToJrt.toUri().toURL()});
                 FileSystem fs = FileSystems.newFileSystem(URI.create("jrt:/"),
                         Collections.emptyMap(),
                         loader)) {
                byte[] result = Files.readAllBytes(fs.getPath("/modules/" + module + "/" + res));
                // if succeeded, we create the ClassFile
                if (result.length > 0) {
                    try {
                        DataInputStream din = new DataInputStream(new ByteArrayInputStream(result));
                        cf = new ClassFile(din);
                    } catch (Exception ex) {
                        // ignore exception
                    }
                }
            } catch (Exception ex) {
            }
        }

        return cf;
    }

    @Override
    public PackageMapNode createPackageMap() {
        PackageMapNode packageMap = new PackageMapNode();
        for (String className : this.classNameToClassFile.keySet())
            packageMap.add(className);
        return packageMap;
    }

    /**
     * Since stuff on the current classpath never changes (we don't support
     * hotswapping), this method always returns <code>0</code>.
     *
     * @return <code>0</code> always.
     */
    @Override
    public long getLastModified() {
        return 0;
    }

    @Override
    public String getLocationAsString() {
        return null;
    }

    @Override
    public int hashCode() {
        return this.classNameToClassFile.hashCode();
    }

    @Override
    public int hashCodeImpl() {
        return Objects.hashCode(this);
    }

    @Override
    public void bulkClassFileCreationEnd() throws IOException {

    }

    @Override
    public void bulkClassFileCreationStart() throws IOException {

    }

    @Override
    public ClassFile createClassFileBulk(String string) throws IOException {
        return this.createClassFileImpl(string);
    }
}
