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
        return this.getAsciiStream(this.findColumn(columnLabel));
    }

    public Array getArray(String columnLabel) throws SQLException {
        return this.getArray(this.findColumn(columnLabel));
    }

    public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
        return this.getBigDecimal(this.findColumn(columnLabel));
    }

    public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
        return this.getBigDecimal(this.findColumn(columnLabel));
    }

    public InputStream getBinaryStream(String columnLabel) throws SQLException {
        return this.getBinaryStream(this.findColumn(columnLabel));
    }

    public Blob getBlob(String columnLabel) throws SQLException {
        return this.getBlob(this.findColumn(columnLabel));
    }

    public byte[] getBytes(String columnLabel) throws SQLException {
        return this.getBytes(this.findColumn(columnLabel));
    }

    public Reader getCharacterStream(String columnLabel) throws SQLException {
        return this.getCharacterStream(this.findColumn(columnLabel));
    }

    public Clob getClob(String columnLabel) throws SQLException {
        return this.getClob(this.findColumn(columnLabel));
    }

    public Date getDate(String columnLabel) throws SQLException {
        return this.getDate(this.findColumn(columnLabel));
    }

    public Date getDate(String columnLabel, Calendar cal) throws SQLException {
        return this.getDate(this.findColumn(columnLabel));
    }

    public double getDouble(String columnLabel) throws SQLException {
        return this.getDouble(this.findColumn(columnLabel));
    }

    public float getFloat(String columnLabel) throws SQLException {
        return this.getFloat(this.findColumn(columnLabel));
    }

    public int getInt(String columnLabel) throws SQLException {
        return this.getInt(this.findColumn(columnLabel));
    }

    public Reader getNCharacterStream(String columnLabel) throws SQLException {
        return this.getNCharacterStream(this.findColumn(columnLabel));
    }

    public NClob getNClob(String columnLabel) throws SQLException {
        return this.getNClob(this.findColumn(columnLabel));
    }

    public String getNString(String columnLabel) throws SQLException {
        return this.getNString(this.findColumn(columnLabel));
    }

    public Object getObject(String columnLabel) throws SQLException {
        return this.getObject(this.findColumn(columnLabel));
    }

    public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
        return this.getObject(this.findColumn(columnLabel));
    }

    public Ref getRef(String columnLabel) throws SQLException {
        return this.getRef(this.findColumn(columnLabel));
    }

    public RowId getRowId(String columnLabel) throws SQLException {
        return this.getRowId(this.findColumn(columnLabel));
    }

    public SQLXML getSQLXML(String columnLabel) throws SQLException {
        return this.getSQLXML(this.findColumn(columnLabel));
    }

    public short getShort(String columnLabel) throws SQLException {
        return this.getShort(this.findColumn(columnLabel));
    }

    public String getString(String columnLabel) throws SQLException {
        return this.getString(this.findColumn(columnLabel));
    }

    public Time getTime(String columnLabel) throws SQLException {
        return this.getTime(this.findColumn(columnLabel));
    }

    public Time getTime(String columnLabel, Calendar cal) throws SQLException {
        return this.getTime(this.findColumn(columnLabel));
    }

    public Timestamp getTimestamp(String columnLabel) throws SQLException {
        return this.getTimestamp(this.findColumn(columnLabel));
    }

    public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
        return this.getTimestamp(this.findColumn(columnLabel));
    }

    public URL getURL(String columnLabel) throws SQLException {
        return this.getURL(this.findColumn(columnLabel));
    }

    public InputStream getUnicodeStream(String columnLabel) throws SQLException {
        return this.getUnicodeStream(this.findColumn(columnLabel));
    }

    public void updateArray(String columnLabel, Array x) throws SQLException {
        this.updateArray(this.findColumn(columnLabel), x);
    }

    public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
        this.updateAsciiStream(this.findColumn(columnLabel), x);
    }

    public void updateAsciiStream(String columnLabel, InputStream x, int length) throws SQLException {
        this.updateAsciiStream(this.findColumn(columnLabel), x, length);
    }

    public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
        this.updateAsciiStream(this.findColumn(columnLabel), x, length);
    }

    public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
        this.updateBinaryStream(this.findColumn(columnLabel), x);
    }

    public void updateBinaryStream(String columnLabel, InputStream x, int length) throws SQLException {
        this.updateBinaryStream(this.findColumn(columnLabel), x, length);
    }

    public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
        this.updateBinaryStream(this.findColumn(columnLabel), x, length);
    }

    public void updateBlob(String columnLabel, Blob x) throws SQLException {
        this.updateBlob(this.findColumn(columnLabel), x);
    }

    public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
        this.updateBlob(this.findColumn(columnLabel), inputStream, length);
    }

    public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
        this.updateBlob(this.findColumn(columnLabel), inputStream);
    }

    public void updateBoolean(String columnLabel, boolean x) throws SQLException {
        this.updateBoolean(this.findColumn(columnLabel), x);
    }

    public void updateByte(String columnLabel, byte x) throws SQLException {
        this.updateByte(this.findColumn(columnLabel), x);
    }

    public void updateBytes(String columnLabel, byte[] x) throws SQLException {
        this.updateBytes(this.findColumn(columnLabel), x);
    }

    public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
        this.updateCharacterStream(this.findColumn(columnLabel), reader);
    }

    public void updateCharacterStream(String columnLabel, Reader reader, int length) throws SQLException {
        this.updateCharacterStream(this.findColumn(columnLabel), reader, length);
    }

    public void updateClob(String columnLabel, Clob x) throws SQLException {
        this.updateClob(this.findColumn(columnLabel), x);
    }

    public void updateClob(String columnLabel, Reader reader) throws SQLException {
        this.updateClob(this.findColumn(columnLabel), reader);
    }

    public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
        this.updateClob(this.findColumn(columnLabel), reader, length);
    }

    public void updateDate(String columnLabel, Date x) throws SQLException {
        this.updateDate(this.findColumn(columnLabel), x);
    }

    public void updateDouble(String columnLabel, double x) throws SQLException {
        this.updateDouble(this.findColumn(columnLabel), x);
    }

    public void updateFloat(String columnLabel, float x) throws SQLException {
        this.updateFloat(this.findColumn(columnLabel), x);
    }

    public void updateInt(String columnLabel, int x) throws SQLException {
        this.updateInt(this.findColumn(columnLabel), x);
    }

    public void updateLong(String columnLabel, long x) throws SQLException {
        this.updateLong(this.findColumn(columnLabel), x);
    }

    public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
        this.updateNCharacterStream(this.findColumn(columnLabel), reader);
    }

    public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
        this.updateNCharacterStream(this.findColumn(columnLabel), reader, length);
    }

    public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
        this.updateNClob(this.findColumn(columnLabel), nClob);
    }

    public void updateNClob(String columnLabel, Reader reader) throws SQLException {
        this.updateNClob(this.findColumn(columnLabel), reader);
    }

    public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
        this.updateNClob(this.findColumn(columnLabel), reader, length);
    }

    public void updateNString(String columnLabel, String nString) throws SQLException {
        this.updateNString(this.findColumn(columnLabel), nString);
    }

    public void updateNull(String columnLabel) throws SQLException {
        this.updateNull(this.findColumn(columnLabel));
    }

    public void updateObject(String columnLabel, Object x) throws SQLException {
        this.updateObject(this.findColumn(columnLabel), x);
    }

    public void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {
        this.updateObject(this.findColumn(columnLabel), x);
    }

    public void updateRef(String columnLabel, Ref x) throws SQLException {
        this.updateRef(this.findColumn(columnLabel), x);
    }

    public byte getByte(String columnLabel) throws SQLException {
        return this.getByte(this.findColumn(columnLabel));
    }

    public void updateRowId(String columnLabel, RowId x) throws SQLException {
        this.updateRowId(this.findColumn(columnLabel), x);
    }

    public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
        this.updateCharacterStream(this.findColumn(columnLabel), reader, length);
    }

    public long getLong(String columnLabel) throws SQLException {
        return this.getLong(this.findColumn(columnLabel));
    }

    public void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException {
        this.updateBigDecimal(this.findColumn(columnLabel), x);
    }

    public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
        this.updateSQLXML(this.findColumn(columnLabel), xmlObject);
    }

    public void updateShort(String columnLabel, short x) throws SQLException {
        this.updateShort(this.findColumn(columnLabel), x);
    }

    public void updateString(String columnLabel, String x) throws SQLException {
        this.updateString(this.findColumn(columnLabel), x);
    }

    public void updateTime(String columnLabel, Time x) throws SQLException {
        this.updateTime(this.findColumn(columnLabel), x);
    }

    public boolean getBoolean(String columnLabel) throws SQLException {
        return this.getBoolean(this.findColumn(columnLabel));
    }

    public void updateTimestamp(String columnLabel, Timestamp x) throws SQLException {
        this.updateTimestamp(this.findColumn(columnLabel), x);
    }

    public Array getArray(int columnIndex) {
        throw new UnsupportedOperationException();
    }

    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        return (InputStream) this.getObject(columnIndex);
    }

    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        return (BigDecimal) this.getObject(columnIndex);
    }

    public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
        return (BigDecimal) this.getObject(columnIndex);
    }

    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        return (InputStream) this.getObject(columnIndex);
    }

    public Blob getBlob(int columnIndex) throws SQLException {
        return (Blob) this.getObject(columnIndex);
    }

    public boolean getBoolean(int columnIndex) throws SQLException {
        return (Boolean) this.getObject(columnIndex);
    }

    public byte getByte(int columnIndex) throws SQLException {
        return (Byte) this.getObject(columnIndex);
    }

    public byte[] getBytes(int columnIndex) throws SQLException {
        return (byte[]) this.getObject(columnIndex);
    }

    public Reader getCharacterStream(int columnIndex) throws SQLException {
        return (Reader) this.getObject(columnIndex);
    }

    public Clob getClob(int columnIndex) throws SQLException {
        return (Clob) this.getObject(columnIndex);
    }

    public Reader getNCharacterStream(int columnIndex) throws SQLException {
        return (Reader) this.getObject(columnIndex);
    }

    public NClob getNClob(int columnIndex) throws SQLException {
        return (NClob) this.getObject(columnIndex);
    }

    public String getNString(int columnIndex) throws SQLException {
        return (String) this.getObject(columnIndex);
    }

    public URL getURL(int columnIndex) throws SQLException {
        return (URL) this.getObject(columnIndex);
    }

    public InputStream getUnicodeStream(int columnIndex) throws SQLException {
        return (InputStream) this.getObject(columnIndex);
    }

    public short getShort(int columnIndex) throws SQLException {
        return (Short) this.getObject(columnIndex);
    }

    public String getString(int columnIndex) throws SQLException {
        return (String) this.getObject(columnIndex);
    }

    public Time getTime(int columnIndex) throws SQLException {
        return (Time) this.getObject(columnIndex);
    }

    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        return (Time) this.getObject(columnIndex);
    }

    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        return (Timestamp) this.getObject(columnIndex);
    }

    public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
        return (Timestamp) this.getObject(columnIndex);
    }

    public float getFloat(int columnIndex) throws SQLException {
        return (Float) this.getObject(columnIndex);
    }

    public Date getDate(int columnIndex) throws SQLException {
        return (Date) this.getObject(columnIndex);
    }

    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
        return (Date) this.getObject(columnIndex);
    }

    public double getDouble(int columnIndex) throws SQLException {
        return (Double) this.getObject(columnIndex);
    }

    public int getInt(int columnIndex) throws SQLException {
        return (Integer) this.getObject(columnIndex);
    }

    public long getLong(int columnIndex) throws SQLException {
        return (Long) this.getObject(columnIndex);
    }

    public <T> T getObject(int columnIndex, Class<T> arg1) throws SQLException {
        return (T) this.getObject(columnIndex);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\AbstractResultSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */