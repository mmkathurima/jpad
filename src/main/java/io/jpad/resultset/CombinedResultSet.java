package io.jpad.resultset;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.timestored.sqldash.chart.BaseResultSet;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

class CombinedResultSet
        extends BaseResultSet
        implements KeyedResultSet {
    private final ResultSet leftRS;
    private final ResultSet rightRS;
    private final int leftColCount;
    private final ResultSetMetaData resultSetMetaData;
    private final String caption;

    public CombinedResultSet(ResultSet leftRS, ResultSet rightRS, String caption) {

        this.leftRS = Preconditions.checkNotNull(leftRS);

        this.rightRS = Preconditions.checkNotNull(rightRS);

        this.caption = Preconditions.checkNotNull(caption);

        try {

            leftRS.last();

            rightRS.last();

            Preconditions.checkArgument((leftRS.getRow() == rightRS.getRow()), "left and risghtRS have same number rows.");

            leftRS.first();

            rightRS.first();

            ResultSetMetaData leftMD = leftRS.getMetaData();

            ResultSetMetaData rightMD = rightRS.getMetaData();

            this.leftColCount = leftMD.getColumnCount();

            int colCount = leftMD.getColumnCount() + rightMD.getColumnCount();

            List<String> colNames = Lists.newArrayListWithExpectedSize(colCount);

            List<Integer> colTypes = Lists.newArrayListWithExpectedSize(colCount);

            int i;

            for (i = 0; i < leftMD.getColumnCount(); i++) {

                colNames.add(leftMD.getColumnName(i + 1));

                colTypes.add(Integer.valueOf(leftMD.getColumnType(i + 1)));
            }

            for (i = 0; i < rightMD.getColumnCount(); i++) {

                colNames.add(rightMD.getColumnName(i + 1));

                colTypes.add(Integer.valueOf(rightMD.getColumnType(i + 1)));
            }

            this.resultSetMetaData = new SimpleResultSetMetaData(colNames, colTypes);
        } catch (SQLException e) {

            throw new IllegalArgumentException(e);
        }
    }

    public int getNumberOfKeyColumns() {

        return this.leftColCount;
    }

    public String getCaption() {

        return this.caption;
    }

    public boolean absolute(int row) throws SQLException {

        this.leftRS.absolute(row);

        return this.rightRS.absolute(row);
    }

    public void afterLast() throws SQLException {

        this.leftRS.afterLast();

        this.rightRS.afterLast();
    }

    public void beforeFirst() throws SQLException {

        this.leftRS.beforeFirst();

        this.rightRS.beforeFirst();
    }

    public int findColumn(String columnLabel) throws SQLException {

        try {

            return this.leftRS.findColumn(columnLabel);
        } catch (SQLException sqlE) {

            return this.rightRS.findColumn(columnLabel);
        }
    }

    public boolean first() throws SQLException {

        this.leftRS.first();

        return this.rightRS.first();
    }

    public ResultSetMetaData getMetaData() throws SQLException {

        return this.resultSetMetaData;
    }

    public Object getObject(int columnIndex) throws SQLException {

        if (columnIndex <= this.leftColCount) {

            return this.leftRS.getObject(columnIndex);
        }

        return this.rightRS.getObject(columnIndex - this.leftColCount);
    }

    public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {

        return this.getObject(columnIndex);
    }

    public int getRow() throws SQLException {

        return this.leftRS.getRow();
    }

    public boolean isAfterLast() throws SQLException {

        return this.leftRS.isAfterLast();
    }

    public boolean isBeforeFirst() throws SQLException {

        return this.leftRS.isBeforeFirst();
    }

    public boolean isFirst() throws SQLException {

        return this.leftRS.isFirst();
    }

    public boolean isLast() throws SQLException {

        return this.leftRS.isLast();
    }

    public boolean last() throws SQLException {

        this.leftRS.last();

        return this.rightRS.last();
    }

    public boolean next() throws SQLException {

        this.leftRS.next();

        return this.rightRS.next();
    }

    public boolean previous() throws SQLException {

        this.leftRS.previous();

        return this.rightRS.previous();
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\resultset\CombinedResultSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */