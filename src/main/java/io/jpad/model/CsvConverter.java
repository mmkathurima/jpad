package io.jpad.model;


import io.jpad.resultset.KeyedResultSet;
import io.jpad.scratch.CapturedObject;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CsvConverter {
    private static final Logger log = Logger.getLogger(CsvConverter.class.getName());


    private static final String NL = "\r\n";


    static String convert(List<CapturedObject> dumps) {

        StringWriter s = new StringWriter();

        for (CapturedObject co : dumps) {

            if (!co.getName().isEmpty()) {

                s.append(escapeCommas(co.getName())).append("\r\n");

            }

            try {

                KeyedResultSet keyedResultSet = co.getResultSet();

                if (keyedResultSet == null) {

                    s.append(escapeCommas(co.getObject().toString()));

                } else {

                    appendAsCsv(s, keyedResultSet);

                }

            } catch (SQLException | IOException e) {

                log.log(Level.WARNING, "problem appending resultset", e);

            }

            s.append("\r\n");

        }

        return s.toString();

    }


    public static void writeTo(Writer writer, @NotNull ResultSet rs) throws SQLException, IOException {

        appendAsCsv(writer, rs);

    }


    private static void appendAsCsv(Writer sb, ResultSet rs) throws SQLException, IOException {

        ResultSetMetaData md = rs.getMetaData();
        int c;

        for (c = 1; c <= md.getColumnCount(); c++) {

            sb.append(md.getColumnName(c));

            if (c != md.getColumnCount()) {

                sb.append(",");

            }

        }

        sb.append("\r\n");


        rs.beforeFirst();

        while (rs.next()) {

            for (c = 1; c <= md.getColumnCount(); c++) {

                Object o = rs.getObject(c);

                String s = (o == null) ? "" : escapeCommas(o.toString());

                sb.append(s);

                if (c != md.getColumnCount()) {

                    sb.append(",");

                }

            }

            sb.append("\r\n");

        }

    }


    private static String escapeCommas(String s) {

        if (s.contains(",")) {

            return "\"" + s.replace("\"", "\"\"") + "\"";

        }

        return s;

    }

}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\model\CsvConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */