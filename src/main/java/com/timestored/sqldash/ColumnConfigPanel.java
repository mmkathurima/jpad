package com.timestored.sqldash;

import com.timestored.sqldash.chart.ChartViewConfiguration;
import com.timestored.sqldash.model.ChartWidget;
import com.timestored.sqldash.model.Widget;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.util.List;

class ColumnConfigPanel
        extends JPanel
        implements Widget.Listener {
    private static final long serialVersionUID = 1L;
    private final JPanel domPanel;
    private final JPanel colPanel;
    private final ChartWidget dataModel;

    public ColumnConfigPanel(ChartWidget dataModel) {
        this.dataModel = dataModel;
        dataModel.addListener(this);

        this.setBorder(BorderFactory.createTitledBorder("Column Configuration"));
        this.setLayout(new BoxLayout(this, 1));

        this.add(new JLabel("Domain Names"));
        this.domPanel = new JPanel();
        this.domPanel.setLayout(new BoxLayout(this.domPanel, 1));
        this.add(this.domPanel);

        this.add(new JLabel("Col Names"));
        this.colPanel = new JPanel();
        this.colPanel.setLayout(new BoxLayout(this.colPanel, 1));
        this.add(this.colPanel);
    }

    public void display(ChartViewConfiguration chartViewConfig) {
        List<String> configCols = chartViewConfig.getAllColumnNames();

        this.domPanel.removeAll();
        this.domPanel.setLayout(new GridLayout(configCols.size(), 4));
        for (String c : configCols) {
            this.domPanel.add(new JLabel(c));
            this.domPanel.add(new JLabel(chartViewConfig.isDomainColumn(c) ? "domain" : ""));
            this.domPanel.add(new JLabel(chartViewConfig.getAxis(c).toString()));
            this.domPanel.add(new JLabel(chartViewConfig.getShape(c).toString()));
        }
        this.domPanel.invalidate();
    }

    public void configChanged(Widget w) {
        this.display(this.dataModel.getChartViewConfig());
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\ColumnConfigPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */