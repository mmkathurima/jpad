package com.timestored.sqldash.chart;

import com.timestored.connections.JdbcTypes;
import com.timestored.theme.Icon;

import java.awt.Component;
import java.util.List;

public interface ViewStrategy {
    UpdateableView getView(ChartTheme paramChartTheme);

    String getDescription();

    String getFormatExplainationHtml();

    String getFormatExplaination();

    List<ExampleView> getExamples();

    String getQueryEg(JdbcTypes paramJdbcTypes);

    Component getControlPanel();

    Icon getIcon();
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\ViewStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */