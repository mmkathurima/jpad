package com.timestored.sqldash.chart;

import com.timestored.connections.JdbcTypes;
import com.timestored.theme.Icon;
import com.timestored.theme.Theme;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;

public enum NoRedrawViewStrategy
        implements ViewStrategy {
    INSTANCE;

    private static final String DESC = "Do not draw anything. This selection does nothing and is for use by those who do not want a chart, allowing quicker result drawing times.";

    public UpdateableView getView(ChartTheme theme) {
        return new UpdateableView() {
            public void update(ResultSet rs, ChartResultSet chartResultSet) throws ChartFormatException {
            }

            public Component getComponent() {
                JPanel p = new JPanel(new BorderLayout());
                p.add(Theme.getHeader("No Chart Drawing"), "North");
                p.add(Theme.getTextArea("noRedrawText", "Do not draw anything. This selection does nothing and is for use by those who do not want a chart, allowing quicker result drawing times."), "Center");
                return p;
            }
        };
    }

    public String getDescription() {
        return "No Redraw";
    }

    public String getFormatExplainationHtml() {
        return "Do not draw anything. This selection does nothing and is for use by those who do not want a chart, allowing quicker result drawing times.";
    }

    public String getFormatExplaination() {
        return "Do not draw anything. This selection does nothing and is for use by those who do not want a chart, allowing quicker result drawing times.";
    }

    public Icon getIcon() {
        return null;
    }

    public String getQueryEg(JdbcTypes jdbcType) {
        return null;
    }

    public List<ExampleView> getExamples() {
        return Collections.emptyList();
    }

    public Component getControlPanel() {
        return new JPanel();
    }

    public String toString() {
        return this.getDescription();
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\NoRedrawViewStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */