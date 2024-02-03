package com.timestored.sqldash.stockdb;

import java.util.Date;
import java.util.Objects;

public class BidAsk {
    private final String sym;
    private final Date time;
    private final double bid;
    private final double ask;

    public BidAsk(String sym, Date time, double bid, double ask) {
        this.sym = sym;
        this.time = time;
        this.bid = bid;
        this.ask = ask;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof BidAsk)) return false;
        BidAsk other = (BidAsk) o;
        if (!other.canEqual(this)) return false;
        Object this$sym = this.sym;
        Object other$sym = other.sym;
        if (!Objects.equals(this$sym, other$sym)) return false;
        Object this$time = this.time;
        Object other$time = other.time;
        return Objects.equals(this$time, other$time) && (Double.compare(this.bid, other.bid) == 0 && (Double.compare(this.ask, other.ask) == 0));
    }

    protected boolean canEqual(Object other) {
        return other instanceof BidAsk;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        Object $sym = this.sym;
        result = result * 59 + (($sym == null) ? 0 : $sym.hashCode());
        Object $time = this.time;
        result = result * 59 + (($time == null) ? 0 : $time.hashCode());
        long $bid = Double.doubleToLongBits(this.bid);
        result = result * 59 + (int) ($bid >>> 32L ^ $bid);
        long $ask = Double.doubleToLongBits(this.ask);
        return result * 59 + (int) ($ask >>> 32L ^ $ask);
    }

    public String toString() {
        return "BidAsk(sym=" + this.sym + ", time=" + this.time + ", bid=" + this.bid + ", ask=" + this.ask + ")";
    }

    public String getSym() {
        return this.sym;
    }

    public Date getTime() {
        return this.time;
    }

    public double getBid() {
        return this.bid;
    }

    public double getAsk() {
        return this.ask;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\stockdb\BidAsk.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */