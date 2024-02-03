package io.jpad.resultset;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.timestored.sqldash.chart.BaseResultSet;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

        List<Integer> types = Lists.newArrayListWithCapacity(colNames.length);

        for (String c : colNames) {

            types.add(Integer.valueOf(12));
        }

        this.rsmd = new SimpleResultSetMetaData(Arrays.asList(colNames), types);
    }

    public ResultSetMetaData getMetaData() throws SQLException {
        return this.rsmd;
    }

    public int getNumberOfKeyColumns() {
        return this.keyColumns;
    }

    public String getCaption() {
        return this.caption;
    }

    public void afterLast() throws SQLException {
    }

    public boolean absolute(int row) throws SQLException {

        return false;
    }

    public void beforeFirst() throws SQLException {
    }

    public int findColumn(String columnLabel) throws SQLException {

        return 0;
    }

    public boolean first() throws SQLException {
        return false;
    }

    public Object getObject(int columnIndex) throws SQLException {

        return null;
    }

    public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
        return null;
    }

    public int getRow() throws SQLException {
        return 0;
    }

    public boolean isAfterLast() throws SQLException {
        return false;
    }

    public boolean isBeforeFirst() throws SQLException {
        return false;
    }

    public boolean isFirst() throws SQLException {
        return false;
    }

    public boolean isLast() throws SQLException {
        return false;
    }

    public boolean last() throws SQLException {
        return false;
    }

    public boolean next() throws SQLException {
        return false;
    }

    public boolean previous() throws SQLException {

        return false;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\resultset\EmptyResultSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */