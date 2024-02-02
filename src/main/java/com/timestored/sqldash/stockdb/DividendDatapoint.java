package com.timestored.sqldash.stockdb;

import com.google.common.base.MoreObjects;

import java.util.Date;


public class DividendDatapoint {
    private final Date date;
    private final double div;

    DividendDatapoint(Date date, double div) {
        this.date = date;
        this.div = div;
    }

    public Date getDate() {
        return this.date;
    }

    public double getDiv() {
        return this.div;
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("date", this.date).add("div", this.div).toString();
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\stockdb\DividendDatapoint.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */