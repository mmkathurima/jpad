package com.timestored.sqldash.stockdb;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.timestored.connections.JdbcTypes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

class KdbConfig
        implements DBConfig {
    public static final DBConfig INSTANCE = new KdbConfig();
    private static final String SEP = "f;\r\n\t ";

    public List<String> toInserts(String sym, List<OHLCDataPoint> d) {
        if (d.isEmpty()) {
            return Collections.emptyList();
        }
        DateFormat dateDF = new SimpleDateFormat("yyyy.MM.dd");

        StringBuilder sb = new StringBuilder("q)`ohlc insert ([] sym:(),");
        for (OHLCDataPoint dp : d) sb.append("`" + sym);
        sb.append(";\r\n\t date:");
        for (OHLCDataPoint dp : d) sb.append(" ").append(dateDF.format(dp.getDate()));
        sb.append(";\r\n\t open:");
        for (OHLCDataPoint dp : d) this.appendNumL(sb, dp.getOpen());
        sb.append("f;\r\n\t high:");
        for (OHLCDataPoint dp : d) this.appendNumL(sb, dp.getHigh());
        sb.append("f;\r\n\t low:");
        for (OHLCDataPoint dp : d) this.appendNumL(sb, dp.getLow());
        sb.append("f;\r\n\t close:");
        for (OHLCDataPoint dp : d) this.appendNumL(sb, dp.getClose());
        sb.append("f;\r\n\t volume:");
        for (OHLCDataPoint dp : d) this.appendNumL(sb, dp.getVol());
        sb.append("f;\r\n\t adjClose:");
        for (OHLCDataPoint dp : d) this.appendNumL(sb, dp.getAdjClose());
        sb.append("f);");
        return this.toList(sb);
    }

    public Set<JdbcTypes> getSupportedJdbcTypes() {
        return Sets.newHashSet(JdbcTypes.KDB);
    }

    public String getExistingTablesSql() {
        return "([] t:tables[])";
    }

    public String getTableCountSql(String tab) {
        return "([] c:enlist `long$count " + tab + ")";
    }

    public List<String> toInserts(List<Stock> stocks) {
        if (stocks.isEmpty()) {
            return Collections.emptyList();
        }

        StringBuilder sb = new StringBuilder("q)`stock insert ([] sym:(),");
        for (Stock s : stocks) sb.append("`").append(s.getSymbol());
        sb.append(";\r\n\t name:(");

        if (stocks.size() <= 1) {
            sb.append("enlist ");
        }
        boolean firstItem = true;
        for (Stock s : stocks) {
            if (!firstItem) {
                sb.append(";");
            }
            firstItem = false;
            sb.append("(),\"");
            sb.append(s.getName().replace("\"", "\\\"")).append("\"");
        }
        sb.append(")");

        sb.append(";\r\n\t price:");
        for (Stock s : stocks) this.appendNumL(sb, s.getPrice().doubleValue());
        sb.append("f;\r\n\t volume:");
        for (Stock s : stocks) this.appendNumL(sb, s.getVolume());
        sb.append("f;\r\n\t pe:");
        for (Stock s : stocks) this.appendNumL(sb, s.getPe());
        sb.append("f;\r\n\t eps:");
        for (Stock s : stocks) this.appendNumL(sb, s.getEps());
        sb.append("f;\r\n\t week52low:");
        for (Stock s : stocks) this.appendNumL(sb, s.getWeek52low());
        sb.append("f;\r\n\t week52high:");
        for (Stock s : stocks) this.appendNumL(sb, s.getWeek52high());
        sb.append("f;\r\n\t daylow:");
        for (Stock s : stocks) this.appendNumL(sb, s.getDaylow());
        sb.append("f;\r\n\t dayhigh:");
        for (Stock s : stocks) this.appendNumL(sb, s.getDayhigh());
        sb.append("f;\r\n\t movingav50day:");
        for (Stock s : stocks) this.appendNumL(sb, s.getMovingav50day());
        sb.append("f;\r\n\t marketcap:");
        for (Stock s : stocks) this.appendNumL(sb, s.getMarketcap());
        sb.append("f);");
        return this.toList(sb);
    }

    private void appendNumL(StringBuilder sb, double v) {
        sb.append(" ").append(Double.isNaN(v) ? "0n" : Double.valueOf(v));
    }

    public List<String> getInitSql() {
        StringBuilder sb = new StringBuilder("q)ohlc:([] sym:`$(); date:`date$()");
        this.appendDoubleCols(sb, new String[]{"open", "high", "low", "close", "volume", "adjClose"});
        sb.append(");\r\n");

        sb.append("stock:([] sym:`$(); name:()");
        this.appendDoubleCols(sb, new String[]{"price", "volume", "pe", "eps", "week52low", "week52high", "daylow", "dayhigh", "movingav50day", "marketcap"});

        sb.append(");\r\n");
        sb.append("quote:([] sym:`$(); time:`time$()");
        this.appendDoubleCols(sb, new String[]{"bid", "ask"});
        sb.append(");\r\n");
        return this.toList(sb);
    }

    private ArrayList<String> toList(StringBuilder sb) {
        ArrayList<String> r = Lists.newArrayListWithCapacity(1);
        r.add(sb.toString());
        return r;
    }

    private void appendDoubleCols(StringBuilder sb, String[] c1) {
        for (String s : c1) {
            sb.append("; ").append(s).append(":`float$()");
        }
    }

    public List<String> toLiveInserts(List<BidAsk> lps) {
        if (lps.isEmpty()) {
            return Collections.emptyList();
        }
        DateFormat timeDF = new SimpleDateFormat("HH:mm:ss.SSS");

        StringBuilder sb = new StringBuilder("q)`quote insert ([] sym:(),");
        for (BidAsk dp : lps) sb.append("`" + dp.getSym());
        sb.append(";\r\n\t time:");
        for (BidAsk dp : lps) sb.append(" ").append(timeDF.format(dp.getTime()));
        sb.append(";\r\n\t bid:");
        for (BidAsk dp : lps) this.appendNumL(sb, dp.getBid());
        sb.append("f;\r\n\t ask:");
        for (BidAsk dp : lps) this.appendNumL(sb, dp.getAsk());
        sb.append("f);");
        return this.toList(sb);
    }

    public String getSelectAdjPriceHistorySql(String sym) {
        return "select date,adjClose from ohlc where sym=`" + Preconditions.checkNotNull(sym);
    }

    public String getOhlcSQL(String sym, int pastDays) {
        return "select from ohlc where sym=`" + Preconditions.checkNotNull(sym) + ",date>.z.d-" + pastDays;
    }

    public String getMarketCapVolSQL() {
        return "select sym,name,marketcap,volume from stock";
    }

    public String getStockEpsSQL() {
        return "select sym,eps from stock";
    }

    public String getWeeklyMonthlyVolume() {
        return "5#`Week xdesc select Week:sum volume where date>.z.d-7,Month:sum volume where date>.z.d-30 by sym from ohlc";
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\stockdb\KdbConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */