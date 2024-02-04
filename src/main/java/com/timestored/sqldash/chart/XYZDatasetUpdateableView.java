package com.timestored.sqldash.chart;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.DefaultXYZDataset;

import java.awt.Component;
import java.sql.ResultSet;
import java.util.List;

class XYZDatasetUpdateableView
        implements UpdateableView {
    private final ChartPanel chartPanel;
    private final DefaultXYZDataset dataset;

    public XYZDatasetUpdateableView(ChartPanel chartPanel, DefaultXYZDataset dataset) {
        this.dataset = dataset;
        this.chartPanel = chartPanel;
    }

    private static void add(ChartResultSet colResultSet, DefaultXYZDataset dataset) throws ChartFormatException {
        List<ChartResultSet.NumericCol> numCols = colResultSet.getNumericColumns();
        if (numCols.size() < 3) {
            throw new ChartFormatException("Need atleast three numerical columns.");
        }

        double[][] vals = new double[3][];
        for (int i = 0; i < 3; i++) {
            vals[i] = numCols.get(i).getDoubles();
        }
        dataset.addSeries("", vals);
    }

    public void update(ResultSet rs, ChartResultSet chartResultSet) throws ChartFormatException {
        if (chartResultSet == null) {
            throw new ChartFormatException("Could not create chart result set.");
        }

        for (int s = 0; s < this.dataset.getSeriesCount(); s++) {
            this.dataset.removeSeries(this.dataset.getSeriesKey(s));
        }

        add(chartResultSet, this.dataset);

        XYPlot xyplot = this.chartPanel.getChart().getXYPlot();
        if (xyplot != null) {

            double minY = 0.0D;
            double maxY = 0.0D;
            double minX = 0.0D;
            double maxX = 0.0D;

            for (int series = 0; series < this.dataset.getSeriesCount(); series++) {
                for (int item = 0; item < this.dataset.getItemCount(series); item++) {
                    double x = this.dataset.getX(series, item).doubleValue();
                    if (x < minX) {
                        minX = x;
                    } else if (x > maxX) {
                        maxX = x;
                    }

                    double y = this.dataset.getY(series, item).doubleValue();
                    if (y < minY) {
                        minY = y;
                    } else if (y > maxY) {
                        maxY = y;
                    }
                }
            }
            List<ChartResultSet.NumericCol> numCols = chartResultSet.getNumericColumns();
            NumberAxis numberaxisX = (NumberAxis) xyplot.getDomainAxis();
            numberaxisX.setLabel(numCols.get(0).getLabel());
            numberaxisX.setRange((minX > 0.0D) ? 0.0D : (minX * 1.5D), (maxX < 0.0D) ? 0.0D : (maxX * 1.15D));

            NumberAxis numberaxisY = (NumberAxis) xyplot.getRangeAxis();
            numberaxisY.setLabel(numCols.get(1).getLabel());
            numberaxisY.setRange((minY > 0.0D) ? 0.0D : (minY * 1.5D), (maxY < 0.0D) ? 0.0D : (maxY * 1.15D));
        }
    }

    public Component getComponent() {
        return this.chartPanel;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\XYZDatasetUpdateableView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */