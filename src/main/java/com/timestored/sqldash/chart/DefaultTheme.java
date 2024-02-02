package com.timestored.sqldash.chart;

import com.google.common.base.Preconditions;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.util.List;
import java.util.Locale;


class DefaultTheme
        implements ChartTheme {
    private static final StandardXYBarPainter barPainter = new StandardXYBarPainter();
    private static final StandardBarPainter sbarPainter = new StandardBarPainter();
    private static final DefaultTheme INSTANCE = new DefaultTheme(new LightColorScheme(), "Default", "Default");

    private final ColorScheme colorScheme;

    private final StandardChartTheme chartTheme;
    private final DrawingSupplier drawingSupplier;
    private final String description;
    private final String title;

    private DefaultTheme(final ColorScheme colorScheme, String title, String description) {
        this.colorScheme = Preconditions.checkNotNull(colorScheme);
        this.title = Preconditions.checkNotNull(title);
        this.description = Preconditions.checkNotNull(description);


        this.chartTheme = (StandardChartTheme) StandardChartTheme.createJFreeTheme();
        this.chartTheme.setXYBarPainter(barPainter);
        this.chartTheme.setBarPainter(sbarPainter);

        this.chartTheme.setShadowVisible(false);
        this.chartTheme.setShadowPaint(colorScheme.getGridlines());

        this.chartTheme.setPlotBackgroundPaint(colorScheme.getBG());
        this.chartTheme.setDomainGridlinePaint(colorScheme.getGridlines());
        this.chartTheme.setRangeGridlinePaint(colorScheme.getGridlines());
        this.chartTheme.setPlotOutlinePaint(colorScheme.getGridlines());
        this.chartTheme.setChartBackgroundPaint(colorScheme.getBG());
        this.chartTheme.setTitlePaint(colorScheme.getFG());


        this.chartTheme.setAxisLabelPaint(colorScheme.getText());
        this.chartTheme.setLabelLinkPaint(colorScheme.getFG());


        this.chartTheme.setLegendItemPaint(colorScheme.getText());
        this.chartTheme.setLegendBackgroundPaint(colorScheme.getBG());


        if (Locale.getDefault().getLanguage().equals(Locale.SIMPLIFIED_CHINESE.getLanguage())) {
            Font oldExtraLargeFont = this.chartTheme.getExtraLargeFont();
            Font oldLargeFont = this.chartTheme.getLargeFont();
            Font oldRegularFont = this.chartTheme.getRegularFont();
            Font oldSmallFont = this.chartTheme.getSmallFont();

            Font extraLargeFont = new Font("Sans-serif", oldExtraLargeFont.getStyle(), oldExtraLargeFont.getSize());
            Font largeFont = new Font("Sans-serif", oldLargeFont.getStyle(), oldLargeFont.getSize());
            Font regularFont = new Font("Sans-serif", oldRegularFont.getStyle(), oldRegularFont.getSize());
            Font smallFont = new Font("Sans-serif", oldSmallFont.getStyle(), oldSmallFont.getSize());


            this.chartTheme.setExtraLargeFont(extraLargeFont);
            this.chartTheme.setLargeFont(largeFont);
            this.chartTheme.setRegularFont(regularFont);
            this.chartTheme.setSmallFont(smallFont);
        }

        this.drawingSupplier = new DefaultDrawingSupplier() {
            private static final long serialVersionUID = 1L;
            final Color[] colors = colorScheme.getColorArray();
            int i = 0;
            int j = 0;

            public Paint getNextPaint() {
                return this.colors[this.i++ % this.colors.length];
            }

            public Paint getNextFillPaint() {
                return this.colors[this.j++ % this.colors.length];
            }
        };
        this.chartTheme.setDrawingSupplier(this.drawingSupplier);
    }


    public static ChartTheme getInstance(ColorScheme colorScheme, String title, String description) {
        return new DefaultTheme(colorScheme, title, description);
    }

    public static ChartTheme getInstance() {
        return INSTANCE;
    }

    public static void setAxisColor(ValueAxis valueAxis, ColorScheme colorScheme) {
        valueAxis.setAxisLinePaint(colorScheme.getFG());
        valueAxis.setTickMarkPaint(colorScheme.getText());
        valueAxis.setTickLabelPaint(colorScheme.getText());
    }

    public JFreeChart apply(JFreeChart chart) {
        Plot p = chart.getPlot();
        p.setDrawingSupplier(this.drawingSupplier);

        p.setForegroundAlpha(0.8F);
        chart.setAntiAlias(true);
        chart.setTextAntiAlias(true);

        this.chartTheme.apply(chart);

        if (chart.getPlot() instanceof CombinedDomainXYPlot) {

            List<Plot> plots = ((CombinedDomainXYPlot) chart.getPlot()).getSubplots();
            for (Plot plot : plots) {
                int domainAxisCount = ((XYPlot) plot).getDomainAxisCount();
                int rangeAxisCount = ((XYPlot) plot).getRangeAxisCount();
                int i;
                for (i = 0; i < domainAxisCount; i++) {
                    setAxisColor(((XYPlot) plot).getDomainAxis(i), this.colorScheme);
                }
                for (i = 0; i < rangeAxisCount; i++) {
                    setAxisColor(((XYPlot) plot).getRangeAxis(i), this.colorScheme);
                }
            }

        } else {

            Plot plot = chart.getPlot();
            if (plot instanceof XYPlot) {
                XYPlot xyPlot = (XYPlot) plot;
                int domainAxisCount = xyPlot.getDomainAxisCount();
                int rangeAxisCount = xyPlot.getRangeAxisCount();
                int i;
                for (i = 0; i < domainAxisCount; i++) {
                    setAxisColor(xyPlot.getDomainAxis(i), this.colorScheme);
                }
                for (i = 0; i < rangeAxisCount; i++) {
                    setAxisColor(xyPlot.getRangeAxis(i), this.colorScheme);
                }
            }
        }


        if (chart.getPlot() instanceof CategoryPlot) {
            CategoryPlot categoryPlot = (CategoryPlot) chart.getPlot();
            categoryPlot.setDomainGridlinesVisible(true);
            CategoryAxis categoryAxis = categoryPlot.getDomainAxis();
            categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);
            double margin = 0.02D;
            categoryAxis.setCategoryMargin(0.02D);
            categoryAxis.setLowerMargin(0.0D);
            categoryAxis.setUpperMargin(0.0D);

            categoryAxis.setAxisLinePaint(this.colorScheme.getGridlines());
            categoryAxis.setTickMarkPaint(this.colorScheme.getFG());
            categoryAxis.setTickLabelPaint(this.colorScheme.getFG());

            setAxisColor(categoryPlot.getRangeAxis(), this.colorScheme);
        } else if (chart.getPlot() instanceof PiePlot) {
            PiePlot piePlot = (PiePlot) chart.getPlot();
            piePlot.setLabelOutlinePaint(this.colorScheme.getFG());
            piePlot.setLabelLinkPaint(this.colorScheme.getFG());
        }
        return chart;
    }

    public boolean showChartLegend() {
        return true;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public Color getForegroundColor() {
        return this.colorScheme.getFG();
    }

    public Color getBackgroundColor() {
        return this.colorScheme.getBG();
    }

    public Color getAltBackgroundColor() {
        return this.colorScheme.getAltBG();
    }

    public Color getSelectedBackgroundColor() {
        return this.colorScheme.getSelectedBG();
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\DefaultTheme.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */