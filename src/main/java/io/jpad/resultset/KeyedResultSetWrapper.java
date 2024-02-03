package io.jpad.resultset;

import com.google.common.base.Preconditions;
import com.timestored.sqldash.chart.BaseResultSet;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

public class KeyedResultSetWrapper
        extends BaseResultSet
        implements KeyedResultSet {
    private final ResultSet rs;
    private final String caption;
    private final int numberOfKeyColumns;

    public KeyedResultSetWrapper(@NotNull ResultSet rs, int numberOfKeyColumns, @NotNull String caption) {

        this.rs = Preconditions.checkNotNull(rs);

        this.caption = Preconditions.checkNotNull(caption);

        try {

            Preconditions.checkArgument((numberOfKeyColumns >= 0 && numberOfKeyColumns < rs.getMetaData().getColumnCount()));
        } catch (SQLException e) {

            throw new IllegalStateException();
        }

        this.numberOfKeyColumns = numberOfKeyColumns;
    }

    public int getNumberOfKeyColumns() {
        return this.numberOfKeyColumns;
    }

    public String getCaption() {

        return this.caption;
    }

    public boolean absolute(int row) throws SQLException {
        return this.rs.absolute(row);
    }

    public void afterLast() throws SQLException {
        this.rs.afterLast();
    }

    public void beforeFirst() throws SQLException {
        this.rs.beforeFirst();
    }

    public int findColumn(String columnLabel) throws SQLException {
        return this.rs.findColumn(columnLabel);
    }

    public boolean first() throws SQLException {
        return this.rs.first();
    }

    public ResultSetMetaData getMetaData() throws SQLException {
        return this.rs.getMetaData();
    }

    public Object getObject(int columnIndex) throws SQLException {

        return this.rs.getObject(columnIndex);
    }

    public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {

        return this.rs.getObject(columnIndex, map);
    }

    public int getRow() throws SQLException {
        return this.rs.getRow();
    }

    public boolean isAfterLast() throws SQLException {
        return this.rs.isAfterLast();
    }

    public boolean isBeforeFirst() throws SQLException {
        return this.rs.isBeforeFirst();
    }

    public boolean isFirst() throws SQLException {
        return this.rs.isFirst();
    }

    public boolean isLast() throws SQLException {
        return this.rs.isLast();
    }

    public boolean last() throws SQLException {
        return this.rs.last();
    }

    public boolean next() throws SQLException {
        return this.rs.next();
    }

    public boolean previous() throws SQLException {

        return this.rs.previous();
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\resultset\KeyedResultSetWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */