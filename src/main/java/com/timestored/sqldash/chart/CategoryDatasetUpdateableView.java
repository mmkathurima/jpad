package com.timestored.sqldash.chart;

import com.google.common.base.Preconditions;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.Component;
import java.awt.Font;
import java.sql.ResultSet;
import java.text.DecimalFormat;

class CategoryDatasetUpdateableView
        implements UpdateableView {
    private static final Font TINY_FONT = new Font("Times New Roman", 0, 0);

    private final ChartPanel chartPanel;
    private final DefaultCategoryDataset dataset;

    public CategoryDatasetUpdateableView(ChartTheme theme, JFreeChart chart) {
        Preconditions.checkNotNull(chart);
        Preconditions.checkNotNull(theme);

        this.dataset = new DefaultCategoryDataset();
        chart.getCategoryPlot().setDataset(this.dataset);

        this.chartPanel = new ChartPanel(theme.apply(chart), false, true, true, false, true);
    }

    public static DefaultCategoryDataset add(ChartResultSet colResultSet, DefaultCategoryDataset dataset) {
        for (ChartResultSet.NumericCol numCol : colResultSet.getNumericColumns()) {
            double[] vals = numCol.getDoubles();
            for (int i = 0; i < vals.length; i++) {
                dataset.addValue(vals[i], numCol.getLabel(), colResultSet.getRowLabel(i));
            }
        }
        return dataset;
    }

    public void update(ResultSet rs, ChartResultSet chartRS) throws ChartFormatException {
        if (chartRS == null) {
            throw new ChartFormatException("Could not construct ResultSet.");
        }
        if (chartRS.getNumericColumns().size() < 1) {
            throw new ChartFormatException("Atleast one numeric column is required.");
        }

        JFreeChart chart = this.chartPanel.getChart();
        CategoryPlot cplot = chart.getCategoryPlot();
        CategoryItemRenderer renderer = cplot.getRenderer();

        renderer.setDefaultToolTipGenerator(new StandardCategoryToolTipGenerator(Tooltip.LABEL_XY_FORMAT, new DecimalFormat("#,###.##")));

        cplot.getDomainAxis().setLabel(chartRS.getRowTitle());
        ValueAxis rangeAxis = cplot.getRangeAxis();
        if (chartRS.getNumericColumns().size() == 1) {
            rangeAxis.setLabel(chartRS.getNumericColumns().get(0).getLabel());
        } else {
            rangeAxis.setLabel("");
        }

        this.dataset.clear();
        add(chartRS, this.dataset);

        if (this.dataset.getColumnCount() > 30) {
            CategoryAxis dAxis = cplot.getDomainAxis();
            int i = 0;
            int m = this.dataset.getColumnCount() / 7;
            for (Object key : this.dataset.getColumnKeys()) {
                if (i % m != 0) {
                    dAxis.setTickLabelFont((Comparable) key, TINY_FONT);
                }
                i++;
            }
        }

        chart.getLegend().setVisible((this.dataset.getRowCount() > 1));
    }

    public Component getComponent() {
        return this.chartPanel;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\CategoryDatasetUpdateableView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */