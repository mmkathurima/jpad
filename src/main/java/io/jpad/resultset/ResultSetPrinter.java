package io.jpad.resultset;


import com.google.common.base.Preconditions;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;


public class ResultSetPrinter {
    public static final int MAX_ROWS = 25;
    public static final int MAX_COLS = 80;


    public static String toString(ResultSet resultSet) {

        return toString(resultSet, 0, 25, 80);

    }


    public static String toString(ResultSet resultSet, int colDivider, int maxRows, int maxCols) {

        Preconditions.checkArgument((maxRows > 0), "maxRows > 0");

        Preconditions.checkArgument((maxCols > 0), "maxCols > 0");

        Preconditions.checkArgument((colDivider >= 0), "colDivider >= 0");

        Preconditions.checkNotNull(resultSet);


        StringBuilder[] sbs = new StringBuilder[maxRows];

        StringBuilder titleRow = new StringBuilder(maxCols);

        StringBuilder rulerRow = new StringBuilder(maxCols);


        try {

            ResultSetMetaData rsmd = resultSet.getMetaData();

            int numberOfColumns = rsmd.getColumnCount();

            Preconditions.checkArgument((colDivider <= numberOfColumns), "colDivider <= numberOfColumns");

            resultSet.last();

            int displayRows = Math.min(resultSet.getRow(), maxRows);


            for (int r = 0; r < displayRows; r++) {

                sbs[r] = new StringBuilder(maxCols);

            }


            int maxRowWidth = 0;

            for (int c = 1; c <= numberOfColumns && maxRowWidth <= maxCols; c++) {


                String colSpace = (c == 1) ? "" : " ";


                if (colDivider != 0 && c == colDivider + 1) {

                    titleRow.append("|");

                    rulerRow.append("|");

                    for (int j = 0; j < displayRows; j++) {

                        sbs[j].append("|");

                    }

                }


                resultSet.beforeFirst();


                titleRow.append(colSpace).append(rsmd.getColumnName(c));

                maxRowWidth = Math.max(maxRowWidth, titleRow.length());

                int i;

                for (i = 0; i < displayRows; i++) {

                    resultSet.next();

                    sbs[i].append(colSpace).append(resultSet.getObject(c));


                    if (sbs[i].length() > maxRowWidth) {

                        maxRowWidth = sbs[i].length();

                    }

                }


                titleRow.append(spaces(maxRowWidth - titleRow.length()));

                rulerRow.append(repeat(maxRowWidth - rulerRow.length(), '-'));

                for (i = 0; i < displayRows; i++) {

                    sbs[i].append(spaces(maxRowWidth - sbs[i].length()));

                }

            }


            String N = "\r\n";

            if (titleRow.length() > maxCols) {

                titleRow.setLength(maxCols - 2);

                rulerRow.setLength(maxCols - 2);

                titleRow.append("..").append("\r\n").append(rulerRow).append("..");

            } else {

                titleRow.append("\r\n").append(rulerRow);

            }

            for (StringBuilder s : sbs) {

                if (s != null) {

                    if (s.length() > maxCols) {

                        titleRow.append("\r\n").append(s.substring(0, maxCols - 2)).append("..");

                    } else {

                        titleRow.append("\r\n").append(s);

                    }

                    s.setLength(0);

                }

            }

            resultSet.last();

            if (resultSet.getRow() > maxRows) {

                titleRow.append("\r\n").append("..");

            }

            return titleRow.toString();

        } catch (SQLException e) {

            return "UnrenderableResultSet";

        } finally {

            try {

                resultSet.beforeFirst();

            } catch (SQLException e) {
            }

        }

    }


    private static String repeat(int n, char c) {

        Preconditions.checkArgument((n >= 0));

        if (n == 0) {

            return "";

        }

        char[] charArray = new char[n];

        Arrays.fill(charArray, c);

        return new String(charArray);

    }


    private static String spaces(int n) {

        return repeat(n, ' ');

    }

}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\resultset\ResultSetPrinter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */