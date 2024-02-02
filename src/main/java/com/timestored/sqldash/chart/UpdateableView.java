package com.timestored.sqldash.chart;

import java.awt.Component;
import java.sql.ResultSet;

interface UpdateableView {
    void update(ResultSet paramResultSet, ChartResultSet paramChartResultSet) throws ChartFormatException;

    Component getComponent();
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\UpdateableView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */