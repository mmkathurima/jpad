package com.timestored.sqldash.chart;

import com.timestored.sqldash.theme.DBIcons;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;

import java.awt.Color;


class AreaChartViewStrategy
        extends AbstractCategoryViewStrategy {
    public static final ViewStrategy INSTANCE = new AreaChartViewStrategy();

    private AreaChartViewStrategy() {
        super("Area Chart", DBIcons.CHART_AREA);
    }

    public UpdateableView getView(ChartTheme theme) {
        JFreeChart chart = ChartFactory.createAreaChart("", "", "", null, PlotOrientation.VERTICAL, theme.showChartLegend(), true, false);

        chart.setBackgroundPaint(Color.white);
        chart.getPlot().setForegroundAlpha(0.8F);
        return new CategoryDatasetUpdateableView(theme, chart);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\AreaChartViewStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */