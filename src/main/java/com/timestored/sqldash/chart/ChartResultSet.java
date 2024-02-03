package com.timestored.sqldash.chart;

import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.jfree.data.time.Day;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.RegularTimePeriod;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

class ChartResultSet {
    private final List<NumericCol> numericColumns;
    private final List<StringyCol> stringyColumns;
    private final TimeCol timeCol;
    private final List<String> rowLabels;
    private final String rowTitle;
    private final String colTitle;

    ChartResultSet(List<NumericCol> numericColumns, List<StringyCol> stringyColumns, List<String> rowTitles, TimeCol timeCol, String rowTitle, String colTitle) {
        this.numericColumns = Collections.unmodifiableList(Preconditions.checkNotNull(numericColumns));
        this.stringyColumns = Collections.unmodifiableList(Preconditions.checkNotNull(stringyColumns));
        this.rowLabels = Collections.unmodifiableList(Preconditions.checkNotNull(rowTitles));
        this.rowTitle = Preconditions.checkNotNull(rowTitle);
        this.colTitle = Preconditions.checkNotNull(colTitle);
        this.timeCol = timeCol;

        int rowCount = rowTitles.size();
        Preconditions.checkArgument((timeCol == null || (timeCol.getDates()).length == rowCount));
        for (NumericCol c : this.numericColumns) {
            Preconditions.checkArgument(((c.getDoubles()).length == rowCount));
        }
        for (StringyCol c : this.stringyColumns) {
            Preconditions.checkArgument((c.getVals().size() == rowCount));
        }
    }

    public static ChartResultSet getInstance(ResultSet rs) throws SQLException {
        return ChartResultSetBuilder.getChartResultSet(rs);
    }

    public static ChartResultSet getTransposedInstance(ResultSet rs) throws SQLException {
        return ChartResultSetBuilder.getChartResultSet(rs);
    }

    public NumericCol getNumericalColumn(String columnLabel) {
        for (NumericCol nc : this.numericColumns) {
            if (nc.getLabel().equalsIgnoreCase(columnLabel)) {
                return nc;
            }
        }
        return null;
    }

    public int getRowCount() {
        return this.rowLabels.size();
    }

    public TimeCol getTimeCol() {
        return this.timeCol;
    }

    public List<NumericCol> getNumericColumns() {
        return this.numericColumns;
    }

    public List<StringyCol> getStringyColumns() {
        return this.stringyColumns;
    }

    public String getRowLabel(int row) {
        return this.rowLabels.get(row);
    }

    private List<String> getColumnNames(List<? extends Col> cols) {
        List<String> n = Lists.newArrayList();
        for (Col c : cols) {
            n.add(c.getLabel());
        }
        return n;
    }

    public String toString() {
        MoreObjects.ToStringHelper tsh = MoreObjects.toStringHelper(this).add("numericColumns", Joiner.on(',').join(this.getColumnNames(this.numericColumns)));

        if (this.timeCol == null) {
            tsh.add("timeCol", "no time col");
        } else {
            tsh.add("timeCol", this.timeCol.getLabel());
        }
        if (this.numericColumns.size() > 0) {
            tsh.add("numeric rows", (this.numericColumns.get(0).getDoubles()).length);
        }
        return tsh.toString();
    }

    public String getRowTitle() {
        return this.rowTitle;
    }

    public String getColTitle() {
        return this.colTitle;
    }

    public StringyCol getRowLabels() {
        List<Object> objList = new ArrayList(this.rowLabels.size());
        for (String s : this.rowLabels) {
            objList.add(s);
        }
        return new StringyCol(this.rowTitle, 12, objList);
    }

    public abstract static class Col {
        private final int type;

        private final String name;

        Col(String name, int type) {
            this.name = name;
            this.type = type;
        }

        public String getLabel() {
            return this.name;
        }

        public int getType() {
            return this.type;
        }
    }

    public static class NumericCol
            extends Col {
        private final double[] vals;

        NumericCol(String name, int type, double[] vals) {
            super(name, type);
            this.vals = vals;
        }

        public double[] getDoubles() {
            return this.vals;
        }

        public String toString() {
            return "NumericCol [vals=" + Arrays.toString(this.vals) + "]";
        }
    }

    public static class StringyCol
            extends Col {
        protected final List<Object> vals;

        StringyCol(String name, int type, List<Object> vals) {
            super(name, type);
            this.vals = vals;
        }

        public List<Object> getVals() {
            return this.vals;
        }

        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = 31 * result + ((this.vals == null) ? 0 : this.vals.hashCode());
            return result;
        }

        public boolean equals(Object obj) {
            if (obj != null && this.getClass() == obj.getClass()) {
                StringyCol that = (StringyCol) obj;
                if (this.vals.size() != that.vals.size()) {
                    return false;
                }
                for (int i = 0; i < this.vals.size(); i++) {
                    if (!this.vals.get(i).toString().equals(that.vals.get(i).toString())) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }

        public String toString() {
            return "StringyCol [vals=" + this.vals + "]";
        }
    }

    public static class TimeCol
            extends StringyCol {
        private volatile Date[] dates;

        TimeCol(String name, int type, List<Object> vals) {
            super(name, type, vals);
        }

        public Date[] getDates() {
            if (this.dates == null) {
                synchronized (this) {
                    if (this.dates == null) {
                        this.dates = this.convertToDate(this.vals);
                    }
                }
            }
            return this.dates;
        }

        public RegularTimePeriod[] getRegularTimePeriods() {
            return this.convertToJFreeTime(this.vals);
        }

        private RegularTimePeriod[] convertToJFreeTime(List<Object> timeObjects) {
            int rowCount = timeObjects.size();
            RegularTimePeriod[] res = new RegularTimePeriod[rowCount];
            int unconvertedRows = 0;

            for (int row = 0; row < rowCount; row++) {
                Day day = new Day();
                Object timeObject = timeObjects.get(row);
                RegularTimePeriod timePeriod = null;
                if (timeObject instanceof Time) {
                    Time t = (Time) timeObject;
                    Millisecond millisecond = new Millisecond(new Date(t.getTime()));
                } else if (timeObject instanceof Timestamp) {
                    Millisecond millisecond = new Millisecond((Timestamp) timeObject);
                } else if (timeObject instanceof Date) {
                    day = new Day((Date) timeObject);
                } else {
                    unconvertedRows++;
                }
                res[row] = day;
            }
            if (rowCount > 0 && unconvertedRows == rowCount) {
                throw new IllegalArgumentException("Could not convert any rows of the time column");
            }
            return res;
        }

        private Date[] convertToDate(List<Object> timeObjects) {
            int rowCount = timeObjects.size();
            Date[] res = new Date[rowCount];
            int nullRows = 0;
            for (int row = 0; row < rowCount; row++) {
                Object timeObject = timeObjects.get(row);
                Date timePeriod = null;
                if (timeObject instanceof Date) {
                    timePeriod = (Date) timeObject;
                } else if (timeObject instanceof Timestamp) {
                    Timestamp t = (Timestamp) timeObject;
                    timePeriod = new Date(t.getTime());
                } else {

                    nullRows++;
                }
                res[row] = timePeriod;
            }

            if (rowCount > 0 && nullRows == rowCount) {
                throw new IllegalArgumentException("no known time row found");
            }
            return res;
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\ChartResultSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */