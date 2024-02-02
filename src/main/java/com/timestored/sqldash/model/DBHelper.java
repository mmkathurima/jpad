package com.timestored.sqldash.model;

import com.google.common.base.Objects;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DBHelper {
    private static final Logger LOG = Logger.getLogger(DBHelper.class.getName());


    public static boolean isEqual(ResultSet rsA, ResultSet rsB) {
        if (rsB == null) {
            return (rsA == null);
        }
        if (rsA == null) {
            return (rsB == null);
        }
        try {
            ResultSetMetaData mdA = rsA.getMetaData();
            int cols = mdA.getColumnCount();
            ResultSetMetaData mdB = rsB.getMetaData();
            if (cols != mdB.getColumnCount())
                return false;
            int c;
            for (c = 1; c <= cols; c++) {
                if (!mdA.getColumnName(c).equals(mdB.getColumnName(c))) {
                    return false;
                }
            }
            rsA.beforeFirst();
            rsB.beforeFirst();


            while (rsA.next() & rsB.next()) {
                for (c = 1; c <= cols; c++) {
                    boolean eq = Objects.equal(rsA.getObject(c), rsB.getObject(c));
                    if (!eq) {
                        if (LOG.isLoggable(Level.FINER)) {
                            LOG.log(Level.FINE, " rsA.getObject(c) = " + rsA.getObject(c).toString());
                            LOG.log(Level.FINE, " rsB.getObject(c) = " + rsB.getObject(c).toString());
                        }
                        return false;
                    }
                }
            }
            return (rsA.isAfterLast() && rsB.isAfterLast());
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "Error. Assuming isEqualResultSets false", e);

            return false;
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\model\DBHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */