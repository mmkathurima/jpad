package com.timestored.sqldash.chart;

import java.lang.reflect.Array;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

public class SimpleResultSet
        extends BaseResultSet {
    private final String[] colNames;
    private final Object[] colValues;
    private final ResultSetMetaData resultSetMetaData;
    private int idx;

    public SimpleResultSet(String[] colNames) {
        this(colNames, new Object[0]);
    }

    public SimpleResultSet(String[] colNames, Object[] colValues) {
        this.colNames = colNames;
        this.colValues = colValues;

        int[] types;
        if (colValues.length == 0) {
            types = new int[colNames.length];
            Arrays.fill(types, 12);
        } else {
            types = getTypes(colValues);
        }

        this.resultSetMetaData = new SimpleResultSetMetaData(colNames, types);
    }

    private static int getType(Object o) {
        if (o instanceof String[])
            return 12;
        if (o instanceof boolean[])
            return -7;
        if (o instanceof short[])
            return 5;
        if (o instanceof int[])
            return 4;
        if (o instanceof long[])
            return -5;
        if (o instanceof float[])
            return 7;
        if (o instanceof double[])
            return 8;
        if (o instanceof java.sql.Date[])
            return 91;
        if (o instanceof java.sql.Time[])
            return 92;
        if (o instanceof java.sql.Timestamp[]) {
            return 93;
        }
        return 12;
    }

    private static int[] getTypes(Object[] colValues) {
        int[] r = new int[colValues.length];
        for (int i = 0; i < r.length; i++) {
            r[i] = getType(colValues[i]);
        }
        return r;
    }

    public boolean absolute(int row) {
        this.idx = row - 1;
        return true;
    }

    public void beforeFirst() {
        this.idx = -1;
    }

    public void afterLast() {
        if (this.colValues.length > 0) {
            this.idx = Array.getLength(this.colValues[0]) + 1;
        }
    }

    public int findColumn(String columnLabel) throws SQLException {
        for (int i = 0; i < this.colNames.length; i++) {
            if (this.colNames[i].equals(columnLabel)) {
                return i + 1;
            }
        }
        throw new SQLException();
    }

    public boolean first() {
        this.idx = 0;
        return true;
    }

    public ResultSetMetaData getMetaData() {
        return this.resultSetMetaData;
    }

    public Object getObject(int columnIndex) {
        return Array.get(this.colValues[columnIndex - 1], this.idx);
    }

    public Object getObject(int columnIndex, Map<String, Class<?>> map) {
        throw new UnsupportedOperationException();
    }

    public int getRow() {
        return this.idx;
    }

    public boolean isAfterLast() {
        return (this.idx >= this.colNames.length);
    }

    public boolean isBeforeFirst() {
        return (this.idx < 0);
    }

    public boolean isFirst() {
        return (this.idx == 0);
    }

    public boolean isLast() {
        return (this.idx == this.colNames.length - 1);
    }

    public boolean last() {
        if (this.colValues.length > 0) {
            this.idx = Array.getLength(this.colValues[0]);
            return true;
        }
        return false;
    }

    public boolean next() {
        this.idx++;
        return (this.colValues.length > 0 && this.idx < Array.getLength(this.colValues[0]));
    }

    public boolean previous() {
        this.idx--;
        return (this.idx >= 0);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\SimpleResultSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */