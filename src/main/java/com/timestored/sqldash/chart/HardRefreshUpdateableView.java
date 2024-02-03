package com.timestored.sqldash.chart;

import com.google.common.base.Preconditions;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.sql.ResultSet;

class HardRefreshUpdateableView
        implements UpdateableView {
    private final ViewGetter viewGetter;
    private final JPanel panel = new JPanel(new BorderLayout());

    public HardRefreshUpdateableView(ViewGetter viewGetter) {
        this.viewGetter = Preconditions.checkNotNull(viewGetter);
    }

    public void update(ResultSet resultSet, ChartResultSet chartResultSet) throws ChartFormatException {
        if (chartResultSet == null) {
            throw new ChartFormatException("Could not construct ResultSet.");
        }

        Component c = this.viewGetter.getView(resultSet, chartResultSet);

        if (c != null) {
            Component com = c;
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    HardRefreshUpdateableView.this.panel.removeAll();
                    HardRefreshUpdateableView.this.panel.add(com, "Center");
                    HardRefreshUpdateableView.this.panel.revalidate();
                }
            });
        }
    }

    public Component getComponent() {
        return this.panel;
    }

    public interface ViewGetter {
        Component getView(ResultSet param1ResultSet, ChartResultSet param1ChartResultSet) throws ChartFormatException;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\HardRefreshUpdateableView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */