package kx;

import java.io.*;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.TimeZone;
import java.util.UUID;


public class C {
    private static final char[][] FUNC_1;
    private static final char[][] FUNC_2;
    private static final String[] ADVERBS = new String[]{"'", "/", "\\", "':", "/:", "\\:"};
    public static Object[] NULL;
    static int ni;
    static long nj;
    static double nf;
    static long k;
    static long n;
    static int[] nt;
    static long t;
    private static String e = "ISO-8859-1";
    private static PrintStream out = System.out;

    static {
        String[] monadics = {null, "+:", "-:", "*:", "%:", "&:", "|:", "^:", "=:", "<:", ">:", "$:", ",:", "#:", "_:", "~:", "!:", "?:", "@:", ".:", "0::", "1::", "2::", "avg", "last", "sum", "prd", "min", "max", "exit", "getenv", "abs", "sqrt", "log", "exp", "sin", "asin", "cos", "acos", "tan", "atan", "plist"};


        FUNC_1 = toChars(monadics);

        String[] dyads = {":", "+", "-", "*", "%", "&", "|", "^", "=", "<", ">", "$", ",", "#", "_", "~", "!", "?", "@", ".", "0:", "1:", "2:", "in", "within", "like", "bin", "ss", "insert", "wsum", "wavg", "div", "xexp", "setenv"};


        FUNC_2 = toChars(dyads);


        ni = Integer.MIN_VALUE;
        nj = Long.MIN_VALUE;
        nf = Double.NaN;


        k = 946684800000L;
        n = 1000000000L;


        nt = new int[]{0, 1, 16, 0, 1, 2, 4, 8, 4, 8, 1, 0, 8, 4, 4, 8, 8, 4, 4, 4};


        NULL = new Object[]{null, Boolean.FALSE, new UUID(0L, 0L), null, Byte.valueOf((byte) 0), -32768, Integer.valueOf(ni), Long.valueOf(nj), new Float(nf), new Double(nf), new Character(' '), "", new Timestamp(nj), new Month(ni), new Date(nj), new Date(nj), new Timespan(nj), new Minute(ni), new Second(ni), new Time(nj)};
    }

    public Socket s;
    public TimeZone tz;
    DataInputStream i;
    OutputStream o;
    byte[] b;
    byte[] B;
    int j;
    int J;
    int vt;
    boolean a;
    private int sync = 0;

    public C(ServerSocket s) throws IOException {
        this(s, null);
    }

    public C(String h, int p) throws KException, IOException {
        this(h, p, System.getProperty("user.name"));
    }

    public C(ServerSocket s, IAuthenticate a) throws IOException {
        this.tz = TimeZone.getDefault();
        io(s.accept());
        int n = this.i.read(this.b = new byte[99]);
        if (a != null) if (!a.authenticate(new String(this.b, 0, (n > 1) ? (n - 2) : 0))) {
            close();
            throw new IOException("access");
        }
        this.vt = (n > 1) ? this.b[n - 2] : 0;
        this.b[0] = (byte) ((this.vt < 3) ? this.vt : 3);
        this.o.write(this.b, 0, 1);
    }

    public C(String h, int p, String u) throws KException, IOException {
        this.tz = TimeZone.getDefault();
        this.B = new byte[2 + ns(u)];
        io(new Socket(h, p));
        this.J = 0;
        w(u + "\003");
        this.o.write(this.B);
        if (1 != this.i.read(this.B, 0, 1)) {
            close();
            this.B = new byte[1 + ns(u)];
            io(new Socket(h, p));
            this.J = 0;
            w(u);
            this.o.write(this.B);
            if (1 != this.i.read(this.B, 0, 1)) {
                close();
                throw new KException("access");
            }
        }
        this.vt = Math.min(this.B[0], 3);
    }

    protected C() {
        this.tz = TimeZone.getDefault();
    }

    private static char[][] toChars(String[] monadics) {
        char[][] a = new char[monadics.length][];
        for (int i = 0; i < a.length; i++) a[i] = (monadics[i] == null) ? null : monadics[i].toCharArray();
        return a;
    }

    public static void setEncoding(String e) throws UnsupportedEncodingException {
        C.e = e;
        out = new PrintStream(System.out, true, e);
    }

    public static Object NULL(char c1) {
        return NULL[" bg xhijefcspmdznuvt".indexOf(c1)];
    }

    public static int t(Object x) {
        return (x instanceof Boolean) ? -1 : ((x instanceof UUID) ? -2 : ((x instanceof Byte) ? -4 : ((x instanceof Short) ? -5 : ((x instanceof Integer) ? -6 : ((x instanceof Long) ? -7 : ((x instanceof Float) ? -8 : ((x instanceof Double) ? -9 : ((x instanceof Character) ? -10 : ((x instanceof String) ? -11 : ((x instanceof Date) ? -14 : ((x instanceof Time) ? -19 : ((x instanceof Timestamp) ? -12 : ((x instanceof Date) ? -15 : ((x instanceof Timespan) ? -16 : ((x instanceof Month) ? -13 : ((x instanceof Minute) ? -17 : ((x instanceof Second) ? -18 : ((x instanceof boolean[]) ? 1 : ((x instanceof UUID[]) ? 2 : ((x instanceof byte[]) ? 4 : ((x instanceof short[]) ? 5 : ((x instanceof int[]) ? 6 : ((x instanceof long[]) ? 7 : ((x instanceof float[]) ? 8 : ((x instanceof double[]) ? 9 : ((x instanceof char[]) ? 10 : ((x instanceof String[]) ? 11 : ((x instanceof Date[]) ? 14 : ((x instanceof Time[]) ? 19 : ((x instanceof Timestamp[]) ? 12 : ((x instanceof Date[]) ? 15 : ((x instanceof Timespan[]) ? 16 : ((x instanceof Month[]) ? 13 : ((x instanceof Minute[]) ? 17 : ((x instanceof Second[]) ? 18 : ((x instanceof Flip) ? 98 : ((x instanceof Dict) ? 99 : 0)))))))))))))))))))))))))))))))))))));
    }

    static int ns(String s) throws UnsupportedEncodingException {
        if (s == null) return 0;
        int i;
        if (-1 < (i = s.indexOf(String.valueOf(false)))) s = s.substring(0, i);
        return (s.getBytes(e)).length;
    }

    public static int n(Object x) throws UnsupportedEncodingException {
        return (x instanceof Dict) ? n(((Dict) x).x) : ((x instanceof Flip) ? n(((Flip) x).y[0]) : ((x instanceof char[]) ? ((new String((char[]) x)).getBytes(e)).length : Array.getLength(x)));
    }

    public static boolean qn(Object x) {
        int t = -t(x);
        return (t > 4 && x.equals(NULL[t]));
    }

    public static Object at(Object x, int i) {
        return qn(x = Array.get(x, i)) ? null : x;
    }

    public static void set(Object x, int i, Object y) {
        Array.set(x, i, (null == y) ? NULL[t(x)] : y);
    }

    static int find(String[] x, String y) {
        int i = 0;
        while (i < x.length && !x[i].equals(y))
            i++;
        return i;
    }

    public static Flip td(Object X) throws UnsupportedEncodingException {
        if (X instanceof Flip)
            return (Flip) X;
        Dict d = (Dict) X;
        Flip a = (Flip) d.x, b = (Flip) d.y;
        int m = n(a.x), n = n(b.x);
        String[] x = new String[m + n];
        System.arraycopy(a.x, 0, x, 0, m);
        System.arraycopy(b.x, 0, x, m, n);
        Object[] y = new Object[m + n];
        System.arraycopy(a.y, 0, y, 0, m);
        System.arraycopy(b.y, 0, y, m, n);
        return new Flip(new Dict(x, y));
    }

    public static Object O(Object x) {
        out.println(x);
        return x;
    }

    public static void O(int x) {
        out.println(x);
    }

    public static void O(boolean x) {
        out.println(x);
    }

    public static void O(long x) {
        out.println(x);
    }

    public static void O(double x) {
        out.println(x);
    }

    public static long t() {
        return System.currentTimeMillis();
    }

    public static void tm() {
        long u = t;
        t = t();
        if (u > 0L)
            O(t - u);
    }

    static String i2(int i) {
        return (new DecimalFormat("00")).format(i);
    }

    static String i9(int i) {
        return (new DecimalFormat("000000000")).format(i);
    }

    void io(Socket x) throws IOException {
        this.s = x;
        this.i = new DataInputStream(this.s.getInputStream());
        this.o = this.s.getOutputStream();
    }

    public void close() throws IOException {
        if (null != this.s) {
            this.s.close();
            this.s = null;
        }
        if (null != this.i) {
            this.i.close();
            this.i = null;
        }
        if (null != this.o) {
            this.o.close();
            this.o = null;
        }
    }

    private void u() {
        int n = 0, r = 0, f = 0, s = 8, p = s;
        short i = 0;
        this.j = 0;
        byte[] dst = new byte[ri()];
        int d = this.j;
        int[] aa = new int[256];
        while (s < dst.length) {
            if (i == 0) {
                f = 0xFF & this.b[d++];
                i = 1;
            }
            if ((f & i) != 0) {
                r = aa[0xFF & this.b[d++]];
                dst[s++] = dst[r++];
                dst[s++] = dst[r++];
                n = 0xFF & this.b[d++];
                for (int m = 0; m < n; m++) dst[s + m] = dst[r + m];
            } else {
                dst[s++] = this.b[d++];
            }
            while (p < s - 1) aa[0xFF & dst[p] ^ 0xFF & dst[p + 1]] = p++;
            if ((f & i) != 0) p = s += n;
            i = (short) (i * 2);
            if (i == 256) i = 0;
        }
        this.b = dst;
        this.j = 8;
    }

    void w(byte x) {
        this.B[this.J++] = x;
    }

    boolean rb() {
        return (1 == this.b[this.j++]);
    }

    void w(boolean x) {
        w((byte) (x ? 1 : 0));
    }

    char rc() {
        return (char) (this.b[this.j++] & 0xFF);
    }

    void w(char c1) {
        w((byte) c1);
    }

    short rh() {
        int x = this.b[this.j++], y = this.b[this.j++];
        return (short) (this.a ? (x & 0xFF | y << 8) : (x << 8 | y & 0xFF));
    }

    void w(short h) {
        w((byte) (h >> 8));
        w((byte) h);
    }

    int ri() {
        int x = rh(), y = rh();
        return this.a ? (x & 0xFFFF | y << 16) : (x << 16 | y & 0xFFFF);
    }

    void w(int i) {
        w((short) (i >> 16));
        w((short) i);
    }

    UUID rg() {
        boolean oa = this.a;
        this.a = false;
        UUID g = new UUID(rj(), rj());
        this.a = oa;
        return g;
    }

    void w(UUID uuid) {
        if (this.vt < 3) throw new RuntimeException("Guid not valid pre kdb+3.0");
        w(uuid.getMostSignificantBits());
        w(uuid.getLeastSignificantBits());
    }

    long rj() {
        int x = ri(), y = ri();
        return this.a ? (x & 0xFFFFFFFFL | (long) y << 32L) : ((long) x << 32L | y & 0xFFFFFFFFL);
    }

    void w(long j) {
        w((int) (j >> 32L));
        w((int) j);
    }

    float re() {
        return Float.intBitsToFloat(ri());
    }

    void w(float e) {
        w(Float.floatToIntBits(e));
    }

    double rf() {
        return Double.longBitsToDouble(rj());
    }

    void w(double f) {
        w(Double.doubleToLongBits(f));
    }

    Month rm() {
        return new Month(ri());
    }

    void w(Month m) {
        w(m.i);
    }

    Minute ru() {
        return new Minute(ri());
    }

    void w(Minute u) {
        w(u.i);
    }

    Second rv() {
        return new Second(ri());
    }

    void w(Second v) {
        w(v.i);
    }

    Timespan rn() {
        return new Timespan(rj());
    }

    void w(Timespan n) {
        if (this.vt < 1) throw new RuntimeException("Timespan not valid pre kdb+2.6");
        w(n.j);
    }

    long o(long x) {
        return this.tz.getOffset(x);
    }

    long lg(long x) {
        return x + o(x);
    }

    long gl(long x) {
        return x - o(x - o(x));
    }

    Date rd() {
        int i = ri();
        return new Date((i == ni) ? nj : gl(k + 86400000L * i));
    }

    void w(java.util.Date d) {
        long j = d.getTime();
        w((j == nj) ? ni : (int) (lg(j) / 86400000L - 10957L));
    }

    Time rt() {
        int i = ri();
        return new Time((i == ni) ? nj : gl(i));
    }

    void w(Time t) {
        long j = t.getTime();
        w((j == nj) ? ni : (int) (lg(j) % 86400000L));
    }

    Date rz() {
        double f = rf();
        return new Date(Double.isNaN(f) ? nj : gl(k + Math.round(8.64E7D * f)));
    }

    void w(Date z) {
        long j = z.getTime();
        w((j == nj) ? nf : ((lg(j) - k) / 8.64E7D));
    }

    Timestamp rp() {
        long j = rj(), d = (j < 0L) ? ((j + 1L) / n - 1L) : (j / n);
        Timestamp p = new Timestamp((j == nj) ? j : gl(k + 1000L * d));
        if (j != nj) p.setNanos((int) (j - n * d));
        return p;
    }

    void w(Timestamp p) {
        long j = p.getTime();
        if (this.vt < 1) throw new RuntimeException("Timestamp not valid pre kdb+2.6");
        w((j == nj) ? j : (1000000L * (lg(j) - k) + (p.getNanos() % 1000000)));
    }

    String rs() throws UnsupportedEncodingException {
        int i = this.j;
        while (this.b[this.j++] != 0) ;
        return (i == this.j - 1) ? "" : new String(this.b, i, this.j - 1 - i, e);
    }

    void w(String s) throws UnsupportedEncodingException {
        int i = 0, n = ns(s);
        byte[] b = s.getBytes(e);
        while (i < n) w(b[i++]);
        this.B[this.J++] = 0;
    }

    Object r() throws UnsupportedEncodingException {
        Object[] L;
        boolean[] B;
        UUID[] arrayOfUUID;
        byte[] G;
        short[] H;
        int[] I;
        long[] J;
        float[] E;
        double[] F;
        char[] C;
        String[] S;
        Timestamp[] P;
        Month[] M;
        Date[] D;
        Date[] Z;
        Timespan[] N;
        Minute[] U;
        Second[] V;
        Time[] T;
        int i = 0, t = this.b[this.j++];
        if (t < 0) switch (t) {
            case -1:
                return Boolean.valueOf(rb());
            case -2:
                return rg();
            case -4:
                return Byte.valueOf(this.b[this.j++]);
            case -5:
                return Short.valueOf(rh());
            case -6:
                return Integer.valueOf(ri());
            case -7:
                return Long.valueOf(rj());
            case -8:
                return new Float(re());
            case -9:
                return new Double(rf());
            case -10:
                return new Character(rc());
            case -11:
                return rs();
            case -12:
                return rp();
            case -13:
                return rm();
            case -14:
                return rd();
            case -15:
                return rz();
            case -16:
                return rn();
            case -17:
                return ru();
            case -18:
                return rv();
            case -19:
                return rt();
        }
        if (t > 99) {
            if (t == 100) {
                rs();
                return r();
            }
            if (t == 102 || t == 101) {
                int p = this.b[this.j];
                char[][] funcs = (t == 101) ? FUNC_1 : FUNC_2;
                if (p < funcs.length && p >= 0) {
                    this.j++;
                    return funcs[p];
                }
            } else if (t >= 106 && t <= 111) {
                String adverb = ADVERBS[t - 106];
                Object def = r();
                if (def instanceof char[]) return (new String((char[]) def) + adverb).toCharArray();
                return null;
            }
            if (t == 104) {
                String s = "";
                boolean first = true;
                for (int j = ri(); i < j; i++) {
                    Object o = r();
                    if (o instanceof char[]) o = new String((char[]) o);
                    s = s + o;
                    if (first) {
                        first = false;
                        s = s + "[";
                    } else if (i != j - 1) {
                        s = s + ";";
                    } else {
                        s = s + "]";
                    }
                }
                return s.toCharArray();
            }
            if (t == 101 && this.b[this.j] == -1) {
                this.j++;
                return "";
            }
            if (t < 104) return (this.b[this.j++] == 0 && t == 101) ? null : "func";
            if (t > 105) {
                r();
            } else {
                for (int j = ri(); i < j; i++) r();
            }
            return "func";
        }
        if (t == 99) return new Dict(r(), r());
        this.j++;
        if (t == 98) return new Flip((Dict) r());
        int n = ri();
        switch (t) {
            case 0:
                L = new Object[n];
                for (; i < n; i++) L[i] = r();
                return L;
            case 1:
                B = new boolean[n];
                for (; i < n; i++) B[i] = rb();
                return B;
            case 2:
                arrayOfUUID = new UUID[n];
                for (; i < n; i++) arrayOfUUID[i] = rg();
                return arrayOfUUID;
            case 4:
                G = new byte[n];
                for (; i < n; i++) G[i] = this.b[this.j++];
                return G;
            case 5:
                H = new short[n];
                for (; i < n; i++) H[i] = rh();
                return H;
            case 6:
                I = new int[n];
                for (; i < n; i++) I[i] = ri();
                return I;
            case 7:
                J = new long[n];
                for (; i < n; i++) J[i] = rj();
                return J;
            case 8:
                E = new float[n];
                for (; i < n; i++) E[i] = re();
                return E;
            case 9:
                F = new double[n];
                for (; i < n; i++) F[i] = rf();
                return F;
            case 10:
                C = (new String(this.b, this.j, n, e)).toCharArray();
                this.j += n;
                return C;
            case 11:
                S = new String[n];
                for (; i < n; i++) S[i] = rs();
                return S;
            case 12:
                P = new Timestamp[n];
                for (; i < n; i++) P[i] = rp();
                return P;
            case 13:
                M = new Month[n];
                for (; i < n; i++) M[i] = rm();
                return M;
            case 14:
                D = new Date[n];
                for (; i < n; i++) D[i] = rd();
                return D;
            case 15:
                Z = new Date[n];
                for (; i < n; i++) Z[i] = rz();
                return Z;
            case 16:
                N = new Timespan[n];
                for (; i < n; i++) N[i] = rn();
                return N;
            case 17:
                U = new Minute[n];
                for (; i < n; i++) U[i] = ru();
                return U;
            case 18:
                V = new Second[n];
                for (; i < n; i++) V[i] = rv();
                return V;
            case 19:
                T = new Time[n];
                for (; i < n; i++) T[i] = rt();
                return T;
        }
        return null;
    }

    public int nx(Object x) throws UnsupportedEncodingException {
        int i = 0, t = t(x);
        if (t == 99) return 1 + nx(((Dict) x).x) + nx(((Dict) x).y);
        if (t == 98) return 3 + nx(((Flip) x).x) + nx(((Flip) x).y);
        if (t < 0) return (t == -11) ? (2 + ns((String) x)) : (1 + nt[-t]);
        int j = 6;
        int n = n(x);
        if (t == 0 || t == 11) {
            for (; i < n; i++) j += (t == 0) ? nx(((Object[]) x)[i]) : (1 + ns(((String[]) x)[i]));
        } else {
            j += n * nt[t];
        }
        return j;
    }

    void w(Object x) throws UnsupportedEncodingException {
        int i = 0, t = t(x);
        w((byte) t);
        if (t < 0) switch (t) {
            case -1:
                w(((Boolean) x).booleanValue());
                return;
            case -2:
                w((UUID) x);
                return;
            case -4:
                w(((Byte) x).byteValue());
                return;
            case -5:
                w(((Short) x).shortValue());
                return;
            case -6:
                w(((Integer) x).intValue());
                return;
            case -7:
                w(((Long) x).longValue());
                return;
            case -8:
                w(((Float) x).floatValue());
                return;
            case -9:
                w(((Double) x).doubleValue());
                return;
            case -10:
                w(((Character) x).charValue());
                return;
            case -11:
                w((String) x);
                return;
            case -12:
                w((Timestamp) x);
                return;
            case -13:
                w((Month) x);
                return;
            case -14:
                w((Date) x);
                return;
            case -15:
                w((Date) x);
                return;
            case -16:
                w((Timespan) x);
                return;
            case -17:
                w((Minute) x);
                return;
            case -18:
                w((Second) x);
                return;
            case -19:
                w((Time) x);
                return;
        }
        if (t == 99) {
            Dict r = (Dict) x;
            w(r.x);
            w(r.y);
            return;
        }
        this.B[this.J++] = 0;
        if (t == 98) {
            Flip r = (Flip) x;
            this.B[this.J++] = 99;
            w(r.x);
            w(r.y);
            return;
        }
        int n;
        w(n = n(x));
        if (t == 10) {
            byte[] b = (new String((char[]) x)).getBytes(e);
            while (i < b.length) w(b[i++]);
        } else {
            for (; i < n; i++) {
                if (t == 0) {
                    w(((Object[]) x)[i]);
                } else if (t == 1) {
                    w(((boolean[]) x)[i]);
                } else if (t == 2) {
                    w(((UUID[]) x)[i]);
                } else if (t == 4) {
                    w(((byte[]) x)[i]);
                } else if (t == 5) {
                    w(((short[]) x)[i]);
                } else if (t == 6) {
                    w(((int[]) x)[i]);
                } else if (t == 7) {
                    w(((long[]) x)[i]);
                } else if (t == 8) {
                    w(((float[]) x)[i]);
                } else if (t == 9) {
                    w(((double[]) x)[i]);
                } else if (t == 11) {
                    w(((String[]) x)[i]);
                } else if (t == 12) {
                    w(((Timestamp[]) x)[i]);
                } else if (t == 13) {
                    w(((Month[]) x)[i]);
                } else if (t == 14) {
                    w(((Date[]) x)[i]);
                } else if (t == 15) {
                    w(((Date[]) x)[i]);
                } else if (t == 16) {
                    w(((Timespan[]) x)[i]);
                } else if (t == 17) {
                    w(((Minute[]) x)[i]);
                } else if (t == 18) {
                    w(((Second[]) x)[i]);
                } else {
                    w(((Time[]) x)[i]);
                }
            }
        }
    }

    protected void w(int i, Object x) throws IOException {
        int n = nx(x) + 8;
        synchronized (this.o) {
            this.B = new byte[n];
            this.B[0] = 0;
            this.B[1] = (byte) i;
            this.J = 4;
            w(n);
            w(x);
            this.o.write(this.B);
        }
    }

    public void kr(Object x) throws IOException {
        if (this.sync == 0) throw new IOException("Unexpected response msg");
        this.sync--;
        w(2, x);
    }

    public void ke(String s) throws IOException {
        if (this.sync == 0) throw new IOException("Unexpected error msg");
        this.sync--;
        int n = 2 + ns(s) + 8;
        synchronized (this.o) {
            this.B = new byte[n];
            this.B[0] = 0;
            this.B[1] = 2;
            this.J = 4;
            w(n);
            w(-128);
            w(s);
            this.o.write(this.B);
        }
    }

    public void ks(String s) throws IOException {
        w(0, cs(s));
    }

    public void ks(Object x) throws IOException {
        w(0, x);
    }

    char[] cs(String s) {
        return s.toCharArray();
    }

    public void ks(String s, Object x) throws IOException {
        Object[] a = {cs(s), x};
        w(0, a);
    }

    public void ks(String s, Object x, Object y) throws IOException {
        Object[] a = {cs(s), x, y};
        w(0, a);
    }

    public void ks(String s, Object x, Object y, Object z) throws IOException {
        Object[] a = {cs(s), x, y, z};
        w(0, a);
    }

    public Object k() throws KException, IOException {
        synchronized (this.i) {
            this.i.readFully(this.b = new byte[8]);
            this.a = (this.b[0] == 1);
            byte msgType = this.b[1];
            if (msgType == 1) {
                close();
                throw new IOException("Cannot process sync msg from remote");
            }
            if (this.b[1] == 1) this.sync++;
            boolean bool = (this.b[2] == 1);
            this.j = 4;
            this.i.readFully(this.b = new byte[ri() - 8]);
            if (bool) {
                u();
            } else {
                this.j = 0;
            }
            if (this.b[0] == Byte.MIN_VALUE) {
                this.j = 1;
                throw new KException(rs());
            }
            return r();
        }
    }

    public synchronized Object k(Object x) throws KException, IOException {
        w(1, x);
        return k();
    }

    public Object k(String s) throws KException, IOException {
        return k(cs(s));
    }

    public Object k(String s, Object x) throws KException, IOException {
        Object[] a = {cs(s), x};
        return k(a);
    }

    public Object k(String s, Object x, Object y) throws KException, IOException {
        Object[] a = {cs(s), x, y};
        return k(a);
    }

    public Object k(String s, Object x, Object y, Object z) throws KException, IOException {
        Object[] a = {cs(s), x, y, z};
        return k(a);
    }

    public interface IAuthenticate {
        boolean authenticate(String param1String);
    }

    public static class Month implements Comparable<Month> {
        public int i;

        public Month(int x) {
            this.i = x;
        }

        public String toString() {
            int m = this.i + 24000, y = m / 12;
            return (this.i == C.ni) ? "" : (C.i2(y / 100) + C.i2(y % 100) + "-" + C.i2(1 + m % 12));
        }

        public boolean equals(Object o) {
            return o instanceof Month && ((((Month) o).i == this.i));
        }

        public int hashCode() {
            return this.i;
        }

        public int compareTo(Month m) {
            return this.i - m.i;
        }
    }

    public static class Minute implements Comparable<Minute> {
        public int i;

        public Minute(int x) {
            this.i = x;
        }

        public String toString() {
            return (this.i == C.ni) ? "" : (C.i2(this.i / 60) + ":" + C.i2(this.i % 60));
        }

        public boolean equals(Object o) {
            return o instanceof Minute && ((((Minute) o).i == this.i));
        }

        public int hashCode() {
            return this.i;
        }

        public int compareTo(Minute m) {
            return this.i - m.i;
        }
    }

    public static class Second implements Comparable<Second> {
        public int i;

        public Second(int x) {
            this.i = x;
        }

        public String toString() {
            return (this.i == C.ni) ? "" : ((new C.Minute(this.i / 60)).toString() + ':' + C.i2(this.i % 60));
        }

        public boolean equals(Object o) {
            return o instanceof Second && ((((Second) o).i == this.i));
        }

        public int hashCode() {
            return this.i;
        }

        public int compareTo(Second s) {
            return this.i - s.i;
        }
    }

    public static class Timespan implements Comparable<Timespan> {
        public long j;

        public Timespan(long x) {
            this.j = x;
        }

        public String toString() {
            if (this.j == C.nj) return "";
            String s = (this.j < 0L) ? "-" : "";
            long jj = (this.j < 0L) ? -this.j : this.j;
            int d = (int) (jj / 86400000000000L);
            if (d != 0) s = s + d + "D";
            return s + C.i2((int) (jj % 86400000000000L / 3600000000000L)) + ":" + C.i2((int) (jj % 3600000000000L / 60000000000L)) + ":" + C.i2((int) (jj % 60000000000L / 1000000000L)) + "." + C.i9((int) (jj % 1000000000L));
        }

        public int compareTo(Timespan t) {
            return (this.j > t.j) ? 1 : ((this.j < t.j) ? -1 : 0);
        }

        public boolean equals(Object o) {
            return o instanceof Timespan && ((((Timespan) o).j == this.j));
        }

        public int hashCode() {
            return (int) (this.j ^ this.j >>> 32L);
        }
    }

    public static class Dict {
        public Object x;
        public Object y;

        public Dict(Object X, Object Y) {
            this.x = X;
            this.y = Y;
        }
    }

    public static class Flip {
        public String[] x;
        public Object[] y;

        public Flip(C.Dict X) {
            this.x = (String[]) X.x;
            this.y = (Object[]) X.y;
        }

        public Object at(String s) {
            return this.y[C.find(this.x, s)];
        }
    }

    public static class KException extends Exception {
        KException(String s) {
            super(s);
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\kx\c.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */