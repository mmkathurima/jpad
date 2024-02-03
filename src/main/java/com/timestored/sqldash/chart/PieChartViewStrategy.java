package com.timestored.sqldash.chart;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.timestored.connections.JdbcTypes;
import com.timestored.sqldash.theme.DBIcons;
import com.timestored.theme.Icon;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieToolTipGenerator;
import org.jfree.chart.plot.MultiplePiePlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.util.TableOrder;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import java.awt.Component;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public enum PieChartViewStrategy
        implements ViewStrategy {
    INSTANCE;

    private static final String[] FORMATA;

    static {
        FORMATA = new String[]{"Each numerical column represents a pie chart.", "The title of each pie chart will be the column title.", "Each row will be a section of the pie and will use the row title as a label."};
    }

    public UpdateableView getView(ChartTheme theme) {
        Preconditions.checkNotNull(theme);

        return new HardRefreshUpdateableView(new HardRefreshUpdateableView.ViewGetter() {

            public Component getView(ResultSet resultSet, ChartResultSet colResultSet) throws ChartFormatException {
                if (colResultSet.getNumericColumns().size() < 1) {
                    throw new ChartFormatException("There must be atleast one number column.");
                }

                DefaultCategoryDataset catData = CategoryDatasetUpdateableView.add(colResultSet, new DefaultCategoryDataset());
                JFreeChart chart = ChartFactory.createMultiplePieChart("", catData, TableOrder.BY_ROW, true, true, false);

                if (catData.getRowCount() > 1) {
                    MultiplePiePlot plot = (MultiplePiePlot) chart.getPlot();
                    JFreeChart subchart = plot.getPieChart();
                    PiePlot p = (PiePlot) subchart.getPlot();
                    p.setToolTipGenerator(new StandardPieToolTipGenerator(Tooltip.LABEL_XY_FORMAT, new DecimalFormat("#,###.##"), NumberFormat.getPercentInstance()));

                    p.setLabelGenerator(null);
                }

                ChartPanel cp = new ChartPanel(theme.apply(chart), false, true, true, false, true);

                return cp;
            }
        });
    }

    private List<PieGroup> createPies(ChartResultSet chartResultSet) {
        List<PieGroup> res = new ArrayList<PieGroup>();
        for (ChartResultSet.NumericCol numCol : chartResultSet.getNumericColumns()) {
            DefaultPieDataset dataset = new DefaultPieDataset();
            double[] pieVals = numCol.getDoubles();
            for (int r = 0; r < pieVals.length; r++) {
                dataset.setValue(chartResultSet.getRowLabel(r), pieVals[r]);
            }
            res.add(new PieGroup(dataset, numCol.getLabel()));
        }

        return res;
    }

    public String getDescription() {
        return "Pie Chart";
    }

    public Icon getIcon() {
        return DBIcons.CHART_PIE;
    }

    public String toString() {
        return PieChartViewStrategy.class.getSimpleName() + "[" + this.getDescription() + "]";
    }

    public String getFormatExplainationHtml() {
        return "<ol><li>" + Joiner.on("</li><li>").join(FORMATA) + "</li></ol>";
    }

    public String getFormatExplaination() {
        return Joiner.on("\r\n").join(FORMATA);
    }

    public List<ExampleView> getExamples() {
        String name = "Multiple Pie Chart Example";
        String description = "Using multiple numerical columns gives a pie chart for each column.";
        ExampleView MultiPieNQ = new ExampleView(name, description, ExampleTestCases.COUNTRY_STATS);

        name = "Single Pie Chart Example";
        description = "A single numerical columns gives a single pie chart for that column.";
        ExampleView SinglePieNQ = new ExampleView(name, description, ExampleTestCases.COUNTRY_STATS_GDP_ONLY);

        return ImmutableList.of(SinglePieNQ, MultiPieNQ);
    }

    public String getQueryEg(JdbcTypes jdbcType) {
        if (jdbcType.equals(JdbcTypes.KDB)) {
            return ExampleTestCases.COUNTRY_STATS_GDP_ONLY.getKdbQuery();
        }
        return null;
    }

    public Component getControlPanel() {
        return null;
    }

    private static class PieGroup {
        private final PieDataset dataset;
        private final String title;

        private PieGroup(PieDataset pieDataset, String title) {
            this.dataset = pieDataset;
            this.title = title;
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\PieChartViewStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */