package com.timestored.sqldash.chart;

import com.google.common.collect.ImmutableList;
import com.timestored.connections.JdbcTypes;
import com.timestored.swingxx.SaveTableMouseAdapter;
import com.timestored.theme.Icon;
import com.timestored.theme.Theme;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class DataTableViewStrategy
        implements ViewStrategy {
    private static final List<ExampleView> EXAMPLES;
    private static final TimeStringValuer TIME_STRINGVAL = new TimeStringValuer();
    private static final String FORMAT = "Any format of table is acceptable, all rows/columns will be shown as a plain table.";
    private static final ViewStrategy INSTANCE = new DataTableViewStrategy(false);
    private static final ViewStrategy DEBUG_INSTANCE = new DataTableViewStrategy(true);

    static {
        ExampleView ev3 = new ExampleView("Many Columned Table", "All rows/columns will be shown as a plain table.", ExampleTestCases.COUNTRY_STATS);

        EXAMPLES = ImmutableList.of(ev3);
    }

    private final boolean debugView;

    private DataTableViewStrategy(boolean debugView) {
        this.debugView = debugView;
    }

    public static ViewStrategy getInstance(boolean debugView) {
        if (debugView) {
            return DEBUG_INSTANCE;
        }
        return INSTANCE;
    }

    public UpdateableView getView(ChartTheme theme) {
        return new DataTableUpdateableView(theme, this.debugView);
    }

    public String getDescription() {
        return this.debugView ? "Debug Table" : "Data Table";
    }

    public String getFormatExplainationHtml() {
        return "Any format of table is acceptable, all rows/columns will be shown as a plain table.";
    }

    public String getFormatExplaination() {
        return "Any format of table is acceptable, all rows/columns will be shown as a plain table.";
    }

    public List<ExampleView> getExamples() {
        return EXAMPLES;
    }

    public String getQueryEg(JdbcTypes jdbcType) {
        if (jdbcType.equals(JdbcTypes.KDB)) {
            return ExampleTestCases.COUNTRY_STATS.getKdbQuery();
        }
        return null;
    }

    public Component getControlPanel() {
        return null;
    }

    public Icon getIcon() {
        return Theme.CIcon.TABLE_ELEMENT;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + (this.debugView ? 1231 : 1237);
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        DataTableViewStrategy other = (DataTableViewStrategy) obj;
        return this.debugView == other.debugView;
    }

    private static class DataTableUpdateableView
            implements UpdateableView {
        private static final String PREFIX = "sd_";

        private final DefaultTableModel tableModel;

        private final JXTable table;
        private final JPanel p;
        private final boolean debugView;
        private List<Color> rowBgColors;
        private List<Color> rowFgColors;

        public DataTableUpdateableView(ChartTheme theme, boolean debugView) {
            this.tableModel = new DefaultTableModel();
            this.debugView = debugView;
            this.table = new JXTable(this.tableModel);
            this.table.addMouseListener(new SaveTableMouseAdapter(this.table, Theme.CIcon.CSV.get()));
            this.table.setEditable(false);

            DefaultTableRenderer defaultTabRenderer = new DefaultTableRenderer(TIME_STRINGVAL, 4) {

                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                    synchronized (this) {
                        Color bcol = theme.getBackgroundColor();
                        if (isSelected) {
                            bcol = theme.getSelectedBackgroundColor();
                        } else if (row % 2 == 1) {
                            bcol = theme.getAltBackgroundColor();
                        }
                        if (DataTableViewStrategy.DataTableUpdateableView.this.rowBgColors != null && row < DataTableViewStrategy.DataTableUpdateableView.this.rowBgColors.size()) {
                            Color cl = DataTableViewStrategy.DataTableUpdateableView.this.rowBgColors.get(row);
                            if (cl != null) {
                                bcol = cl;
                            }
                        }
                        c.setBackground(bcol);

                        Color col = theme.getForegroundColor();
                        if (isSelected) {
                            col = col.darker();
                        }
                        if (DataTableViewStrategy.DataTableUpdateableView.this.rowFgColors != null && row < DataTableViewStrategy.DataTableUpdateableView.this.rowFgColors.size()) {
                            Color cl = DataTableViewStrategy.DataTableUpdateableView.this.rowFgColors.get(row);
                            if (cl != null) {
                                col = cl;
                            }
                        }
                        c.setForeground(col);
                    }

                    return c;
                }
            };
            this.table.setDefaultRenderer(Object.class, defaultTabRenderer);

            this.table.packAll();
            this.table.setAutoResizeMode(0);
            JScrollPane scrollPane = new JScrollPane(this.table, 20, 31);

            JTableHeader thead = this.table.getTableHeader();
            scrollPane.setColumnHeaderView(thead);
            this.table.setPreferredScrollableViewportSize(this.table.getPreferredSize());

            thead.setBackground(theme.getBackgroundColor());
            scrollPane.setBackground(theme.getBackgroundColor());
            scrollPane.getViewport().setBackground(theme.getBackgroundColor());
            this.table.setBackground(theme.getBackgroundColor());

            this.p = new JPanel(new BorderLayout());
            this.p.add(scrollPane, "Center");
        }

        private static Color getColor(String cVal) {
            Color cl = null;
            try {

                try {
                    Field f = Color.class.getField(cVal);
                    cl = (Color) f.get(null);
                } catch (SecurityException e) {
                } catch (NoSuchFieldException e) {
                } catch (IllegalArgumentException e) {
                } catch (IllegalAccessException e) {
                }

                if (cl == null) {
                    cl = Color.decode(cVal);
                }
            } catch (NumberFormatException e) {
            }

            return cl;
        }

        public void update(ResultSet rs, ChartResultSet chartResultSet) throws ChartFormatException {
            try {
                this.rebuildTableModel(rs);
            } catch (SQLException e) {
                throw new ChartFormatException("Could not create ResultSet.");
            }
        }

        public Component getComponent() {
            return this.p;
        }

        private void rebuildTableModel(ResultSet rs) throws SQLException {
            rs.beforeFirst();
            ResultSetMetaData metaData = rs.getMetaData();

            Vector<String> columnNames = new Vector<String>();
            Vector<String> cleanNames = new Vector<String>();
            int columnCount = metaData.getColumnCount();
            for (int c = 1; c <= columnCount; c++) {
                String cn = metaData.getColumnName(c);
                columnNames.add(cn);
                if (!cn.toLowerCase().startsWith("sd_")) {
                    cleanNames.add(cn);
                }
            }

            List<Color> rowBgColorsNew = new ArrayList<Color>();
            List<Color> rowFgColorsNew = new ArrayList<Color>();

            Vector<Vector<Object>> data = new Vector<Vector<Object>>();
            while (rs.next()) {
                Vector<Object> vector = new Vector();
                for (int cIdx = 1; cIdx <= columnCount; cIdx++) {
                    String cn = columnNames.get(cIdx - 1).toLowerCase();
                    if (this.debugView || !cn.startsWith("sd_")) {
                        vector.add(rs.getObject(cIdx));
                    }

                    if (cn.equals("sd_bgcolor")) {
                        rowBgColorsNew.add(getColor("" + rs.getObject(cIdx)));
                    } else if (cn.equals("sd_fgcolor")) {
                        rowFgColorsNew.add(getColor("" + rs.getObject(cIdx)));
                    }
                }
                data.add(vector);
            }

            synchronized (this) {
                this.rowBgColors = (rowBgColorsNew.size() > 0) ? rowBgColorsNew : null;
                this.rowFgColors = (rowFgColorsNew.size() > 0) ? rowFgColorsNew : null;
            }

            this.tableModel.setDataVector(data, this.debugView ? columnNames : cleanNames);
            this.table.packTable(5);
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\DataTableViewStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */