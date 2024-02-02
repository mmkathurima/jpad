package com.timestored.misc;

import com.google.common.io.CharStreams;
import com.google.common.io.Files;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class IOUtils {
    public static String toString(File file, Charset charset, int bytes) throws IOException {
        int bytesToRead = (bytes != -1) ? bytes : (int) file.length();
        byte[] buffer = new byte[bytesToRead];
        BufferedInputStream f = new BufferedInputStream(new FileInputStream(file));
        f.read(buffer);
        f.close();
        return new String(buffer, charset);
    }


    public static String toString(File file, Charset charset) throws IOException {
        return toString(file, charset, -1);
    }


    public static String toString(File file) throws IOException {
        return toString(file, StandardCharsets.UTF_8, -1);
    }


    public static String toString(Class c, String resourceName) throws IOException {
        InputStream is = c.getResourceAsStream(resourceName);
        return CharStreams.toString(new InputStreamReader(is));
    }

    public static void writeStringToFile(String s, File file) throws IOException {
        PrintWriter pw = new PrintWriter(file, StandardCharsets.UTF_8);
        pw.print(s);
        pw.close();
    }

    public static String toString(Throwable e) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(bos));
        return bos.toString();
    }


    public static File createTempCopy(String filename, InputStream is) throws IOException {
        File tempf = Files.createTempDir();
        File f = new File(tempf, filename);
        byte[] buffer = new byte[is.available()];
        is.read(buffer);
        Files.write(buffer, f);
        return f;
    }


    public static boolean containsMoreThanMaxFiles(File file, int maxFiles) {
        return (countFiles(file, maxFiles, 0) > maxFiles);
    }

    private static int countFiles(File file, int maxFiles, int currentCount) {
        int i = currentCount;

        String[] fileNames = file.list();
        i += fileNames.length;
        if (i > maxFiles) {
            return i;
        }
        for (String fn : fileNames) {
            File f = new File(file, fn);
            if (f.isDirectory()) {
                i = countFiles(f, maxFiles, i);
                if (i > maxFiles) {
                    return i;
                }
            }
        }

        return i;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\misc\IOUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */