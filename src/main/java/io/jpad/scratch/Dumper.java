package io.jpad.scratch;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import io.jpad.display.ToStringer;
import io.jpad.resultset.KeyedResultSet;
import io.jpad.resultset.ResultSetPrinter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Dumper {
    private static final Logger log = Logger.getLogger(Dumper.class.getName());

    private static final ArrayList<CapturedObject> dumps = new ArrayList<>();
    private static final boolean printDumps = true;
    private static final int CAPACITY = 1024;
    private static final ByteArrayOutputStream baos = new ByteArrayOutputStream(16384);
    private static final StringBuilder sb = new StringBuilder(1024);

    private static PrintStream originalOut;
    private static PrintStream originalErr;
    private static int maxRows = 18;
    private static int maxCols = 120;

    public static int getMaxRows() {
        return maxRows;
    }

    public static void setMaxRows(int maxRows) {
        Dumper.maxRows = maxRows;
    }

    public static int getMaxCols() {
        return maxCols;
    }

    public static void setMaxCols(int maxCols) {
        Dumper.maxCols = maxCols;
    }

    public static <T> T Dump(T val) {

        return Dump(val, "");
    }

    private static void displayDontCapture(String s) {

        originalOut.append(s);

        sb.append(s);
    }

    public static <T> T Dump(T val, String title) {

        flushStandardOut();

        CapturedObject co = new CapturedObject(title, val);

        String t = toString(co);

        if (t.contains("\n") && title.trim().length() > 0) {

            displayDontCapture("________" + title + "________\r\n");
        } else if (title.trim().length() > 0) {

            displayDontCapture(title + " => ");
        }

        displayDontCapture(t + "\r\n");

        dumps.add(co);

        return val;
    }

    private static String toString(CapturedObject co) {

        Object o = co.getObject();

        String s = ToStringer.asText(o);

        if (s != null) {

            return s;
        }

        if (isToStringPreferred(o)) {

            return o.toString();
        }

        if (o instanceof java.util.Collection) {

            KeyedResultSet krs = co.getResultSet();

            if (krs != null) {

                String c = krs.getCaption();

                if (c != null && c.trim().length() > 0) {

                    s = krs.getCaption() + "\r\n";
                }

                return s + ResultSetPrinter.toString(krs, krs.getNumberOfKeyColumns(), maxRows, maxCols);
            }
        } else if (o instanceof ResultSet) {

            KeyedResultSet krs = co.getResultSet();

            return ResultSetPrinter.toString(krs, krs.getNumberOfKeyColumns(), maxRows, maxCols);
        }

        return (o == null) ? "null" : o.toString();
    }

    private static boolean isToStringPreferred(Object o) {

        return (o instanceof List || o instanceof java.util.Set || o instanceof com.google.common.collect.Multiset);
    }

    private static void flushStandardOut() {

        try {

            baos.flush();

            if (baos.size() > 0) {

                String outTxt = baos.toString();

                sb.append(outTxt);

                dumps.add(new CapturedObject("", outTxt));

                baos.reset();
            }
        } catch (IOException e) {

            log.log(Level.WARNING, "Could not record system output", e);
        }
    }

    public static <T> Stream<T> Dump(Stream<T> val) {

        return Dump(val, "");
    }

    public static <T> Stream<T> Dump(Stream<T> val, String title) {

        List<T> l = val.collect(Collectors.toList());

        Dump(l, title);

        return l.stream();
    }

    public static List<CapturedObject> GetDumps() {
        return ImmutableList.copyOf(dumps);
    }

    public static void Clear() {

        dumps.clear();
    }

    static void clear() {

        baos.reset();

        sb.setLength(0);
    }

    public static void captureSystemOutput() {

        clear();

        OutputStreamCombiner outCombiner = new OutputStreamCombiner(Lists.newArrayList(baos, System.out));

        OutputStreamCombiner errCombiner = new OutputStreamCombiner(Lists.newArrayList(baos, System.err));

        PrintStream outPs = new PrintStream(outCombiner);

        PrintStream errPs = new PrintStream(errCombiner);

        originalOut = System.out;

        originalErr = System.err;

        System.setOut(outPs);

        System.setErr(errPs);
    }

    public static void restoreSystemOut() {

        System.setOut(originalOut);

        System.setErr(originalErr);
    }

    public static String getOutput() {

        flushStandardOut();

        return sb.toString();
    }

    private static class OutputStreamCombiner extends OutputStream {
        private final List<OutputStream> outputStreams;

        public OutputStreamCombiner(List<OutputStream> outputStreams) {

            this.outputStreams = outputStreams;
        }

        public void write(int b) throws IOException {

            for (OutputStream os : this.outputStreams) {

                os.write(b);
            }
        }

        public void flush() throws IOException {

            for (OutputStream os : this.outputStreams) {

                os.flush();
            }
        }

        public void close() throws IOException {

            for (OutputStream os : this.outputStreams)

                os.close();
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\scratch\Dumper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */