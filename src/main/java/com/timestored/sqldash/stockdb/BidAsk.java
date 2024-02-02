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
        Object this$sym = getSym(), other$sym = other.getSym();
        if (!Objects.equals(this$sym, other$sym)) return false;
        Object this$time = getTime(), other$time = other.getTime();
        return Objects.equals(this$time, other$time) && (Double.compare(getBid(), other.getBid()) == 0 && (Double.compare(getAsk(), other.getAsk()) == 0));
    }

    protected boolean canEqual(Object other) {
        return other instanceof BidAsk;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $sym = getSym();
        result = result * 59 + (($sym == null) ? 0 : $sym.hashCode());
        Object $time = getTime();
        result = result * 59 + (($time == null) ? 0 : $time.hashCode());
        long $bid = Double.doubleToLongBits(getBid());
        result = result * 59 + (int) ($bid >>> 32L ^ $bid);
        long $ask = Double.doubleToLongBits(getAsk());
        return result * 59 + (int) ($ask >>> 32L ^ $ask);
    }

    public String toString() {
        return "BidAsk(sym=" + getSym() + ", time=" + getTime() + ", bid=" + getBid() + ", ask=" + getAsk() + ")";
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