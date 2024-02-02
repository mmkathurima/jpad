package com.timestored.sqldash.exampledb;

import com.google.common.base.Preconditions;
import com.timestored.sqldash.chart.ViewStrategy;

class MyExampleChartQuery
        implements ExampleChartQuery {
    private final ViewStrategy vs;
    private final String sqlQuery;
    private final String name;
    private final String description;

    public MyExampleChartQuery(String name, ViewStrategy vs, String sqlQuery, String description) {
        this.vs = Preconditions.checkNotNull(vs);
        this.sqlQuery = Preconditions.checkNotNull(sqlQuery);
        this.name = Preconditions.checkNotNull(name);
        this.description = (description == null) ? "" : description;
    }

    public MyExampleChartQuery(String name, ViewStrategy vs, String sqlQuery) {
        this(name, vs, sqlQuery, null);
    }

    public ViewStrategy getSupportedViewStrategy() {
        return this.vs;
    }

    public String getSqlQuery() {
        return this.sqlQuery;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\exampledb\MyExampleChartQuery.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */