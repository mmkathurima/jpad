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
    static int v;
    static int[] SQLTYPE = {0, 16, 0, 0, -2, 5, 4, -5, 7, 8, 0, 12, 93, 91, 91, 93, 93, 92, 92, 92};
    static String[] TYPE = {"", "boolean", "", "", "byte", "short", "int", "long", "real", "float", "char", "symbol", "timestamp", "month", "date", "timestamp", "timespan", "minute", "second", "time"};

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
    public Logger getParentLogger() {
        return Logger.getLogger(this.getClass().getName());
    }

    public boolean acceptsURL(String s) {
        return s.startsWith("jdbc:q:");
    }

    public java.sql.Connection connect(String s, Properties p) throws SQLException {
        return !this.acceptsURL(s) ? null : new Connection(s.substring(7), (p != null) ? p.get("user") : p, (p != null) ? p.get("password") : p);
    }

    public DriverPropertyInfo[] getPropertyInfo(String s, Properties p) {
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

        public java.sql.ResultSetMetaData getMetaData() {
            return new ResultSetMetaData(this.f, this.d);
        }

        public int findColumn(String s) {
            return 1 + find(this.f, s);
        }

        public boolean next() {
            return (++this.r < this.n);
        }

        public boolean wasNull() {
            return (this.o == null);
        }

        public Object getObject(int i) {
            this.o = C.at(this.d[i - 1], this.r);

            if (this.o instanceof C.Month)
                return new MonthDate((C.Month) this.o);
            if (this.o instanceof C.Second)
                return new SecondTime((C.Second) this.o);
            if (this.o instanceof C.Minute)
                return new MinuteTime((C.Minute) this.o);
            if (this.o instanceof C.Timespan) {
                return new TimespanTimestamp((C.Timespan) this.o);
            }

            return (this.o instanceof char[]) ? new String((char[]) this.o) : this.o;
        }

        public boolean getBoolean(int i) {
            return (Boolean) this.getObject(i);
        }

        public byte getByte(int i) {
            return (Byte) this.getObject(i);
        }

        public short getShort(int i) {
            Object x = this.getObject(i);
            return (x == null) ? 0 : (Short) x;
        }

        public int getInt(int i) {
            Object x = this.getObject(i);
            return (x == null) ? 0 : (Integer) x;
        }

        public long getLong(int i) {
            Object x = this.getObject(i);
            return (x == null) ? 0L : (Long) x;
        }

        public float getFloat(int i) {
            Object x = this.getObject(i);
            return (x == null) ? 0.0F : (Float) x;
        }

        public double getDouble(int i) {
            Object x = this.getObject(i);
            return (x == null) ? 0.0D : (Double) x;
        }

        public String getString(int i) {
            Object x = this.getObject(i);
            return (x == null) ? null : x.toString();
        }

        public Date getDate(int i) {
            return (Date) this.getObject(i);
        }

        public Time getTime(int i) {
            return (Time) this.getObject(i);
        }

        public Timestamp getTimestamp(int i) {
            return (Timestamp) this.getObject(i);
        }

        public byte[] getBytes(int i) throws SQLException {
            q();
            return null;
        }

        public BigDecimal getBigDecimal(int i, int scale) throws SQLException {
            q();
            return null;
        }

        public InputStream getAsciiStream(int i) throws SQLException {
            q();
            return null;
        }

        public InputStream getUnicodeStream(int i) throws SQLException {
            q();
            return null;
        }

        public InputStream getBinaryStream(int i) throws SQLException {
            q();
            return null;
        }

        public Object getObject(String s) {
            return this.getObject(this.findColumn(s));
        }

        public boolean getBoolean(String s) {
            return this.getBoolean(this.findColumn(s));
        }

        public byte getByte(String s) {
            return this.getByte(this.findColumn(s));
        }

        public short getShort(String s) {
            return this.getShort(this.findColumn(s));
        }

        public int getInt(String s) {
            return this.getInt(this.findColumn(s));
        }

        public long getLong(String s) {
            return this.getLong(this.findColumn(s));
        }

        public float getFloat(String s) {
            return this.getFloat(this.findColumn(s));
        }

        public double getDouble(String s) {
            return this.getDouble(this.findColumn(s));
        }

        public String getString(String s) {
            return this.getString(this.findColumn(s));
        }

        public Date getDate(String s) {
            return this.getDate(this.findColumn(s));
        }

        public Time getTime(String s) {
            return this.getTime(this.findColumn(s));
        }

        public Timestamp getTimestamp(String s) {
            return this.getTimestamp(this.findColumn(s));
        }

        public byte[] getBytes(String s) throws SQLException {
            return this.getBytes(this.findColumn(s));
        }

        public BigDecimal getBigDecimal(String s, int scale) throws SQLException {
            return this.getBigDecimal(this.findColumn(s), scale);
        }

        public InputStream getAsciiStream(String s) throws SQLException {
            return this.getAsciiStream(this.findColumn(s));
        }

        public InputStream getUnicodeStream(String s) throws SQLException {
            return this.getUnicodeStream(this.findColumn(s));
        }

        public InputStream getBinaryStream(String s) throws SQLException {
            return this.getBinaryStream(this.findColumn(s));
        }

        public SQLWarning getWarnings() {
            return null;
        }

        public void clearWarnings() {
        }

        public String getCursorName() throws SQLException {
            q("cur");
            return "";
        }

        public void close() {
            this.d = null;
        }

        public Reader getCharacterStream(int columnIndex) throws SQLException {
            q();
            return null;
        }

        public Reader getCharacterStream(String columnName) throws SQLException {
            q();
            return null;
        }

        public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
            q();
            return null;
        }

        public BigDecimal getBigDecimal(String columnName) throws SQLException {
            q();
            return null;
        }

        public boolean isBeforeFirst() {
            return (this.r < 0);
        }

        public boolean isAfterLast() {
            return (this.r >= this.n);
        }

        public boolean isFirst() {
            return (this.r == 0);
        }

        public boolean isLast() {
            return (this.r == this.n - 1);
        }

        public void beforeFirst() {
            this.r = -1;
        }

        public void afterLast() {
            this.r = this.n;
        }

        public boolean first() {
            this.r = 0;
            return (this.n > 0);
        }

        public boolean last() {
            this.r = this.n - 1;
            return (this.n > 0);
        }

        public int getRow() {
            return this.r + 1;
        }

        public boolean absolute(int row) {
            this.r = row - 1;
            return (this.r < this.n);
        }

        public boolean relative(int rows) {
            this.r += rows;
            return (this.r >= 0 && this.r < this.n);
        }

        public boolean previous() {
            this.r--;
            return (this.r >= 0);
        }

        public int getFetchDirection() {
            return java.sql.ResultSet.FETCH_FORWARD;
        }

        public void setFetchDirection(int direction) throws SQLException {
            q("fd");
        }

        public int getFetchSize() {
            return 0;
        }

        public void setFetchSize(int rows) {
        }

        public int getType() {
            return java.sql.ResultSet.TYPE_SCROLL_SENSITIVE;
        }

        public int getConcurrency() {
            return java.sql.ResultSet.CONCUR_READ_ONLY;
        }

        public boolean rowUpdated() throws SQLException {
            q();
            return false;
        }

        public boolean rowInserted() throws SQLException {
            q();
            return false;
        }

        public boolean rowDeleted() throws SQLException {
            q();
            return false;
        }

        public void updateNull(int columnIndex) throws SQLException {
            q();
        }

        public void updateBoolean(int columnIndex, boolean x) throws SQLException {
            q();
        }

        public void updateByte(int columnIndex, byte x) throws SQLException {
            q();
        }

        public void updateShort(int columnIndex, short x) throws SQLException {
            q();
        }

        public void updateInt(int columnIndex, int x) throws SQLException {
            q();
        }

        public void updateLong(int columnIndex, long x) throws SQLException {
            q();
        }

        public void updateFloat(int columnIndex, float x) throws SQLException {
            q();
        }

        public void updateDouble(int columnIndex, double x) throws SQLException {
            q();
        }

        public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
            q();
        }

        public void updateString(int columnIndex, String x) throws SQLException {
            q();
        }

        public void updateBytes(int columnIndex, byte[] x) throws SQLException {
            q();
        }

        public void updateDate(int columnIndex, Date x) throws SQLException {
            q();
        }

        public void updateTime(int columnIndex, Time x) throws SQLException {
            q();
        }

        public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
            q();
        }

        public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
            q();
        }

        public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
            q();
        }

        public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
            q();
        }

        public void updateObject(int columnIndex, Object x, int scale) throws SQLException {
            q();
        }

        public void updateObject(int columnIndex, Object x) throws SQLException {
            q();
        }

        public void updateNull(String columnName) throws SQLException {
            q();
        }

        public void updateBoolean(String columnName, boolean x) throws SQLException {
            q();
        }

        public void updateByte(String columnName, byte x) throws SQLException {
            q();
        }

        public void updateShort(String columnName, short x) throws SQLException {
            q();
        }

        public void updateInt(String columnName, int x) throws SQLException {
            q();
        }

        public void updateLong(String columnName, long x) throws SQLException {
            q();
        }

        public void updateFloat(String columnName, float x) throws SQLException {
            q();
        }

        public void updateDouble(String columnName, double x) throws SQLException {
            q();
        }

        public void updateBigDecimal(String columnName, BigDecimal x) throws SQLException {
            q();
        }

        public void updateString(String columnName, String x) throws SQLException {
            q();
        }

        public void updateBytes(String columnName, byte[] x) throws SQLException {
            q();
        }

        public void updateDate(String columnName, Date x) throws SQLException {
            q();
        }

        public void updateTime(String columnName, Time x) throws SQLException {
            q();
        }

        public void updateTimestamp(String columnName, Timestamp x) throws SQLException {
            q();
        }

        public void updateAsciiStream(String columnName, InputStream x, int length) throws SQLException {
            q();
        }

        public void updateBinaryStream(String columnName, InputStream x, int length) throws SQLException {
            q();
        }

        public void updateCharacterStream(String columnName, Reader reader, int length) throws SQLException {
            q();
        }

        public void updateObject(String columnName, Object x, int scale) throws SQLException {
            q();
        }

        public void updateObject(String columnName, Object x) throws SQLException {
            q();
        }

        public void insertRow() throws SQLException {
            q();
        }

        public void updateRow() throws SQLException {
            q();
        }

        public void deleteRow() throws SQLException {
            q();
        }

        public void refreshRow() throws SQLException {
            q();
        }

        public void cancelRowUpdates() throws SQLException {
            q();
        }

        public void moveToInsertRow() throws SQLException {
            q();
        }

        public void moveToCurrentRow() throws SQLException {
            q();
        }

        public java.sql.Statement getStatement() {
            return this.Statement;
        }

        public Object getObject(int i, Map map) throws SQLException {
            q();
            return null;
        }

        public Ref getRef(int i) throws SQLException {
            q();
            return null;
        }

        public Blob getBlob(int i) throws SQLException {
            q();
            return null;
        }

        public Clob getClob(int i) throws SQLException {
            q();
            return null;
        }

        public Array getArray(int i) throws SQLException {
            q();
            return null;
        }

        public Object getObject(String colName, Map map) throws SQLException {
            q();
            return null;
        }

        public Ref getRef(String colName) throws SQLException {
            q();
            return null;
        }

        public Blob getBlob(String colName) throws SQLException {
            q();
            return null;
        }

        public Clob getClob(String colName) throws SQLException {
            q();
            return null;
        }

        public Array getArray(String colName) throws SQLException {
            q();
            return null;
        }

        public Date getDate(int columnIndex, Calendar cal) throws SQLException {
            q();
            return null;
        }

        public Date getDate(String columnName, Calendar cal) throws SQLException {
            q();
            return null;
        }

        public Time getTime(int columnIndex, Calendar cal) throws SQLException {
            q();
            return null;
        }

        public Time getTime(String columnName, Calendar cal) throws SQLException {
            q();
            return null;
        }

        public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
            q();
            return null;
        }

        public Timestamp getTimestamp(String columnName, Calendar cal) throws SQLException {
            q();
            return null;
        }

        public URL getURL(int columnIndex) throws SQLException {
            q();
            return null;
        }

        public URL getURL(String columnName) throws SQLException {
            q();
            return null;
        }

        public void updateRef(int columnIndex, Ref x) throws SQLException {
            q();
        }

        public void updateRef(String columnName, Ref x) throws SQLException {
            q();
        }

        public void updateBlob(int columnIndex, Blob x) throws SQLException {
            q();
        }

        public void updateBlob(String columnName, Blob x) throws SQLException {
            q();
        }

        public void updateClob(int columnIndex, Clob x) throws SQLException {
            q();
        }

        public void updateClob(String columnName, Clob x) throws SQLException {
            q();
        }

        public void updateArray(int columnIndex, Array x) throws SQLException {
            q();
        }

        public void updateArray(String columnName, Array x) throws SQLException {
            q();
        }

        public RowId getRowId(int i) throws SQLException {
            q();
            return null;
        }

        public RowId getRowId(String string) throws SQLException {
            q();
            return null;
        }

        public void updateRowId(int i, RowId rowid) throws SQLException {
            q();
        }

        public void updateRowId(String string, RowId rowid) throws SQLException {
            q();
        }

        public int getHoldability() throws SQLException {
            q();
            return 0;
        }

        public boolean isClosed() {
            return (this.d == null);
        }

        public void updateNString(int i, String string) throws SQLException {
            q();
        }

        public void updateNString(String string, String string1) throws SQLException {
            q();
        }

        public void updateNClob(int i, NClob nclob) throws SQLException {
            q();
        }

        public void updateNClob(String string, NClob nclob) throws SQLException {
            q();
        }

        public NClob getNClob(int i) throws SQLException {
            q();
            return null;
        }

        public NClob getNClob(String string) throws SQLException {
            q();
            return null;
        }

        public SQLXML getSQLXML(int i) throws SQLException {
            q();
            return null;
        }

        public SQLXML getSQLXML(String string) throws SQLException {
            q();
            return null;
        }

        public void updateSQLXML(int i, SQLXML sqlxml) throws SQLException {
            q();
        }

        public void updateSQLXML(String string, SQLXML sqlxml) throws SQLException {
            q();
        }

        public String getNString(int i) throws SQLException {
            q();
            return null;
        }

        public String getNString(String string) throws SQLException {
            q();
            return null;
        }

        public Reader getNCharacterStream(int i) throws SQLException {
            q();
            return null;
        }

        public Reader getNCharacterStream(String string) throws SQLException {
            q();
            return null;
        }

        public void updateNCharacterStream(int i, Reader reader, long l) throws SQLException {
            q();
        }

        public void updateNCharacterStream(String string, Reader reader, long l) throws SQLException {
            q();
        }

        public void updateAsciiStream(int i, InputStream in, long l) throws SQLException {
            q();
        }

        public void updateBinaryStream(int i, InputStream in, long l) throws SQLException {
            q();
        }

        public void updateCharacterStream(int i, Reader reader, long l) throws SQLException {
            q();
        }

        public void updateAsciiStream(String string, InputStream in, long l) throws SQLException {
            q();
        }

        public void updateBinaryStream(String string, InputStream in, long l) throws SQLException {
            q();
        }

        public void updateCharacterStream(String string, Reader reader, long l) throws SQLException {
            q();
        }

        public void updateBlob(int i, InputStream in, long l) throws SQLException {
            q();
        }

        public void updateBlob(String string, InputStream in, long l) throws SQLException {
            q();
        }

        public void updateClob(int i, Reader reader, long l) throws SQLException {
            q();
        }

        public void updateClob(String string, Reader reader, long l) throws SQLException {
            q();
        }

        public void updateNClob(int i, Reader reader, long l) throws SQLException {
            q();
        }

        public void updateNClob(String string, Reader reader, long l) throws SQLException {
            q();
        }

        public void updateNCharacterStream(int i, Reader reader) throws SQLException {
            q();
        }

        public void updateNCharacterStream(String string, Reader reader) throws SQLException {
            q();
        }

        public void updateAsciiStream(int i, InputStream in) throws SQLException {
            q();
        }

        public void updateBinaryStream(int i, InputStream in) throws SQLException {
            q();
        }

        public void updateCharacterStream(int i, Reader reader) throws SQLException {
            q();
        }

        public void updateAsciiStream(String string, InputStream in) throws SQLException {
            q();
        }

        public void updateBinaryStream(String string, InputStream in) throws SQLException {
            q();
        }

        public void updateCharacterStream(String string, Reader reader) throws SQLException {
            q();
        }

        public void updateBlob(int i, InputStream in) throws SQLException {
            q();
        }

        public void updateBlob(String string, InputStream in) throws SQLException {
            q();
        }

        public void updateClob(int i, Reader reader) throws SQLException {
            q();
        }

        public void updateClob(String string, Reader reader) throws SQLException {
            q();
        }

        public void updateNClob(int i, Reader reader) throws SQLException {
            q();
        }

        public void updateNClob(String string, Reader reader) throws SQLException {
            q();
        }

        @Override
        public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
            q();
            return null;
        }

        @Override
        public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
            q();
            return null;
        }

        public <T> T unwrap(Class<T> type) throws SQLException {
            q();
            return null;
        }

        public boolean isWrapperFor(Class<?> type) throws SQLException {
            q();
            return false;
        }

        private static class TimespanTimestamp
                extends Timestamp {
            private final C.Timespan t;

            public TimespanTimestamp(C.Timespan t) {
                super(t.j / 1000000L - TimeZone.getDefault().getOffset(0L));
                this.setNanos((int) (t.j % 1000000000L));
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

        public int getColumnCount() {
            return this.f.length;
        }

        public String getColumnName(int i) {
            return this.f[i - 1];
        }

        public String getColumnTypeName(int i) {
            return TYPE[C.t(this.d[i - 1])];
        }

        public int getColumnDisplaySize(int i) {
            return 11;
        }

        public int getScale(int i) {
            return 2;
        }

        public int isNullable(int i) {
            return java.sql.ResultSetMetaData.columnNullable;
        }

        public String getColumnLabel(int i) {
            return this.getColumnName(i);
        }

        public int getColumnType(int i) {
            return SQLTYPE[C.t(this.d[i - 1])];
        }

        public int getPrecision(int i) {
            return 11;
        }

        public boolean isSigned(int i) {
            return true;
        }

        public String getTableName(int i) {
            return "";
        }

        public String getSchemaName(int i) {
            return "";
        }

        public String getCatalogName(int i) {
            return "";
        }

        public boolean isReadOnly(int i) {
            return false;
        }

        public boolean isWritable(int i) {
            return false;
        }

        public boolean isDefinitelyWritable(int i) {
            return false;
        }

        public boolean isAutoIncrement(int i) {
            return false;
        }

        public boolean isCaseSensitive(int i) {
            return true;
        }

        public boolean isSearchable(int i) {
            return true;
        }

        public boolean isCurrency(int i) {
            return false;
        }

        public String getColumnClassName(int column) throws SQLException {
            q("col");
            return null;
        }

        public <T> T unwrap(Class<T> type) throws SQLException {
            q();
            return null;
        }

        public boolean isWrapperFor(Class<?> type) throws SQLException {
            q();
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
                q(e);
            }
        }

        public Object ex(String s, Object[] p) throws SQLException {
            try {
                return (0 < C.n(p)) ? this.c.k(s, p) : this.c.k(".o.ex", s.toCharArray());
            } catch (Exception e) {

                if (e.getMessage().contains("Connection reset by peer")) {
                    try {
                        this.close();
                    } catch (SQLException e1) {
                    }
                }
                q(e);
                return null;
            }
        }

        public ResultSet qx(String s) throws SQLException {
            try {
                return new ResultSet(null, this.c.k(s));
            } catch (Exception e) {
                q(e);
                return null;
            }
        }

        public ResultSet qx(String s, Object x) throws SQLException {
            try {
                return new ResultSet(null, this.c.k(s, x));
            } catch (Exception e) {
                q(e);
                return null;
            }
        }

        public boolean getAutoCommit() {
            return this.a;
        }

        public void setAutoCommit(boolean b) {
            this.a = b;
        }

        public void rollback() {
        }

        public void commit() {
        }

        public boolean isClosed() {
            return (this.c == null);
        }

        public java.sql.Statement createStatement() {
            return new Statement(this);
        }

        public java.sql.DatabaseMetaData getMetaData() {
            return new DatabaseMetaData(this);
        }

        public java.sql.PreparedStatement prepareStatement(String s) {
            return new PreparedStatement(this, s);
        }

        public java.sql.CallableStatement prepareCall(String s) {
            return new CallableStatement(this, s);
        }

        public String nativeSQL(String s) {
            return s;
        }

        public boolean isReadOnly() {
            return this.b;
        }

        public void setReadOnly(boolean x) {
            this.b = x;
        }

        public String getCatalog() throws SQLException {
            q("cat");
            return null;
        }

        public void setCatalog(String s) throws SQLException {
            q("cat");
        }

        public int getTransactionIsolation() {
            return this.i;
        }

        public void setTransactionIsolation(int x) {
            this.i = x;
        }

        public SQLWarning getWarnings() {
            return null;
        }

        public void clearWarnings() {
        }

        public void close() throws SQLException {
            if (!this.isClosed()) {
                try {
                    this.c.close();
                } catch (IOException e) {
                    q(e);
                } finally {
                    this.c = null;
                }
            }
        }

        public java.sql.Statement createStatement(int resultSetType, int resultSetConcurrency) {
            return new Statement(this);
        }

        public java.sql.PreparedStatement prepareStatement(String s, int resultSetType, int resultSetConcurrency) {
            return new PreparedStatement(this, s);
        }

        public java.sql.CallableStatement prepareCall(String s, int resultSetType, int resultSetConcurrency) {
            return new CallableStatement(this, s);
        }

        public Map getTypeMap() {
            return null;
        }

        public void setTypeMap(Map map) {
        }

        public int getHoldability() {
            return this.h;
        }

        public void setHoldability(int holdability) {
            this.h = holdability;
        }

        public Savepoint setSavepoint() throws SQLException {
            q("sav");
            return null;
        }

        public Savepoint setSavepoint(String name) throws SQLException {
            q("sav");
            return null;
        }

        public void rollback(Savepoint savepoint) {
        }

        public void releaseSavepoint(Savepoint savepoint) {
        }

        public java.sql.Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) {
            return new Statement(this);
        }

        public java.sql.PreparedStatement prepareStatement(String s, int resultSetType, int resultSetConcurrency, int resultSetHoldability) {
            return new PreparedStatement(this, s);
        }

        public java.sql.CallableStatement prepareCall(String s, int resultSetType, int resultSetConcurrency, int resultSetHoldability) {
            return new CallableStatement(this, s);
        }

        public java.sql.PreparedStatement prepareStatement(String s, int autoGeneratedKeys) {
            return new PreparedStatement(this, s);
        }

        public java.sql.PreparedStatement prepareStatement(String s, int[] columnIndexes) {
            return new PreparedStatement(this, s);
        }

        public java.sql.PreparedStatement prepareStatement(String s, String[] columnNames) {
            return new PreparedStatement(this, s);
        }

        public Clob createClob() throws SQLException {
            q();
            return null;
        }

        public Blob createBlob() throws SQLException {
            q();
            return null;
        }

        public NClob createNClob() throws SQLException {
            q();
            return null;
        }

        public SQLXML createSQLXML() throws SQLException {
            q();
            return null;
        }

        public boolean isValid(int i) throws SQLException {
            if (i < 0)
                q();
            return (this.c != null);
        }

        public void setClientInfo(String k, String v) {
            this.clientInfo.setProperty(k, v);
        }

        public String getClientInfo(String k) {
            return (String) this.clientInfo.get(k);
        }

        public Properties getClientInfo() {
            return this.clientInfo;
        }

        public void setClientInfo(Properties p) {
            this.clientInfo = p;
        }

        public Array createArrayOf(String string, Object[] os) throws SQLException {
            q();
            return null;
        }

        public Struct createStruct(String string, Object[] os) throws SQLException {
            q();
            return null;
        }

        @Override
        public String getSchema() {
            return this.schema;
        }

        @Override
        public void setSchema(String schema) {
            this.schema = schema;
        }

        @Override
        public void abort(Executor executor) {

        }

        @Override
        public void setNetworkTimeout(Executor executor, int milliseconds) {

        }

        @Override
        public int getNetworkTimeout() {
            return 0;
        }

        public <T> T unwrap(Class<T> type) throws SQLException {
            q();
            return null;
        }

        public boolean isWrapperFor(Class<?> type) throws SQLException {
            q();
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

        public int getMaxRows() {
            return this.R;
        }

        public void setMaxRows(int i) {
            this.R = i;
        }

        public int getQueryTimeout() {
            return this.T;
        }

        public void setQueryTimeout(int i) {
            this.T = i;
        }

        public int getMaxFieldSize() {
            return 0;
        }

        public void setMaxFieldSize(int i) {
        }

        public void setEscapeProcessing(boolean b) {
        }

        public void cancel() {
        }

        public SQLWarning getWarnings() {
            return null;
        }

        public void clearWarnings() {
        }

        public void setCursorName(String name) throws SQLException {
            q("cur");
        }

        public boolean getMoreResults() {
            return false;
        }

        public void close() {
            this.Connection = null;
        }

        public int getFetchDirection() {
            return 0;
        }

        public void setFetchDirection(int direction) throws SQLException {
            q("fd");
        }

        public int getFetchSize() {
            return 0;
        }

        public void setFetchSize(int rows) {
        }

        public int getResultSetConcurrency() {
            return java.sql.ResultSet.CONCUR_READ_ONLY;
        }

        public int getResultSetType() {
            return java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE;
        }

        public void addBatch(String sql) throws SQLException {
            q("bat");
        }

        public void clearBatch() {
        }

        public int[] executeBatch() {
            return new int[0];
        }

        public java.sql.Connection getConnection() {
            return this.Connection;
        }

        public boolean getMoreResults(int current) {
            return false;
        }

        public java.sql.ResultSet getGeneratedKeys() {
            return null;
        }

        public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
            q("a");
            return 0;
        }

        public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
            q("a");
            return 0;
        }

        public int executeUpdate(String sql, String[] columnNames) throws SQLException {
            q("a");
            return 0;
        }

        public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
            q("a");
            return false;
        }

        public boolean execute(String sql, int[] columnIndexes) throws SQLException {
            q("a");
            return false;
        }

        public boolean execute(String sql, String[] columnNames) throws SQLException {
            q("a");
            return false;
        }

        public int getResultSetHoldability() {
            return 1;
        }

        public boolean isClosed() {
            return (this.Connection == null || this.Connection.isClosed());
        }

        public boolean isPoolable() throws SQLException {
            if (this.isClosed())
                throw new SQLException("Closed");
            return this.poolable;
        }

        public void setPoolable(boolean b) throws SQLException {
            if (this.isClosed())
                throw new SQLException("Closed");
            this.poolable = b;
        }

        @Override
        public void closeOnCompletion() {

        }

        @Override
        public boolean isCloseOnCompletion() {
            return false;
        }

        public <T> T unwrap(Class<T> type) throws SQLException {
            q();
            return null;
        }

        public boolean isWrapperFor(Class<?> type) throws SQLException {
            q();
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
            return this.executeQuery(this.s);
        }

        public int executeUpdate() throws SQLException {
            return this.executeUpdate(this.s);
        }

        public boolean execute() throws SQLException {
            return this.execute(this.s);
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
            this.setObject(i, x);
        }

        public void setObject(int i, Object x, int targetSqlType, int scale) throws SQLException {
            this.setObject(i, x);
        }

        public void setNull(int i, int t) throws SQLException {
            this.setObject(i, C.NULL[find(SQLTYPE, t)]);
        }

        public void setBoolean(int i, boolean x) throws SQLException {
            this.setObject(i, x);
        }

        public void setByte(int i, byte x) throws SQLException {
            this.setObject(i, x);
        }

        public void setShort(int i, short x) throws SQLException {
            this.setObject(i, x);
        }

        public void setInt(int i, int x) throws SQLException {
            this.setObject(i, x);
        }

        public void setLong(int i, long x) throws SQLException {
            this.setObject(i, x);
        }

        public void setFloat(int i, float x) throws SQLException {
            this.setObject(i, x);
        }

        public void setDouble(int i, double x) throws SQLException {
            this.setObject(i, x);
        }

        public void setString(int i, String x) throws SQLException {
            this.setObject(i, x);
        }

        public void setDate(int i, Date x) throws SQLException {
            this.setObject(i, x);
        }

        public void setTime(int i, Time x) throws SQLException {
            this.setObject(i, x);
        }

        public void setTimestamp(int i, Timestamp x) throws SQLException {
            this.setObject(i, x);
        }

        public void setBytes(int i, byte[] x) throws SQLException {
            q();
        }

        public void setBigDecimal(int i, BigDecimal x) throws SQLException {
            q();
        }

        public void setAsciiStream(int i, InputStream x, int length) throws SQLException {
            q();
        }

        public void setUnicodeStream(int i, InputStream x, int length) throws SQLException {
            q();
        }

        public void setBinaryStream(int i, InputStream x, int length) throws SQLException {
            q();
        }

        public void addBatch() {
        }

        public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
            q();
        }

        public void setRef(int i, Ref x) throws SQLException {
            q();
        }

        public void setBlob(int i, Blob x) throws SQLException {
            q();
        }

        public void setClob(int i, Clob x) throws SQLException {
            q();
        }

        public void setArray(int i, Array x) throws SQLException {
            q();
        }

        public java.sql.ResultSetMetaData getMetaData() throws SQLException {
            q("m");
            return null;
        }

        public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
            q();
        }

        public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
            q();
        }

        public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
            q();
        }

        public void setNull(int paramIndex, int sqlType, String typeName) throws SQLException {
            q();
        }

        public void setURL(int parameterIndex, URL x) throws SQLException {
            q();
        }

        public ParameterMetaData getParameterMetaData() throws SQLException {
            q("m");
            return null;
        }

        public void setRowId(int i, RowId rowid) throws SQLException {
            q();
        }

        public void setNString(int i, String string) throws SQLException {
            q();
        }

        public void setNCharacterStream(int i, Reader reader, long l) throws SQLException {
            q();
        }

        public void setNClob(int i, NClob nclob) throws SQLException {
            q();
        }

        public void setClob(int i, Reader reader, long l) throws SQLException {
            q();
        }

        public void setBlob(int i, InputStream in, long l) throws SQLException {
            q();
        }

        public void setNClob(int i, Reader reader, long l) throws SQLException {
            q();
        }

        public void setSQLXML(int i, SQLXML sqlxml) throws SQLException {
            q();
        }

        public void setAsciiStream(int i, InputStream in, long l) throws SQLException {
            q();
        }

        public void setBinaryStream(int i, InputStream in, long l) throws SQLException {
            q();
        }

        public void setCharacterStream(int i, Reader reader, long l) throws SQLException {
            q();
        }

        public void setAsciiStream(int i, InputStream in) throws SQLException {
            q();
        }

        public void setBinaryStream(int i, InputStream in) throws SQLException {
            q();
        }

        public void setCharacterStream(int i, Reader reader) throws SQLException {
            q();
        }

        public void setNCharacterStream(int i, Reader reader) throws SQLException {
            q();
        }

        public void setClob(int i, Reader reader) throws SQLException {
            q();
        }

        public void setBlob(int i, InputStream in) throws SQLException {
            q();
        }

        public void setNClob(int i, Reader reader) throws SQLException {
            q();
        }
    }

    public class CallableStatement
            extends PreparedStatement implements java.sql.CallableStatement {
        public CallableStatement(Connection c, String s) {
            super(c, s);
        }

        public void registerOutParameter(int i, int sqlType) {
        }

        public void registerOutParameter(int i, int sqlType, int scale) {
        }

        public boolean wasNull() {
            return false;
        }

        public String getString(int i) {
            return null;
        }

        public boolean getBoolean(int i) {
            return false;
        }

        public byte getByte(int i) {
            return 0;
        }

        public short getShort(int i) {
            return 0;
        }

        public int getInt(int i) {
            return 0;
        }

        public long getLong(int i) {
            return 0L;
        }

        public float getFloat(int i) {
            return 0.0F;
        }

        public double getDouble(int i) {
            return 0.0D;
        }

        public BigDecimal getBigDecimal(int i, int scale) {
            return null;
        }

        public Date getDate(int i) {
            return null;
        }

        public Time getTime(int i) {
            return null;
        }

        public Timestamp getTimestamp(int i) {
            return null;
        }

        public byte[] getBytes(int i) {
            return null;
        }

        public Object getObject(int i) {
            return null;
        }

        public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
            q();
            return null;
        }

        public Object getObject(int i, Map map) throws SQLException {
            q();
            return null;
        }

        public Ref getRef(int i) throws SQLException {
            q();
            return null;
        }

        public Blob getBlob(int i) throws SQLException {
            q();
            return null;
        }

        public Clob getClob(int i) throws SQLException {
            q();
            return null;
        }

        public Array getArray(int i) throws SQLException {
            q();
            return null;
        }

        public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
            q();
            return null;
        }

        public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
            q();
            return null;
        }

        public Timestamp getTimestamp(int parameterIndex, Calendar cal) throws SQLException {
            q();
            return null;
        }

        public void registerOutParameter(int paramIndex, int sqlType, String typeName) throws SQLException {
            q();
        }

        public void registerOutParameter(String parameterName, int sqlType) throws SQLException {
            q();
        }

        public void registerOutParameter(String parameterName, int sqlType, int scale) throws SQLException {
            q();
        }

        public void registerOutParameter(String parameterName, int sqlType, String typeName) throws SQLException {
            q();
        }

        public URL getURL(int parameterIndex) throws SQLException {
            q();
            return null;
        }

        public void setURL(String parameterName, URL val) throws SQLException {
            q();
        }

        public void setNull(String parameterName, int sqlType) throws SQLException {
            q();
        }

        public void setBoolean(String parameterName, boolean x) throws SQLException {
            q();
        }

        public void setByte(String parameterName, byte x) throws SQLException {
            q();
        }

        public void setShort(String parameterName, short x) throws SQLException {
            q();
        }

        public void setInt(String parameterName, int x) throws SQLException {
            q();
        }

        public void setLong(String parameterName, long x) throws SQLException {
            q();
        }

        public void setFloat(String parameterName, float x) throws SQLException {
            q();
        }

        public void setDouble(String parameterName, double x) throws SQLException {
            q();
        }

        public void setBigDecimal(String parameterName, BigDecimal x) throws SQLException {
            q();
        }

        public void setString(String parameterName, String x) throws SQLException {
            q();
        }

        public void setBytes(String parameterName, byte[] x) throws SQLException {
            q();
        }

        public void setDate(String parameterName, Date x) throws SQLException {
            q();
        }

        public void setTime(String parameterName, Time x) throws SQLException {
            q();
        }

        public void setTimestamp(String parameterName, Timestamp x) throws SQLException {
            q();
        }

        public void setAsciiStream(String parameterName, InputStream x, int length) throws SQLException {
            q();
        }

        public void setBinaryStream(String parameterName, InputStream x, int length) throws SQLException {
            q();
        }

        public void setObject(String parameterName, Object x, int targetSqlType, int scale) throws SQLException {
            q();
        }

        public void setObject(String parameterName, Object x, int targetSqlType) throws SQLException {
            q();
        }

        public void setObject(String parameterName, Object x) throws SQLException {
            q();
        }

        public void setCharacterStream(String parameterName, Reader reader, int length) throws SQLException {
            q();
        }

        public void setDate(String parameterName, Date x, Calendar cal) throws SQLException {
            q();
        }

        public void setTime(String parameterName, Time x, Calendar cal) throws SQLException {
            q();
        }

        public void setTimestamp(String parameterName, Timestamp x, Calendar cal) throws SQLException {
            q();
        }

        public void setNull(String parameterName, int sqlType, String typeName) throws SQLException {
            q();
        }

        public String getString(String parameterName) {
            return null;
        }

        public boolean getBoolean(String parameterName) {
            return false;
        }

        public byte getByte(String parameterName) {
            return 0;
        }

        public short getShort(String parameterName) {
            return 0;
        }

        public int getInt(String parameterName) {
            return 0;
        }

        public long getLong(String parameterName) {
            return 0L;
        }

        public float getFloat(String parameterName) {
            return 0.0F;
        }

        public double getDouble(String parameterName) {
            return 0.0D;
        }

        public byte[] getBytes(String parameterName) {
            return null;
        }

        public Date getDate(String parameterName) {
            return null;
        }

        public Time getTime(String parameterName) {
            return null;
        }

        public Timestamp getTimestamp(String parameterName) {
            return null;
        }

        public Object getObject(String parameterName) {
            return null;
        }

        public BigDecimal getBigDecimal(String parameterName) {
            return null;
        }

        public Object getObject(String parameterName, Map map) {
            return null;
        }

        public Ref getRef(String parameterName) {
            return null;
        }

        public Blob getBlob(String parameterName) {
            return null;
        }

        public Clob getClob(String parameterName) {
            return null;
        }

        public Array getArray(String parameterName) {
            return null;
        }

        public Date getDate(String parameterName, Calendar cal) {
            return null;
        }

        public Time getTime(String parameterName, Calendar cal) {
            return null;
        }

        public Timestamp getTimestamp(String parameterName, Calendar cal) {
            return null;
        }

        public URL getURL(String parameterName) {
            return null;
        }

        public RowId getRowId(int i) throws SQLException {
            q();
            return null;
        }

        public RowId getRowId(String string) throws SQLException {
            q();
            return null;
        }

        public void setRowId(String string, RowId rowid) throws SQLException {
            q();
        }

        public void setNString(String string, String string1) throws SQLException {
            q();
        }

        public void setNCharacterStream(String string, Reader reader, long l) throws SQLException {
            q();
        }

        public void setNClob(String string, NClob nclob) throws SQLException {
            q();
        }

        public void setClob(String string, Reader reader, long l) throws SQLException {
            q();
        }

        public void setBlob(String string, InputStream in, long l) throws SQLException {
            q();
        }

        public void setNClob(String string, Reader reader, long l) throws SQLException {
            q();
        }

        public NClob getNClob(int i) throws SQLException {
            q();
            return null;
        }

        public NClob getNClob(String string) throws SQLException {
            q();
            return null;
        }

        public void setSQLXML(String string, SQLXML sqlxml) throws SQLException {
            q();
        }

        public SQLXML getSQLXML(int i) throws SQLException {
            q();
            return null;
        }

        public SQLXML getSQLXML(String string) throws SQLException {
            q();
            return null;
        }

        public String getNString(int i) throws SQLException {
            q();
            return null;
        }

        public String getNString(String string) throws SQLException {
            q();
            return null;
        }

        public Reader getNCharacterStream(int i) throws SQLException {
            q();
            return null;
        }

        public Reader getNCharacterStream(String string) throws SQLException {
            q();
            return null;
        }

        public Reader getCharacterStream(int i) throws SQLException {
            q();
            return null;
        }

        public Reader getCharacterStream(String string) throws SQLException {
            q();
            return null;
        }

        public void setBlob(String string, Blob blob) throws SQLException {
            q();
        }

        public void setClob(String string, Clob clob) throws SQLException {
            q();
        }

        public void setAsciiStream(String string, InputStream in, long l) throws SQLException {
            q();
        }

        public void setBinaryStream(String string, InputStream in, long l) throws SQLException {
            q();
        }

        public void setCharacterStream(String string, Reader reader, long l) throws SQLException {
            q();
        }

        public void setAsciiStream(String string, InputStream in) throws SQLException {
            q();
        }

        public void setBinaryStream(String string, InputStream in) throws SQLException {
            q();
        }

        public void setCharacterStream(String string, Reader reader) throws SQLException {
            q();
        }

        public void setNCharacterStream(String string, Reader reader) throws SQLException {
            q();
        }

        public void setClob(String string, Reader reader) throws SQLException {
            q();
        }

        public void setBlob(String string, InputStream in) throws SQLException {
            q();
        }

        public void setNClob(String string, Reader reader) throws SQLException {
            q();
        }

        @Override
        public <T> T getObject(int parameterIndex, Class<T> type) throws SQLException {
            q();
            return null;
        }

        @Override
        public <T> T getObject(String parameterName, Class<T> type) throws SQLException {
            q();
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
            final String getMetaInf = "raze{([]TABLE_CAT:`;TABLE_SCHEM:`;TABLE_NAME:system string`a`b x=`VIEW;TABLE_TYPE:x)} each";
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
            q("pk");
            return this.Connection.qx("");
        }

        public java.sql.ResultSet getImportedKeys(String a, String b, String t) throws SQLException {
            q("imp");
            return this.Connection.qx("");
        }

        public java.sql.ResultSet getProcedures(String a, String b, String p) throws SQLException {
            q("pr");
            return this.Connection.qx("");
        }

        public java.sql.ResultSet getExportedKeys(String a, String b, String t) throws SQLException {
            q("exp");
            return null;
        }

        public java.sql.ResultSet getCrossReference(String pa, String pb, String pt, String fa, String fb, String ft) throws SQLException {
            q("cr");
            return null;
        }

        public java.sql.ResultSet getIndexInfo(String a, String b, String t, boolean unique, boolean approximate) throws SQLException {
            q("ii");
            return null;
        }

        public java.sql.ResultSet getProcedureColumns(String a, String b, String p, String c) throws SQLException {
            q("pc");
            return null;
        }

        public java.sql.ResultSet getColumnPrivileges(String a, String b, String table, String columnNamePattern) throws SQLException {
            q("cp");
            return null;
        }

        public java.sql.ResultSet getTablePrivileges(String a, String b, String t) throws SQLException {
            q("tp");
            return null;
        }

        public java.sql.ResultSet getBestRowIdentifier(String a, String b, String t, int scope, boolean nullable) throws SQLException {
            q("br");
            return null;
        }

        public java.sql.ResultSet getVersionColumns(String a, String b, String t) throws SQLException {
            q("vc");
            return null;
        }

        public boolean allProceduresAreCallable() {
            return true;
        }

        public boolean allTablesAreSelectable() {
            return true;
        }

        public boolean dataDefinitionCausesTransactionCommit() {
            return false;
        }

        public boolean dataDefinitionIgnoredInTransactions() {
            return false;
        }

        public boolean doesMaxRowSizeIncludeBlobs() {
            return true;
        }

        public String getSchemaTerm() {
            return "schema";
        }

        public String getProcedureTerm() {
            return "procedure";
        }

        public String getCatalogTerm() {
            return "catalog";
        }

        public String getCatalogSeparator() {
            return ".";
        }

        public int getMaxBinaryLiteralLength() {
            return 0;
        }

        public int getMaxCharLiteralLength() {
            return 0;
        }

        public int getMaxColumnNameLength() {
            return 0;
        }

        public int getMaxColumnsInGroupBy() {
            return 0;
        }

        public int getMaxColumnsInIndex() {
            return 0;
        }

        public int getMaxColumnsInOrderBy() {
            return 0;
        }

        public int getMaxColumnsInSelect() {
            return 0;
        }

        public int getMaxColumnsInTable() {
            return 0;
        }

        public int getMaxConnections() {
            return 0;
        }

        public int getMaxCursorNameLength() {
            return 0;
        }

        public int getMaxIndexLength() {
            return 0;
        }

        public int getMaxSchemaNameLength() {
            return 0;
        }

        public int getMaxProcedureNameLength() {
            return 0;
        }

        public int getMaxCatalogNameLength() {
            return 0;
        }

        public int getMaxRowSize() {
            return 0;
        }

        public int getMaxStatementLength() {
            return 0;
        }

        public int getMaxStatements() {
            return 0;
        }

        public int getMaxTableNameLength() {
            return 0;
        }

        public int getMaxTablesInSelect() {
            return 0;
        }

        public int getMaxUserNameLength() {
            return 0;
        }

        public int getDefaultTransactionIsolation() {
            return 8;
        }

        public String getSQLKeywords() {
            return "show,meta,load,save";
        }

        public String getNumericFunctions() {
            return "";
        }

        public String getStringFunctions() {
            return "";
        }

        public String getSystemFunctions() {
            return "";
        }

        public String getTimeDateFunctions() {
            return "";
        }

        public String getSearchStringEscape() {
            return "";
        }

        public String getExtraNameCharacters() {
            return "";
        }

        public String getIdentifierQuoteString() {
            return "";
        }

        public String getURL() {
            return null;
        }

        public String getUserName() {
            return "";
        }

        public String getDatabaseProductName() {
            return "kdb";
        }

        public String getDatabaseProductVersion() {
            return "2.0";
        }

        public String getDriverName() {
            return "jdbc";
        }

        public String getDriverVersion() {
            return V + "." + v;
        }

        public int getDriverMajorVersion() {
            return V;
        }

        public int getDriverMinorVersion() {
            return v;
        }

        public boolean isCatalogAtStart() {
            return true;
        }

        public boolean isReadOnly() {
            return false;
        }

        public boolean nullsAreSortedHigh() {
            return false;
        }

        public boolean nullsAreSortedLow() {
            return true;
        }

        public boolean nullsAreSortedAtStart() {
            return false;
        }

        public boolean nullsAreSortedAtEnd() {
            return false;
        }

        public boolean supportsMixedCaseIdentifiers() {
            return false;
        }

        public boolean storesUpperCaseIdentifiers() {
            return false;
        }

        public boolean storesLowerCaseIdentifiers() {
            return false;
        }

        public boolean storesMixedCaseIdentifiers() {
            return true;
        }

        public boolean supportsMixedCaseQuotedIdentifiers() {
            return true;
        }

        public boolean storesUpperCaseQuotedIdentifiers() {
            return false;
        }

        public boolean storesLowerCaseQuotedIdentifiers() {
            return false;
        }

        public boolean storesMixedCaseQuotedIdentifiers() {
            return true;
        }

        public boolean supportsAlterTableWithAddColumn() {
            return true;
        }

        public boolean supportsAlterTableWithDropColumn() {
            return true;
        }

        public boolean supportsTableCorrelationNames() {
            return true;
        }

        public boolean supportsDifferentTableCorrelationNames() {
            return true;
        }

        public boolean supportsColumnAliasing() {
            return true;
        }

        public boolean nullPlusNonNullIsNull() {
            return true;
        }

        public boolean supportsExpressionsInOrderBy() {
            return true;
        }

        public boolean supportsOrderByUnrelated() {
            return false;
        }

        public boolean supportsGroupBy() {
            return true;
        }

        public boolean supportsGroupByUnrelated() {
            return false;
        }

        public boolean supportsGroupByBeyondSelect() {
            return false;
        }

        public boolean supportsLikeEscapeClause() {
            return false;
        }

        public boolean supportsMultipleResultSets() {
            return false;
        }

        public boolean supportsMultipleTransactions() {
            return false;
        }

        public boolean supportsNonNullableColumns() {
            return true;
        }

        public boolean supportsMinimumSQLGrammar() {
            return true;
        }

        public boolean supportsCoreSQLGrammar() {
            return true;
        }

        public boolean supportsExtendedSQLGrammar() {
            return false;
        }

        public boolean supportsANSI92EntryLevelSQL() {
            return true;
        }

        public boolean supportsANSI92IntermediateSQL() {
            return false;
        }

        public boolean supportsANSI92FullSQL() {
            return false;
        }

        public boolean supportsIntegrityEnhancementFacility() {
            return false;
        }

        public boolean supportsOuterJoins() {
            return false;
        }

        public boolean supportsFullOuterJoins() {
            return false;
        }

        public boolean supportsLimitedOuterJoins() {
            return false;
        }

        public boolean supportsConvert() {
            return false;
        }

        public boolean supportsConvert(int fromType, int toType) {
            return false;
        }

        public boolean supportsSchemasInDataManipulation() {
            return false;
        }

        public boolean supportsSchemasInProcedureCalls() {
            return false;
        }

        public boolean supportsSchemasInTableDefinitions() {
            return false;
        }

        public boolean supportsSchemasInIndexDefinitions() {
            return false;
        }

        public boolean supportsSchemasInPrivilegeDefinitions() {
            return false;
        }

        public boolean supportsCatalogsInDataManipulation() {
            return false;
        }

        public boolean supportsCatalogsInProcedureCalls() {
            return false;
        }

        public boolean supportsCatalogsInTableDefinitions() {
            return false;
        }

        public boolean supportsCatalogsInIndexDefinitions() {
            return false;
        }

        public boolean supportsCatalogsInPrivilegeDefinitions() {
            return false;
        }

        public boolean supportsSelectForUpdate() {
            return false;
        }

        public boolean supportsPositionedDelete() {
            return false;
        }

        public boolean supportsPositionedUpdate() {
            return false;
        }

        public boolean supportsOpenCursorsAcrossCommit() {
            return true;
        }

        public boolean supportsOpenCursorsAcrossRollback() {
            return true;
        }

        public boolean supportsOpenStatementsAcrossCommit() {
            return true;
        }

        public boolean supportsOpenStatementsAcrossRollback() {
            return true;
        }

        public boolean supportsStoredProcedures() {
            return false;
        }

        public boolean supportsSubqueriesInComparisons() {
            return true;
        }

        public boolean supportsSubqueriesInExists() {
            return true;
        }

        public boolean supportsSubqueriesInIns() {
            return true;
        }

        public boolean supportsSubqueriesInQuantifieds() {
            return true;
        }

        public boolean supportsCorrelatedSubqueries() {
            return true;
        }

        public boolean supportsUnion() {
            return true;
        }

        public boolean supportsUnionAll() {
            return true;
        }

        public boolean supportsTransactions() {
            return true;
        }

        public boolean supportsTransactionIsolationLevel(int level) {
            return true;
        }

        public boolean supportsDataDefinitionAndDataManipulationTransactions() {
            return true;
        }

        public boolean supportsDataManipulationTransactionsOnly() {
            return false;
        }

        public boolean usesLocalFiles() {
            return false;
        }

        public boolean usesLocalFilePerTable() {
            return false;
        }

        public boolean supportsResultSetType(int type) {
            return (type != 1005);
        }

        public boolean supportsResultSetConcurrency(int type, int concurrency) {
            return (type == 1007);
        }

        public boolean ownUpdatesAreVisible(int type) {
            return false;
        }

        public boolean ownDeletesAreVisible(int type) {
            return false;
        }

        public boolean ownInsertsAreVisible(int type) {
            return false;
        }

        public boolean othersUpdatesAreVisible(int type) {
            return false;
        }

        public boolean othersDeletesAreVisible(int type) {
            return false;
        }

        public boolean othersInsertsAreVisible(int type) {
            return false;
        }

        public boolean updatesAreDetected(int type) {
            return false;
        }

        public boolean deletesAreDetected(int type) {
            return false;
        }

        public boolean insertsAreDetected(int type) {
            return false;
        }

        public boolean supportsBatchUpdates() {
            return false;
        }

        public java.sql.ResultSet getUDTs(String catalog, String schemaPattern, String typeNamePattern, int[] types) {
            return null;
        }

        public java.sql.Connection getConnection() {
            return this.Connection;
        }

        public boolean supportsSavepoints() {
            return false;
        }

        public boolean supportsNamedParameters() {
            return false;
        }

        public boolean supportsMultipleOpenResults() {
            return false;
        }

        public boolean supportsGetGeneratedKeys() {
            return false;
        }

        public java.sql.ResultSet getSuperTypes(String catalog, String schemaPattern, String typeNamePattern) {
            return null;
        }

        public java.sql.ResultSet getSuperTables(String catalog, String schemaPattern, String tableNamePattern) {
            return null;
        }

        public java.sql.ResultSet getAttributes(String catalog, String schemaPattern, String typeNamePattern, String attributeNamePattern) {
            return null;
        }

        public boolean supportsResultSetHoldability(int holdability) {
            return false;
        }

        public int getResultSetHoldability() {
            return 0;
        }

        public int getDatabaseMajorVersion() {
            return 0;
        }

        public int getDatabaseMinorVersion() {
            return 0;
        }

        public int getJDBCMajorVersion() {
            return 0;
        }

        public int getJDBCMinorVersion() {
            return 0;
        }

        public int getSQLStateType() {
            return 0;
        }

        public boolean locatorsUpdateCopy() {
            return false;
        }

        public boolean supportsStatementPooling() {
            return false;
        }

        public RowIdLifetime getRowIdLifetime() throws SQLException {
            q();
            return null;
        }

        public java.sql.ResultSet getSchemas(String string, String string1) throws SQLException {
            q();
            return null;
        }

        public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
            q();
            return false;
        }

        public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
            q();
            return false;
        }

        public java.sql.ResultSet getClientInfoProperties() throws SQLException {
            q();
            return null;
        }

        public java.sql.ResultSet getFunctions(String string, String string1, String string2) throws SQLException {
            q();
            return null;
        }

        public java.sql.ResultSet getFunctionColumns(String string, String string1, String string2, String string3) throws SQLException {
            q();
            return null;
        }

        @Override
        public java.sql.ResultSet getPseudoColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
            q();
            return null;
        }

        @Override
        public boolean generatedKeyAlwaysReturned() throws SQLException {
            q();
            return false;
        }

        public <T> T unwrap(Class<T> type) throws SQLException {
            q();
            return null;
        }

        public boolean isWrapperFor(Class<?> type) throws SQLException {
            q();
            return false;
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\kx\jdbc.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */