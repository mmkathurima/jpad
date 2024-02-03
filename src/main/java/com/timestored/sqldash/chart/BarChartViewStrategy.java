package com.timestored.sqldash.chart;

import com.timestored.sqldash.theme.DBIcons;
import net.jcip.annotations.Immutable;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;

@Immutable
public class BarChartViewStrategy
        extends AbstractCategoryViewStrategy {
    public static final ViewStrategy INSTANCE = new BarChartViewStrategy();

    private BarChartViewStrategy() {
        super("Bar Chart", DBIcons.CHART_BAR);
    }

    public UpdateableView getView(ChartTheme theme) {
        JFreeChart chart = ChartFactory.createBarChart("", "", "values", null, PlotOrientation.VERTICAL, theme.showChartLegend(), true, false);

        return new CategoryDatasetUpdateableView(theme, chart);
    }

    public String toString() {
        return BarChartViewStrategy.class.getSimpleName() + "[" + this.getDescription() + "]";
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\BarChartViewStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */