package com.timestored.sqldash.chart;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.timestored.connections.JdbcTypes;
import com.timestored.theme.Icon;

import java.awt.Component;
import java.util.List;

abstract class AbstractCategoryViewStrategy
        implements ViewStrategy {
    public static final List<ExampleView> EXAMPLES;
    private static final String[] FORMATA = {"The first string columns are used as category labels.", "Whatever numeric columns appear after the strings represents a separate series in the chart."};

    static {
        ExampleView ev1 = new ExampleView("Multiple Series with Time X-Axis", "2 series are shown, one for Sales, One for Costs. x-axis is in months.", ExampleTestCases.MONTHLY_COSTS_SALES);

        ExampleView ev2 = new ExampleView("Single Category", "One number column generates one series.", ExampleTestCases.COUNTRY_STATS_GDP_ONLY);

        ExampleView ev3 = new ExampleView("Multiple series", "Each column is a new series/colored bar.", ExampleTestCases.COUNTRY_STATS);

        ExampleView ev4 = new ExampleView("Multiple series with many categories on the X-axis", "Each column is a new series/colored bar. When there are a lot of categories, some labels are hidden.", ExampleTestCases.MONTHLY_COSTS_SALES_OVER_MANY_YEARS);

        EXAMPLES = ImmutableList.of(ev1, ev2, ev3, ev4);
    }

    private final String title;
    private final Icon icon;

    public AbstractCategoryViewStrategy(String title, Icon icon) {
        this.title = Preconditions.checkNotNull(title);
        this.icon = Preconditions.checkNotNull(icon);
    }

    public String getFormatExplainationHtml() {
        return "<ol><li>" + Joiner.on("</li><li>").join(FORMATA) + "</li></ol>";
    }

    public String getFormatExplaination() {
        return Joiner.on("\r\n").join(FORMATA);
    }

    public List<ExampleView> getExamples() {
        return EXAMPLES;
    }

    public String getQueryEg(JdbcTypes jdbcType) {
        if (jdbcType.equals(JdbcTypes.KDB)) {
            return ExampleTestCases.MONTHLY_COSTS_SALES.getKdbQuery();
        }
        return null;
    }

    public Component getControlPanel() {
        return null;
    }

    public String getDescription() {
        return this.title;
    }

    public Icon getIcon() {
        return this.icon;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\AbstractCategoryViewStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */