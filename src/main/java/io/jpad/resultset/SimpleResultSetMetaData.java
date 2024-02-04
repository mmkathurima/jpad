package io.jpad.resultset;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.sql.ResultSetMetaData;
import java.util.List;

class SimpleResultSetMetaData
        implements ResultSetMetaData {
    private final List<String> colNames;
    private final List<Integer> colTypes;

    public SimpleResultSetMetaData(String colName, int colType) {

        this(Lists.newArrayList(colName), Lists.newArrayList(colType));
    }

    public SimpleResultSetMetaData(List<String> colNames, List<Integer> colTypes) {

        Preconditions.checkArgument((colNames.size() >= 0));

        Preconditions.checkArgument((colNames.size() == colTypes.size()));

        this.colNames = colNames;

        this.colTypes = colTypes;
    }

    public int getColumnCount() {

        return this.colNames.size();
    }

    public String getColumnName(int column) {

        return this.colNames.get(column - 1);
    }

    public int getColumnType(int column) {

        return this.colTypes.get(column - 1);
    }

    public String getColumnClassName(int column) {

        throw new UnsupportedOperationException();
    }

    public String getColumnTypeName(int column) {

        throw new UnsupportedOperationException();
    }

    public <T> T unwrap(Class<T> iface) {

        throw new UnsupportedOperationException();
    }

    public boolean isWrapperFor(Class<?> iface) {

        throw new UnsupportedOperationException();
    }

    public boolean isAutoIncrement(int column) {

        return false;
    }

    public boolean isCaseSensitive(int column) {

        return true;
    }

    public boolean isSearchable(int column) {

        throw new UnsupportedOperationException();
    }

    public boolean isCurrency(int column) {

        throw new UnsupportedOperationException();
    }

    public int isNullable(int column) {

        throw new UnsupportedOperationException();
    }

    public boolean isSigned(int column) {

        throw new UnsupportedOperationException();
    }

    public int getColumnDisplaySize(int column) {

        throw new UnsupportedOperationException();
    }

    public String getColumnLabel(int column) {

        return this.getColumnName(column);
    }

    public String getSchemaName(int column) {

        return "";
    }

    public int getPrecision(int column) {

        throw new UnsupportedOperationException();
    }

    public int getScale(int column) {

        throw new UnsupportedOperationException();
    }

    public String getTableName(int column) {

        return "";
    }

    public String getCatalogName(int column) {

        return "";
    }

    public boolean isReadOnly(int column) {
        return true;
    }

    public boolean isWritable(int column) {

        return false;
    }

    public boolean isDefinitelyWritable(int column) {

        return false;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\resultset\SimpleResultSetMetaData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */