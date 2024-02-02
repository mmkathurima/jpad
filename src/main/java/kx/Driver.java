package kx;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.concurrent.Executor;
import java.util.logging.Logger;

public class Driver implements java.sql.Driver {
    static int V = 2;
    static int v = 0;
    static int[] SQLTYPE = new int[]{0, 16, 0, 0, -2, 5, 4, -5, 7, 8, 0, 12, 93, 91, 91, 93, 93, 92, 92, 92};
    static String[] TYPE = new String[]{"", "boolean", "", "", "byte", "short", "int", "long", "real", "float", "char", "symbol", "timestamp", "month", "date", "timestamp", "timespan", "minute", "second", "time"};

    static {
        try {
            DriverManager.registerDriver(new Driver());
        } catch (Exception e) {
            O(e.getMessage());
        }
    }

    static void O(String s) {
        System.out.println(s);
    }

    static int find(String[] x, String s) {
        int i = 0;
        while (i < x.length && !s.equals(x[i]))
            i++;
        return i;
    }

    static int find(int[] x, int j) {
        int i = 0;
        while (i < x.length && x[i] != j)
            i++;
        return i;
    }

    static void q(String s) throws SQLException {
        throw new SQLException(s);
    }

    static void q() throws SQLException {
        throw new SQLFeatureNotSupportedException("nyi");
    }

    static void q(Exception e) throws SQLException {
        throw new SQLException(e.getMessage());
    }

    public int getMajorVersion() {
        return V;
    }

    public int getMinorVersion() {
        return v;
    }

    public boolean jdbcCompliant() {
        return false;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return Logger.getLogger(this.getClass().getName());
    }

    public boolean acceptsURL(String s) {
        return s.startsWith("jdbc:q:");
    }

    public java.sql.Connection connect(String s, Properties p) throws SQLException {
        return !acceptsURL(s) ? null : new Connection(s.substring(7), (p != null) ? p.get("user") : p, (p != null) ? p.get("password") : p);
    }

    public DriverPropertyInfo[] getPropertyInfo(String s, Properties p) throws SQLException {
        return new DriverPropertyInfo[0];
    }

    public static class ResultSet implements java.sql.ResultSet {
        private final Statement Statement;
        private final String[] f;
        private final int n;
        private Object o;
        private Object[] d;
        private int r;

        public ResultSet(Statement s, Object x) throws SQLException {
            this.Statement = s;

            try {
                C.Flip a = C.td(x);
                this.f = a.x;
                this.d = a.y;
                this.n = C.n(this.d[0]);
                this.r = -1;
            } catch (UnsupportedEncodingException ex) {
                throw new SQLException(ex);
            }
        }

        public java.sql.ResultSetMetaData getMetaData() throws SQLException {
            return new ResultSetMetaData(this.f, this.d);
        }

        public int findColumn(String s) throws SQLException {
            return 1 + Driver.find(this.f, s);
        }

        public boolean next() throws SQLException {
            return (++this.r < this.n);
        }

        public boolean wasNull() throws SQLException {
            return (this.o == null);
        }

        public Object getObject(int i) throws SQLException {
            this.o = C.at(this.d[i - 1], this.r);

            if (this.o instanceof C.Month)
                return new MonthDate((C.Month) this.o);
            if (this.o instanceof C.Second)
                return new SecondTime((C.Second) this.o);
            if (this.o instanceof C.Minute)
                return new MinuteTime((C.Minute) this.o);
            if (this.o instanceof C.Minute)
                return new MinuteTime((C.Minute) this.o);
            if (this.o instanceof C.Timespan) {
                return new TimespanTimestamp((C.Timespan) this.o);
            }

            return (this.o instanceof char[]) ? new String((char[]) this.o) : this.o;
        }

        public boolean getBoolean(int i) throws SQLException {
            return ((Boolean) getObject(i)).booleanValue();
        }

        public byte getByte(int i) throws SQLException {
            return ((Byte) getObject(i)).byteValue();
        }

        public short getShort(int i) throws SQLException {
            Object x = getObject(i);
            return (x == null) ? 0 : ((Short) x).shortValue();
        }

        public int getInt(int i) throws SQLException {
            Object x = getObject(i);
            return (x == null) ? 0 : ((Integer) x).intValue();
        }

        public long getLong(int i) throws SQLException {
            Object x = getObject(i);
            return (x == null) ? 0L : ((Long) x).longValue();
        }

        public float getFloat(int i) throws SQLException {
            Object x = getObject(i);
            return (x == null) ? 0.0F : ((Float) x).floatValue();
        }

        public double getDouble(int i) throws SQLException {
            Object x = getObject(i);
            return (x == null) ? 0.0D : ((Double) x).doubleValue();
        }

        public String getString(int i) throws SQLException {
            Object x = getObject(i);
            return (x == null) ? null : x.toString();
        }

        public Date getDate(int i) throws SQLException {
            return (Date) getObject(i);
        }

        public Time getTime(int i) throws SQLException {
            return (Time) getObject(i);
        }

        public Timestamp getTimestamp(int i) throws SQLException {
            return (Timestamp) getObject(i);
        }

        public byte[] getBytes(int i) throws SQLException {
            Driver.q();
            return null;
        }

        public BigDecimal getBigDecimal(int i, int scale) throws SQLException {
            Driver.q();
            return null;
        }

        public InputStream getAsciiStream(int i) throws SQLException {
            Driver.q();
            return null;
        }

        public InputStream getUnicodeStream(int i) throws SQLException {
            Driver.q();
            return null;
        }

        public InputStream getBinaryStream(int i) throws SQLException {
            Driver.q();
            return null;
        }

        public Object getObject(String s) throws SQLException {
            return getObject(findColumn(s));
        }

        public boolean getBoolean(String s) throws SQLException {
            return getBoolean(findColumn(s));
        }

        public byte getByte(String s) throws SQLException {
            return getByte(findColumn(s));
        }

        public short getShort(String s) throws SQLException {
            return getShort(findColumn(s));
        }

        public int getInt(String s) throws SQLException {
            return getInt(findColumn(s));
        }

        public long getLong(String s) throws SQLException {
            return getLong(findColumn(s));
        }

        public float getFloat(String s) throws SQLException {
            return getFloat(findColumn(s));
        }

        public double getDouble(String s) throws SQLException {
            return getDouble(findColumn(s));
        }

        public String getString(String s) throws SQLException {
            return getString(findColumn(s));
        }

        public Date getDate(String s) throws SQLException {
            return getDate(findColumn(s));
        }

        public Time getTime(String s) throws SQLException {
            return getTime(findColumn(s));
        }

        public Timestamp getTimestamp(String s) throws SQLException {
            return getTimestamp(findColumn(s));
        }

        public byte[] getBytes(String s) throws SQLException {
            return getBytes(findColumn(s));
        }

        public BigDecimal getBigDecimal(String s, int scale) throws SQLException {
            return getBigDecimal(findColumn(s), scale);
        }

        public InputStream getAsciiStream(String s) throws SQLException {
            return getAsciiStream(findColumn(s));
        }

        public InputStream getUnicodeStream(String s) throws SQLException {
            return getUnicodeStream(findColumn(s));
        }

        public InputStream getBinaryStream(String s) throws SQLException {
            return getBinaryStream(findColumn(s));
        }

        public SQLWarning getWarnings() throws SQLException {
            return null;
        }

        public void clearWarnings() throws SQLException {
        }

        public String getCursorName() throws SQLException {
            Driver.q("cur");
            return "";
        }

        public void close() throws SQLException {
            this.d = null;
        }

        public Reader getCharacterStream(int columnIndex) throws SQLException {
            Driver.q();
            return null;
        }

        public Reader getCharacterStream(String columnName) throws SQLException {
            Driver.q();
            return null;
        }

        public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
            Driver.q();
            return null;
        }

        public BigDecimal getBigDecimal(String columnName) throws SQLException {
            Driver.q();
            return null;
        }

        public boolean isBeforeFirst() throws SQLException {
            return (this.r < 0);
        }

        public boolean isAfterLast() throws SQLException {
            return (this.r >= this.n);
        }

        public boolean isFirst() throws SQLException {
            return (this.r == 0);
        }

        public boolean isLast() throws SQLException {
            return (this.r == this.n - 1);
        }

        public void beforeFirst() throws SQLException {
            this.r = -1;
        }

        public void afterLast() throws SQLException {
            this.r = this.n;
        }

        public boolean first() throws SQLException {
            this.r = 0;
            return (this.n > 0);
        }

        public boolean last() throws SQLException {
            this.r = this.n - 1;
            return (this.n > 0);
        }

        public int getRow() throws SQLException {
            return this.r + 1;
        }

        public boolean absolute(int row) throws SQLException {
            this.r = row - 1;
            return (this.r < this.n);
        }

        public boolean relative(int rows) throws SQLException {
            this.r += rows;
            return (this.r >= 0 && this.r < this.n);
        }

        public boolean previous() throws SQLException {
            this.r--;
            return (this.r >= 0);
        }

        public int getFetchDirection() throws SQLException {
            return 1000;
        }

        public void setFetchDirection(int direction) throws SQLException {
            Driver.q("fd");
        }

        public int getFetchSize() throws SQLException {
            return 0;
        }

        public void setFetchSize(int rows) throws SQLException {
        }

        public int getType() throws SQLException {
            return 1005;
        }

        public int getConcurrency() throws SQLException {
            return 1007;
        }

        public boolean rowUpdated() throws SQLException {
            Driver.q();
            return false;
        }

        public boolean rowInserted() throws SQLException {
            Driver.q();
            return false;
        }

        public boolean rowDeleted() throws SQLException {
            Driver.q();
            return false;
        }

        public void updateNull(int columnIndex) throws SQLException {
            Driver.q();
        }

        public void updateBoolean(int columnIndex, boolean x) throws SQLException {
            Driver.q();
        }

        public void updateByte(int columnIndex, byte x) throws SQLException {
            Driver.q();
        }

        public void updateShort(int columnIndex, short x) throws SQLException {
            Driver.q();
        }

        public void updateInt(int columnIndex, int x) throws SQLException {
            Driver.q();
        }

        public void updateLong(int columnIndex, long x) throws SQLException {
            Driver.q();
        }

        public void updateFloat(int columnIndex, float x) throws SQLException {
            Driver.q();
        }

        public void updateDouble(int columnIndex, double x) throws SQLException {
            Driver.q();
        }

        public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
            Driver.q();
        }

        public void updateString(int columnIndex, String x) throws SQLException {
            Driver.q();
        }

        public void updateBytes(int columnIndex, byte[] x) throws SQLException {
            Driver.q();
        }

        public void updateDate(int columnIndex, Date x) throws SQLException {
            Driver.q();
        }

        public void updateTime(int columnIndex, Time x) throws SQLException {
            Driver.q();
        }

        public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
            Driver.q();
        }

        public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
            Driver.q();
        }

        public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
            Driver.q();
        }

        public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
            Driver.q();
        }

        public void updateObject(int columnIndex, Object x, int scale) throws SQLException {
            Driver.q();
        }

        public void updateObject(int columnIndex, Object x) throws SQLException {
            Driver.q();
        }

        public void updateNull(String columnName) throws SQLException {
            Driver.q();
        }

        public void updateBoolean(String columnName, boolean x) throws SQLException {
            Driver.q();
        }

        public void updateByte(String columnName, byte x) throws SQLException {
            Driver.q();
        }

        public void updateShort(String columnName, short x) throws SQLException {
            Driver.q();
        }

        public void updateInt(String columnName, int x) throws SQLException {
            Driver.q();
        }

        public void updateLong(String columnName, long x) throws SQLException {
            Driver.q();
        }

        public void updateFloat(String columnName, float x) throws SQLException {
            Driver.q();
        }

        public void updateDouble(String columnName, double x) throws SQLException {
            Driver.q();
        }

        public void updateBigDecimal(String columnName, BigDecimal x) throws SQLException {
            Driver.q();
        }

        public void updateString(String columnName, String x) throws SQLException {
            Driver.q();
        }

        public void updateBytes(String columnName, byte[] x) throws SQLException {
            Driver.q();
        }

        public void updateDate(String columnName, Date x) throws SQLException {
            Driver.q();
        }

        public void updateTime(String columnName, Time x) throws SQLException {
            Driver.q();
        }

        public void updateTimestamp(String columnName, Timestamp x) throws SQLException {
            Driver.q();
        }

        public void updateAsciiStream(String columnName, InputStream x, int length) throws SQLException {
            Driver.q();
        }

        public void updateBinaryStream(String columnName, InputStream x, int length) throws SQLException {
            Driver.q();
        }

        public void updateCharacterStream(String columnName, Reader reader, int length) throws SQLException {
            Driver.q();
        }

        public void updateObject(String columnName, Object x, int scale) throws SQLException {
            Driver.q();
        }

        public void updateObject(String columnName, Object x) throws SQLException {
            Driver.q();
        }

        public void insertRow() throws SQLException {
            Driver.q();
        }

        public void updateRow() throws SQLException {
            Driver.q();
        }

        public void deleteRow() throws SQLException {
            Driver.q();
        }

        public void refreshRow() throws SQLException {
            Driver.q();
        }

        public void cancelRowUpdates() throws SQLException {
            Driver.q();
        }

        public void moveToInsertRow() throws SQLException {
            Driver.q();
        }

        public void moveToCurrentRow() throws SQLException {
            Driver.q();
        }

        public java.sql.Statement getStatement() throws SQLException {
            return this.Statement;
        }

        public Object getObject(int i, Map map) throws SQLException {
            Driver.q();
            return null;
        }

        public Ref getRef(int i) throws SQLException {
            Driver.q();
            return null;
        }

        public Blob getBlob(int i) throws SQLException {
            Driver.q();
            return null;
        }

        public Clob getClob(int i) throws SQLException {
            Driver.q();
            return null;
        }

        public Array getArray(int i) throws SQLException {
            Driver.q();
            return null;
        }

        public Object getObject(String colName, Map map) throws SQLException {
            Driver.q();
            return null;
        }

        public Ref getRef(String colName) throws SQLException {
            Driver.q();
            return null;
        }

        public Blob getBlob(String colName) throws SQLException {
            Driver.q();
            return null;
        }

        public Clob getClob(String colName) throws SQLException {
            Driver.q();
            return null;
        }

        public Array getArray(String colName) throws SQLException {
            Driver.q();
            return null;
        }

        public Date getDate(int columnIndex, Calendar cal) throws SQLException {
            Driver.q();
            return null;
        }

        public Date getDate(String columnName, Calendar cal) throws SQLException {
            Driver.q();
            return null;
        }

        public Time getTime(int columnIndex, Calendar cal) throws SQLException {
            Driver.q();
            return null;
        }

        public Time getTime(String columnName, Calendar cal) throws SQLException {
            Driver.q();
            return null;
        }

        public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
            Driver.q();
            return null;
        }

        public Timestamp getTimestamp(String columnName, Calendar cal) throws SQLException {
            Driver.q();
            return null;
        }

        public URL getURL(int columnIndex) throws SQLException {
            Driver.q();
            return null;
        }

        public URL getURL(String columnName) throws SQLException {
            Driver.q();
            return null;
        }

        public void updateRef(int columnIndex, Ref x) throws SQLException {
            Driver.q();
        }

        public void updateRef(String columnName, Ref x) throws SQLException {
            Driver.q();
        }

        public void updateBlob(int columnIndex, Blob x) throws SQLException {
            Driver.q();
        }

        public void updateBlob(String columnName, Blob x) throws SQLException {
            Driver.q();
        }

        public void updateClob(int columnIndex, Clob x) throws SQLException {
            Driver.q();
        }

        public void updateClob(String columnName, Clob x) throws SQLException {
            Driver.q();
        }

        public void updateArray(int columnIndex, Array x) throws SQLException {
            Driver.q();
        }

        public void updateArray(String columnName, Array x) throws SQLException {
            Driver.q();
        }

        public RowId getRowId(int i) throws SQLException {
            Driver.q();
            return null;
        }

        public RowId getRowId(String string) throws SQLException {
            Driver.q();
            return null;
        }

        public void updateRowId(int i, RowId rowid) throws SQLException {
            Driver.q();
        }

        public void updateRowId(String string, RowId rowid) throws SQLException {
            Driver.q();
        }

        public int getHoldability() throws SQLException {
            Driver.q();
            return 0;
        }

        public boolean isClosed() throws SQLException {
            return (this.d == null);
        }

        public void updateNString(int i, String string) throws SQLException {
            Driver.q();
        }

        public void updateNString(String string, String string1) throws SQLException {
            Driver.q();
        }

        public void updateNClob(int i, NClob nclob) throws SQLException {
            Driver.q();
        }

        public void updateNClob(String string, NClob nclob) throws SQLException {
            Driver.q();
        }

        public NClob getNClob(int i) throws SQLException {
            Driver.q();
            return null;
        }

        public NClob getNClob(String string) throws SQLException {
            Driver.q();
            return null;
        }

        public SQLXML getSQLXML(int i) throws SQLException {
            Driver.q();
            return null;
        }

        public SQLXML getSQLXML(String string) throws SQLException {
            Driver.q();
            return null;
        }

        public void updateSQLXML(int i, SQLXML sqlxml) throws SQLException {
            Driver.q();
        }

        public void updateSQLXML(String string, SQLXML sqlxml) throws SQLException {
            Driver.q();
        }

        public String getNString(int i) throws SQLException {
            Driver.q();
            return null;
        }

        public String getNString(String string) throws SQLException {
            Driver.q();
            return null;
        }

        public Reader getNCharacterStream(int i) throws SQLException {
            Driver.q();
            return null;
        }

        public Reader getNCharacterStream(String string) throws SQLException {
            Driver.q();
            return null;
        }

        public void updateNCharacterStream(int i, Reader reader, long l) throws SQLException {
            Driver.q();
        }

        public void updateNCharacterStream(String string, Reader reader, long l) throws SQLException {
            Driver.q();
        }

        public void updateAsciiStream(int i, InputStream in, long l) throws SQLException {
            Driver.q();
        }

        public void updateBinaryStream(int i, InputStream in, long l) throws SQLException {
            Driver.q();
        }

        public void updateCharacterStream(int i, Reader reader, long l) throws SQLException {
            Driver.q();
        }

        public void updateAsciiStream(String string, InputStream in, long l) throws SQLException {
            Driver.q();
        }

        public void updateBinaryStream(String string, InputStream in, long l) throws SQLException {
            Driver.q();
        }

        public void updateCharacterStream(String string, Reader reader, long l) throws SQLException {
            Driver.q();
        }

        public void updateBlob(int i, InputStream in, long l) throws SQLException {
            Driver.q();
        }

        public void updateBlob(String string, InputStream in, long l) throws SQLException {
            Driver.q();
        }

        public void updateClob(int i, Reader reader, long l) throws SQLException {
            Driver.q();
        }

        public void updateClob(String string, Reader reader, long l) throws SQLException {
            Driver.q();
        }

        public void updateNClob(int i, Reader reader, long l) throws SQLException {
            Driver.q();
        }

        public void updateNClob(String string, Reader reader, long l) throws SQLException {
            Driver.q();
        }

        public void updateNCharacterStream(int i, Reader reader) throws SQLException {
            Driver.q();
        }

        public void updateNCharacterStream(String string, Reader reader) throws SQLException {
            Driver.q();
        }

        public void updateAsciiStream(int i, InputStream in) throws SQLException {
            Driver.q();
        }

        public void updateBinaryStream(int i, InputStream in) throws SQLException {
            Driver.q();
        }

        public void updateCharacterStream(int i, Reader reader) throws SQLException {
            Driver.q();
        }

        public void updateAsciiStream(String string, InputStream in) throws SQLException {
            Driver.q();
        }

        public void updateBinaryStream(String string, InputStream in) throws SQLException {
            Driver.q();
        }

        public void updateCharacterStream(String string, Reader reader) throws SQLException {
            Driver.q();
        }

        public void updateBlob(int i, InputStream in) throws SQLException {
            Driver.q();
        }

        public void updateBlob(String string, InputStream in) throws SQLException {
            Driver.q();
        }

        public void updateClob(int i, Reader reader) throws SQLException {
            Driver.q();
        }

        public void updateClob(String string, Reader reader) throws SQLException {
            Driver.q();
        }

        public void updateNClob(int i, Reader reader) throws SQLException {
            Driver.q();
        }

        public void updateNClob(String string, Reader reader) throws SQLException {
            Driver.q();
        }

        @Override
        public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
            Driver.q();
            return null;
        }

        @Override
        public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
            Driver.q();
            return null;
        }

        public <T> T unwrap(Class<T> type) throws SQLException {
            Driver.q();
            return null;
        }

        public boolean isWrapperFor(Class<?> type) throws SQLException {
            Driver.q();
            return false;
        }

        private static class TimespanTimestamp
                extends Timestamp {
            private final C.Timespan t;


            public TimespanTimestamp(C.Timespan t) {
                super(t.j / 1000000L - TimeZone.getDefault().getOffset(0L));
                setNanos((int) (t.j % 1000000000L));
                this.t = t;
            }

            public String toString() {
                return this.t.toString();
            }
        }

        private static class MonthDate extends Date {
            private final C.Month m;

            public MonthDate(C.Month m) {
                super(getYear(m) - 1900, getMonth(m) - 1, 1);
                this.m = m;
            }

            private static int getYear(C.Month m) {
                return (m.i + 24000) / 12;
            }

            private static int getMonth(C.Month m) {
                return 1 + (m.i + 24000) % 12;
            }

            public String toString() {
                return this.m.toString();
            }
        }

        private static class MinuteTime extends Time {
            private final C.Minute m;

            public MinuteTime(C.Minute m) {
                super(m.i / 60, m.i % 60, 0);
                this.m = m;
            }

            public String toString() {
                return this.m.toString();
            }
        }

        private static class SecondTime extends Time {
            private final C.Second s;

            public SecondTime(C.Second s) {
                super(s.i / 60 * 60, s.i / 60, s.i % 60);
                this.s = s;
            }

            public String toString() {
                return this.s.toString();
            }
        }
    }

    public static class ResultSetMetaData
            implements java.sql.ResultSetMetaData {
        private final String[] f;
        private final Object[] d;

        public ResultSetMetaData(String[] x, Object[] y) {
            this.f = x;
            this.d = y;
        }

        public int getColumnCount() throws SQLException {
            return this.f.length;
        }

        public String getColumnName(int i) throws SQLException {
            return this.f[i - 1];
        }

        public String getColumnTypeName(int i) throws SQLException {
            return Driver.TYPE[C.t(this.d[i - 1])];
        }

        public int getColumnDisplaySize(int i) throws SQLException {
            return 11;
        }

        public int getScale(int i) throws SQLException {
            return 2;
        }

        public int isNullable(int i) throws SQLException {
            return 1;
        }

        public String getColumnLabel(int i) throws SQLException {
            return getColumnName(i);
        }

        public int getColumnType(int i) throws SQLException {
            return Driver.SQLTYPE[C.t(this.d[i - 1])];
        }

        public int getPrecision(int i) throws SQLException {
            return 11;
        }

        public boolean isSigned(int i) throws SQLException {
            return true;
        }

        public String getTableName(int i) throws SQLException {
            return "";
        }

        public String getSchemaName(int i) throws SQLException {
            return "";
        }

        public String getCatalogName(int i) throws SQLException {
            return "";
        }

        public boolean isReadOnly(int i) throws SQLException {
            return false;
        }

        public boolean isWritable(int i) throws SQLException {
            return false;
        }

        public boolean isDefinitelyWritable(int i) throws SQLException {
            return false;
        }

        public boolean isAutoIncrement(int i) throws SQLException {
            return false;
        }

        public boolean isCaseSensitive(int i) throws SQLException {
            return true;
        }

        public boolean isSearchable(int i) throws SQLException {
            return true;
        }

        public boolean isCurrency(int i) throws SQLException {
            return false;
        }

        public String getColumnClassName(int column) throws SQLException {
            Driver.q("col");
            return null;
        }


        public <T> T unwrap(Class<T> type) throws SQLException {
            Driver.q();
            return null;
        }

        public boolean isWrapperFor(Class<?> type) throws SQLException {
            Driver.q();
            return false;
        }
    }

    public class Connection
            implements java.sql.Connection {
        private C c;
        private String schema;
        private boolean a = true;
        private boolean b;
        private int i = 8;
        private int h = 1;
        private Properties clientInfo = new Properties();

        public Connection(String s, Object u, Object p) throws SQLException {
            int i = s.indexOf(":");
            try {
                this.c = new C(s.substring(0, i), Integer.parseInt(s.substring(i + 1)), (u == null) ? "" : (u + ":" + p));
            } catch (Exception e) {
                Driver.q(e);
            }
        }

        public Object ex(String s, Object[] p) throws SQLException {
            try {
                return (0 < C.n(p)) ? this.c.k(s, p) : this.c.k(".o.ex", s.toCharArray());
            } catch (Exception e) {

                if (e.getMessage().contains("Connection reset by peer")) {
                    try {
                        close();
                    } catch (SQLException e1) {
                    }
                }
                Driver.q(e);
                return null;
            }
        }

        public ResultSet qx(String s) throws SQLException {
            try {
                return new ResultSet(null, this.c.k(s));
            } catch (Exception e) {
                Driver.q(e);
                return null;
            }
        }

        public ResultSet qx(String s, Object x) throws SQLException {
            try {
                return new ResultSet(null, this.c.k(s, x));
            } catch (Exception e) {
                Driver.q(e);
                return null;
            }
        }

        public boolean getAutoCommit() throws SQLException {
            return this.a;
        }

        public void setAutoCommit(boolean b) throws SQLException {
            this.a = b;
        }

        public void rollback() throws SQLException {
        }

        public void commit() throws SQLException {
        }

        public boolean isClosed() throws SQLException {
            return (this.c == null);
        }

        public java.sql.Statement createStatement() throws SQLException {
            return new Statement(this);
        }

        public java.sql.DatabaseMetaData getMetaData() throws SQLException {
            return new DatabaseMetaData(this);
        }

        public java.sql.PreparedStatement prepareStatement(String s) throws SQLException {
            return new PreparedStatement(this, s);
        }

        public java.sql.CallableStatement prepareCall(String s) throws SQLException {
            return new CallableStatement(this, s);
        }

        public String nativeSQL(String s) throws SQLException {
            return s;
        }

        public boolean isReadOnly() throws SQLException {
            return this.b;
        }

        public void setReadOnly(boolean x) throws SQLException {
            this.b = x;
        }

        public String getCatalog() throws SQLException {
            Driver.q("cat");
            return null;
        }

        public void setCatalog(String s) throws SQLException {
            Driver.q("cat");
        }

        public int getTransactionIsolation() throws SQLException {
            return this.i;
        }

        public void setTransactionIsolation(int x) throws SQLException {
            this.i = x;
        }

        public SQLWarning getWarnings() throws SQLException {
            return null;
        }

        public void clearWarnings() throws SQLException {
        }

        public void close() throws SQLException {
            if (!isClosed()) {
                try {
                    this.c.close();
                } catch (IOException e) {
                    Driver.q(e);
                } finally {
                    this.c = null;
                }
            }
        }

        public java.sql.Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
            return new Statement(this);
        }

        public java.sql.PreparedStatement prepareStatement(String s, int resultSetType, int resultSetConcurrency) throws SQLException {
            return new PreparedStatement(this, s);
        }

        public java.sql.CallableStatement prepareCall(String s, int resultSetType, int resultSetConcurrency) throws SQLException {
            return new CallableStatement(this, s);
        }

        public Map getTypeMap() throws SQLException {
            return null;
        }

        public void setTypeMap(Map map) throws SQLException {
        }

        public int getHoldability() throws SQLException {
            return this.h;
        }

        public void setHoldability(int holdability) throws SQLException {
            this.h = holdability;
        }

        public Savepoint setSavepoint() throws SQLException {
            Driver.q("sav");
            return null;
        }

        public Savepoint setSavepoint(String name) throws SQLException {
            Driver.q("sav");
            return null;
        }

        public void rollback(Savepoint savepoint) throws SQLException {
        }

        public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        }

        public java.sql.Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            return new Statement(this);
        }

        public java.sql.PreparedStatement prepareStatement(String s, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            return new PreparedStatement(this, s);
        }

        public java.sql.CallableStatement prepareCall(String s, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            return new CallableStatement(this, s);
        }

        public java.sql.PreparedStatement prepareStatement(String s, int autoGeneratedKeys) throws SQLException {
            return new PreparedStatement(this, s);
        }

        public java.sql.PreparedStatement prepareStatement(String s, int[] columnIndexes) throws SQLException {
            return new PreparedStatement(this, s);
        }

        public java.sql.PreparedStatement prepareStatement(String s, String[] columnNames) throws SQLException {
            return new PreparedStatement(this, s);
        }

        public Clob createClob() throws SQLException {
            Driver.q();
            return null;
        }

        public Blob createBlob() throws SQLException {
            Driver.q();
            return null;
        }

        public NClob createNClob() throws SQLException {
            Driver.q();
            return null;
        }

        public SQLXML createSQLXML() throws SQLException {
            Driver.q();
            return null;
        }

        public boolean isValid(int i) throws SQLException {
            if (i < 0)
                Driver.q();
            return (this.c != null);
        }

        public void setClientInfo(String k, String v) throws SQLClientInfoException {
            this.clientInfo.setProperty(k, v);
        }

        public String getClientInfo(String k) throws SQLException {
            return (String) this.clientInfo.get(k);
        }

        public Properties getClientInfo() throws SQLException {
            return this.clientInfo;
        }

        public void setClientInfo(Properties p) throws SQLClientInfoException {
            this.clientInfo = p;
        }

        public Array createArrayOf(String string, Object[] os) throws SQLException {
            Driver.q();
            return null;
        }

        public Struct createStruct(String string, Object[] os) throws SQLException {
            Driver.q();
            return null;
        }

        @Override
        public String getSchema() throws SQLException {
            return this.schema;
        }

        @Override
        public void setSchema(String schema) throws SQLException {
            this.schema = schema;
        }

        @Override
        public void abort(Executor executor) throws SQLException {

        }

        @Override
        public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {

        }

        @Override
        public int getNetworkTimeout() throws SQLException {
            return 0;
        }

        public <T> T unwrap(Class<T> type) throws SQLException {
            Driver.q();
            return null;
        }

        public boolean isWrapperFor(Class<?> type) throws SQLException {
            Driver.q();
            return false;
        }
    }

    public class Statement implements java.sql.Statement {
        protected Object[] p = new Object[0];
        boolean poolable;
        private Connection Connection;
        private Object r;
        private int R;
        private int T;


        public Statement(Connection x) {
            this.poolable = false;
            this.Connection = x;
        }

        public int executeUpdate(String s) throws SQLException {
            this.Connection.ex(s, this.p);
            return -1;
        }

        public java.sql.ResultSet executeQuery(String s) throws SQLException {
            return new ResultSet(this, this.Connection.ex(s, this.p));
        }

        public boolean execute(String s) throws SQLException {
            return (null != (this.r = this.Connection.ex(s, this.p)));
        }

        public java.sql.ResultSet getResultSet() throws SQLException {
            return new ResultSet(this, this.r);
        }

        public int getUpdateCount() {
            return -1;
        }

        public int getMaxRows() throws SQLException {
            return this.R;
        }

        public void setMaxRows(int i) throws SQLException {
            this.R = i;
        }

        public int getQueryTimeout() throws SQLException {
            return this.T;
        }

        public void setQueryTimeout(int i) throws SQLException {
            this.T = i;
        }

        public int getMaxFieldSize() throws SQLException {
            return 0;
        }

        public void setMaxFieldSize(int i) throws SQLException {
        }

        public void setEscapeProcessing(boolean b) throws SQLException {
        }

        public void cancel() throws SQLException {
        }

        public SQLWarning getWarnings() throws SQLException {
            return null;
        }

        public void clearWarnings() throws SQLException {
        }

        public void setCursorName(String name) throws SQLException {
            Driver.q("cur");
        }

        public boolean getMoreResults() throws SQLException {
            return false;
        }

        public void close() throws SQLException {
            this.Connection = null;
        }

        public int getFetchDirection() throws SQLException {
            return 0;
        }

        public void setFetchDirection(int direction) throws SQLException {
            Driver.q("fd");
        }

        public int getFetchSize() throws SQLException {
            return 0;
        }

        public void setFetchSize(int rows) throws SQLException {
        }

        public int getResultSetConcurrency() throws SQLException {
            return 1007;
        }

        public int getResultSetType() throws SQLException {
            return 1004;
        }

        public void addBatch(String sql) throws SQLException {
            Driver.q("bat");
        }

        public void clearBatch() throws SQLException {
        }

        public int[] executeBatch() throws SQLException {
            return new int[0];
        }

        public java.sql.Connection getConnection() throws SQLException {
            return this.Connection;
        }

        public boolean getMoreResults(int current) throws SQLException {
            return false;
        }

        public java.sql.ResultSet getGeneratedKeys() throws SQLException {
            return null;
        }

        public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
            Driver.q("a");
            return 0;
        }

        public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
            Driver.q("a");
            return 0;
        }

        public int executeUpdate(String sql, String[] columnNames) throws SQLException {
            Driver.q("a");
            return 0;
        }

        public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
            Driver.q("a");
            return false;
        }

        public boolean execute(String sql, int[] columnIndexes) throws SQLException {
            Driver.q("a");
            return false;
        }

        public boolean execute(String sql, String[] columnNames) throws SQLException {
            Driver.q("a");
            return false;
        }

        public int getResultSetHoldability() throws SQLException {
            return 1;
        }

        public boolean isClosed() throws SQLException {
            return (this.Connection == null || this.Connection.isClosed());
        }

        public boolean isPoolable() throws SQLException {
            if (isClosed())
                throw new SQLException("Closed");
            return this.poolable;
        }

        public void setPoolable(boolean b) throws SQLException {
            if (isClosed())
                throw new SQLException("Closed");
            this.poolable = b;
        }

        @Override
        public void closeOnCompletion() throws SQLException {

        }

        @Override
        public boolean isCloseOnCompletion() throws SQLException {
            return false;
        }

        public <T> T unwrap(Class<T> type) throws SQLException {
            Driver.q();
            return null;
        }

        public boolean isWrapperFor(Class<?> type) throws SQLException {
            Driver.q();
            return false;
        }
    }

    public class PreparedStatement
            extends Statement implements java.sql.PreparedStatement {
        private final String s;

        public PreparedStatement(Connection connection1, String x) {
            super(connection1);
            this.s = x;
        }

        public java.sql.ResultSet executeQuery() throws SQLException {
            return executeQuery(this.s);
        }

        public int executeUpdate() throws SQLException {
            return executeUpdate(this.s);
        }

        public boolean execute() throws SQLException {
            return execute(this.s);
        }

        public void clearParameters() throws SQLException {
            try {
                for (int i = 0; i < C.n(this.p); )
                    this.p[i++] = null;
            } catch (UnsupportedEncodingException ex) {
                throw new SQLException(ex);
            }
        }

        public void setObject(int i, Object x) throws SQLException {
            int n;
            try {
                n = C.n(this.p);
            } catch (UnsupportedEncodingException ex) {
                throw new SQLException(ex);
            }
            if (i > n) {
                Object[] r = new Object[i];
                System.arraycopy(this.p, 0, r, 0, n);
                this.p = r;
                while (n < i)
                    this.p[n++] = null;
            }
            this.p[i - 1] = x;
        }

        public void setObject(int i, Object x, int targetSqlType) throws SQLException {
            setObject(i, x);
        }

        public void setObject(int i, Object x, int targetSqlType, int scale) throws SQLException {
            setObject(i, x);
        }

        public void setNull(int i, int t) throws SQLException {
            setObject(i, C.NULL[Driver.find(Driver.SQLTYPE, t)]);
        }

        public void setBoolean(int i, boolean x) throws SQLException {
            setObject(i, Boolean.valueOf(x));
        }

        public void setByte(int i, byte x) throws SQLException {
            setObject(i, Byte.valueOf(x));
        }

        public void setShort(int i, short x) throws SQLException {
            setObject(i, Short.valueOf(x));
        }

        public void setInt(int i, int x) throws SQLException {
            setObject(i, Integer.valueOf(x));
        }

        public void setLong(int i, long x) throws SQLException {
            setObject(i, Long.valueOf(x));
        }

        public void setFloat(int i, float x) throws SQLException {
            setObject(i, new Float(x));
        }

        public void setDouble(int i, double x) throws SQLException {
            setObject(i, new Double(x));
        }

        public void setString(int i, String x) throws SQLException {
            setObject(i, x);
        }

        public void setDate(int i, Date x) throws SQLException {
            setObject(i, x);
        }

        public void setTime(int i, Time x) throws SQLException {
            setObject(i, x);
        }

        public void setTimestamp(int i, Timestamp x) throws SQLException {
            setObject(i, x);
        }

        public void setBytes(int i, byte[] x) throws SQLException {
            Driver.q();
        }

        public void setBigDecimal(int i, BigDecimal x) throws SQLException {
            Driver.q();
        }

        public void setAsciiStream(int i, InputStream x, int length) throws SQLException {
            Driver.q();
        }

        public void setUnicodeStream(int i, InputStream x, int length) throws SQLException {
            Driver.q();
        }

        public void setBinaryStream(int i, InputStream x, int length) throws SQLException {
            Driver.q();
        }

        public void addBatch() throws SQLException {
        }

        public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
            Driver.q();
        }

        public void setRef(int i, Ref x) throws SQLException {
            Driver.q();
        }

        public void setBlob(int i, Blob x) throws SQLException {
            Driver.q();
        }

        public void setClob(int i, Clob x) throws SQLException {
            Driver.q();
        }

        public void setArray(int i, Array x) throws SQLException {
            Driver.q();
        }

        public java.sql.ResultSetMetaData getMetaData() throws SQLException {
            Driver.q("m");
            return null;
        }

        public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
            Driver.q();
        }

        public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
            Driver.q();
        }

        public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
            Driver.q();
        }

        public void setNull(int paramIndex, int sqlType, String typeName) throws SQLException {
            Driver.q();
        }


        public void setURL(int parameterIndex, URL x) throws SQLException {
            Driver.q();
        }

        public ParameterMetaData getParameterMetaData() throws SQLException {
            Driver.q("m");
            return null;
        }


        public void setRowId(int i, RowId rowid) throws SQLException {
            Driver.q();
        }

        public void setNString(int i, String string) throws SQLException {
            Driver.q();
        }

        public void setNCharacterStream(int i, Reader reader, long l) throws SQLException {
            Driver.q();
        }

        public void setNClob(int i, NClob nclob) throws SQLException {
            Driver.q();
        }

        public void setClob(int i, Reader reader, long l) throws SQLException {
            Driver.q();
        }

        public void setBlob(int i, InputStream in, long l) throws SQLException {
            Driver.q();
        }

        public void setNClob(int i, Reader reader, long l) throws SQLException {
            Driver.q();
        }

        public void setSQLXML(int i, SQLXML sqlxml) throws SQLException {
            Driver.q();
        }

        public void setAsciiStream(int i, InputStream in, long l) throws SQLException {
            Driver.q();
        }

        public void setBinaryStream(int i, InputStream in, long l) throws SQLException {
            Driver.q();
        }

        public void setCharacterStream(int i, Reader reader, long l) throws SQLException {
            Driver.q();
        }

        public void setAsciiStream(int i, InputStream in) throws SQLException {
            Driver.q();
        }

        public void setBinaryStream(int i, InputStream in) throws SQLException {
            Driver.q();
        }

        public void setCharacterStream(int i, Reader reader) throws SQLException {
            Driver.q();
        }

        public void setNCharacterStream(int i, Reader reader) throws SQLException {
            Driver.q();
        }

        public void setClob(int i, Reader reader) throws SQLException {
            Driver.q();
        }

        public void setBlob(int i, InputStream in) throws SQLException {
            Driver.q();
        }

        public void setNClob(int i, Reader reader) throws SQLException {
            Driver.q();
        }
    }

    public class CallableStatement
            extends PreparedStatement implements java.sql.CallableStatement {
        public CallableStatement(Connection c, String s) {
            super(c, s);
        }

        public void registerOutParameter(int i, int sqlType) throws SQLException {
        }

        public void registerOutParameter(int i, int sqlType, int scale) throws SQLException {
        }

        public boolean wasNull() throws SQLException {
            return false;
        }

        public String getString(int i) throws SQLException {
            return null;
        }

        public boolean getBoolean(int i) throws SQLException {
            return false;
        }

        public byte getByte(int i) throws SQLException {
            return 0;
        }

        public short getShort(int i) throws SQLException {
            return 0;
        }

        public int getInt(int i) throws SQLException {
            return 0;
        }

        public long getLong(int i) throws SQLException {
            return 0L;
        }

        public float getFloat(int i) throws SQLException {
            return 0.0F;
        }

        public double getDouble(int i) throws SQLException {
            return 0.0D;
        }

        public BigDecimal getBigDecimal(int i, int scale) throws SQLException {
            return null;
        }

        public Date getDate(int i) throws SQLException {
            return null;
        }

        public Time getTime(int i) throws SQLException {
            return null;
        }

        public Timestamp getTimestamp(int i) throws SQLException {
            return null;
        }

        public byte[] getBytes(int i) throws SQLException {
            return null;
        }

        public Object getObject(int i) throws SQLException {
            return null;
        }

        public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
            Driver.q();
            return null;
        }

        public Object getObject(int i, Map map) throws SQLException {
            Driver.q();
            return null;
        }

        public Ref getRef(int i) throws SQLException {
            Driver.q();
            return null;
        }

        public Blob getBlob(int i) throws SQLException {
            Driver.q();
            return null;
        }

        public Clob getClob(int i) throws SQLException {
            Driver.q();
            return null;
        }

        public Array getArray(int i) throws SQLException {
            Driver.q();
            return null;
        }

        public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
            Driver.q();
            return null;
        }

        public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
            Driver.q();
            return null;
        }

        public Timestamp getTimestamp(int parameterIndex, Calendar cal) throws SQLException {
            Driver.q();
            return null;
        }

        public void registerOutParameter(int paramIndex, int sqlType, String typeName) throws SQLException {
            Driver.q();
        }


        public void registerOutParameter(String parameterName, int sqlType) throws SQLException {
            Driver.q();
        }

        public void registerOutParameter(String parameterName, int sqlType, int scale) throws SQLException {
            Driver.q();
        }

        public void registerOutParameter(String parameterName, int sqlType, String typeName) throws SQLException {
            Driver.q();
        }

        public URL getURL(int parameterIndex) throws SQLException {
            Driver.q();
            return null;
        }

        public void setURL(String parameterName, URL val) throws SQLException {
            Driver.q();
        }

        public void setNull(String parameterName, int sqlType) throws SQLException {
            Driver.q();
        }

        public void setBoolean(String parameterName, boolean x) throws SQLException {
            Driver.q();
        }

        public void setByte(String parameterName, byte x) throws SQLException {
            Driver.q();
        }

        public void setShort(String parameterName, short x) throws SQLException {
            Driver.q();
        }

        public void setInt(String parameterName, int x) throws SQLException {
            Driver.q();
        }

        public void setLong(String parameterName, long x) throws SQLException {
            Driver.q();
        }

        public void setFloat(String parameterName, float x) throws SQLException {
            Driver.q();
        }

        public void setDouble(String parameterName, double x) throws SQLException {
            Driver.q();
        }

        public void setBigDecimal(String parameterName, BigDecimal x) throws SQLException {
            Driver.q();
        }

        public void setString(String parameterName, String x) throws SQLException {
            Driver.q();
        }

        public void setBytes(String parameterName, byte[] x) throws SQLException {
            Driver.q();
        }

        public void setDate(String parameterName, Date x) throws SQLException {
            Driver.q();
        }

        public void setTime(String parameterName, Time x) throws SQLException {
            Driver.q();
        }

        public void setTimestamp(String parameterName, Timestamp x) throws SQLException {
            Driver.q();
        }

        public void setAsciiStream(String parameterName, InputStream x, int length) throws SQLException {
            Driver.q();
        }

        public void setBinaryStream(String parameterName, InputStream x, int length) throws SQLException {
            Driver.q();
        }

        public void setObject(String parameterName, Object x, int targetSqlType, int scale) throws SQLException {
            Driver.q();
        }

        public void setObject(String parameterName, Object x, int targetSqlType) throws SQLException {
            Driver.q();
        }

        public void setObject(String parameterName, Object x) throws SQLException {
            Driver.q();
        }

        public void setCharacterStream(String parameterName, Reader reader, int length) throws SQLException {
            Driver.q();
        }

        public void setDate(String parameterName, Date x, Calendar cal) throws SQLException {
            Driver.q();
        }

        public void setTime(String parameterName, Time x, Calendar cal) throws SQLException {
            Driver.q();
        }

        public void setTimestamp(String parameterName, Timestamp x, Calendar cal) throws SQLException {
            Driver.q();
        }

        public void setNull(String parameterName, int sqlType, String typeName) throws SQLException {
            Driver.q();
        }

        public String getString(String parameterName) throws SQLException {
            return null;
        }

        public boolean getBoolean(String parameterName) throws SQLException {
            return false;
        }

        public byte getByte(String parameterName) throws SQLException {
            return 0;
        }

        public short getShort(String parameterName) throws SQLException {
            return 0;
        }

        public int getInt(String parameterName) throws SQLException {
            return 0;
        }

        public long getLong(String parameterName) throws SQLException {
            return 0L;
        }

        public float getFloat(String parameterName) throws SQLException {
            return 0.0F;
        }

        public double getDouble(String parameterName) throws SQLException {
            return 0.0D;
        }

        public byte[] getBytes(String parameterName) throws SQLException {
            return null;
        }

        public Date getDate(String parameterName) throws SQLException {
            return null;
        }

        public Time getTime(String parameterName) throws SQLException {
            return null;
        }

        public Timestamp getTimestamp(String parameterName) throws SQLException {
            return null;
        }

        public Object getObject(String parameterName) throws SQLException {
            return null;
        }

        public BigDecimal getBigDecimal(String parameterName) throws SQLException {
            return null;
        }

        public Object getObject(String parameterName, Map map) throws SQLException {
            return null;
        }

        public Ref getRef(String parameterName) throws SQLException {
            return null;
        }

        public Blob getBlob(String parameterName) throws SQLException {
            return null;
        }

        public Clob getClob(String parameterName) throws SQLException {
            return null;
        }

        public Array getArray(String parameterName) throws SQLException {
            return null;
        }

        public Date getDate(String parameterName, Calendar cal) throws SQLException {
            return null;
        }

        public Time getTime(String parameterName, Calendar cal) throws SQLException {
            return null;
        }

        public Timestamp getTimestamp(String parameterName, Calendar cal) throws SQLException {
            return null;
        }

        public URL getURL(String parameterName) throws SQLException {
            return null;
        }


        public RowId getRowId(int i) throws SQLException {
            Driver.q();
            return null;
        }

        public RowId getRowId(String string) throws SQLException {
            Driver.q();
            return null;
        }

        public void setRowId(String string, RowId rowid) throws SQLException {
            Driver.q();
        }

        public void setNString(String string, String string1) throws SQLException {
            Driver.q();
        }

        public void setNCharacterStream(String string, Reader reader, long l) throws SQLException {
            Driver.q();
        }

        public void setNClob(String string, NClob nclob) throws SQLException {
            Driver.q();
        }

        public void setClob(String string, Reader reader, long l) throws SQLException {
            Driver.q();
        }

        public void setBlob(String string, InputStream in, long l) throws SQLException {
            Driver.q();
        }

        public void setNClob(String string, Reader reader, long l) throws SQLException {
            Driver.q();
        }

        public NClob getNClob(int i) throws SQLException {
            Driver.q();
            return null;
        }

        public NClob getNClob(String string) throws SQLException {
            Driver.q();
            return null;
        }

        public void setSQLXML(String string, SQLXML sqlxml) throws SQLException {
            Driver.q();
        }

        public SQLXML getSQLXML(int i) throws SQLException {
            Driver.q();
            return null;
        }

        public SQLXML getSQLXML(String string) throws SQLException {
            Driver.q();
            return null;
        }

        public String getNString(int i) throws SQLException {
            Driver.q();
            return null;
        }

        public String getNString(String string) throws SQLException {
            Driver.q();
            return null;
        }

        public Reader getNCharacterStream(int i) throws SQLException {
            Driver.q();
            return null;
        }

        public Reader getNCharacterStream(String string) throws SQLException {
            Driver.q();
            return null;
        }

        public Reader getCharacterStream(int i) throws SQLException {
            Driver.q();
            return null;
        }

        public Reader getCharacterStream(String string) throws SQLException {
            Driver.q();
            return null;
        }

        public void setBlob(String string, Blob blob) throws SQLException {
            Driver.q();
        }

        public void setClob(String string, Clob clob) throws SQLException {
            Driver.q();
        }

        public void setAsciiStream(String string, InputStream in, long l) throws SQLException {
            Driver.q();
        }

        public void setBinaryStream(String string, InputStream in, long l) throws SQLException {
            Driver.q();
        }

        public void setCharacterStream(String string, Reader reader, long l) throws SQLException {
            Driver.q();
        }

        public void setAsciiStream(String string, InputStream in) throws SQLException {
            Driver.q();
        }

        public void setBinaryStream(String string, InputStream in) throws SQLException {
            Driver.q();
        }

        public void setCharacterStream(String string, Reader reader) throws SQLException {
            Driver.q();
        }

        public void setNCharacterStream(String string, Reader reader) throws SQLException {
            Driver.q();
        }

        public void setClob(String string, Reader reader) throws SQLException {
            Driver.q();
        }

        public void setBlob(String string, InputStream in) throws SQLException {
            Driver.q();
        }

        public void setNClob(String string, Reader reader) throws SQLException {
            Driver.q();
        }

        @Override
        public <T> T getObject(int parameterIndex, Class<T> type) throws SQLException {
            Driver.q();
            return null;
        }

        @Override
        public <T> T getObject(String parameterName, Class<T> type) throws SQLException {
            Driver.q();
            return null;
        }
    }

    public class DatabaseMetaData
            implements java.sql.DatabaseMetaData {
        private final Connection Connection;

        public DatabaseMetaData(Connection x) {
            this.Connection = x;
        }

        public java.sql.ResultSet getCatalogs() throws SQLException {
            return this.Connection.qx("([]TABLE_CAT:`symbol$())");
        }

        public java.sql.ResultSet getSchemas() throws SQLException {
            return this.Connection.qx("([]TABLE_SCHEM:`symbol$())");
        }

        public java.sql.ResultSet getTableTypes() throws SQLException {
            return this.Connection.qx("([]TABLE_TYPE:`TABLE`VIEW)");
        }


        public java.sql.ResultSet getTables(String a, String b, String t, String[] x) throws SQLException {
            String getMetaInf = "raze{([]TABLE_CAT:`;TABLE_SCHEM:`;TABLE_NAME:system string`a`b x=`VIEW;TABLE_TYPE:x)} each";
            if (x == null) {
                return this.Connection.qx(getMetaInf + " enlist `");
            }
            return this.Connection.qx(getMetaInf, x);
        }

        public java.sql.ResultSet getTypeInfo() throws SQLException {
            return this.Connection.qx("`DATA_TYPE xasc([]TYPE_NAME:`boolean`byte`short`int`long`real`float`symbol`date`time`timestamp;DATA_TYPE:16 -2 5 4 -5 7 8 12 91 92 93;PRECISION:11;LITERAL_PREFIX:`;LITERAL_SUFFIX:`;CREATE_PARAMS:`;NULLABLE:1h;CASE_SENSITIVE:1b;SEARCHABLE:1h;UNSIGNED_ATTRIBUTE:0b;FIXED_PREC_SCALE:0b;AUTO_INCREMENT:0b;LOCAL_TYPE_NAME:`;MINIMUM_SCALE:0h;MAXIMUM_SCALE:0h;SQL_DATA_TYPE:0;SQL_DATETIME_SUB:0;NUM_PREC_RADIX:10)");
        }


        public java.sql.ResultSet getColumns(String a, String b, String t, String c) throws SQLException {
            if (t.startsWith("%"))
                t = "";
            return this.Connection.qx("select TABLE_CAT:`,TABLE_SCHEM:`,TABLE_NAME:n,COLUMN_NAME:c,DATA_TYPE:0,TYPE_NAME:t,COLUMN_SIZE:2000000000,BUFFER_LENGTH:0,DECIMAL_DIGITS:16,NUM_PREC_RADIX:10,NULLABLE:1,REMARKS:`,COLUMN_DEF:`,SQL_DATA_TYPE:0,SQL_DATETIME_SUB:0,CHAR_OCTET_LENGTH:2000000000,ORDINAL_POSITION:1+til count n,NULLABLE:`YES from .Q.nct`" + t);
        }


        public java.sql.ResultSet getPrimaryKeys(String a, String b, String t) throws SQLException {
            Driver.q("pk");
            return this.Connection.qx("");
        }

        public java.sql.ResultSet getImportedKeys(String a, String b, String t) throws SQLException {
            Driver.q("imp");
            return this.Connection.qx("");
        }

        public java.sql.ResultSet getProcedures(String a, String b, String p) throws SQLException {
            Driver.q("pr");
            return this.Connection.qx("");
        }

        public java.sql.ResultSet getExportedKeys(String a, String b, String t) throws SQLException {
            Driver.q("exp");
            return null;
        }


        public java.sql.ResultSet getCrossReference(String pa, String pb, String pt, String fa, String fb, String ft) throws SQLException {
            Driver.q("cr");
            return null;
        }


        public java.sql.ResultSet getIndexInfo(String a, String b, String t, boolean unique, boolean approximate) throws SQLException {
            Driver.q("ii");
            return null;
        }

        public java.sql.ResultSet getProcedureColumns(String a, String b, String p, String c) throws SQLException {
            Driver.q("pc");
            return null;
        }


        public java.sql.ResultSet getColumnPrivileges(String a, String b, String table, String columnNamePattern) throws SQLException {
            Driver.q("cp");
            return null;
        }


        public java.sql.ResultSet getTablePrivileges(String a, String b, String t) throws SQLException {
            Driver.q("tp");
            return null;
        }


        public java.sql.ResultSet getBestRowIdentifier(String a, String b, String t, int scope, boolean nullable) throws SQLException {
            Driver.q("br");
            return null;
        }


        public java.sql.ResultSet getVersionColumns(String a, String b, String t) throws SQLException {
            Driver.q("vc");
            return null;
        }


        public boolean allProceduresAreCallable() throws SQLException {
            return true;
        }

        public boolean allTablesAreSelectable() throws SQLException {
            return true;
        }

        public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
            return false;
        }

        public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
            return false;
        }

        public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
            return true;
        }

        public String getSchemaTerm() throws SQLException {
            return "schema";
        }

        public String getProcedureTerm() throws SQLException {
            return "procedure";
        }

        public String getCatalogTerm() throws SQLException {
            return "catalog";
        }

        public String getCatalogSeparator() throws SQLException {
            return ".";
        }

        public int getMaxBinaryLiteralLength() throws SQLException {
            return 0;
        }

        public int getMaxCharLiteralLength() throws SQLException {
            return 0;
        }

        public int getMaxColumnNameLength() throws SQLException {
            return 0;
        }

        public int getMaxColumnsInGroupBy() throws SQLException {
            return 0;
        }

        public int getMaxColumnsInIndex() throws SQLException {
            return 0;
        }

        public int getMaxColumnsInOrderBy() throws SQLException {
            return 0;
        }

        public int getMaxColumnsInSelect() throws SQLException {
            return 0;
        }

        public int getMaxColumnsInTable() throws SQLException {
            return 0;
        }

        public int getMaxConnections() throws SQLException {
            return 0;
        }

        public int getMaxCursorNameLength() throws SQLException {
            return 0;
        }

        public int getMaxIndexLength() throws SQLException {
            return 0;
        }

        public int getMaxSchemaNameLength() throws SQLException {
            return 0;
        }

        public int getMaxProcedureNameLength() throws SQLException {
            return 0;
        }

        public int getMaxCatalogNameLength() throws SQLException {
            return 0;
        }

        public int getMaxRowSize() throws SQLException {
            return 0;
        }

        public int getMaxStatementLength() throws SQLException {
            return 0;
        }

        public int getMaxStatements() throws SQLException {
            return 0;
        }

        public int getMaxTableNameLength() throws SQLException {
            return 0;
        }

        public int getMaxTablesInSelect() throws SQLException {
            return 0;
        }

        public int getMaxUserNameLength() throws SQLException {
            return 0;
        }

        public int getDefaultTransactionIsolation() throws SQLException {
            return 8;
        }

        public String getSQLKeywords() throws SQLException {
            return "show,meta,load,save";
        }

        public String getNumericFunctions() throws SQLException {
            return "";
        }

        public String getStringFunctions() throws SQLException {
            return "";
        }

        public String getSystemFunctions() throws SQLException {
            return "";
        }

        public String getTimeDateFunctions() throws SQLException {
            return "";
        }

        public String getSearchStringEscape() throws SQLException {
            return "";
        }

        public String getExtraNameCharacters() throws SQLException {
            return "";
        }

        public String getIdentifierQuoteString() throws SQLException {
            return "";
        }

        public String getURL() throws SQLException {
            return null;
        }

        public String getUserName() throws SQLException {
            return "";
        }

        public String getDatabaseProductName() throws SQLException {
            return "kdb";
        }

        public String getDatabaseProductVersion() throws SQLException {
            return "2.0";
        }

        public String getDriverName() throws SQLException {
            return "jdbc";
        }

        public String getDriverVersion() throws SQLException {
            return Driver.V + "." + Driver.v;
        }

        public int getDriverMajorVersion() {
            return Driver.V;
        }

        public int getDriverMinorVersion() {
            return Driver.v;
        }

        public boolean isCatalogAtStart() throws SQLException {
            return true;
        }

        public boolean isReadOnly() throws SQLException {
            return false;
        }

        public boolean nullsAreSortedHigh() throws SQLException {
            return false;
        }

        public boolean nullsAreSortedLow() throws SQLException {
            return true;
        }

        public boolean nullsAreSortedAtStart() throws SQLException {
            return false;
        }

        public boolean nullsAreSortedAtEnd() throws SQLException {
            return false;
        }

        public boolean supportsMixedCaseIdentifiers() throws SQLException {
            return false;
        }

        public boolean storesUpperCaseIdentifiers() throws SQLException {
            return false;
        }

        public boolean storesLowerCaseIdentifiers() throws SQLException {
            return false;
        }

        public boolean storesMixedCaseIdentifiers() throws SQLException {
            return true;
        }

        public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
            return true;
        }

        public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
            return false;
        }

        public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
            return false;
        }

        public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
            return true;
        }

        public boolean supportsAlterTableWithAddColumn() throws SQLException {
            return true;
        }

        public boolean supportsAlterTableWithDropColumn() throws SQLException {
            return true;
        }

        public boolean supportsTableCorrelationNames() throws SQLException {
            return true;
        }

        public boolean supportsDifferentTableCorrelationNames() throws SQLException {
            return true;
        }

        public boolean supportsColumnAliasing() throws SQLException {
            return true;
        }

        public boolean nullPlusNonNullIsNull() throws SQLException {
            return true;
        }

        public boolean supportsExpressionsInOrderBy() throws SQLException {
            return true;
        }

        public boolean supportsOrderByUnrelated() throws SQLException {
            return false;
        }

        public boolean supportsGroupBy() throws SQLException {
            return true;
        }

        public boolean supportsGroupByUnrelated() throws SQLException {
            return false;
        }

        public boolean supportsGroupByBeyondSelect() throws SQLException {
            return false;
        }

        public boolean supportsLikeEscapeClause() throws SQLException {
            return false;
        }

        public boolean supportsMultipleResultSets() throws SQLException {
            return false;
        }

        public boolean supportsMultipleTransactions() throws SQLException {
            return false;
        }

        public boolean supportsNonNullableColumns() throws SQLException {
            return true;
        }

        public boolean supportsMinimumSQLGrammar() throws SQLException {
            return true;
        }

        public boolean supportsCoreSQLGrammar() throws SQLException {
            return true;
        }

        public boolean supportsExtendedSQLGrammar() throws SQLException {
            return false;
        }

        public boolean supportsANSI92EntryLevelSQL() throws SQLException {
            return true;
        }

        public boolean supportsANSI92IntermediateSQL() throws SQLException {
            return false;
        }

        public boolean supportsANSI92FullSQL() throws SQLException {
            return false;
        }

        public boolean supportsIntegrityEnhancementFacility() throws SQLException {
            return false;
        }

        public boolean supportsOuterJoins() throws SQLException {
            return false;
        }

        public boolean supportsFullOuterJoins() throws SQLException {
            return false;
        }

        public boolean supportsLimitedOuterJoins() throws SQLException {
            return false;
        }

        public boolean supportsConvert() throws SQLException {
            return false;
        }

        public boolean supportsConvert(int fromType, int toType) throws SQLException {
            return false;
        }

        public boolean supportsSchemasInDataManipulation() throws SQLException {
            return false;
        }

        public boolean supportsSchemasInProcedureCalls() throws SQLException {
            return false;
        }

        public boolean supportsSchemasInTableDefinitions() throws SQLException {
            return false;
        }

        public boolean supportsSchemasInIndexDefinitions() throws SQLException {
            return false;
        }

        public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
            return false;
        }

        public boolean supportsCatalogsInDataManipulation() throws SQLException {
            return false;
        }

        public boolean supportsCatalogsInProcedureCalls() throws SQLException {
            return false;
        }

        public boolean supportsCatalogsInTableDefinitions() throws SQLException {
            return false;
        }

        public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
            return false;
        }

        public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
            return false;
        }

        public boolean supportsSelectForUpdate() throws SQLException {
            return false;
        }

        public boolean supportsPositionedDelete() throws SQLException {
            return false;
        }

        public boolean supportsPositionedUpdate() throws SQLException {
            return false;
        }

        public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
            return true;
        }

        public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
            return true;
        }

        public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
            return true;
        }

        public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
            return true;
        }

        public boolean supportsStoredProcedures() throws SQLException {
            return false;
        }

        public boolean supportsSubqueriesInComparisons() throws SQLException {
            return true;
        }

        public boolean supportsSubqueriesInExists() throws SQLException {
            return true;
        }

        public boolean supportsSubqueriesInIns() throws SQLException {
            return true;
        }

        public boolean supportsSubqueriesInQuantifieds() throws SQLException {
            return true;
        }

        public boolean supportsCorrelatedSubqueries() throws SQLException {
            return true;
        }

        public boolean supportsUnion() throws SQLException {
            return true;
        }

        public boolean supportsUnionAll() throws SQLException {
            return true;
        }

        public boolean supportsTransactions() throws SQLException {
            return true;
        }

        public boolean supportsTransactionIsolationLevel(int level) throws SQLException {
            return true;
        }

        public boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException {
            return true;
        }

        public boolean supportsDataManipulationTransactionsOnly() throws SQLException {
            return false;
        }

        public boolean usesLocalFiles() throws SQLException {
            return false;
        }

        public boolean usesLocalFilePerTable() throws SQLException {
            return false;
        }

        public boolean supportsResultSetType(int type) throws SQLException {
            return (type != 1005);
        }

        public boolean supportsResultSetConcurrency(int type, int concurrency) throws SQLException {
            return (type == 1007);
        }

        public boolean ownUpdatesAreVisible(int type) throws SQLException {
            return false;
        }

        public boolean ownDeletesAreVisible(int type) throws SQLException {
            return false;
        }

        public boolean ownInsertsAreVisible(int type) throws SQLException {
            return false;
        }

        public boolean othersUpdatesAreVisible(int type) throws SQLException {
            return false;
        }

        public boolean othersDeletesAreVisible(int type) throws SQLException {
            return false;
        }

        public boolean othersInsertsAreVisible(int type) throws SQLException {
            return false;
        }

        public boolean updatesAreDetected(int type) throws SQLException {
            return false;
        }

        public boolean deletesAreDetected(int type) throws SQLException {
            return false;
        }

        public boolean insertsAreDetected(int type) throws SQLException {
            return false;
        }

        public boolean supportsBatchUpdates() throws SQLException {
            return false;
        }


        public java.sql.ResultSet getUDTs(String catalog, String schemaPattern, String typeNamePattern, int[] types) throws SQLException {
            return null;
        }

        public java.sql.Connection getConnection() throws SQLException {
            return this.Connection;
        }


        public boolean supportsSavepoints() throws SQLException {
            return false;
        }

        public boolean supportsNamedParameters() throws SQLException {
            return false;
        }

        public boolean supportsMultipleOpenResults() throws SQLException {
            return false;
        }

        public boolean supportsGetGeneratedKeys() throws SQLException {
            return false;
        }


        public java.sql.ResultSet getSuperTypes(String catalog, String schemaPattern, String typeNamePattern) throws SQLException {
            return null;
        }


        public java.sql.ResultSet getSuperTables(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
            return null;
        }


        public java.sql.ResultSet getAttributes(String catalog, String schemaPattern, String typeNamePattern, String attributeNamePattern) throws SQLException {
            return null;
        }

        public boolean supportsResultSetHoldability(int holdability) throws SQLException {
            return false;
        }

        public int getResultSetHoldability() throws SQLException {
            return 0;
        }

        public int getDatabaseMajorVersion() throws SQLException {
            return 0;
        }

        public int getDatabaseMinorVersion() throws SQLException {
            return 0;
        }

        public int getJDBCMajorVersion() throws SQLException {
            return 0;
        }

        public int getJDBCMinorVersion() throws SQLException {
            return 0;
        }

        public int getSQLStateType() throws SQLException {
            return 0;
        }

        public boolean locatorsUpdateCopy() throws SQLException {
            return false;
        }

        public boolean supportsStatementPooling() throws SQLException {
            return false;
        }


        public RowIdLifetime getRowIdLifetime() throws SQLException {
            Driver.q();
            return null;
        }

        public java.sql.ResultSet getSchemas(String string, String string1) throws SQLException {
            Driver.q();
            return null;
        }

        public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
            Driver.q();
            return false;
        }

        public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
            Driver.q();
            return false;
        }

        public java.sql.ResultSet getClientInfoProperties() throws SQLException {
            Driver.q();
            return null;
        }

        public java.sql.ResultSet getFunctions(String string, String string1, String string2) throws SQLException {
            Driver.q();
            return null;
        }


        public java.sql.ResultSet getFunctionColumns(String string, String string1, String string2, String string3) throws SQLException {
            Driver.q();
            return null;
        }

        @Override
        public java.sql.ResultSet getPseudoColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
            Driver.q();
            return null;
        }

        @Override
        public boolean generatedKeyAlwaysReturned() throws SQLException {
            Driver.q();
            return false;
        }

        public <T> T unwrap(Class<T> type) throws SQLException {
            Driver.q();
            return null;
        }

        public boolean isWrapperFor(Class<?> type) throws SQLException {
            Driver.q();
            return false;
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\kx\jdbc.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */