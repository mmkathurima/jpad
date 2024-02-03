package io.jpad;

import com.google.common.io.Files;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class JPadParamsTest {
    private static String FILE_PATH;

    @BeforeClass
    public static void makeEmptyFile() throws IOException {
        File tempFile = new File(Files.createTempDir(), "testParse.jpad");
        tempFile.createNewFile();
        FILE_PATH = tempFile.getAbsolutePath();
    }

    @Test
    public void testParse() throws IOException {
        JPadParams p = JPadParams.parse(FILE_PATH, "-args", "1");
        Assert.assertEquals("[1]", Arrays.toString(p.programArgs));
        p = JPadParams.parse(FILE_PATH, "-args", "1", "2");
        Assert.assertEquals("[1, 2]", Arrays.toString(p.programArgs));
    }

    @Test
    public void testParseEmptyArgs() throws IOException {
        JPadParams p = JPadParams.parse(FILE_PATH, "-args");
        Assert.assertEquals("[]", Arrays.toString(p.programArgs));

        p = JPadParams.parse(FILE_PATH);
        Assert.assertEquals("[]", Arrays.toString(p.programArgs));
    }

    @Test
    public void testParseCommand() {
        String[] s = JPadParams.parseCommand("-args 1");
        Assert.assertEquals("[-args, 1]", Arrays.toString(s));
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\JPadParamsTest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */