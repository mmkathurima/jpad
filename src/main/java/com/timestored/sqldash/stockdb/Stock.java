package com.timestored.sqldash.stockdb;

import com.google.common.base.MoreObjects;
import net.jcip.annotations.Immutable;


@Immutable
public class Stock {
    private final String symbol;
    private final String name;
    private final double price;
    private final long volume;
    private final double pe;
    private final double eps;
    private final double week52low;
    private final double week52high;
    private final double daylow;
    private final double dayhigh;
    private final double movingav50day;
    private final double marketcap;

    Stock(String symbol, String name, double price, long volume, double pe, double eps, double week52low, double week52high, double daylow, double dayhigh, double movingav50day, double marketcap) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
        this.volume = volume;
        this.pe = pe;
        this.eps = eps;
        this.week52low = week52low;
        this.week52high = week52high;
        this.daylow = daylow;
        this.dayhigh = dayhigh;
        this.movingav50day = movingav50day;
        this.marketcap = marketcap;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public Double getPrice() {
        return Double.valueOf(this.price);
    }

    public long getVolume() {
        return this.volume;
    }

    public double getPe() {
        return this.pe;
    }

    public double getEps() {
        return this.eps;
    }

    public double getWeek52low() {
        return this.week52low;
    }

    public double getWeek52high() {
        return this.week52high;
    }

    public double getDaylow() {
        return this.daylow;
    }

    public double getDayhigh() {
        return this.dayhigh;
    }

    public double getMovingav50day() {
        return this.movingav50day;
    }

    public double getMarketcap() {
        return this.marketcap;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("symbol", this.symbol).add("name", this.name).add("price", this.price).add("volume", this.volume).add("pe", this.pe).add("eps", this.eps).add("week52low", this.week52low).add("week52high", this.week52high).add("daylow", this.daylow).add("dayhigh", this.dayhigh).add("movingav50day", this.movingav50day).add("marketcap", this.marketcap).toString();
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\stockdb\Stock.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */