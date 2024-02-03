package io.jpad.model;

import com.timestored.misc.HtmlUtils;
import io.jpad.resultset.KeyedResultSet;
import io.jpad.scratch.CapturedObject;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class HtmlConverter {
    private static final Logger log = Logger.getLogger(HtmlConverter.class.getName());

    public static String convert(List<CapturedObject> dumps) {

        StringBuilder sb = new StringBuilder();

        for (CapturedObject co : dumps) {

            KeyedResultSet rs = co.getResultSet();

            String n = co.getName();

            if (n.trim().length() > 0) {

                sb.append("<h2>").append(n).append("</h2>");
            }

            appendContent(sb, co.getObject(), rs);

            sb.append("<br />\r\n");
        }

        return sb.toString();
    }

    private static void appendContent(StringBuilder sb, Object o, KeyedResultSet rs) {

        if (rs != null) {

            appendAsHtmlTable(sb, rs);
        } else {

            String s = (o == null) ? "null" : o.toString();

            HtmlUtils.appendEscapedHtml(sb, s);
        }
    }

    private static void appendAsHtmlTable(StringBuilder sb, KeyedResultSet rs) {

        sb.append("<table>");

        if (!rs.getCaption().isEmpty()) {

            sb.append("<caption>");

            HtmlUtils.appendEscapedHtml(sb, rs.getCaption());

            sb.append("</caption>");
        }

        try {

            ResultSetMetaData md = rs.getMetaData();

            sb.append("\r\n\t<thead><tr>");
            int c;

            for (c = 1; c <= md.getColumnCount(); c++) {

                sb.append("<th>");

                HtmlUtils.appendEscapedHtml(sb, md.getColumnName(c));

                sb.append("</th>");
            }

            sb.append("</tr></thead><tbody>");

            rs.beforeFirst();

            while (rs.next()) {

                sb.append("\r\n\t<tr>");

                for (c = 1; c <= md.getColumnCount(); c++) {

                    sb.append("<td>");

                    Object o = rs.getObject(c);

                    if (o instanceof KeyedResultSet) {

                        appendContent(sb, o, (KeyedResultSet) o);
                    } else {

                        appendContent(sb, o, null);
                    }

                    sb.append("</td>");
                }

                sb.append("</tr>");
            }
        } catch (SQLException e) {

            log.log(Level.SEVERE, "Could not output RS as html table", e);
        }

        sb.append("\r\n</tbody></table>\r\n");
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\model\HtmlConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */