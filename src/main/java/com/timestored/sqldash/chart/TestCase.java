package com.timestored.sqldash.chart;

import com.google.common.base.MoreObjects;

import java.sql.ResultSet;
import java.sql.SQLException;


class TestCase {
    private final String kdbQuery;
    private final String name;
    private final ResultSet resultSet;

    TestCase(String name, ResultSet resultSet, String kdbQuery) {
        this.kdbQuery = kdbQuery;
        this.resultSet = resultSet;
        this.name = name;
    }


    public String toString() {
        return MoreObjects.toStringHelper(this).add("kdbQuery", this.kdbQuery).add("name", this.name).add("resultSet", this.resultSet).toString();
    }


    public String getName() {
        return this.name;
    }


    public String getKdbQuery() {
        return this.kdbQuery;
    }


    public ResultSet getResultSet() {
        return this.resultSet;
    }

    public ChartResultSet getColResultSet() throws SQLException {
        return ChartResultSet.getInstance(this.resultSet);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\TestCase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */