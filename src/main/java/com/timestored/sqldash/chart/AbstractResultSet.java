package com.timestored.sqldash.chart;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;


abstract class AbstractResultSet
        implements ResultSet {
    public InputStream getAsciiStream(String columnLabel) throws SQLException {
        return getAsciiStream(findColumn(columnLabel));
    }


    public Array getArray(String columnLabel) throws SQLException {
        return getArray(findColumn(columnLabel));
    }


    public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
        return getBigDecimal(findColumn(columnLabel));
    }


    public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
        return getBigDecimal(findColumn(columnLabel));
    }


    public InputStream getBinaryStream(String columnLabel) throws SQLException {
        return getBinaryStream(findColumn(columnLabel));
    }


    public Blob getBlob(String columnLabel) throws SQLException {
        return getBlob(findColumn(columnLabel));
    }


    public byte[] getBytes(String columnLabel) throws SQLException {
        return getBytes(findColumn(columnLabel));
    }


    public Reader getCharacterStream(String columnLabel) throws SQLException {
        return getCharacterStream(findColumn(columnLabel));
    }


    public Clob getClob(String columnLabel) throws SQLException {
        return getClob(findColumn(columnLabel));
    }


    public Date getDate(String columnLabel) throws SQLException {
        return getDate(findColumn(columnLabel));
    }


    public Date getDate(String columnLabel, Calendar cal) throws SQLException {
        return getDate(findColumn(columnLabel));
    }


    public double getDouble(String columnLabel) throws SQLException {
        return getDouble(findColumn(columnLabel));
    }


    public float getFloat(String columnLabel) throws SQLException {
        return getFloat(findColumn(columnLabel));
    }


    public int getInt(String columnLabel) throws SQLException {
        return getInt(findColumn(columnLabel));
    }


    public Reader getNCharacterStream(String columnLabel) throws SQLException {
        return getNCharacterStream(findColumn(columnLabel));
    }


    public NClob getNClob(String columnLabel) throws SQLException {
        return getNClob(findColumn(columnLabel));
    }


    public String getNString(String columnLabel) throws SQLException {
        return getNString(findColumn(columnLabel));
    }


    public Object getObject(String columnLabel) throws SQLException {
        return getObject(findColumn(columnLabel));
    }


    public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
        return getObject(findColumn(columnLabel));
    }


    public Ref getRef(String columnLabel) throws SQLException {
        return getRef(findColumn(columnLabel));
    }


    public RowId getRowId(String columnLabel) throws SQLException {
        return getRowId(findColumn(columnLabel));
    }


    public SQLXML getSQLXML(String columnLabel) throws SQLException {
        return getSQLXML(findColumn(columnLabel));
    }


    public short getShort(String columnLabel) throws SQLException {
        return getShort(findColumn(columnLabel));
    }


    public String getString(String columnLabel) throws SQLException {
        return getString(findColumn(columnLabel));
    }


    public Time getTime(String columnLabel) throws SQLException {
        return getTime(findColumn(columnLabel));
    }


    public Time getTime(String columnLabel, Calendar cal) throws SQLException {
        return getTime(findColumn(columnLabel));
    }


    public Timestamp getTimestamp(String columnLabel) throws SQLException {
        return getTimestamp(findColumn(columnLabel));
    }


    public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
        return getTimestamp(findColumn(columnLabel));
    }


    public URL getURL(String columnLabel) throws SQLException {
        return getURL(findColumn(columnLabel));
    }


    public InputStream getUnicodeStream(String columnLabel) throws SQLException {
        return getUnicodeStream(findColumn(columnLabel));
    }


    public void updateArray(String columnLabel, Array x) throws SQLException {
        updateArray(findColumn(columnLabel), x);
    }


    public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
        updateAsciiStream(findColumn(columnLabel), x);
    }


    public void updateAsciiStream(String columnLabel, InputStream x, int length) throws SQLException {
        updateAsciiStream(findColumn(columnLabel), x, length);
    }


    public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
        updateAsciiStream(findColumn(columnLabel), x, length);
    }


    public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
        updateBinaryStream(findColumn(columnLabel), x);
    }


    public void updateBinaryStream(String columnLabel, InputStream x, int length) throws SQLException {
        updateBinaryStream(findColumn(columnLabel), x, length);
    }


    public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
        updateBinaryStream(findColumn(columnLabel), x, length);
    }


    public void updateBlob(String columnLabel, Blob x) throws SQLException {
        updateBlob(findColumn(columnLabel), x);
    }


    public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
        updateBlob(findColumn(columnLabel), inputStream, length);
    }


    public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
        updateBlob(findColumn(columnLabel), inputStream);
    }


    public void updateBoolean(String columnLabel, boolean x) throws SQLException {
        updateBoolean(findColumn(columnLabel), x);
    }


    public void updateByte(String columnLabel, byte x) throws SQLException {
        updateByte(findColumn(columnLabel), x);
    }


    public void updateBytes(String columnLabel, byte[] x) throws SQLException {
        updateBytes(findColumn(columnLabel), x);
    }


    public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
        updateCharacterStream(findColumn(columnLabel), reader);
    }


    public void updateCharacterStream(String columnLabel, Reader reader, int length) throws SQLException {
        updateCharacterStream(findColumn(columnLabel), reader, length);
    }


    public void updateClob(String columnLabel, Clob x) throws SQLException {
        updateClob(findColumn(columnLabel), x);
    }


    public void updateClob(String columnLabel, Reader reader) throws SQLException {
        updateClob(findColumn(columnLabel), reader);
    }


    public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
        updateClob(findColumn(columnLabel), reader, length);
    }


    public void updateDate(String columnLabel, Date x) throws SQLException {
        updateDate(findColumn(columnLabel), x);
    }


    public void updateDouble(String columnLabel, double x) throws SQLException {
        updateDouble(findColumn(columnLabel), x);
    }


    public void updateFloat(String columnLabel, float x) throws SQLException {
        updateFloat(findColumn(columnLabel), x);
    }


    public void updateInt(String columnLabel, int x) throws SQLException {
        updateInt(findColumn(columnLabel), x);
    }


    public void updateLong(String columnLabel, long x) throws SQLException {
        updateLong(findColumn(columnLabel), x);
    }


    public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
        updateNCharacterStream(findColumn(columnLabel), reader);
    }


    public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
        updateNCharacterStream(findColumn(columnLabel), reader, length);
    }


    public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
        updateNClob(findColumn(columnLabel), nClob);
    }


    public void updateNClob(String columnLabel, Reader reader) throws SQLException {
        updateNClob(findColumn(columnLabel), reader);
    }


    public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
        updateNClob(findColumn(columnLabel), reader, length);
    }


    public void updateNString(String columnLabel, String nString) throws SQLException {
        updateNString(findColumn(columnLabel), nString);
    }


    public void updateNull(String columnLabel) throws SQLException {
        updateNull(findColumn(columnLabel));
    }


    public void updateObject(String columnLabel, Object x) throws SQLException {
        updateObject(findColumn(columnLabel), x);
    }


    public void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {
        updateObject(findColumn(columnLabel), x);
    }


    public void updateRef(String columnLabel, Ref x) throws SQLException {
        updateRef(findColumn(columnLabel), x);
    }


    public byte getByte(String columnLabel) throws SQLException {
        return getByte(findColumn(columnLabel));
    }


    public void updateRowId(String columnLabel, RowId x) throws SQLException {
        updateRowId(findColumn(columnLabel), x);
    }


    public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
        updateCharacterStream(findColumn(columnLabel), reader, length);
    }


    public long getLong(String columnLabel) throws SQLException {
        return getLong(findColumn(columnLabel));
    }


    public void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException {
        updateBigDecimal(findColumn(columnLabel), x);
    }


    public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
        updateSQLXML(findColumn(columnLabel), xmlObject);
    }


    public void updateShort(String columnLabel, short x) throws SQLException {
        updateShort(findColumn(columnLabel), x);
    }


    public void updateString(String columnLabel, String x) throws SQLException {
        updateString(findColumn(columnLabel), x);
    }


    public void updateTime(String columnLabel, Time x) throws SQLException {
        updateTime(findColumn(columnLabel), x);
    }


    public boolean getBoolean(String columnLabel) throws SQLException {
        return getBoolean(findColumn(columnLabel));
    }


    public void updateTimestamp(String columnLabel, Timestamp x) throws SQLException {
        updateTimestamp(findColumn(columnLabel), x);
    }


    public Array getArray(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        return (InputStream) getObject(columnIndex);
    }


    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        return (BigDecimal) getObject(columnIndex);
    }


    public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
        return (BigDecimal) getObject(columnIndex);
    }


    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        return (InputStream) getObject(columnIndex);
    }


    public Blob getBlob(int columnIndex) throws SQLException {
        return (Blob) getObject(columnIndex);
    }


    public boolean getBoolean(int columnIndex) throws SQLException {
        return ((Boolean) getObject(columnIndex)).booleanValue();
    }


    public byte getByte(int columnIndex) throws SQLException {
        return ((Byte) getObject(columnIndex)).byteValue();
    }


    public byte[] getBytes(int columnIndex) throws SQLException {
        return (byte[]) getObject(columnIndex);
    }


    public Reader getCharacterStream(int columnIndex) throws SQLException {
        return (Reader) getObject(columnIndex);
    }


    public Clob getClob(int columnIndex) throws SQLException {
        return (Clob) getObject(columnIndex);
    }


    public Reader getNCharacterStream(int columnIndex) throws SQLException {
        return (Reader) getObject(columnIndex);
    }


    public NClob getNClob(int columnIndex) throws SQLException {
        return (NClob) getObject(columnIndex);
    }


    public String getNString(int columnIndex) throws SQLException {
        return (String) getObject(columnIndex);
    }


    public URL getURL(int columnIndex) throws SQLException {
        return (URL) getObject(columnIndex);
    }


    public InputStream getUnicodeStream(int columnIndex) throws SQLException {
        return (InputStream) getObject(columnIndex);
    }


    public short getShort(int columnIndex) throws SQLException {
        return ((Short) getObject(columnIndex)).shortValue();
    }


    public String getString(int columnIndex) throws SQLException {
        return (String) getObject(columnIndex);
    }


    public Time getTime(int columnIndex) throws SQLException {
        return (Time) getObject(columnIndex);
    }


    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        return (Time) getObject(columnIndex);
    }


    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        return (Timestamp) getObject(columnIndex);
    }


    public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
        return (Timestamp) getObject(columnIndex);
    }


    public float getFloat(int columnIndex) throws SQLException {
        return ((Float) getObject(columnIndex)).floatValue();
    }


    public Date getDate(int columnIndex) throws SQLException {
        return (Date) getObject(columnIndex);
    }


    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
        return (Date) getObject(columnIndex);
    }


    public double getDouble(int columnIndex) throws SQLException {
        return ((Double) getObject(columnIndex)).doubleValue();
    }


    public int getInt(int columnIndex) throws SQLException {
        return ((Integer) getObject(columnIndex)).intValue();
    }


    public long getLong(int columnIndex) throws SQLException {
        return ((Long) getObject(columnIndex)).longValue();
    }

    public <T> T getObject(int columnIndex, Class<T> arg1) throws SQLException {
        return (T) getObject(columnIndex);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\AbstractResultSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */