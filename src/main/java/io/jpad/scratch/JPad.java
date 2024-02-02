package io.jpad.scratch;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.timestored.misc.CmdRunner;
import io.jpad.JPadConfig;
import io.jpad.model.CsvConverter;
import io.jpad.resultset.ResultSetAdapter;

import javax.swing.JOptionPane;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;


public class JPad {


    private static final Cache<Object, Object> cache;
    private static final int percentageDone = 0;
    public static boolean isGUI = false;

    static {

        cache = CacheBuilder.newBuilder().initialCapacity(128).maximumSize(128L).build();

    }


    public static void cacheClear(String name) {

        cache.invalidate(name);

    }


    public static void cacheClearAll() {

        cache.invalidateAll();

    }


    public static <T> T cache(String name, Class<T> cls, Callable<T> callable) {

        Object o = cache.getIfPresent(name);

        if (o != null && o.getClass().equals(cls)) {

            return (T) o;

        }


        cache.invalidate(name);

        try {

            o = cache.get(name, callable);

        } catch (ExecutionException e) {
        }

        if (o != null && o.getClass().equals(cls)) {

            return (T) o;

        }


        return null;

    }


    public static String cmd(String command) throws IOException {

        return CmdRunner.run(command);

    }


    public static String cmd(String[] commands) throws IOException {

        return CmdRunner.run(commands);

    }


    public static void writeCsv(Object o, File file) throws IOException {

        try (PrintWriter pw = new PrintWriter(file)) {

            ResultSet rs;

            if (o instanceof ResultSet) {

                rs = (ResultSet) o;

            } else {

                rs = ResultSetAdapter.get(o);

            }

            if (rs != null) {

                try {

                    CsvConverter.writeTo(pw, rs);

                } catch (SQLException e) {

                    throw new IOException(e);

                }

            }

        }

    }


    public static int getArg(String[] args, int idx, String title, int defaultNum) {

        return Integer.parseInt(getArg(args, idx, title, "" + defaultNum));

    }


    public static double getArg(String[] args, int idx, String title, double defaultNum) {

        return Double.parseDouble(getArg(args, idx, title, "" + defaultNum));

    }


    public static float getArg(String[] args, int idx, String title, float defaultNum) {

        return Float.parseFloat(getArg(args, idx, title, "" + defaultNum));

    }


    public static long getArg(String[] args, int idx, String title, long defaultNum) {

        return Long.parseLong(getArg(args, idx, title, "" + defaultNum));

    }


    public static String getArg(String[] args, int idx, String title, String defaultValue) {

        String r = null;

        if (idx < 0) {

            throw new IllegalArgumentException("Invalid Args[Idx]");

        }

        if (idx < args.length) {

            r = args[idx];

        } else {

            r = requestInput(title, defaultValue);

        }

        if (r.trim().isEmpty()) {

            return defaultValue;

        }

        return r;

    }


    public static int requestInput(String title, int defaultNum) {

        return Integer.parseInt(requestInput(title, "" + defaultNum));

    }


    public static double requestInput(String title, double defaultNum) {

        return Double.parseDouble(requestInput(title, "" + defaultNum));

    }


    public static float requestInput(String title, float defaultNum) {

        return Float.parseFloat(requestInput(title, "" + defaultNum));

    }


    public static long requestInput(String title, long defaultNum) {

        return Long.parseLong(requestInput(title, "" + defaultNum));

    }


    public static String requestInput(String title, String defaultValue) {

        String r, message = "Please enter " + title + ":";

        if (isGUI) {

            r = JOptionPane.showInputDialog(message, defaultValue);

        } else {

            System.out.println(message + "[" + defaultValue + "]");

            try (Scanner sc = new Scanner(System.in)) {

                r = sc.next();

                if (r.trim().isEmpty()) {

                    r = defaultValue;

                }

            }

        }

        if (r == null) {

            throw new RuntimeException("User refused to enter argument " + title);

        }

        return r;

    }


    public static String getArg(String[] args, int idx, String title) {

        return getArg(args, idx, title, "");

    }


    public static String readLine(String message) {

        if (isGUI) {

            return JOptionPane.showInputDialog(message);

        }

        System.out.println(message);

        try (Scanner sc = new Scanner(System.in)) {

            return sc.next();

        }

    }


    private static void display(String message) {

        if (isGUI) {

            JOptionPane.showMessageDialog(null, message);

        } else {

            System.out.println(message);

        }

    }


    public static Path getScriptsFolder() {

        return JPadConfig.SCRIPTS_FOLDER.toPath();

    }


    public boolean isGUI() {

        return isGUI;

    }

}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\scratch\JPad.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */