package com.timestored.sqldash.exampledb;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.timestored.connections.JdbcTypes;
import com.timestored.sqldash.chart.*;
import com.timestored.sqldash.stockdb.*;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class StockDBAdapter
        implements ExampleChartDB {
    private static final Logger LOG = Logger.getLogger(StockDBAdapter.class.getName());
    private static final String sym = "AMZN";
    private static final String symName = "Amazon";
    private static List<Stock> stocks;
    private static List<BidAsk> lps;
    private static Map<String, List<OHLCDataPoint>> ohlcData;
    private final DBConfig dbConfig;
    private final String name;
    private final JdbcTypes jdbcTypes;

    public StockDBAdapter(DBConfig dbConfig, String name, JdbcTypes jdbcTypes) {
        this.dbConfig = Preconditions.checkNotNull(dbConfig);
        this.name = Preconditions.checkNotNull(name);
        this.jdbcTypes = Preconditions.checkNotNull(jdbcTypes);
    }

    public static Collection<ExampleChartDB> getStockExampleChartDBs() throws IOException {
        Set<String> syms = StockFetcher.packedTickers;
        stocks = StockFetcher.getStock(syms);
        lps = StockFetcher.getFakePrices(syms);
        ohlcData = Maps.newHashMap();
        for (String s : syms) {
            ohlcData.put(s, StockFetcher.getOHLC(s));
        }

        List<ExampleChartDB> l = Lists.newArrayList();
        for (DBConfig dbConfig : DemoFactory.getDBConfigs()) {
            for (JdbcTypes jdbcType : dbConfig.getSupportedJdbcTypes()) {
                l.add(new StockDBAdapter(dbConfig, "Yahoo Finance", jdbcType));
            }
        }
        return l;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return "";
    }

    public JdbcTypes getDbType() {
        return this.jdbcTypes;
    }

    public List<String> getInitSQL(boolean withComments) {
        List<String> l = Lists.newArrayList();
        this.comment(withComments, l, "Init SQL");
        l.addAll(this.dbConfig.getInitSql());
        this.comment(withComments, l, "Stocks");
        l.addAll(this.dbConfig.toInserts(stocks));
        this.comment(withComments, l, "Live Bids Asks");
        l.addAll(this.dbConfig.toLiveInserts(lps));
        this.comment(withComments, l, "OHLC");
        for (String s : ohlcData.keySet()) {
            l.addAll(this.dbConfig.toInserts(s, ohlcData.get(s)));
        }

        return l;
    }

    private void comment(boolean withComments, List<String> l, String comment) {
        if (withComments) {
            l.add("\r\n" + this.jdbcTypes.getComment(" " + comment + " ") + "\r\n");
        }
    }

    public List<ExampleChartQuery> getQueries() {
        List<ExampleChartQuery> qs = new ArrayList<ExampleChartQuery>();
        qs.add(new MyExampleChartQuery("Market Cap Volume", HeatMapViewStrategy.INSTANCE, this.dbConfig.getMarketCapVolSQL()));
        qs.add(new MyExampleChartQuery("Amazon 60 Day History", CandleStickViewStrategy.INSTANCE, this.dbConfig.getOhlcSQL("AMZN", 60)));
        qs.add(new MyExampleChartQuery("Amazon Adjusted Price History", TimeseriesViewStrategy.INSTANCE, this.dbConfig.getSelectAdjPriceHistorySql("AMZN")));
        qs.add(new MyExampleChartQuery("Stock Earnings Per Share", BarChartViewStrategy.INSTANCE, this.dbConfig.getStockEpsSQL()));
        qs.add(new MyExampleChartQuery("Trading Volumes", PieChartViewStrategy.INSTANCE, this.dbConfig.getWeeklyMonthlyVolume()));

        return qs;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\exampledb\StockDBAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */