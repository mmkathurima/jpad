package com.timestored.sqldash.chart;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Map;

public abstract class BaseResultSet
        extends AbstractResultSet {
    public <T> T getObject(String arg0, Class<T> arg1) {
        throw new UnsupportedOperationException();
    }

    public boolean isWrapperFor(Class<?> arg0) {
        throw new UnsupportedOperationException();
    }

    public Object getObject(String columnLabel, Map<String, Class<?>> map) {
        throw new UnsupportedOperationException();
    }

    public <T> T unwrap(Class<T> arg0) {
        return null;
    }

    public void cancelRowUpdates() {
        throw new UnsupportedOperationException();
    }

    public void clearWarnings() {
        throw new UnsupportedOperationException();
    }

    public void insertRow() {
        throw new UnsupportedOperationException();
    }

    public Ref getRef(int columnIndex) {
        throw new UnsupportedOperationException();
    }

    public void close() {
        throw new UnsupportedOperationException();
    }

    public void deleteRow() {
        throw new UnsupportedOperationException();
    }

    public String getCursorName() {
        throw new UnsupportedOperationException();
    }

    public int getFetchDirection() {
        throw new UnsupportedOperationException();
    }

    public void setFetchDirection(int direction) {
        throw new UnsupportedOperationException();
    }

    public int getFetchSize() {
        throw new UnsupportedOperationException();
    }

    public void setFetchSize(int rows) {
        throw new UnsupportedOperationException();
    }

    public int getHoldability() {
        throw new UnsupportedOperationException();
    }

    public RowId getRowId(int columnIndex) {
        throw new UnsupportedOperationException();
    }

    public SQLXML getSQLXML(int columnIndex) {
        throw new UnsupportedOperationException();
    }

    public Statement getStatement() {
        throw new UnsupportedOperationException();
    }

    public int getType() {
        throw new UnsupportedOperationException();
    }

    public SQLWarning getWarnings() {
        return null;
    }

    public int getConcurrency() {
        throw new UnsupportedOperationException();
    }

    public boolean isClosed() {
        throw new UnsupportedOperationException();
    }

    public void moveToCurrentRow() {
        throw new UnsupportedOperationException();
    }

    public void moveToInsertRow() {
        throw new UnsupportedOperationException();
    }

    public void refreshRow() {
        throw new UnsupportedOperationException();
    }

    public boolean relative(int rows) {
        throw new UnsupportedOperationException();
    }

    public boolean rowDeleted() {
        throw new UnsupportedOperationException();
    }

    public boolean rowInserted() {
        throw new UnsupportedOperationException();
    }

    public boolean rowUpdated() {
        throw new UnsupportedOperationException();
    }

    public void updateAsciiStream(int columnIndex, InputStream x) {
        throw new UnsupportedOperationException();
    }

    public void updateAsciiStream(int columnIndex, InputStream x, int length) {
        throw new UnsupportedOperationException();
    }

    public void updateAsciiStream(int columnIndex, InputStream x, long length) {
        throw new UnsupportedOperationException();
    }

    public void updateBigDecimal(int columnIndex, BigDecimal x) {
        throw new UnsupportedOperationException();
    }

    public void updateBinaryStream(int columnIndex, InputStream x) {
        throw new UnsupportedOperationException();
    }

    public void updateBinaryStream(int columnIndex, InputStream x, int length) {
        throw new UnsupportedOperationException();
    }

    public void updateBinaryStream(int columnIndex, InputStream x, long length) {
        throw new UnsupportedOperationException();
    }

    public void updateBlob(int columnIndex, Blob x) {
        throw new UnsupportedOperationException();
    }

    public void updateBlob(int columnIndex, InputStream inputStream) {
        throw new UnsupportedOperationException();
    }

    public void updateBlob(int columnIndex, InputStream inputStream, long length) {
        throw new UnsupportedOperationException();
    }

    public void updateBoolean(int columnIndex, boolean x) {
        throw new UnsupportedOperationException();
    }

    public void updateByte(int columnIndex, byte x) {
        throw new UnsupportedOperationException();
    }

    public void updateBytes(int columnIndex, byte[] x) {
        throw new UnsupportedOperationException();
    }

    public void updateCharacterStream(int columnIndex, Reader x) {
        throw new UnsupportedOperationException();
    }

    public void updateCharacterStream(int columnIndex, Reader x, int length) {
        throw new UnsupportedOperationException();
    }

    public void updateCharacterStream(int columnIndex, Reader x, long length) {
        throw new UnsupportedOperationException();
    }

    public void updateClob(int columnIndex, Clob x) {
        throw new UnsupportedOperationException();
    }

    public void updateClob(int columnIndex, Reader reader) {
        throw new UnsupportedOperationException();
    }

    public void updateClob(int columnIndex, Reader reader, long length) {
        throw new UnsupportedOperationException();
    }

    public void updateDate(int columnIndex, Date x) {
        throw new UnsupportedOperationException();
    }

    public void updateDouble(int columnIndex, double x) {
        throw new UnsupportedOperationException();
    }

    public void updateFloat(int columnIndex, float x) {
        throw new UnsupportedOperationException();
    }

    public void updateInt(int columnIndex, int x) {
        throw new UnsupportedOperationException();
    }

    public void updateNCharacterStream(int columnIndex, Reader x) {
        throw new UnsupportedOperationException();
    }

    public void updateNCharacterStream(int columnIndex, Reader x, long length) {
        throw new UnsupportedOperationException();
    }

    public void updateNClob(int columnIndex, NClob nClob) {
        throw new UnsupportedOperationException();
    }

    public void updateNClob(int columnIndex, Reader reader) {
        throw new UnsupportedOperationException();
    }

    public void updateNClob(int columnIndex, Reader reader, long length) {
        throw new UnsupportedOperationException();
    }

    public void updateNString(int columnIndex, String nString) {
        throw new UnsupportedOperationException();
    }

    public void updateNull(int columnIndex) {
        throw new UnsupportedOperationException();
    }

    public void updateObject(int columnIndex, Object x) {
        throw new UnsupportedOperationException();
    }

    public void updateObject(int columnIndex, Object x, int scaleOrLength) {
        throw new UnsupportedOperationException();
    }

    public void updateRef(int columnIndex, Ref x) {
        throw new UnsupportedOperationException();
    }

    public void updateRow() {
        throw new UnsupportedOperationException();
    }

    public void updateRowId(int columnIndex, RowId x) {
        throw new UnsupportedOperationException();
    }

    public void updateSQLXML(int columnIndex, SQLXML xmlObject) {
        throw new UnsupportedOperationException();
    }

    public void updateShort(int columnIndex, short x) {
        throw new UnsupportedOperationException();
    }

    public void updateString(int columnIndex, String x) {
        throw new UnsupportedOperationException();
    }

    public void updateTime(int columnIndex, Time x) {
        throw new UnsupportedOperationException();
    }

    public void updateTimestamp(int columnIndex, Timestamp x) {
        throw new UnsupportedOperationException();
    }

    public boolean wasNull() {
        throw new UnsupportedOperationException();
    }

    public void updateLong(int columnIndex, long x) {
        throw new UnsupportedOperationException();
    }

    public void updateArray(int columnIndex, Array x) {
        throw new UnsupportedOperationException();
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\BaseResultSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */