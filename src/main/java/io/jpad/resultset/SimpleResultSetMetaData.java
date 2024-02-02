package io.jpad.resultset;


import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;


class SimpleResultSetMetaData
        implements ResultSetMetaData {
    private final List<String> colNames;
    private final List<Integer> colTypes;


    public SimpleResultSetMetaData(String colName, int colType) {

        this(Lists.newArrayList(colName), Lists.newArrayList(Integer.valueOf(colType)));

    }


    public SimpleResultSetMetaData(List<String> colNames, List<Integer> colTypes) {

        Preconditions.checkArgument((colNames.size() >= 0));

        Preconditions.checkArgument((colNames.size() == colTypes.size()));

        this.colNames = colNames;

        this.colTypes = colTypes;

    }


    public int getColumnCount() throws SQLException {

        return this.colNames.size();

    }


    public String getColumnName(int column) throws SQLException {

        return this.colNames.get(column - 1);

    }


    public int getColumnType(int column) throws SQLException {

        return this.colTypes.get(column - 1).intValue();

    }


    public String getColumnClassName(int column) throws SQLException {

        throw new UnsupportedOperationException();

    }


    public String getColumnTypeName(int column) throws SQLException {

        throw new UnsupportedOperationException();

    }


    public <T> T unwrap(Class<T> iface) throws SQLException {

        throw new UnsupportedOperationException();

    }


    public boolean isWrapperFor(Class<?> iface) throws SQLException {

        throw new UnsupportedOperationException();

    }


    public boolean isAutoIncrement(int column) throws SQLException {

        return false;

    }


    public boolean isCaseSensitive(int column) throws SQLException {

        return true;

    }


    public boolean isSearchable(int column) throws SQLException {

        throw new UnsupportedOperationException();

    }


    public boolean isCurrency(int column) throws SQLException {

        throw new UnsupportedOperationException();

    }


    public int isNullable(int column) throws SQLException {

        throw new UnsupportedOperationException();

    }


    public boolean isSigned(int column) throws SQLException {

        throw new UnsupportedOperationException();

    }


    public int getColumnDisplaySize(int column) throws SQLException {

        throw new UnsupportedOperationException();

    }


    public String getColumnLabel(int column) throws SQLException {

        return getColumnName(column);

    }


    public String getSchemaName(int column) throws SQLException {

        return "";

    }


    public int getPrecision(int column) throws SQLException {

        throw new UnsupportedOperationException();

    }


    public int getScale(int column) throws SQLException {

        throw new UnsupportedOperationException();

    }


    public String getTableName(int column) throws SQLException {

        return "";

    }

    public String getCatalogName(int column) throws SQLException {

        return "";

    }


    public boolean isReadOnly(int column) throws SQLException {
        return true;
    }

    public boolean isWritable(int column) throws SQLException {

        return false;

    }

    public boolean isDefinitelyWritable(int column) throws SQLException {

        return false;

    }

}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\resultset\SimpleResultSetMetaData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */