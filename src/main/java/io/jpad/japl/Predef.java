package io.jpad.japl;


import java.util.Locale;


public class Predef {

    public static void exit() {

        System.exit(0);
    }

    public static void exit(int status) {

        System.exit(status);

    }


    public static void print(boolean b) {
        System.out.print(b);
    }


    public static void print(int b) {
        System.out.print(b);
    }


    public static void print(short b) {
        System.out.print(b);
    }


    public static void print(long b) {
        System.out.print(b);
    }


    public static void print(double b) {
        System.out.print(b);
    }


    public static void print(String b) {
        System.out.print(b);
    }


    public static void print(char[] b) {
        System.out.print(b);
    }


    public static void print(char b) {
        System.out.print(b);
    }

    public static void print(Object b) {

        System.out.print(b);

    }


    public static void println(boolean b) {
        System.out.println(b);
    }


    public static void println(int b) {
        System.out.println(b);
    }


    public static void println(short b) {
        System.out.println(b);
    }


    public static void println(long b) {
        System.out.println(b);
    }


    public static void println(double b) {
        System.out.println(b);
    }


    public static void println(String b) {
        System.out.println(b);
    }


    public static void println(char[] b) {
        System.out.println(b);
    }


    public static void println(char b) {
        System.out.println(b);
    }

    public static void println(Object b) {

        System.out.println(b);

    }


    public static void println(String format, Object... args) {
        System.out.printf(format, args);
    }

    public static void println(Locale l, String format, Object... args) {

        System.out.printf(l, format, args);

    }


    public static String readLine() {
        return System.console().readLine();
    }


    public static String readLine(String fmt, Object... args) {
        return System.console().readLine(fmt, args);
    }


    public static char[] readPassword() {
        return System.console().readPassword();
    }


    public static char[] readPassword(String fmt, Object... args) {
        return System.console().readPassword(fmt, args);
    }


    public static boolean readBoolean() {
        while (true) {


            try {
                return Boolean.parseBoolean(readLine());
            } catch (NumberFormatException e) {
            }

        }
    }

    public static short readShort() {
        while (true) {
            try {
                return Short.parseShort(readLine());
            } catch (NumberFormatException e) {
            }
        }

    }

    public static int readInt() {
        while (true) {
            try {
                return Integer.parseInt(readLine());
            } catch (NumberFormatException e) {
            }
        }

    }

    public static long readLong() {
        while (true) {
            try {
                return Long.parseLong(readLine());
            } catch (NumberFormatException e) {
            }
        }

    }

    public static float readFloat() {
        while (true) {
            try {
                return Float.parseFloat(readLine());
            } catch (NumberFormatException e) {
            }
        }

    }

    public static double readDouble() {
        while (true) {
            try {
                return Double.parseDouble(readLine());
            } catch (NumberFormatException e) {
            }
        }

    }


}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\japl\Predef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */