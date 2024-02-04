package com.timestored.sqldash.stockdb;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class StockFetcher {
    private static final String QUOTE_CSV = "http://finance.yahoo.com/d/quotes.csv?s=";
    private static final Logger LOG = Logger.getLogger(StockFetcher.class.getName());

    private static final String DATE_PATTERN = "yyyy-MM-dd";

    public static Set<String> packedTickers = Sets.newHashSet("AMZN", "FB", "GOOG", "MSFT", "YHOO");

    public static List<Stock> getStock(Collection<String> symbols) throws IOException {
        LOG.info("get yahoo finance static data for " + Joiner.on(",").join(symbols));

        URL yahoo = new URL("http://finance.yahoo.com/d/quotes.csv?s=" + Joiner.on("+").join(symbols) + "&f=l1vrejkghm3j1sn");
        LOG.info("URL: " + yahoo);
        URLConnection connection = yahoo.openConnection();
        InputStream is = connection.getInputStream();

        return readStocksCsv(is);
    }

    public static List<BidAsk> getFakePrices(Set<String> syms) {
        Random r = new Random(0L);
        List<BidAsk> res = Lists.newArrayListWithExpectedSize(syms.size());
        for (String s : syms) {
            double latestBid = (r.nextFloat() * r.nextInt(200));
            double latestAsk = latestBid + r.nextFloat() * 0.05D;
            res.add(new BidAsk(s, new Date(), latestBid, latestAsk));
        }
        return res;
    }

    private static List<BidAsk> readLivePricesCsv(InputStream is) throws IOException {
        List<BidAsk> r = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line;

        while ((line = br.readLine()) != null) {

            try {
                String[] ls = line.split(",");

                String sym = stripQuotes(ls[0]);
                double bid = toD(ls[1]);
                double ask = toD(ls[2]);
                r.add(new BidAsk(sym, new Date(), bid, ask));
            } catch (NumberFormatException e) {
            }
        }

        return r;
    }

    public static List<Stock> getHardcodedStocks() {
        InputStream is = StockFetcher.class.getResourceAsStream("static.csv");
        try {
            return readStocksCsv(is);
        } catch (IOException e) {
            LOG.warning("Couldn't find internally packed csv static data");

            return Collections.emptyList();
        }
    }

    private static List<Stock> readStocksCsv(InputStream is) throws IOException {
        List<Stock> r = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line;

        while ((line = br.readLine()) != null) {

            try {
                String[] ls = line.split(",");

                double price = toD(ls[0]);
                int volume = toI(ls[1]);
                double pe = toD(ls[2]);
                double eps = toD(ls[3]);
                double week52low = toD(ls[4]);
                double week52high = toD(ls[5]);
                double daylow = toD(ls[6]);
                double dayhigh = toD(ls[7]);
                double movingav50day = toD(ls[8]);

                String mc = ls[9].toUpperCase();
                char lc = mc.charAt(mc.length() - 1);
                double mul = 1.0D;
                if (lc == 'B') {
                    mul = 1.0E9D;
                    mc = mc.substring(0, mc.length() - 1);
                } else if (lc == 'M') {
                    mul = 1000000.0D;
                    mc = mc.substring(0, mc.length() - 1);
                } else if (lc == 'K') {
                    mul = 1000.0D;
                    mc = mc.substring(0, mc.length() - 1);
                }
                double marketcap = toD(mc) * mul;

                String sym = stripQuotes(ls[10]);

                StringBuilder name = new StringBuilder();
                for (int i = 11; i < ls.length; i++) {
                    name.append(ls[i]);
                }
                name = new StringBuilder(stripQuotes(name.toString()));

                r.add(new Stock(sym, name.toString(), price, volume, pe, eps, week52low, week52high, daylow, dayhigh, movingav50day, marketcap));
            } catch (NumberFormatException e) {
            }
        }

        return r;
    }

    private static String stripQuotes(String sym) {
        if (sym.charAt(0) == '"' && sym.charAt(sym.length() - 1) == '"') {
            sym = sym.substring(1, sym.length() - 1);
        }
        return sym;
    }

    public static List<OHLCDataPoint> getOHLC(String symbol) throws IOException {
        return readToList(getHistoricStream(symbol, false));
    }

    public static Map<String, List<OHLCDataPoint>> getHardcodedOHLC() {
        Map<String, List<OHLCDataPoint>> r = Maps.newHashMap();
        for (String sym : packedTickers) {
            InputStream is = StockFetcher.class.getResourceAsStream(sym.toLowerCase() + ".csv");
            try {
                r.put(sym.toUpperCase(), readToList(is));
            } catch (IOException e) {
                LOG.warning("Couldn't find internally packed csv OHLC data");
            }
        }
        return r;
    }

    private static List<OHLCDataPoint> readToList(InputStream is) throws IOException {
        BufferedReader breader = new BufferedReader(new InputStreamReader(is));
        List<OHLCDataPoint> d = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String line;
        while ((line = breader.readLine()) != null) {

            try {
                String[] sl = line.split(",");

                double open = toD(sl[1]);
                double high = toD(sl[2]);
                double low = toD(sl[3]);
                double close = toD(sl[4]);
                double vol = toD(sl[5]);
                double adjClose = toD(sl[6]);

                d.add(new OHLCDataPoint(sdf.parse(sl[0]), open, high, low, close, vol, adjClose));
            } catch (ParseException e) {
            }
        }

        return d;
    }

    public static List<DividendDatapoint> getDividends(String symbol) throws IOException {
        BufferedReader breader = new BufferedReader(new InputStreamReader(getHistoricStream(symbol, true)));

        List<DividendDatapoint> d = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String line;
        while ((line = breader.readLine()) != null) {
            try {
                String[] sl = line.split(",");
                double div = Double.parseDouble(sl[1]);
                d.add(new DividendDatapoint(sdf.parse(sl[0]), div));
            } catch (ParseException e) {
            }
        }

        return d;
    }

    private static InputStream getHistoricStream(String symbol, boolean populateDividends) throws IOException {
        String baseURL = "http://ichart.finance.yahoo.com/table.csv?s=%symbol%&ignore=.csv&g=d";
        if (populateDividends) {
            baseURL = baseURL.replace("g=d", "g=v");
        }
        baseURL = baseURL.replace("%symbol%", symbol.toUpperCase());

        LOG.info("get yahoo fincance read for " + symbol);
        LOG.info("URL: " + baseURL);

        URLConnection urlConnection = (new URL(baseURL)).openConnection();
        return urlConnection.getInputStream();
    }

    private static double toD(String x) {
        return Pattern.matches("N/A", x) ? Double.NaN : Double.parseDouble(x);
    }

    private static int toI(String x) {
        return Pattern.matches("N/A", x) ? 0 : Integer.parseInt(x);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\stockdb\StockFetcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */