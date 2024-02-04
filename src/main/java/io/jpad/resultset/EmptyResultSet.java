package io.jpad.resultset;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.timestored.sqldash.chart.BaseResultSet;

import java.sql.ResultSetMetaData;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EmptyResultSet
        extends BaseResultSet
        implements KeyedResultSet {
    private final SimpleResultSetMetaData rsmd;
    private final String caption;
    private final int keyColumns;

    EmptyResultSet(String colName) {

        this("", 0, colName);
    }

    EmptyResultSet(String caption, int keyColumns, String... colNames) {

        this.caption = Preconditions.checkNotNull(caption);

        Preconditions.checkArgument((keyColumns >= 0 && keyColumns < colNames.length));

        this.keyColumns = keyColumns;

        List<Integer> types = Arrays.stream(colNames).map(c -> 12).collect(Collectors.toCollection(() -> Lists.newArrayListWithCapacity(colNames.length)));

        this.rsmd = new SimpleResultSetMetaData(Arrays.asList(colNames), types);
    }

    public ResultSetMetaData getMetaData() {
        return this.rsmd;
    }

    public int getNumberOfKeyColumns() {
        return this.keyColumns;
    }

    public String getCaption() {
        return this.caption;
    }

    public void afterLast() {
    }

    public boolean absolute(int row) {
        return false;
    }

    public void beforeFirst() {
    }

    public int findColumn(String columnLabel) {

        return 0;
    }

    public boolean first() {
        return false;
    }

    public Object getObject(int columnIndex) {

        return null;
    }

    public Object getObject(int columnIndex, Map<String, Class<?>> map) {
        return null;
    }

    public int getRow() {
        return 0;
    }

    public boolean isAfterLast() {
        return false;
    }

    public boolean isBeforeFirst() {
        return false;
    }

    public boolean isFirst() {
        return false;
    }

    public boolean isLast() {
        return false;
    }

    public boolean last() {
        return false;
    }

    public boolean next() {
        return false;
    }

    public boolean previous() {

        return false;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\resultset\EmptyResultSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */