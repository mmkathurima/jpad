package com.timestored.sqldash.stockdb;

import com.google.common.base.MoreObjects;
import net.jcip.annotations.Immutable;

import java.util.Date;


@Immutable
public class OHLCDataPoint {
    private final Date date;
    private final double open;
    private final double high;
    private final double low;
    private final double close;
    private final double vol;
    private final double adjClose;

    OHLCDataPoint(Date date, double open, double high, double low, double close, double vol, double adjClose) {
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.vol = vol;
        this.adjClose = adjClose;
    }

    public Date getDate() {
        return this.date;
    }

    public double getOpen() {
        return this.open;
    }

    public double getHigh() {
        return this.high;
    }

    public double getLow() {
        return this.low;
    }

    public double getClose() {
        return this.close;
    }

    public double getVol() {
        return this.vol;
    }

    public double getAdjClose() {
        return this.adjClose;
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("date", this.date).add("open", this.open).add("high", this.high).add("low", this.low).add("close", this.close).add("vol", this.vol).add("adjClose", this.adjClose).toString();
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\stockdb\OHLCDataPoint.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */