package io.jpad.japl;

import com.google.common.base.Preconditions;

import java.util.*;

public class Japl {
    private static final Map<String, Object> data = new HashMap<>();
    private static final Random p = new Random();

    public static int[] set(String key, int[] value) {

        data.put(key, copy(value));
        return value;
    }

    public static double[] set(String key, double[] value) {
        data.put(key, copy(value));
        return value;
    }

    public static int set(String key, int value) {
        data.put(key, Integer.valueOf(value));
        return value;
    }

    public static double set(String key, double value) {

        data.put(key, Double.valueOf(value));
        return value;
    }

    public static Object get(String key) {
        return data.get(key);
    }

    public static int geti(String key) {
        return ((Integer) data.get(key)).intValue();
    }

    public static double getd(String key) {
        return ((Double) data.get(key)).doubleValue();
    }

    public static int[] getia(String key) {
        return copy((int[]) data.get(key));
    }

    public static double[] getda(String key) {

        return copy((double[]) data.get(key));
    }

    private static void cl(double[] x, double[] y) {

        if (x.length != y.length) throw new IllegalArgumentException("length");
    }

    private static void cl(float[] x, float[] y) {

        if (x.length != y.length) throw new IllegalArgumentException("length");
    }

    private static void cl(short[] x, short[] y) {

        if (x.length != y.length) throw new IllegalArgumentException("length");
    }

    private static void cl(long[] x, long[] y) {

        if (x.length != y.length) throw new IllegalArgumentException("length");
    }

    private static void cl(byte[] x, byte[] y) {

        if (x.length != y.length) throw new IllegalArgumentException("length");
    }

    private static void cl(boolean[] x, boolean[] y) {

        cn(x);
        cn(y);

        if (x.length != y.length) throw new LengthKException();
    }

    private static void cl(int[] x, int[] y) {

        if (x.length != y.length) throw new IllegalArgumentException("length");
    }

    private static void cn(Object o) {
        if (o == null) throw new TypeKException();
    }

    private static double[] cD(int[] x) {
        double[] r;

        int i;

        for (r = new double[x.length], i = 0; i < x.length; ) {
            r[i] = x[i];
            i++;
        }
        return r;
    }

    public static boolean[] cB(int[] x) {
        boolean[] r;

        int i;

        for (r = new boolean[x.length], i = 0; i < x.length; ) {
            r[i] = (x[i] > 0);
            i++;
        }
        return r;
    }

    public static boolean[] cB(double[] x) {

        boolean[] r;

        int i;

        for (r = new boolean[x.length], i = 0; i < x.length; ) {
            r[i] = (x[i] > 0.0D);
            i++;
        }
        return r;
    }

    private static int[] cI(List<Integer> l) {

        int[] r = new int[l.size()];

        for (int i = 0; i < r.length; ) {
            r[i] = l.get(i).intValue();
            i++;
        }

        return r;
    }

    public static double[] choose(int x, double y) {

        double[] res = new double[x];

        for (int i = 0; i < x; ) {
            res[i] = p.nextDouble() * y;
            i++;
        }
        return res;
    }

    public static int[] choose(int x, int y) {

        int[] res = new int[x];

        for (int i = 0; i < x; ) {
            res[i] = p.nextInt(y);
            i++;
        }
        return res;
    }

    public static int[] til(int x) {

        if (x < 0) throw new LimitKException();
        int[] r;
        int i;

        for (r = new int[x], i = 0; i < x; ) {
            r[i] = i;
            i++;
        }
        return r;
    }

    public static long[] til(long x) {

        if (x < 0L) throw new LimitKException();
        long[] r;
        int i;

        for (r = new long[(int) x], i = 0; i < x; ) {
            r[i] = i;
            i++;
        }
        return r;
    }

    public static double[] asc(double[] x) {
        Arrays.sort(x);
        return x;
    }

    public static int[] asc(int[] x) {
        Arrays.sort(x);
        return x;
    }

    public static double asc(double x) {
        return x;
    }

    public static double asc(int x) {

        return x;
    }

    public static double max(double[] x) {
        double r = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < x.length; ) {
            if (x[i] > r) r = x[i];
            i++;
        }
        return r;
    }

    public static int max(int[] x) {
        int r = Integer.MIN_VALUE;

        for (int i = 0; i < x.length; ) {
            if (x[i] > r) r = x[i];
            i++;
        }
        return r;
    }

    public static double max(double x) {
        return x;
    }

    public static int max(int x) {

        return x;
    }

    public static double min(double[] x) {
        double r = Double.POSITIVE_INFINITY;

        for (int i = 0; i < x.length; ) {
            if (x[i] < r) r = x[i];
            i++;
        }
        return r;
    }

    public static int min(int[] x) {
        int r = Integer.MAX_VALUE;

        for (int i = 0; i < x.length; ) {
            if (x[i] < r) r = x[i];
            i++;
        }
        return r;
    }

    public static double min(double x) {
        return x;
    }

    public static double min(int x) {

        return x;
    }

    public static double[] reverse(double[] x) {

        for (int i = 0; i < x.length / 2; i++) {

            double t = x[i];
            x[i] = x[x.length - i - 1];
            x[x.length - i - 1] = t;
        }
        return x;
    }

    public static int[] reverse(int[] x) {
        for (int i = 0; i < x.length / 2; i++) {

            int t = x[i];
            x[i] = x[x.length - i - 1];
            x[x.length - i - 1] = t;
        }
        return x;
    }

    public static double desc(double x) {
        return x;
    }

    public static double desc(int x) {

        return x;
    }

    public static double[] copy(double[] x) {

        double[] r = new double[x.length];

        for (int i = 0; i < x.length; ) {
            r[i] = x[i];
            i++;
        }
        return r;
    }

    public static int[] copy(int[] x) {

        int[] r = new int[x.length];

        for (int i = 0; i < x.length; ) {
            r[i] = x[i];
            i++;
        }
        return r;
    }

    public static double[] mod(double[] x, double y) {

        boolean b = (y == 0.0D);

        for (int i = 0; i < x.length; ) {
            x[i] = b ? Double.NaN : (x[i] % y);
            i++;
        }
        return x;
    }

    public static int[] mod(int[] x, int y) {

        if (y == 0) throw new IllegalArgumentException();

        for (int i = 0; i < x.length; ) {
            x[i] = x[i] % y;
            i++;
        }
        return x;
    }

    public static long[] mod(long[] x, long y) {

        if (y == 0L) throw new IllegalArgumentException();

        for (int i = 0; i < x.length; ) {
            x[i] = x[i] % y;
            i++;
        }
        return x;
    }

    public static double sum(double[] x) {

        double r = 0.0D;
        for (double d : x) {
            if (!Double.isNaN(d)) r += d;
        }
        return r;
    }

    public static double sum(double x) {

        return x;
    }

    public static int sum(int[] x) {
        int r = 0;
        for (int d : x) r += d;
        return r;
    }

    public static int sum(int x) {

        return x;
    }

    public static double prd(double[] x) {
        double r = 1.0D;
        for (double d : x) r *= d;
        return r;
    }

    public static double prd(double x) {

        return x;
    }

    public static int prd(int[] x) {
        int r = 1;
        for (int d : x) r *= d;
        return r;
    }

    public static int prd(int x) {

        return x;
    }

    public static double[] sums(double[] x) {

        for (int i = 1; i < x.length; ) {
            x[i] = x[i] + x[i - 1];
            i++;
        }
        return x;
    }

    public static double sums(double x) {

        return x;
    }

    public static double[] prds(double[] x) {
        for (int i = 1; i < x.length; ) {
            x[i] = x[i] * x[i - 1];
            i++;
        }
        return x;
    }

    public static double prds(double x) {

        return x;
    }

    public static int[] where(boolean[] x) {

        cn(x);

        List<Integer> r = new ArrayList<>(x.length);

        for (int i = 0; i < x.length; ) {
            if (x[i]) r.add(Integer.valueOf(i));
            i++;
        }

        return cI(r);
    }

    public static boolean[] equal(int x, int[] y) {

        boolean[] r = new boolean[y.length];

        for (int i = 0; i < y.length; ) {
            r[i] = (y[i] == x);
            i++;
        }

        return r;
    }

    public static double[] index(double[] vals, int[] indices) {

        double[] r = new double[indices.length];

        for (int i = 0; i < r.length; ) {
            r[i] = vals[indices[i]];
            i++;
        }
        return r;
    }

    public static int[] index(int[] vals, int[] indices) {

        int[] r = new int[indices.length];

        for (int i = 0; i < r.length; ) {
            r[i] = vals[indices[i]];
            i++;
        }
        return r;
    }

    public static boolean[] index(boolean[] vals, int[] indices) {

        boolean[] r = new boolean[indices.length];

        for (int i = 0; i < r.length; ) {
            r[i] = vals[indices[i]];
            i++;
        }
        return r;
    }

    public static int[] floor(double[] x) {

        int[] r = new int[x.length];

        for (int i = 0; i < r.length; ) {
            r[i] = (int) Math.floor(x[i]);
            i++;
        }
        return r;
    }

    public static int[] ceiling(double[] x) {

        int[] r = new int[x.length];

        for (int i = 0; i < r.length; ) {
            r[i] = (int) Math.ceil(x[i]);
            i++;
        }
        return r;
    }

    public static int last(int[] x) {

        return x[x.length - 1];
    }

    public static double last(double[] x) {

        return x[x.length - 1];
    }

    public static int first(int[] x) {

        return x[0];
    }

    public static double first(double[] x) {

        return x[0];
    }

    public static double[] join(double[] first, double[]... next) {

        int l = first.length;

        for (double[] d : next) {

            l += d.length;
        }

        double[] r = new double[l];

        int i = 0;

        for (; i < first.length; i++) {

            r[i] = first[i];
        }

        for (double[] d : next) {

            int j = 0;

            for (; j < d.length; j++) {

                r[i + j] = d[j];
            }

            i += j;
        }

        return r;
    }

    public static double[] step(double from, double to, int numSteps) {

        Preconditions.checkArgument((numSteps >= 0));

        if (numSteps == 0)
            return new double[0];

        if (numSteps == 1) {

            return new double[]{(from + to) / 2.0D};
        }

        double[] r = new double[numSteps];

        r[0] = from;

        double step = (to - from) / (numSteps - 1);

        for (int i = 1; i < numSteps; i++) {

            r[i] = r[i - 1] + step;
        }

        r[numSteps - 1] = to;

        return r;
    }

    public static int mul(int x, int y) {

        return x * y;
    }

    public static int[] mul(int[] x, int y) {

        int[] r = new int[x.length];

        for (int i = 0; i < x.length; ) {
            r[i] = x[i] * y;
            i++;
        }
        return r;
    }

    public static int[] mul(int x, int[] y) {

        int[] r = new int[y.length];

        for (int i = 0; i < y.length; ) {
            r[i] = x * y[i];
            i++;
        }
        return r;
    }

    public static int[] mul(int[] x, int[] y) {

        int[] r = new int[x.length];

        cl(x, y);
        for (int i = 0; i < x.length; ) {
            r[i] = x[i] * y[i];
            i++;
        }
        return r;
    }

    public static int mul(byte x, byte y) {

        return x * y;
    }

    public static int[] mul(byte[] x, byte y) {

        int[] r = new int[x.length];

        for (int i = 0; i < x.length; ) {
            r[i] = x[i] * y;
            i++;
        }
        return r;
    }

    public static int[] mul(byte x, byte[] y) {

        int[] r = new int[y.length];

        for (int i = 0; i < y.length; ) {
            r[i] = x * y[i];
            i++;
        }
        return r;
    }

    public static int[] mul(byte[] x, byte[] y) {

        int[] r = new int[x.length];

        cl(x, y);
        for (int i = 0; i < x.length; ) {
            r[i] = x[i] * y[i];
            i++;
        }
        return r;
    }

    public static int mul(short x, short y) {

        return x * y;
    }

    public static int[] mul(short[] x, short y) {

        int[] r = new int[x.length];

        for (int i = 0; i < x.length; ) {
            r[i] = x[i] * y;
            i++;
        }
        return r;
    }

    public static int[] mul(short x, short[] y) {

        int[] r = new int[y.length];

        for (int i = 0; i < y.length; ) {
            r[i] = x * y[i];
            i++;
        }
        return r;
    }

    public static int[] mul(short[] x, short[] y) {

        int[] r = new int[x.length];

        cl(x, y);
        for (int i = 0; i < x.length; ) {
            r[i] = x[i] * y[i];
            i++;
        }
        return r;
    }

    public static long mul(long x, long y) {

        return x * y;
    }

    public static long[] mul(long[] x, long y) {

        long[] r = new long[x.length];

        for (int i = 0; i < x.length; ) {
            r[i] = x[i] * y;
            i++;
        }
        return r;
    }

    public static long[] mul(long x, long[] y) {

        long[] r = new long[y.length];

        for (int i = 0; i < y.length; ) {
            r[i] = x * y[i];
            i++;
        }
        return r;
    }

    public static long[] mul(long[] x, long[] y) {

        long[] r = new long[x.length];

        cl(x, y);
        for (int i = 0; i < x.length; ) {
            r[i] = x[i] * y[i];
            i++;
        }
        return r;
    }

    public static float mul(float x, float y) {

        return x * y;
    }

    public static float[] mul(float[] x, float y) {

        float[] r = new float[x.length];

        for (int i = 0; i < x.length; ) {
            r[i] = x[i] * y;
            i++;
        }
        return r;
    }

    public static float[] mul(float x, float[] y) {

        float[] r = new float[y.length];

        for (int i = 0; i < y.length; ) {
            r[i] = x * y[i];
            i++;
        }
        return r;
    }

    public static float[] mul(float[] x, float[] y) {

        float[] r = new float[x.length];

        cl(x, y);
        for (int i = 0; i < x.length; ) {
            r[i] = x[i] * y[i];
            i++;
        }
        return r;
    }

    public static double mul(double x, double y) {

        return x * y;
    }

    public static double[] mul(double[] x, double y) {

        double[] r = new double[x.length];

        for (int i = 0; i < x.length; ) {
            r[i] = x[i] * y;
            i++;
        }
        return r;
    }

    public static double[] mul(double x, double[] y) {

        double[] r = new double[y.length];

        for (int i = 0; i < y.length; ) {
            r[i] = x * y[i];
            i++;
        }
        return r;
    }

    public static double[] mul(double[] x, double[] y) {
        double[] r = new double[x.length];

        cl(x, y);
        for (int i = 0; i < x.length; ) {
            r[i] = x[i] * y[i];
            i++;
        }
        return r;
    }

    public static double[] mul(double x, int[] y) {
        return mul(x, cD(y));
    }

    public static double[] mul(int[] x, double y) {

        return mul(cD(x), y);
    }

    public static int add(int x, int y) {

        return x + y;
    }

    public static int[] add(int[] x, int y) {

        int[] r = new int[x.length];

        for (int i = 0; i < x.length; ) {
            r[i] = x[i] + y;
            i++;
        }
        return r;
    }

    public static int[] add(int x, int[] y) {

        int[] r = new int[y.length];

        for (int i = 0; i < y.length; ) {
            r[i] = x + y[i];
            i++;
        }
        return r;
    }

    public static int[] add(int[] x, int[] y) {

        int[] r = new int[x.length];

        cl(x, y);
        for (int i = 0; i < x.length; ) {
            r[i] = x[i] + y[i];
            i++;
        }
        return r;
    }

    public static int add(byte x, byte y) {

        return x + y;
    }

    public static int[] add(byte[] x, byte y) {

        int[] r = new int[x.length];

        for (int i = 0; i < x.length; ) {
            r[i] = x[i] + y;
            i++;
        }
        return r;
    }

    public static int[] add(byte x, byte[] y) {

        int[] r = new int[y.length];

        for (int i = 0; i < y.length; ) {
            r[i] = x + y[i];
            i++;
        }
        return r;
    }

    public static int[] add(byte[] x, byte[] y) {

        int[] r = new int[x.length];

        cl(x, y);
        for (int i = 0; i < x.length; ) {
            r[i] = x[i] + y[i];
            i++;
        }
        return r;
    }

    public static int add(short x, short y) {

        return x + y;
    }

    public static int[] add(short[] x, short y) {

        int[] r = new int[x.length];

        for (int i = 0; i < x.length; ) {
            r[i] = x[i] + y;
            i++;
        }
        return r;
    }

    public static int[] add(short x, short[] y) {

        int[] r = new int[y.length];

        for (int i = 0; i < y.length; ) {
            r[i] = x + y[i];
            i++;
        }
        return r;
    }

    public static int[] add(short[] x, short[] y) {

        int[] r = new int[x.length];

        cl(x, y);
        for (int i = 0; i < x.length; ) {
            r[i] = x[i] + y[i];
            i++;
        }
        return r;
    }

    public static long add(long x, long y) {

        return x + y;
    }

    public static long[] add(long[] x, long y) {

        long[] r = new long[x.length];

        for (int i = 0; i < x.length; ) {
            r[i] = x[i] + y;
            i++;
        }
        return r;
    }

    public static long[] add(long x, long[] y) {

        long[] r = new long[y.length];

        for (int i = 0; i < y.length; ) {
            r[i] = x + y[i];
            i++;
        }
        return r;
    }

    public static long[] add(long[] x, long[] y) {

        long[] r = new long[x.length];

        cl(x, y);
        for (int i = 0; i < x.length; ) {
            r[i] = x[i] + y[i];
            i++;
        }
        return r;
    }

    public static float add(float x, float y) {

        return x + y;
    }

    public static float[] add(float[] x, float y) {

        float[] r = new float[x.length];

        for (int i = 0; i < x.length; ) {
            r[i] = x[i] + y;
            i++;
        }
        return r;
    }

    public static float[] add(float x, float[] y) {

        float[] r = new float[y.length];

        for (int i = 0; i < y.length; ) {
            r[i] = x + y[i];
            i++;
        }
        return r;
    }

    public static float[] add(float[] x, float[] y) {

        float[] r = new float[x.length];

        cl(x, y);
        for (int i = 0; i < x.length; ) {
            r[i] = x[i] + y[i];
            i++;
        }
        return r;
    }

    public static double add(double x, double y) {

        return x + y;
    }

    public static double[] add(double[] x, double y) {

        double[] r = new double[x.length];

        for (int i = 0; i < x.length; ) {
            r[i] = x[i] + y;
            i++;
        }
        return r;
    }

    public static double[] add(double x, double[] y) {

        double[] r = new double[y.length];

        for (int i = 0; i < y.length; ) {
            r[i] = x + y[i];
            i++;
        }
        return r;
    }

    public static double[] add(double[] x, double[] y) {
        double[] r = new double[x.length];

        cl(x, y);
        for (int i = 0; i < x.length; ) {
            r[i] = x[i] + y[i];
            i++;
        }
        return r;
    }

    public static double[] add(double x, int[] y) {
        return add(x, cD(y));
    }

    public static double[] add(int[] x, double y) {

        return add(cD(x), y);
    }

    public static int sub(int x, int y) {

        return x - y;
    }

    public static int[] sub(int[] x, int y) {

        int[] r = new int[x.length];

        for (int i = 0; i < x.length; ) {
            r[i] = x[i] - y;
            i++;
        }
        return r;
    }

    public static int[] sub(int x, int[] y) {

        int[] r = new int[y.length];

        for (int i = 0; i < y.length; ) {
            r[i] = x - y[i];
            i++;
        }
        return r;
    }

    public static int[] sub(int[] x, int[] y) {

        int[] r = new int[x.length];

        cl(x, y);
        for (int i = 0; i < x.length; ) {
            r[i] = x[i] - y[i];
            i++;
        }
        return r;
    }

    public static int sub(byte x, byte y) {

        return x - y;
    }

    public static int[] sub(byte[] x, byte y) {

        int[] r = new int[x.length];

        for (int i = 0; i < x.length; ) {
            r[i] = x[i] - y;
            i++;
        }
        return r;
    }

    public static int[] sub(byte x, byte[] y) {

        int[] r = new int[y.length];

        for (int i = 0; i < y.length; ) {
            r[i] = x - y[i];
            i++;
        }
        return r;
    }

    public static int[] sub(byte[] x, byte[] y) {

        int[] r = new int[x.length];

        cl(x, y);
        for (int i = 0; i < x.length; ) {
            r[i] = x[i] - y[i];
            i++;
        }
        return r;
    }

    public static int sub(short x, short y) {

        return x - y;
    }

    public static int[] sub(short[] x, short y) {

        int[] r = new int[x.length];

        for (int i = 0; i < x.length; ) {
            r[i] = x[i] - y;
            i++;
        }
        return r;
    }

    public static int[] sub(short x, short[] y) {

        int[] r = new int[y.length];

        for (int i = 0; i < y.length; ) {
            r[i] = x - y[i];
            i++;
        }
        return r;
    }

    public static int[] sub(short[] x, short[] y) {

        int[] r = new int[x.length];

        cl(x, y);
        for (int i = 0; i < x.length; ) {
            r[i] = x[i] - y[i];
            i++;
        }
        return r;
    }

    public static long sub(long x, long y) {

        return x - y;
    }

    public static long[] sub(long[] x, long y) {

        long[] r = new long[x.length];

        for (int i = 0; i < x.length; ) {
            r[i] = x[i] - y;
            i++;
        }
        return r;
    }

    public static long[] sub(long x, long[] y) {

        long[] r = new long[y.length];

        for (int i = 0; i < y.length; ) {
            r[i] = x - y[i];
            i++;
        }
        return r;
    }

    public static long[] sub(long[] x, long[] y) {

        long[] r = new long[x.length];

        cl(x, y);
        for (int i = 0; i < x.length; ) {
            r[i] = x[i] - y[i];
            i++;
        }
        return r;
    }

    public static float sub(float x, float y) {

        return x - y;
    }

    public static float[] sub(float[] x, float y) {

        float[] r = new float[x.length];

        for (int i = 0; i < x.length; ) {
            r[i] = x[i] - y;
            i++;
        }
        return r;
    }

    public static float[] sub(float x, float[] y) {

        float[] r = new float[y.length];

        for (int i = 0; i < y.length; ) {
            r[i] = x - y[i];
            i++;
        }
        return r;
    }

    public static float[] sub(float[] x, float[] y) {

        float[] r = new float[x.length];

        cl(x, y);
        for (int i = 0; i < x.length; ) {
            r[i] = x[i] - y[i];
            i++;
        }
        return r;
    }

    public static double sub(double x, double y) {

        return x - y;
    }

    public static double[] sub(double[] x, double y) {

        double[] r = new double[x.length];

        for (int i = 0; i < x.length; ) {
            r[i] = x[i] - y;
            i++;
        }
        return r;
    }

    public static double[] sub(double x, double[] y) {

        double[] r = new double[y.length];

        for (int i = 0; i < y.length; ) {
            r[i] = x - y[i];
            i++;
        }
        return r;
    }

    public static double[] sub(double[] x, double[] y) {
        double[] r = new double[x.length];

        cl(x, y);
        for (int i = 0; i < x.length; ) {
            r[i] = x[i] - y[i];
            i++;
        }
        return r;
    }

    public static double[] sub(double x, int[] y) {
        return sub(x, cD(y));
    }

    public static double[] sub(int[] x, double y) {

        return sub(cD(x), y);
    }

    public static int div(int x, int y) {

        return x / y;
    }

    public static int[] div(int[] x, int y) {

        int[] r = new int[x.length];

        for (int i = 0; i < x.length; ) {
            r[i] = x[i] / y;
            i++;
        }
        return r;
    }

    public static int[] div(int x, int[] y) {

        int[] r = new int[y.length];

        for (int i = 0; i < y.length; ) {
            r[i] = x / y[i];
            i++;
        }
        return r;
    }

    public static int[] div(int[] x, int[] y) {

        int[] r = new int[x.length];

        cl(x, y);
        for (int i = 0; i < x.length; ) {
            r[i] = x[i] / y[i];
            i++;
        }
        return r;
    }

    public static int div(byte x, byte y) {

        return x / y;
    }

    public static int[] div(byte[] x, byte y) {

        int[] r = new int[x.length];

        for (int i = 0; i < x.length; ) {
            r[i] = x[i] / y;
            i++;
        }
        return r;
    }

    public static int[] div(byte x, byte[] y) {

        int[] r = new int[y.length];

        for (int i = 0; i < y.length; ) {
            r[i] = x / y[i];
            i++;
        }
        return r;
    }

    public static int[] div(byte[] x, byte[] y) {

        int[] r = new int[x.length];

        cl(x, y);
        for (int i = 0; i < x.length; ) {
            r[i] = x[i] / y[i];
            i++;
        }
        return r;
    }

    public static int div(short x, short y) {

        return x / y;
    }

    public static int[] div(short[] x, short y) {

        int[] r = new int[x.length];

        for (int i = 0; i < x.length; ) {
            r[i] = x[i] / y;
            i++;
        }
        return r;
    }

    public static int[] div(short x, short[] y) {

        int[] r = new int[y.length];

        for (int i = 0; i < y.length; ) {
            r[i] = x / y[i];
            i++;
        }
        return r;
    }

    public static int[] div(short[] x, short[] y) {

        int[] r = new int[x.length];

        cl(x, y);
        for (int i = 0; i < x.length; ) {
            r[i] = x[i] / y[i];
            i++;
        }
        return r;
    }

    public static long div(long x, long y) {

        return x / y;
    }

    public static long[] div(long[] x, long y) {

        long[] r = new long[x.length];

        for (int i = 0; i < x.length; ) {
            r[i] = x[i] / y;
            i++;
        }
        return r;
    }

    public static long[] div(long x, long[] y) {

        long[] r = new long[y.length];

        for (int i = 0; i < y.length; ) {
            r[i] = x / y[i];
            i++;
        }
        return r;
    }

    public static long[] div(long[] x, long[] y) {

        long[] r = new long[x.length];

        cl(x, y);
        for (int i = 0; i < x.length; ) {
            r[i] = x[i] / y[i];
            i++;
        }
        return r;
    }

    public static float div(float x, float y) {

        return x / y;
    }

    public static float[] div(float[] x, float y) {

        float[] r = new float[x.length];

        for (int i = 0; i < x.length; ) {
            r[i] = x[i] / y;
            i++;
        }
        return r;
    }

    public static float[] div(float x, float[] y) {

        float[] r = new float[y.length];

        for (int i = 0; i < y.length; ) {
            r[i] = x / y[i];
            i++;
        }
        return r;
    }

    public static float[] div(float[] x, float[] y) {

        float[] r = new float[x.length];

        cl(x, y);
        for (int i = 0; i < x.length; ) {
            r[i] = x[i] / y[i];
            i++;
        }
        return r;
    }

    public static double div(double x, double y) {

        return x / y;
    }

    public static double[] div(double[] x, double y) {

        double[] r = new double[x.length];

        for (int i = 0; i < x.length; ) {
            r[i] = x[i] / y;
            i++;
        }
        return r;
    }

    public static double[] div(double x, double[] y) {

        double[] r = new double[y.length];

        for (int i = 0; i < y.length; ) {
            r[i] = x / y[i];
            i++;
        }
        return r;
    }

    public static double[] div(double[] x, double[] y) {
        double[] r = new double[x.length];

        cl(x, y);
        for (int i = 0; i < x.length; ) {
            r[i] = x[i] / y[i];
            i++;
        }
        return r;
    }

    public static double[] div(double x, int[] y) {
        return div(x, cD(y));
    }

    public static double[] div(int[] x, double y) {

        return div(cD(x), y);
    }

    public static double[] sin(double[] x) {

        double[] r = new double[x.length];

        for (int i = 0; i < x.length; ) {
            r[i] = Math.sin(x[i]);
            i++;
        }
        return r;
    }

    public static double[] sin(int[] x) {
        return sin(cD(x));
    }

    public static double sin(double x) {
        return Math.sin(x);
    }

    public static double sin(int x) {

        return Math.sin(x);
    }

    public static double[] cos(double[] x) {

        double[] r = new double[x.length];

        for (int i = 0; i < x.length; ) {
            r[i] = Math.cos(x[i]);
            i++;
        }
        return r;
    }

    public static double[] cos(int[] x) {
        return cos(cD(x));
    }

    public static double cos(double x) {
        return Math.cos(x);
    }

    public static double cos(int x) {

        return Math.cos(x);
    }

    public static double[] tan(double[] x) {

        double[] r = new double[x.length];

        for (int i = 0; i < x.length; ) {
            r[i] = Math.tan(x[i]);
            i++;
        }
        return r;
    }

    public static double[] tan(int[] x) {
        return tan(cD(x));
    }

    public static double tan(double x) {
        return Math.tan(x);
    }

    public static double tan(int x) {

        return Math.tan(x);
    }

    public static double[] asin(double[] x) {

        double[] r = new double[x.length];

        for (int i = 0; i < x.length; ) {
            r[i] = Math.asin(x[i]);
            i++;
        }
        return r;
    }

    public static double[] asin(int[] x) {
        return asin(cD(x));
    }

    public static double asin(double x) {
        return Math.asin(x);
    }

    public static double asin(int x) {

        return Math.asin(x);
    }

    public static double[] acos(double[] x) {

        double[] r = new double[x.length];

        for (int i = 0; i < x.length; ) {
            r[i] = Math.acos(x[i]);
            i++;
        }
        return r;
    }

    public static double[] acos(int[] x) {
        return acos(cD(x));
    }

    public static double acos(double x) {
        return Math.acos(x);
    }

    public static double acos(int x) {

        return Math.acos(x);
    }

    public static double[] atan(double[] x) {

        double[] r = new double[x.length];

        for (int i = 0; i < x.length; ) {
            r[i] = Math.atan(x[i]);
            i++;
        }
        return r;
    }

    public static double[] atan(int[] x) {
        return atan(cD(x));
    }

    public static double atan(double x) {
        return Math.atan(x);
    }

    public static double atan(int x) {

        return Math.atan(x);
    }

    public static double[] sqrt(double[] x) {

        double[] r = new double[x.length];

        for (int i = 0; i < x.length; ) {
            r[i] = Math.sqrt(x[i]);
            i++;
        }
        return r;
    }

    public static double[] sqrt(int[] x) {
        return sqrt(cD(x));
    }

    public static double sqrt(double x) {
        return Math.sqrt(x);
    }

    public static double sqrt(int x) {

        return Math.sqrt(x);
    }

    public static double[] abs(double[] x) {

        double[] r = new double[x.length];

        for (int i = 0; i < x.length; ) {
            r[i] = Math.abs(x[i]);
            i++;
        }
        return r;
    }

    public static double[] abs(int[] x) {
        return abs(cD(x));
    }

    public static double abs(double x) {
        return Math.abs(x);
    }

    public static double abs(int x) {

        return Math.abs(x);
    }

    public static boolean and(boolean x, boolean y) {

        return (x && y);
    }

    public static boolean[] and(boolean[] x, boolean[] y) {

        cl(x, y);
        boolean[] r = new boolean[x.length];

        for (int i = 0; i < y.length; ) {
            r[i] = r[i] | ((y[i] && x[i]));
            i++;
        }
        return r;
    }

    public static boolean[] and(int[] x, int[] y) {

        cl(x, y);
        boolean[] r = new boolean[x.length];

        for (int i = 0; i < y.length; ) {
            r[i] = (y[i] > 0 && x[i] > 0);
            i++;
        }
        return r;
    }

    public static boolean[] and(double[] x, double[] y) {
        cl(x, y);
        boolean[] r = new boolean[x.length];

        for (int i = 0; i < y.length; ) {
            r[i] = (y[i] > 0.0D && x[i] > 0.0D);
            i++;
        }
        return r;
    }

    public static boolean[] and(double[] x, int[] y) {
        return and(cB(x), cB(y));
    }

    public static boolean[] and(int[] x, double[] y) {
        return and(cB(x), cB(y));
    }

    public static boolean[] and(boolean[] x, int[] y) {
        return and(x, cB(y));
    }

    public static boolean[] and(int[] x, boolean[] y) {
        return and(cB(x), y);
    }

    public static boolean[] and(boolean[] x, double[] y) {
        return and(x, cB(y));
    }

    public static boolean[] and(double[] x, boolean[] y) {

        return and(cB(x), y);
    }

    public static boolean or(boolean x, boolean y) {

        return (x || y);
    }

    public static boolean[] or(boolean[] x, boolean[] y) {

        cl(x, y);
        boolean[] r = new boolean[x.length];

        for (int i = 0; i < y.length; ) {
            r[i] = r[i] | ((y[i] || x[i]));
            i++;
        }
        return r;
    }

    public static boolean[] or(int[] x, int[] y) {

        cl(x, y);
        boolean[] r = new boolean[x.length];

        for (int i = 0; i < y.length; ) {
            r[i] = (y[i] > 0 || x[i] > 0);
            i++;
        }
        return r;
    }

    public static boolean[] or(double[] x, double[] y) {
        cl(x, y);
        boolean[] r = new boolean[x.length];

        for (int i = 0; i < y.length; ) {
            r[i] = (y[i] > 0.0D || x[i] > 0.0D);
            i++;
        }
        return r;
    }

    public static boolean[] or(double[] x, int[] y) {
        return or(cB(x), cB(y));
    }

    public static boolean[] or(int[] x, double[] y) {
        return or(cB(x), cB(y));
    }

    public static boolean[] or(boolean[] x, int[] y) {
        return or(x, cB(y));
    }

    public static boolean[] or(int[] x, boolean[] y) {
        return or(cB(x), y);
    }

    public static boolean[] or(boolean[] x, double[] y) {
        return or(x, cB(y));
    }

    public static boolean[] or(double[] x, boolean[] y) {

        return or(cB(x), y);
    }

    public static boolean xor(boolean x, boolean y) {

        return x ^ y;
    }

    public static boolean[] xor(boolean[] x, boolean[] y) {

        cl(x, y);
        boolean[] r = new boolean[x.length];

        for (int i = 0; i < y.length; ) {
            r[i] = r[i] | y[i] ^ x[i];
            i++;
        }
        return r;
    }

    public static boolean[] xor(int[] x, int[] y) {

        cl(x, y);
        boolean[] r = new boolean[x.length];

        for (int i = 0; i < y.length; ) {
            r[i] = (y[i] > 0) ^ (x[i] > 0);
            i++;
        }
        return r;
    }

    public static boolean[] xor(double[] x, double[] y) {
        cl(x, y);
        boolean[] r = new boolean[x.length];

        for (int i = 0; i < y.length; ) {
            r[i] = (y[i] > 0.0D) ^ (x[i] > 0.0D);
            i++;
        }
        return r;
    }

    public static boolean[] xor(double[] x, int[] y) {
        return xor(cB(x), cB(y));
    }

    public static boolean[] xor(int[] x, double[] y) {
        return xor(cB(x), cB(y));
    }

    public static boolean[] xor(boolean[] x, int[] y) {
        return xor(x, cB(y));
    }

    public static boolean[] xor(int[] x, boolean[] y) {
        return xor(cB(x), y);
    }

    public static boolean[] xor(boolean[] x, double[] y) {
        return xor(x, cB(y));
    }

    public static boolean[] xor(double[] x, boolean[] y) {

        return xor(cB(x), y);
    }

    public static class TypeKException extends IllegalArgumentException {
    }

    public static class LengthKException extends IllegalArgumentException {
        public LengthKException() {

            super("length");
        }
    }

    public static class LimitKException
            extends IllegalArgumentException {
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\japl\Japl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */