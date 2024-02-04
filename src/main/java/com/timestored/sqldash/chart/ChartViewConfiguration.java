package com.timestored.sqldash.chart;

import java.sql.ResultSet;
import java.util.*;

public class ChartViewConfiguration {
    private static final ColumnConfiguration DEFAULT_COLUMN_CONFIG = new ColumnConfiguration(Shape.LINE, Axis.LEFT);
    private final Set<String> domainColumnNames = new HashSet<>();
    private final Map<String, ColumnConfiguration> colConfigs;

    public ChartViewConfiguration() {
        this.colConfigs = new HashMap<>();
    }

    private boolean refresh(ResultSet tab) {
        this.domainColumnNames.clear();

        return true;
    }

    public List<String> getAllColumnNames() {
        Set<String> cols = new HashSet<>(this.colConfigs.keySet());
        cols.addAll(this.domainColumnNames);
        return new ArrayList<>(cols);
    }

    public boolean isDomainColumn(String colName) {
        return this.domainColumnNames.contains(colName);
    }

    public Shape getShape(String colName) {
        ColumnConfiguration colConfig = this.colConfigs.get(colName);
        if (colConfig == null) {
            throw new IllegalArgumentException("column name not known");
        }
        return colConfig.shape;
    }

    public Axis getAxis(String colName) {
        ColumnConfiguration colConfig = this.colConfigs.get(colName);
        if (colConfig == null) {
            throw new IllegalArgumentException("column name not known:" + colName);
        }
        return colConfig.axis;
    }

    public enum Shape {
        LINE, BAR, AREA, CANDLESTICK, HIGHLOW
    }

    public enum Axis {LEFT, RIGHT, HIDE}

    private static class ColumnConfiguration {
        private final ChartViewConfiguration.Shape shape;
        private final ChartViewConfiguration.Axis axis;

        public ColumnConfiguration(ChartViewConfiguration.Shape shape, ChartViewConfiguration.Axis axis) {
            this.shape = shape;
            this.axis = axis;
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\ChartViewConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */