package com.timestored.sqldash.chart;

import com.google.common.base.Preconditions;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;


class SimpleResultSetMetaData
        implements ResultSetMetaData {
    private final String[] colNames;
    private final int[] colTypes;

    public SimpleResultSetMetaData(String[] colNames, int[] colTypes) {
        this.colNames = Preconditions.checkNotNull(colNames);
        this.colTypes = Preconditions.checkNotNull(colTypes);
        Preconditions.checkArgument((colNames.length == colTypes.length), "length of names and types match");
    }

    public int getColumnCount() throws SQLException {
        return this.colNames.length;
    }


    public int getColumnType(int column) throws SQLException {
        if (column > this.colNames.length) {
            throw new IllegalArgumentException("column outside data range");
        }

        return this.colTypes[column - 1];
    }

    public String getColumnLabel(int column) throws SQLException {
        return getColumnName(column);
    }

    public String getColumnName(int column) throws SQLException {
        return this.colNames[column - 1];
    }

    public String getColumnClassName(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public String getCatalogName(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public int getColumnDisplaySize(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }


    public String getColumnTypeName(int column) throws SQLException {
        return null;
    }

    public int getPrecision(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public int getScale(int column) throws SQLException {
        return 0;
    }

    public String getSchemaName(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public String getTableName(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public boolean isAutoIncrement(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public boolean isCaseSensitive(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public boolean isCurrency(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public boolean isDefinitelyWritable(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public int isNullable(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public boolean isReadOnly(int column) throws SQLException {
        return true;
    }

    public boolean isSearchable(int column) throws SQLException {
        return false;
    }

    public boolean isSigned(int column) throws SQLException {
        return false;
    }

    public boolean isWritable(int column) throws SQLException {
        return false;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\SimpleResultSetMetaData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */