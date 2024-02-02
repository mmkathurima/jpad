package com.timestored.sqldash.chart;

import org.jfree.chart.JFreeChart;

import java.awt.Color;

public interface ChartTheme {
    JFreeChart apply(JFreeChart paramJFreeChart);

    boolean showChartLegend();

    String getDescription();

    String getTitle();

    Color getForegroundColor();

    Color getBackgroundColor();

    Color getAltBackgroundColor();

    Color getSelectedBackgroundColor();
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\ChartTheme.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */