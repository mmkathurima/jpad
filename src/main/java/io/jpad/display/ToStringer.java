package io.jpad.display;

import com.timestored.sqldash.chart.TimeStringValuer;
import org.jetbrains.annotations.Nullable;

import java.text.NumberFormat;
import java.util.logging.Logger;

public class ToStringer {

    public static final int MAX_STRING_LENGTH = 1000;

    public static final int DEFAULT_MAX_FRACTION_DIGITS = 7;
    public static final String INFINITY_SYMBOL = "∞";
    private static final String SPACER = ",";
    private static final Logger LOG = Logger.getLogger(ToStringer.class.getName());

    private static final String SINGLE_ITEM_LIST_PREFIX = ",";

    private static final NumberFormat NUM_FORMAT;
    private static final TimeStringValuer TIME_VALUER = new TimeStringValuer();
    private static int decimalPlaces = 7;
    private static String formatString = "%." + decimalPlaces + "f";

    static {

        NUM_FORMAT = NumberFormat.getInstance();

        setMaximumFractionDigits(decimalPlaces);
    }

    public static void setMaximumFractionDigits(int decimalPlaces) {

        if (decimalPlaces < 0) {

            decimalPlaces = 0;
        }

        if (NUM_FORMAT instanceof java.text.DecimalFormat) {

            NUM_FORMAT.setMaximumFractionDigits(decimalPlaces);
        }

        ToStringer.decimalPlaces = decimalPlaces;

        formatString = "%." + decimalPlaces + "f";
    }

    public static String asLine(Object k) {

        return asText(k, false, true);
    }

    @Nullable
    public static String asText(Object k) {

        return asText(k, false, false);
    }

    @Nullable
    public static String asText(Object k, boolean forTable, boolean singleLine) {

        String s = null;

        try {

            if (k != null) {

                s = vs(k, forTable, singleLine);
            }
        } catch (ClassCastException e) {

        } catch (IllegalArgumentException e) {
        }

        return s;
    }

    public static String asLine(Object k, boolean forTable) {

        return asText(k, forTable, true);
    }

    private static String flatten(int[] a) {

        StringBuilder s = new StringBuilder(a.length * 3);

        s.append("int[] {");

        if (a.length > 0) {

            s.append(format(Integer.valueOf(a[0])));
        }

        for (int i = 1; i < a.length && s.length() < 1000; i++) {

            s.append(",").append(format(Integer.valueOf(a[i])));
        }

        return doEnding(s);
    }

    private static String doEnding(StringBuilder s) {

        if (s.length() >= 1000) {

            s.setLength(996);

            s.append("...");
        }

        return s.append("}").toString();
    }

    private static String flatten(short[] a) {

        StringBuilder s = new StringBuilder(a.length * 3);

        s.append("short[] {");

        if (a.length > 0) {

            s.append(format(Short.valueOf(a[0])));
        }

        for (int i = 1; i < a.length && s.length() < 1000; i++) {

            s.append(",").append(format(Short.valueOf(a[i])));
        }

        return doEnding(s);
    }

    private static String flatten(boolean[] a) {

        StringBuilder s = new StringBuilder(a.length * 3);

        s.append("boolean[] {");

        if (a.length > 0) {

            s.append(format(a[0]));
        }

        for (int i = 1; i < a.length && s.length() < 1000; i++) {

            s.append(",").append(format(a[i]));
        }

        return doEnding(s);
    }

    private static String flatten(byte[] a) {

        StringBuilder s = new StringBuilder(a.length * 3);

        s.append("byte[] {");

        if (a.length > 0) {

            s.append(format(Byte.valueOf(a[0])));
        }

        for (int i = 1; i < a.length && s.length() < 1000; i++) {

            s.append(",").append(format(Byte.valueOf(a[i])));
        }

        return doEnding(s);
    }

    private static String flatten(long[] a) {

        StringBuilder s = new StringBuilder(a.length * 3);

        s.append("long[] {");

        if (a.length > 0) {

            s.append(format(Long.valueOf(a[0])));
        }

        for (int i = 1; i < a.length && s.length() < 1000; i++) {

            s.append(",").append(format(Long.valueOf(a[i])));
        }

        return doEnding(s);
    }

    private static String flatten(float[] a) {

        StringBuilder s = new StringBuilder(a.length * 3);

        s.append("float[] {");

        if (a.length > 0) {

            s.append(format(Float.valueOf(a[0])));
        }

        for (int i = 1; i < a.length && s.length() < 1000; i++) {

            s.append(",").append(format(Float.valueOf(a[i])));
        }

        return doEnding(s);
    }

    private static String flatten(double[] a) {

        StringBuilder s = new StringBuilder(a.length * 3);

        s.append("double[] {");

        if (a.length > 0) {

            s.append(format(Double.valueOf(a[0])));
        }

        for (int i = 1; i < a.length && s.length() < 1000; i++) {

            s.append(",").append(format(Double.valueOf(a[i])));
        }

        return s.append("}").toString();
    }

    private static String flatten(String[] a) {

        StringBuilder s = new StringBuilder(a.length * 3);

        s.append("String[] {");

        if (a.length > 0) {

            s.append(format(a[0]));
        }

        for (int i = 1; i < a.length && s.length() < 1000; i++) {

            s.append(",").append(format(a[i]));
        }

        return s.append("}").toString();
    }

    private static String vs(Object k) {

        return vs(k, false, true);
    }

    @Nullable
    private static String vs(Object k, boolean forTable, boolean singleLine) {

        String li = null;

        if (k == null) {

            li = forTable ? "" : null;
        } else if (k instanceof String[]) {

            li = flatten((String[]) k);
        } else if (k instanceof int[]) {

            li = flatten((int[]) k);
        } else if (k instanceof long[]) {

            li = flatten((long[]) k);
        } else if (k instanceof double[]) {

            li = flatten((double[]) k);
        } else if (k instanceof float[]) {

            li = flatten((float[]) k);
        } else if (k instanceof short[]) {

            li = flatten((short[]) k);
        } else if (k instanceof boolean[]) {

            li = flatten((boolean[]) k);
        } else if (k instanceof byte[]) {

            li = flatten((byte[]) k);
        } else if (k instanceof Character) {

            li = forTable ? k.toString() : ("\"" + k + "\"");
        } else if (k instanceof String[]) {

            li = flatten((String[]) k);
        } else if (k instanceof char[]) {

            li = new String((char[]) k);

            if (singleLine) {

                replaceEscapes(li);
            }

            if (!forTable) {

                li = "\"" + li + "\"";
            }
        } else if (k instanceof String) {

            if (singleLine) {

                li = format((String) k).replace("\r", "\\\r").replace("\n", "\\\n");
            } else {

                li = (String) k;
            }
        } else if (k instanceof Object[]) {

            Object[] o = (Object[]) k;

            StringBuilder s = new StringBuilder(k.getClass().getSimpleName() + " {");

            if (o.length > 0) {

                s.append(vs(o[0], forTable, singleLine));
            }

            for (int i = 1; i < o.length; i++) {

                s.append(",").append(vs(o[i], forTable, singleLine));
            }

            s.append("}");

            li = s.toString();
        }

        return (li == null) ? format(k, forTable) : li;
    }

    private static String format(boolean b) {

        return b ? "true" : "false";
    }

    private static String format(String s) {

        if (s == null) {

            return "null";
        }

        return "\"" + replaceEscapes(s) + "\"";
    }

    private static String replaceEscapes(String s) {

        if (s == null) {

            return "";
        }

        char[] subs = "\\\"'\r\n\t\b\f".toCharArray();

        char[] replacement = "\\\"'rntbf".toCharArray();

        for (int i = 0; i < subs.length; i++) {

            s = s.replace("" + subs[i], "\\" + replacement[i]);
        }

        return s;
    }

    @Nullable
    private static String format(Object o) {

        return format(o, false);
    }

    @Nullable
    private static String format(Object o, boolean forTable) {

        if (o instanceof String && o.equals("")) {

            return "";
        }

        if (o instanceof Float) {

            Float f = (Float) o;

            if (f.isInfinite()) {

                return (f.equals(Float.valueOf(Float.POSITIVE_INFINITY)) ? "" : "-") + "∞";
            }

            return formatFloatingPt(f.floatValue(), forTable);
        }
        if (o instanceof Double) {

            Double d = (Double) o;

            if (d.isInfinite()) {

                return (d.equals(Double.valueOf(Double.POSITIVE_INFINITY)) ? "" : "-") + "∞";
            }

            return formatFloatingPt(((Double) o).doubleValue(), forTable);
        }
        if (o != null && TIME_VALUER.isSupported(o.getClass())) {

            return TIME_VALUER.getString(o);
        }

        if (o instanceof Boolean || o instanceof Long || o instanceof Integer || o instanceof Short || o instanceof Character || o instanceof Byte) {

            return o.toString();
        }

        return null;
    }

    private static String formatFloatingPt(double d, boolean forTable) {

        if (forTable) {

            return NUM_FORMAT.format(d);
        }

        String tmp = String.format(formatString, Double.valueOf(d));

        int dotPos = tmp.lastIndexOf(".");

        if (dotPos == -1) {

            return tmp;
        }

        int lastIdx = tmp.length();

        while (tmp.charAt(lastIdx - 1) == '0') {

            lastIdx--;
        }

        if (tmp.charAt(lastIdx - 1) == '.') {

            lastIdx--;
        }

        return tmp.substring(0, lastIdx);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\display\ToStringer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */