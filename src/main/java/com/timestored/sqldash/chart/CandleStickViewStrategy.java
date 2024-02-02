package com.timestored.sqldash.chart;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.timestored.connections.JdbcTypes;
import com.timestored.sqldash.theme.DBIcons;
import com.timestored.theme.Icon;
import com.timestored.utils.SegmentedTimeline;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.HighLowItemLabelGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.OHLCDataset;

import java.awt.Component;
import java.sql.Date;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;


public enum CandleStickViewStrategy
        implements ViewStrategy {
    INSTANCE;

    private static final String[] FORMATA;

    private static final String[] COL_TITLES;
    private static final String TOOLTIP_FORMAT = "<html><b>{0}:</b><br>{1}<br>{2}</html>";
    private static final String END = ") }[]";
    private static final String queryTHLOCV = "{ c:55+2*til 30; ([] t:raze 2014.03.17+(7*til 6)+\\:til 5; high:c+30; low:c-20; open:60+til 30; close:c; volume:30#3 9 6 5 4 7 8 2 13";
    private static final String queryTHLOC = "{ c:55+2*til 30; ([] t:raze 2014.03.17+(7*til 6)+\\:til 5; high:c+30; low:c-20; open:60+til 30; close:c";
    private static final String queryTHL = "{ c:55+2*til 30; ([] t:raze 2014.03.17+(7*til 6)+\\:til 5; high:c+30; low:c-20";

    static {
        COL_TITLES = new String[]{"high", "low", "open", "close"};

        FORMATA = new String[]{"The table should contain columns labelled open/high/low/close/volume", "<br/>but must atleast contain high/low to allow it to be drawn.", "<br/>Only weekday values are shown."};
    }

    private static DateAxis getTimeAxis(ChartResultSet chartResultSet) {
        DateAxis timeAxis = new DateAxis("Date");

        timeAxis.setTimeline(SegmentedTimeline.newMondayThroughFridayTimeline());
        timeAxis.setLowerMargin(0.02D);
        timeAxis.setUpperMargin(0.02D);


        timeAxis.setAutoTickUnitSelection(false);

        ChartResultSet.TimeCol tc = chartResultSet.getTimeCol();
        if (tc != null) {
            java.util.Date[] dts = tc.getDates();
            if (dts.length > 0) {
                DateTickUnit dtu;
                java.util.Date minDate = dts[0];
                java.util.Date maxDate = dts[0];
                for (java.util.Date d : dts) {
                    if (d.after(maxDate)) {
                        maxDate = d;
                    } else if (d.before(minDate)) {
                        minDate = d;
                    }
                }
                long diff = maxDate.getTime() - minDate.getTime();
                long diffDays = diff / 86400000L;

                if (diffDays < 1L) {
                    dtu = new DateTickUnit(DateTickUnitType.HOUR, 1);
                    timeAxis.setDateFormatOverride(new SimpleDateFormat("h:mm"));
                } else if (diffDays < 10L) {
                    dtu = new DateTickUnit(DateTickUnitType.DAY, 1);
                    timeAxis.setDateFormatOverride(new SimpleDateFormat("dd MMM"));
                } else if (diffDays < 60L) {
                    dtu = new DateTickUnit(DateTickUnitType.DAY, 7);
                    timeAxis.setDateFormatOverride(new SimpleDateFormat("dd MMM"));
                } else {
                    dtu = new DateTickUnit(DateTickUnitType.MONTH, 1);
                    timeAxis.setDateFormatOverride(new SimpleDateFormat("dd MMM"));
                }
                timeAxis.setTickUnit(dtu);
            }
        }
        return timeAxis;
    }

    private static TimeSeriesCollection createVolumeDataset(ChartResultSet colResultSet) {
        ChartResultSet.TimeCol timeCol = colResultSet.getTimeCol();
        ChartResultSet.NumericCol nc = colResultSet.getNumericalColumn("volume");


        if (timeCol != null && nc != null) {
            TimeSeriesCollection dataset = new TimeSeriesCollection();
            RegularTimePeriod[] timePeriods = timeCol.getRegularTimePeriods();
            TimeSeries tSeries = new TimeSeries("" + nc.getLabel());
            int row = 0;
            for (double d : nc.getDoubles()) {
                tSeries.addOrUpdate(timePeriods[row++], d);
            }
            if (!tSeries.isEmpty()) {
                dataset.addSeries(tSeries);
            }
            return dataset;
        }
        return null;
    }

    private static OHLCDataset createOHLCDataset(ChartResultSet chartResultSet) throws ChartFormatException {
        ChartResultSet.TimeCol timeCol = chartResultSet.getTimeCol();
        if (timeCol == null) {
            throw new ChartFormatException("No Time column found.");
        }

        ChartResultSet.NumericCol[] hlocvIndices = new ChartResultSet.NumericCol[4];
        int i = 0;
        for (String columnLabel : COL_TITLES) {
            hlocvIndices[i++] = chartResultSet.getNumericalColumn(columnLabel);
        }


        boolean noOpen = (hlocvIndices[2] == null);
        boolean noClose = (hlocvIndices[3] == null);
        if (noOpen && noClose) {

            hlocvIndices[2] = hlocvIndices[0];
            hlocvIndices[3] = hlocvIndices[0];
        } else if (noOpen) {
            hlocvIndices[2] = hlocvIndices[3];
        } else if (noClose) {
            hlocvIndices[3] = hlocvIndices[2];
        }


        double[][] doubArray = new double[COL_TITLES.length][];
        for (int j = 0; j < COL_TITLES.length; j++) {
            if (hlocvIndices[j] != null) {
                doubArray[j] = hlocvIndices[j].getDoubles();
            }
        }


        java.util.Date[] arrayOfDate = timeCol.getDates();
        double[] vol = new double[chartResultSet.getRowCount()];
        return new DefaultHighLowDataset("Series 1", arrayOfDate, doubArray[0], doubArray[1], doubArray[2], doubArray[3], vol);
    }

    public UpdateableView getView(final ChartTheme theme) {
        Preconditions.checkNotNull(theme);

        return new HardRefreshUpdateableView(new HardRefreshUpdateableView.ViewGetter() {

            public Component getView(ResultSet resultSet, ChartResultSet chartResultSet) throws ChartFormatException {
                if (chartResultSet == null) {
                    throw new ChartFormatException("Could not construct ResultSet.");
                }
                OHLCDataset dataset = CandleStickViewStrategy.createOHLCDataset(chartResultSet);

                DateAxis timeAxis = CandleStickViewStrategy.getTimeAxis(chartResultSet);

                NumberAxis valueAxis1 = new NumberAxis("Price");
                valueAxis1.setAutoRangeIncludesZero(false);

                NumberAxis valueAxis2 = new NumberAxis("Volume");
                valueAxis2.setAutoRangeIncludesZero(false);
                valueAxis2.setNumberFormatOverride(new DecimalFormat("0"));


                CandlestickRenderer candle = new CandlestickRenderer(4.0D, false, new HighLowItemLabelGenerator());

                CombinedDomainXYPlot plot = new CombinedDomainXYPlot(timeAxis);
                XYPlot subplot1 = new XYPlot(dataset, timeAxis, valueAxis1, candle);
                plot.add(subplot1, 3);


                TimeSeriesCollection dataset2 = CandleStickViewStrategy.createVolumeDataset(chartResultSet);
                if (dataset2 != null) {
                    XYBarRenderer rr2 = new XYBarRenderer();
                    rr2.setDefaultToolTipGenerator(new StandardXYToolTipGenerator("<html><b>{0}:</b><br>{1}<br>{2}</html>", new SimpleDateFormat("yyyy-MM-dd"), new DecimalFormat("#,###.00")));

                    XYPlot subplot2 = new XYPlot(dataset2, timeAxis, valueAxis2, rr2);
                    plot.add(subplot2, 1);
                }

                plot.setOrientation(PlotOrientation.VERTICAL);

                JFreeChart chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, plot, false);
                ChartPanel cp = new ChartPanel(theme.apply(chart));

                return cp;
            }
        });
    }

    public String getDescription() {
        return "Candlestick";
    }

    public Icon getIcon() {
        return DBIcons.CHART_CANDLESTICK;
    }

    public String toString() {
        return CandleStickViewStrategy.class.getSimpleName() + "[" + getDescription() + "]";
    }


    public List<ExampleView> getExamples() {
        String description = "A Candlestick showing price movements and fluctuating volume over a period of 6 weeks";
        String name = "Prices going up";
        double[] close = KdbFunctions.add(KdbFunctions.mul(KdbFunctions.til(30), 2.0D), 55.0D);
        double[] open = KdbFunctions.add(KdbFunctions.til(30), 60.0D);
        double[] high = KdbFunctions.add(close, 30.0D);
        double[] low = KdbFunctions.add(close, -20.0D);
        Date[] arrayOfDate = ExampleTestCases.getWeekDays(2014, 3, 17, 30);

        double[] volume = {3.0D, 9.0D, 6.0D, 5.0D, 4.0D, 7.0D, 8.0D, 2.0D, 13.0D, 3.0D, 9.0D, 6.0D, 5.0D, 4.0D, 7.0D, 8.0D, 2.0D, 13.0D, 3.0D, 9.0D, 6.0D, 5.0D, 4.0D, 7.0D, 8.0D, 2.0D, 13.0D, 3.0D, 9.0D, 6.0D};

        ResultSet resultSet = new SimpleResultSet(new String[]{"t", "high", "low", "open", "close", "volume"}, new Object[]{arrayOfDate, high, low, open, close, volume});


        TestCase testCase = new TestCase(name, resultSet, "{ c:55+2*til 30; ([] t:raze 2014.03.17+(7*til 6)+\\:til 5; high:c+30; low:c-20; open:60+til 30; close:c; volume:30#3 9 6 5 4 7 8 2 13) }[]");
        ExampleView fullColEV = new ExampleView(name, description, testCase);


        ResultSet resultSetNoVol = new SimpleResultSet(new String[]{"t", "high", "low", "open", "close"}, new Object[]{arrayOfDate, high, low, open, close});


        name = "Rising Prices, No Volume";
        description = "A candlestick showing only price movements, no volume column.";
        testCase = new TestCase(name, resultSetNoVol, "{ c:55+2*til 30; ([] t:raze 2014.03.17+(7*til 6)+\\:til 5; high:c+30; low:c-20; open:60+til 30; close:c) }[]");
        ExampleView noVolColEV = new ExampleView(name, description, testCase);


        ResultSet resultSetOnlyHighLow = new SimpleResultSet(new String[]{"t", "high", "low"}, new Object[]{arrayOfDate, high, low});


        name = "Rising Prices, Only High Low Columns Shown";
        description = "A candlestick showing only high low prices.";
        testCase = new TestCase(name, resultSetOnlyHighLow, "{ c:55+2*til 30; ([] t:raze 2014.03.17+(7*til 6)+\\:til 5; high:c+30; low:c-20) }[]");
        ExampleView onlyHighLowEV = new ExampleView(name, description, testCase);


        return ImmutableList.of(fullColEV, noVolColEV, onlyHighLowEV);
    }

    public String getQueryEg(JdbcTypes jdbcType) {
        return jdbcType.equals(JdbcTypes.KDB) ? "{ c:55+2*til 30; ([] t:raze 2014.03.17+(7*til 6)+\\:til 5; high:c+30; low:c-20; open:60+til 30; close:c; volume:30#3 9 6 5 4 7 8 2 13) }[]" : null;
    }

    public String getFormatExplainationHtml() {
        return "<ol><li>" + Joiner.on("</li><li>").join(FORMATA) + "</li></ol>";
    }

    public String getFormatExplaination() {
        return Joiner.on("\r\n").join(FORMATA);
    }


    public Component getControlPanel() {
        return null;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\CandleStickViewStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */