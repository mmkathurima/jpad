package com.timestored.sqldash.chart;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.timestored.connections.JdbcTypes;
import com.timestored.sqldash.theme.DBIcons;
import com.timestored.theme.Icon;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

import java.awt.Component;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.List;


public enum ScatterPlotViewStrategy
        implements ViewStrategy {
    INSTANCE;

    private static final String[] FORMATA;
    private static final String TOOLTIP_FORMAT = "<html><b>{0}:</b><br>{1}<br>{2}</html>";

    static {
        FORMATA = new String[]{"Two or more numeric columns are required. ", "</li><li>The values in the first column are used for the X-axis. ", "</li><li>The values in following columns are used for the Y-axis. ", "</li><li>Each column is displayed with a separate color."};
    }

    private static XYDataset createScatterDataset(ChartResultSet chartResultSet) throws ChartFormatException {
        List<ChartResultSet.NumericCol> numCols = chartResultSet.getNumericColumns();
        if (numCols.size() < 2) {
            throw new ChartFormatException("There must be atleast two numeric columns.");
        }


        DefaultXYDataset dataset = new DefaultXYDataset();
        double[] xAxis = numCols.get(0).getDoubles();
        for (int i = 1; i < numCols.size(); i++) {
            String sTitle = numCols.get(i).getLabel();
            dataset.addSeries(sTitle, new double[][]{xAxis, numCols.get(i).getDoubles()});
        }

        return dataset;
    }

    public UpdateableView getView(final ChartTheme theme) {
        Preconditions.checkNotNull(theme);

        return new HardRefreshUpdateableView(new HardRefreshUpdateableView.ViewGetter() {

            public Component getView(ResultSet resultSet, ChartResultSet colResultSet) throws ChartFormatException {
                if (colResultSet == null) {
                    throw new ChartFormatException("Could not create Result Set.");
                }
                XYDataset dataset = ScatterPlotViewStrategy.createScatterDataset(colResultSet);

                String xAxisLabel = colResultSet.getNumericColumns().get(0).getLabel();
                JFreeChart chart = ChartFactory.createScatterPlot("", xAxisLabel, "", dataset, PlotOrientation.VERTICAL, true, true, false);

                XYItemRenderer renderer = chart.getXYPlot().getRenderer();
                StandardXYToolTipGenerator toolTipGenie = new StandardXYToolTipGenerator("<html><b>{0}:</b><br>{1}<br>{2}</html>", NumberFormat.getInstance(), NumberFormat.getInstance());


                renderer.setDefaultToolTipGenerator(toolTipGenie);

                return new ChartPanel(theme.apply(chart));
            }
        });
    }

    public String getDescription() {
        return "Scatter Plot";
    }

    public Icon getIcon() {
        return DBIcons.CHART_SCATTER_PLOT;
    }

    public String getQueryEg(JdbcTypes jdbcType) {
        if (jdbcType.equals(JdbcTypes.KDB)) {
            return ExampleTestCases.COUNTRY_STATS_ADJUSTED_POP.getKdbQuery();
        }
        return null;
    }

    public String toString() {
        return ScatterPlotViewStrategy.class.getSimpleName() + "[" + getDescription() + "]";
    }

    public List<ExampleView> getExamples() {
        ExampleView ev = new ExampleView("Country Population and GDP", "The first column GDP is used for the x-axis. The subsequent columns are then plotted against that x axis as separate colored series.", ExampleTestCases.COUNTRY_STATS_ADJUSTED_POP);


        return ImmutableList.of(ev);
    }

    public String getFormatExplainationHtml() {
        return "<ol><li>" + Joiner.on("</li><li>").join(FORMATA) + "</li></ol>";
    }

    public String getFormatExplaination() {
        return Joiner.on("\r\n").join(FORMATA);
    }


    public Component getControlPanel() {
        return null;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\ScatterPlotViewStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */