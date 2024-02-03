package com.timestored.sqldash.stockdb;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.timestored.connections.JdbcTypes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

class H2DBConfig
        implements DBConfig {
    public static final DBConfig INSTANCE = new H2DBConfig();
    private static final String INS_OHLC = "INSERT INTO ohlc(sym,date,open,high,low,close,volume,adjClose) VALUES(";
    private static final String INS_STOCK = "INSERT INTO stock(sym,name,price,volume,pe,eps,week52low,week52high,daylow,dayhigh,movingav50day,marketcap) VALUES(";
    private static final String INS_QUOTE = "INSERT INTO quote(sym,time,bid,ask) VALUES(";
    private static final String NL = "\r\n";

    private static String toS(double v) {
        return Double.isNaN(v) ? "NULL" : ("" + v);
    }

    private static String getCreateTabPre(String tabName, String innerSql) {
        return " CREATE  TABLE " + tabName + " (" + innerSql + ");";
    }

    private static String getDropTab(String tabName) {
        return "DROP TABLE IF EXISTS " + tabName + ";";
    }

    private static StringBuilder addClear(List<String> list, StringBuilder sb) {
        list.add(sb.toString());
        sb.setLength(0);
        return sb;
    }

    public List<String> toInserts(String sym, List<OHLCDataPoint> dataPoints) {
        StringBuilder sb = new StringBuilder();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        List<String> r = Lists.newArrayListWithExpectedSize(dataPoints.size());

        for (OHLCDataPoint dp : dataPoints) {
            sb.append("INSERT INTO ohlc(sym,date,open,high,low,close,volume,adjClose) VALUES(");
            Joiner.on(",").appendTo(sb, "'" + sym + "'", "'" + df.format(dp.getDate()) + "'", toS(dp.getOpen()), toS(dp.getHigh()), toS(dp.getLow()), toS(dp.getClose()), toS(dp.getVol()), toS(dp.getAdjClose()));

            r.add(sb.append(");").toString());
            sb.setLength(0);
        }
        return r;
    }

    public List<String> toInserts(List<Stock> stocks) {
        List<String> r = Lists.newArrayListWithExpectedSize(stocks.size());
        StringBuilder sb = new StringBuilder();
        for (Stock s : stocks) {
            sb.append("INSERT INTO stock(sym,name,price,volume,pe,eps,week52low,week52high,daylow,dayhigh,movingav50day,marketcap) VALUES(");
            Joiner.on(",").appendTo(sb, "'" + s.getSymbol() + "'", "'" + s.getName().replace("'", "''") + "'", toS(s.getPrice().doubleValue()), toS(s.getVolume()), toS(s.getPe()), toS(s.getEps()), toS(s.getWeek52low()), toS(s.getWeek52high()), toS(s.getDaylow()), toS(s.getDayhigh()), toS(s.getMovingav50day()), toS(s.getMarketcap()));

            r.add(sb.append(");").toString());
            sb.setLength(0);
        }
        return r;
    }

    public List<String> toLiveInserts(List<BidAsk> quotes) {
        List<String> r = Lists.newArrayListWithExpectedSize(quotes.size());
        StringBuilder sb = new StringBuilder();
        DateFormat df = new SimpleDateFormat("HH:mm:ss.SSS");
        for (BidAsk ba : quotes) {
            sb.append("INSERT INTO quote(sym,time,bid,ask) VALUES(");
            Joiner.on(",").appendTo(sb, "'" + ba.getSym() + "'", "'" + df.format(ba.getTime()) + "'", toS(ba.getBid()), toS(ba.getAsk()));

            r.add(sb.append(");").toString());
            sb.setLength(0);
        }
        return r;
    }

    private String getDoubleCols(String[] colnames) {
        StringBuilder sb = new StringBuilder();
        for (String c : colnames) {
            sb.append(",\r\n\t ").append(c).append(" DOUBLE NULL");
        }
        return sb.toString();
    }

    public List<String> getInitSql() {
        ArrayList<String> r = Lists.newArrayList();
        final String NN = " NOT NULL,\r\n\t";

        r.add(getDropTab("ohlc"));
        String q = "sym VARCHAR(10)" + NN + "date DATE NOT NULL" + this.getDoubleCols("open,high,low,close,volume,adjClose".split(","));

        r.add(getCreateTabPre("ohlc", q));

        r.add(getDropTab("stock"));
        q = "sym VARCHAR(10)" + NN + "name VARCHAR(80) NOT NULL" + this.getDoubleCols("price,volume,pe,eps,week52low,week52high,daylow,dayhigh,movingav50day,marketcap".split(","));

        r.add(getCreateTabPre("stock", q));

        r.add(getDropTab("quote"));
        q = "sym VARCHAR(10)" + NN + "time TIME NOT NULL" + this.getDoubleCols("bid,ask".split(","));

        r.add(getCreateTabPre("quote", q));

        return r;
    }

    public String getExistingTablesSql() {
        return "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.Tables;";
    }

    public String getTableCountSql(String tab) {
        return "SELECT COUNT(*) FROM " + Preconditions.checkNotNull(tab) + ";";
    }

    public String getSelectAdjPriceHistorySql(String sym) {
        return "select date,adjClose from ohlc where sym='" + Preconditions.checkNotNull(sym) + "'";
    }

    public String getOhlcSQL(String sym, int pastDays) {
        return "select * from ohlc where sym='" + Preconditions.checkNotNull(sym) + "' AND (date > CURDATE()-" + pastDays + ")";
    }

    public String getStockEpsSQL() {
        return "select sym,eps from stock";
    }

    public String getMarketCapVolSQL() {
        return "select sym,name,marketcap,volume from stock";
    }

    public String getWeeklyMonthlyVolume() {
        return "SELECT * FROM (SELECT SYM,SUM(VOLUME) AS WEEK FROM OHLC     WHERE date>CURDATE()-7 GROUP BY SYM ORDER BY SUM(VOLUME)  LIMIT 5) a LEFT JOIN  (SELECT SYM,SUM(VOLUME) AS MONTH FROM OHLC   WHERE date>CURDATE()-30 GROUP BY SYM) b ON a.SYM=b.SYM";
    }

    public Set<JdbcTypes> getSupportedJdbcTypes() {
        return Sets.newHashSet(JdbcTypes.H2);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\stockdb\H2DBConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */