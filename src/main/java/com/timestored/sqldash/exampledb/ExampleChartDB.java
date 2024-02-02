package com.timestored.sqldash.exampledb;

import com.timestored.connections.JdbcTypes;

import java.util.List;

public interface ExampleChartDB {
    String getName();

    String getDescription();

    List<String> getInitSQL(boolean paramBoolean);

    JdbcTypes getDbType();

    List<ExampleChartQuery> getQueries();
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\exampledb\ExampleChartDB.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */