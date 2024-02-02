package com.timestored.sqldash.chart;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.timestored.connections.JdbcTypes;
import com.timestored.sqldash.theme.DBIcons;
import com.timestored.theme.Icon;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.DefaultXYZDataset;

import java.awt.Component;
import java.util.List;


enum BubbleChartViewStrategy
        implements ViewStrategy {
    INSTANCE;

    private static final String[] FORMATA;

    static {
        FORMATA = new String[]{"The first string columns are used as category labels.", "There must then be 3 numeric columns which are used for x-coord, y-coord, size in that order."};
    }

    public UpdateableView getView(ChartTheme theme) {
        DefaultXYZDataset dataset = new DefaultXYZDataset();
        JFreeChart chart = ChartFactory.createBubbleChart("", "", "", dataset, PlotOrientation.HORIZONTAL, false, true, true);

        XYPlot xyplot = (XYPlot) chart.getPlot();
        xyplot.setForegroundAlpha(0.65F);

        ChartPanel cp = new ChartPanel(theme.apply(chart));
        chart.getXYPlot().getRenderer().setDefaultToolTipGenerator(Tooltip.getXYZNumbersGenerator());

        return new XYZDatasetUpdateableView(cp, dataset);
    }

    public String getDescription() {
        return "Bubble Chart";
    }

    public Icon getIcon() {
        return DBIcons.CHART_BUBBLE;
    }

    public String getFormatExplainationHtml() {
        return "<ol><li>" + Joiner.on("</li><li>").join(FORMATA) + "</li></ol>";
    }

    public String getFormatExplaination() {
        return Joiner.on("\r\n").join(FORMATA);
    }


    public List<ExampleView> getExamples() {
        ExampleView ev = new ExampleView("Single series", "The three columns are used for x-axis,y-axis and size respectively. Notice the GdpPerCapita column has been divided to make it similar sized to the othercolumns so that the bubbles are a sensible size.", ExampleTestCases.COUNTRY_STATS_ADJUSTED_POP);


        return ImmutableList.of(ev);
    }

    public String getQueryEg(JdbcTypes jdbcType) {
        if (jdbcType.equals(JdbcTypes.KDB)) {
            return ExampleTestCases.COUNTRY_STATS_ADJUSTED_POP.getKdbQuery();
        }
        return null;
    }

    public Component getControlPanel() {
        return null;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\BubbleChartViewStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */