package com.timestored.misc;

import com.google.common.io.Files;
import com.timestored.swingxx.SaveableFrame;
import org.junit.Assert;

import java.awt.Component;
import java.io.*;


public class TestHelper {
    private static final int DEFAULT_WIDTH = 400;
    private static final int DEFAULT_HEIGHT = 400;
    private static final String TEST_OUTPUT_DIR = "target" + File.separator + "testout" + File.separator;


    public static String getOutDir() throws IOException {
        System.out.println((new File(".")).getAbsolutePath());

        Files.createParentDirs(new File(TEST_OUTPUT_DIR + File.separator + "a.txt"));
        return TEST_OUTPUT_DIR;
    }


    public static File saveComponentImage(Component c, String filename) throws IOException {
        File f = new File(getOutDir() + filename);
        SaveableFrame.saveComponentImage(c, 400, 400, f, false);
        return f;
    }


    public static boolean assertFilesMatch(File file, InputStream knownFile) {
        if (knownFile != null) {
            try {
                String msg = "checking file match for: " + file.getName();
                Assert.assertTrue(msg, isEqual(new FileInputStream(file), knownFile));
                return true;
            } catch (FileNotFoundException e) {
                Assert.fail("generated image not found: " + e);
            } catch (IOException e) {
                Assert.fail("generated image IO fail: " + e);
            }
        } else {
            Assert.fail("known image not found: " + file.getName());
        }
        return false;
    }


    private static boolean isEqual(InputStream i1, InputStream i2) throws IOException {
        byte[] buf1 = new byte[65536];
        byte[] buf2 = new byte[65536];
        try {
            DataInputStream d2 = new DataInputStream(i2);
            int len;
            while ((len = i1.read(buf1)) > 0) {
                d2.readFully(buf2, 0, len);
                for (int i = 0; i < len; i++) {
                    if (buf1[i] != buf2[i]) return false;
                }
            }
            return (d2.read() < 0);
        } catch (EOFException ioe) {
            return false;
        } finally {
            i1.close();
            i2.close();
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\misc\TestHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */