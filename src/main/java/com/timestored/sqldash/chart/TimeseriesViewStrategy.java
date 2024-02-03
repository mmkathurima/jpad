package com.timestored.sqldash.chart;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.timestored.connections.JdbcTypes;
import com.timestored.theme.Icon;
import com.timestored.theme.Theme;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import java.awt.Component;
import java.sql.Date;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public enum TimeseriesViewStrategy
        implements ViewStrategy {
    INSTANCE;
    private static final String[] FORMATA;
    private static final DecimalFormat DEC_FORMAT;
    private static final String TOOLTIP_FORMAT = "<html><b>{0}:</b><br>{1}<br>{2}</html>";
    private static final String KDB_QUERY = "([] dt:2013.01.01+til 21; cosineWave:cos a; \r\n\t sineWave:sin a:0.6*til 21)";

    static {
        DEC_FORMAT = new DecimalFormat("#,###.##");

        FORMATA = new String[]{"The first date/time column found will be used for the x-axis.", "Each numerical column represents one time series line on the chart."};
    }

    public static SimpleDateFormat getDateFormat(int timeType) {
        SimpleDateFormat dateFormat = null;
        if (timeType == 91) {
            dateFormat = new SimpleDateFormat("d-MMM-yyyy hh:mm:ss");
        } else if (timeType == 92) {
            dateFormat = new SimpleDateFormat("hh:mm:ss");
        }
        return dateFormat;
    }

    public UpdateableView getView(ChartTheme theme) {
        return new HardRefreshUpdateableView(new HardRefreshUpdateableView.ViewGetter() {
            public Component getView(ResultSet rs, ChartResultSet colResultSet) throws ChartFormatException {
                if (colResultSet == null) throw new ChartFormatException("Could not create chart result set.");
                TimeSeriesCollection dataset = new TimeSeriesCollection();
                ChartResultSet.TimeCol timeCol = colResultSet.getTimeCol();
                if (timeCol == null) throw new ChartFormatException("No Time Column Found.");
                this.add(colResultSet, dataset);
                JFreeChart chart = ChartFactory.createTimeSeriesChart("", "Time", "Value", dataset, true, true, false);
                ChartPanel cp = new ChartPanel(theme.apply(chart), false, true, true, false, true);
                XYItemRenderer renderer = cp.getChart().getXYPlot().getRenderer();
                SimpleDateFormat dateFormat = getDateFormat(timeCol.getType());
                if (dateFormat != null) {
                    StandardXYToolTipGenerator ttg = new StandardXYToolTipGenerator("<html><b>{0}:</b><br>{1}<br>{2}</html>", dateFormat, DEC_FORMAT);
                    renderer.setDefaultToolTipGenerator(ttg);
                }
                return cp;
            }

            private void add(ChartResultSet colResultSet, TimeSeriesCollection dataset) {
                ChartResultSet.TimeCol timeCol = colResultSet.getTimeCol();
                if (timeCol != null) {
                    RegularTimePeriod[] timePeriods = timeCol.getRegularTimePeriods();
                    for (ChartResultSet.NumericCol nc : colResultSet.getNumericColumns()) {
                        TimeSeries tSeries = new TimeSeries("" + nc.getLabel());
                        int row = 0;
                        for (double d : nc.getDoubles()) tSeries.addOrUpdate(timePeriods[row++], d);
                        if (!tSeries.isEmpty())
                            dataset.addSeries(tSeries);
                    }
                }
            }
        });
    }

    public String getDescription() {
        return "Time Series";
    }

    public String getFormatExplainationHtml() {
        return "<ol><li>" + Joiner.on("</li><li>").join(FORMATA) + "</li></ol>";
    }

    public Icon getIcon() {
        return Theme.CIcon.CHART_CURVE;
    }

    public String getQueryEg(JdbcTypes jdbcType) {
        return jdbcType.equals(JdbcTypes.KDB) ? "([] dt:2013.01.01+til 21; cosineWave:cos a; \r\n\t sineWave:sin a:0.6*til 21)" : null;
    }

    public String toString() {
        return TimeseriesViewStrategy.class.getSimpleName() + "[" + this.getDescription() + "]";
    }

    public List<ExampleView> getExamples() {
        final String description = "A sine/cosine wave over a period of days.";
        final String name = "Day Sines";
        String[] colNames = {"dt", "cosineWave", "sineWave"};
        double[] a = KdbFunctions.mul(KdbFunctions.til(21), 0.6D);
        Date[] dt = ExampleTestCases.getDays(2013, 1, 1, 21);
        Object[] colValues = {dt, KdbFunctions.cos(a), KdbFunctions.sin(a)};
        ResultSet resultSet = new SimpleResultSet(colNames, colValues);
        ExampleView sineEV = new ExampleView(name, description, new TestCase(name, resultSet, "([] dt:2013.01.01+til 21; cosineWave:cos a; \r\n\t sineWave:sin a:0.6*til 21)"));
        return ImmutableList.of(sineEV);
    }

    public String getFormatExplaination() {
        return Joiner.on("\r\n").join(FORMATA);
    }

    public Component getControlPanel() {
        return null;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\TimeseriesViewStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */