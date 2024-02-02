package com.timestored.sqldash.stockdb;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.timestored.connections.ServerConfig;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FinanceDataDemo {
    private static final int PAUSE = 10000;
    private static final Logger LOG = Logger.getLogger(FinanceDataDemo.class.getName());
    private static final Random r = new Random();
    private static final Set<String> myStocks = Sets.newHashSet("MMM,AXP,T,BA,CAT,CVX,IBM,JPM,MCD,NKE,DIS,V".split(","));
    private final DBConfig demoConfig;
    private final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private final ServerConfig serverConfig;
    private DemoListener listener;
    private Connection conn = null;
    private Thread t;
    private List<BidAsk> latestBidAsk = Collections.emptyList();


    FinanceDataDemo(DBConfig config, ServerConfig serverConfig) throws ClassNotFoundException {
        this.demoConfig = Preconditions.checkNotNull(config);
        this.serverConfig = Preconditions.checkNotNull(serverConfig);
        Class.forName(serverConfig.getJdbcType().getDriver());
    }


    public synchronized void start() throws SQLException {
        if (this.t != null) {
            throw new IllegalStateException("already started");
        }
        if (isStopped()) {
            throw new IllegalStateException("already stopped");
        }


        send(this.demoConfig.getInitSql());


        Runnable r = new Runnable() {
            public void run() {
                Sets.SetView<String> stocks = Sets.union(FinanceDataDemo.myStocks, StockFetcher.packedTickers);
                FinanceDataDemo.this.runn(stocks);
            }
        };
        this.t = new Thread(r);
        this.t.start();


        LOG.info(log("Background data fetcher running..."));
    }


    private synchronized String log(String s) {
        if (this.listener != null) {
            this.listener.message(this.dateFormat.format(new Date()) + " " + s);
        }
        return s;
    }


    private void checkInterrupt() throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
    }

    public synchronized void setListener(DemoListener listener) {
        this.listener = listener;
    }


    private void runn(Collection<String> stocks) {
        List<String> sl = Lists.newArrayList(stocks);
        Collections.sort(sl);
        List<Stock> rs = Collections.emptyList();
        Set<String> ohlcSymsFetched = Sets.newHashSet();

        this.latestBidAsk = Lists.newArrayList();


        try {
            try {
                log("Fetching stock static data for: " + Joiner.on(',').join(sl));
                try {
                    rs = StockFetcher.getStock(Lists.newArrayList(sl));
                } catch (IOException ioe) {
                }


                if (rs.isEmpty()) {
                    rs = StockFetcher.getHardcodedStocks();
                }
                log("Sending static data to database server.");
                send(this.demoConfig.toInserts(rs));


                for (Stock s : rs) {
                    Thread.sleep(50L);
                    String sym = s.getSymbol();
                    log("Fetching and storing OHLC data for " + sym);
                    List<OHLCDataPoint> ohlcPrices = StockFetcher.getOHLC(sym);
                    sendEach(sym, ohlcPrices);
                    sendFirstFakeLivePrice(sym, ohlcPrices);
                    ohlcSymsFetched.add(sym);
                }
            } catch (SQLException e1) {
                LOG.log(Level.SEVERE, log("sql error get/inserting live data"), e1);
            } catch (IOException e) {
                LOG.log(Level.SEVERE, log("IO error get/inserting live data"), e);
            }


            try {
                if (ohlcSymsFetched.size() < 3) {
                    log("Sending fake OHLC data to database server");
                    Map<String, List<OHLCDataPoint>> hardcodedOHLC = StockFetcher.getHardcodedOHLC();
                    hardcodedOHLC.keySet().removeAll(ohlcSymsFetched);

                    for (String sym : hardcodedOHLC.keySet()) {
                        List<OHLCDataPoint> ohlcData = hardcodedOHLC.get(sym);
                        sendEach(sym, ohlcData);
                        sendFirstFakeLivePrice(sym, ohlcData);
                    }
                }
            } catch (SQLException e1) {
                LOG.log(Level.SEVERE, log("sql error get/inserting live data"), e1);
            }


            log("Sending fake live data");
            while (true) {
                fakeSendLivePricesUsingRandomWalk();
                Thread.sleep(10000L);
            }
        } catch (InterruptedException e) {
            log("Shutting down.");
        }
    }


    private void sendFirstFakeLivePrice(String sym, List<OHLCDataPoint> ohlcPrices) throws SQLException {
        List<OHLCDataPoint> a = Lists.newArrayList(ohlcPrices);
        Collections.sort(a, new Comparator<OHLCDataPoint>() {
            public int compare(OHLCDataPoint o, OHLCDataPoint p) {
                return o.getDate().compareTo(p.getDate());
            }
        });
        double val = a.get(a.size() - 1).getClose();
        BidAsk ba = new BidAsk(sym, new Date(), val, val);
        send(this.demoConfig.toLiveInserts(Lists.newArrayList(ba)));
        this.latestBidAsk.add(ba);
    }


    private void fakeSendLivePricesUsingRandomWalk() throws InterruptedException {
        try {
            log("Fetching live data...");
            checkInterrupt();


            for (int i = 0; i < this.latestBidAsk.size(); i++) {
                BidAsk ba = this.latestBidAsk.get(i);
                double latestBid = ba.getBid() * (0.99D + r.nextFloat() * 0.02D);
                double latestAsk = latestBid + r.nextFloat() * 0.05D;
                BidAsk newBidAsk = new BidAsk(ba.getSym(), new Date(), latestBid, latestAsk);
                this.latestBidAsk.set(i, newBidAsk);
            }

            log("Sending " + this.latestBidAsk.size() + " records to database server.");
            send(this.demoConfig.toLiveInserts(this.latestBidAsk));
        } catch (SQLException e) {
            LOG.log(Level.WARNING, log("sql error inserting live prices"), e);
        }
    }

    public void stop() {
        if (this.t == null) {
            throw new IllegalStateException("never started.");
        }
        this.t.interrupt();
        if (this.conn != null) {
            try {
                this.conn.close();
            } catch (SQLException e) {
            }
        }
        LOG.info(log("Background data fetcher stopping..."));
    }


    private void sendEach(String sym, List<OHLCDataPoint> dataPoints) throws SQLException, InterruptedException {
        int BATCH_SIZE = 20;

        List<OHLCDataPoint> t = new ArrayList<OHLCDataPoint>(20);
        for (int i = 1; i <= dataPoints.size(); i++) {
            t.add(dataPoints.get(i - 1));
            if (i % 20 == 0) {
                send(this.demoConfig.toInserts(sym, t));
                t.clear();
                checkInterrupt();
            }
        }
        send(this.demoConfig.toInserts(sym, t));
    }

    private void send(List<String> sqlQueries) throws SQLException {
        for (String sql : sqlQueries) {
            send(sql);
        }
    }


    private void send(String sql) throws SQLException {
        Statement st = null;
        try {
            if (this.conn == null || this.conn.isClosed()) {
                this.conn = DriverManager.getConnection(this.serverConfig.getUrl(), this.serverConfig.getUsername(), this.serverConfig.getPassword());
            }
            st = this.conn.createStatement();
            st.execute(sql);
        } catch (SQLException sqe) {
            LOG.warning("error running sql:\r\n" + sql);
            throw sqe;
        } finally {
            if (st != null)
                st.close();
        }
    }

    public boolean isStopped() {
        return (this.t != null && !this.t.isAlive());
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\stockdb\FinanceDataDemo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */