package com.timestored.sqldash.chart;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.timestored.connections.JdbcTypes;
import com.timestored.sqldash.theme.DBIcons;
import com.timestored.theme.Icon;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.statistics.HistogramDataset;

import java.awt.Component;
import java.sql.ResultSet;
import java.util.List;


public enum HistogramViewStrategy
        implements ViewStrategy {
    INSTANCE;

    public static final int NUMBER_BINS = 100;
    private static final String FORMAT = "Each Numeric column represents a separate series in the histogram. The series values are placed into buckets and their frquency tallied.";
    private static final String KDB_QUERY = "([] Returns:cos 0.0015*til 500; Losses:cos 0.002*til 500)";

    public UpdateableView getView(ChartTheme theme) {
        return new HistogramUpdateableView(theme);
    }

    public String getDescription() {
        return "Histogram";
    }

    public List<ExampleView> getExamples() {
        String description = "Distribution of Returns and Losses";
        String name = "Profit Distribution";

        String[] colNames = {"Returns", "Losses"};
        double[] returns = KdbFunctions.cos(KdbFunctions.mul(KdbFunctions.til(500), 0.0015D));
        double[] losses = KdbFunctions.cos(KdbFunctions.mul(KdbFunctions.til(500), 0.002D));
        Object[] colValues = {returns, losses};
        ResultSet resultSet = new SimpleResultSet(colNames, colValues);

        TestCase testCase = new TestCase(name, resultSet, "([] Returns:cos 0.0015*til 500; Losses:cos 0.002*til 500)");
        return ImmutableList.of(new ExampleView(name, description, testCase));
    }

    public String getFormatExplainationHtml() {
        return "Each Numeric column represents a separate series in the histogram. The series values are placed into buckets and their frquency tallied.";
    }

    public String getFormatExplaination() {
        return "Each Numeric column represents a separate series in the histogram. The series values are placed into buckets and their frquency tallied.";
    }

    public String getQueryEg(JdbcTypes jdbcType) {
        return jdbcType.equals(JdbcTypes.KDB) ? "([] Returns:cos 0.0015*til 500; Losses:cos 0.002*til 500)" : null;
    }

    public Component getControlPanel() {
        return null;
    }

    public Icon getIcon() {
        return DBIcons.CHART_HISTOGRAM;
    }

    private static class HistogramUpdateableView
            implements UpdateableView {
        private final ChartPanel chartPanel;

        public HistogramUpdateableView(ChartTheme theme) {
            Preconditions.checkNotNull(theme);
            JFreeChart chart = ChartFactory.createHistogram("", null, "Frequency", null, PlotOrientation.VERTICAL, true, true, false);


            chart.getXYPlot().getRenderer().setDefaultToolTipGenerator(Tooltip.getXYNumbersGenerator());

            this.chartPanel = new ChartPanel(theme.apply(chart));
        }


        public void update(ResultSet rs, ChartResultSet chartRS) throws ChartFormatException {
            if (chartRS.getNumericColumns().size() < 1) {
                throw new ChartFormatException("There must be atleast one number column.");
            }

            HistogramDataset dataset = new HistogramDataset();
            for (ChartResultSet.NumericCol numCol : chartRS.getNumericColumns()) {
                dataset.addSeries(numCol.getLabel(), numCol.getDoubles(), 100);
            }
            XYPlot xyplot = (XYPlot) this.chartPanel.getChart().getPlot();
            xyplot.setDataset(dataset);
            xyplot.getDomainAxis().setLabel(chartRS.getRowTitle());
        }

        public Component getComponent() {
            return this.chartPanel;
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\HistogramViewStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */