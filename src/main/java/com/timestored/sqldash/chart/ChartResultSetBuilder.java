package com.timestored.sqldash.chart;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


class ChartResultSetBuilder {
    public static ChartResultSet getChartResultSet(ResultSet rs) throws SQLException {
        rs.beforeFirst();
        ResultSetMetaData md = rs.getMetaData();
        int colCount = md.getColumnCount();

        List<ChartResultSet.NumericCol> numericColumns = new ArrayList<ChartResultSet.NumericCol>();
        List<ChartResultSet.StringyCol> stringyColumns = new ArrayList<ChartResultSet.StringyCol>();
        ChartResultSet.TimeCol timeColumn = null;


        String rowTitle = "";
        List<Integer> stringIdxs = new ArrayList<Integer>();
        for (int c = 1; c <= colCount; ) {
            int ctype = md.getColumnType(c);
            if (!SqlHelper.isNumeric(ctype)) {
                rowTitle = rowTitle + ((c == 1) ? "" : " - ") + md.getColumnName(c);
                stringIdxs.add(Integer.valueOf(c));

                c++;
            } else if (c == colCount) break;
        }
        if (rowTitle.length() == 0) {
            rowTitle = "Row";
        }
        List<String> rowLabels = getRowLabels(rs, stringIdxs);
        int rowCount = rowLabels.size();

        for (int i = 1; i <= colCount; i++) {
            int ctype = md.getColumnType(i);
            if (SqlHelper.isNumeric(ctype)) {
                numericColumns.add(new ChartResultSet.NumericCol(md.getColumnName(i), ctype, getDoubles(i, rs, rowCount)));
            } else {
                if (SqlHelper.isTemporal(ctype) && timeColumn == null) {
                    timeColumn = new ChartResultSet.TimeCol(md.getColumnName(i), ctype, getObjects(i, rs));
                }
                stringyColumns.add(new ChartResultSet.StringyCol(md.getColumnName(i), ctype, getObjects(i, rs)));
            }
        }

        return new ChartResultSet(numericColumns, stringyColumns, rowLabels, timeColumn, rowTitle, "");
    }


    private static List<String> getRowLabels(ResultSet rs, List<Integer> stringIdxs) throws SQLException {
        List<String> rowNames = new ArrayList<String>();

        rs.beforeFirst();
        if (stringIdxs.size() > 0) {
            while (rs.next()) {
                String s = "" + rs.getObject(stringIdxs.get(0).intValue());
                for (int idx = 1; idx < stringIdxs.size(); idx++) {
                    s = s + " - " + rs.getObject(stringIdxs.get(idx).intValue());
                }
                rowNames.add(s);
            }
        } else {
            int row = 1;
            while (rs.next()) {
                rowNames.add("" + row++);
            }
        }
        return rowNames;
    }


    private static double[] getDoubles(int column, ResultSet rs, int rowCount) throws SQLException {
        double[] nums = new double[rowCount];
        rs.beforeFirst();
        int i = 0;
        while (rs.next()) {
            Object o = rs.getObject(column);
            if (o == null) {
                nums[i] = Double.NaN;
            } else if (o instanceof Number) {
                Number n = (Number) rs.getObject(column);
                nums[i] = n.doubleValue();
            }
            i++;
        }
        assert i == nums.length;
        return nums;
    }


    private static List<Object> getObjects(int column, ResultSet rs) throws SQLException {
        ArrayList<Object> vals = new ArrayList();
        rs.beforeFirst();
        while (rs.next()) {
            vals.add(rs.getObject(column));
        }
        return vals;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\ChartResultSetBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */