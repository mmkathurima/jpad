package com.timestored.sqldash.exampledb;

import com.timestored.sqldash.chart.ViewStrategy;

public interface ExampleChartQuery {
    String getName();

    String getDescription();

    String getSqlQuery();

    ViewStrategy getSupportedViewStrategy();
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\exampledb\ExampleChartQuery.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */