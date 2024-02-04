package com.timestored.sqldash.chart;

import com.google.common.base.Preconditions;

import java.sql.Date;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ExampleTestCases {
    public static final TestCase COUNTRY_STATS;
    public static final TestCase COUNTRY_STATS_WITHOUT_CONTINENT;
    public static final TestCase COUNTRY_STATS_ADJUSTED_POP;
    public static final TestCase COUNTRY_STATS_GDP_ONLY;
    public static final TestCase MONTHLY_COSTS_SALES;
    public static final TestCase MONTHLY_COSTS_SALES_OVER_MANY_YEARS;
    public static final double[] GDP = {15080.0D, 11300.0D, 4444.0D, 3114.0D, 2228.0D, 9.9D, 113.0D, 196.0D, 104.0D};
    private static final String[] COUNTRIES = {"US", "China", "japan", "Germany", "UK", "Zimbabwe", "Bangladesh", "Nigeria", "Vietnam"};
    private static final String[] CONTINENT = {"NorthAmerica", "Asia", "Asia", "Europe", "Europe", "Africa", "Asia", "Africa", "Asia"};
    private static final double[] GDP_PER_CAPITA = {48300.0D, 8400.0D, 34700.0D, 38100.0D, 36500.0D, 413.0D, 1788.0D, 732.0D, 3359.0D};

    private static final double[] POPULATION = {313847.0D, 1343239.0D, 127938.0D, 81308.0D, 63047.0D, 13010.0D, 152518.0D, 166629.0D, 87840.0D};

    private static final double[] LIFE_EXP = {77.14D, 72.22D, 80.93D, 78.42D, 78.16D, 39.01D, 61.33D, 51.01D, 70.05D};

    private static final String[] MONTHS = getMonths(2000, 0, 12);
    private static final double[] COSTS = {30.0D, 40.0D, 45.0D, 55.0D, 58.0D, 63.0D, 55.0D, 65.0D, 78.0D, 80.0D, 75.0D, 90.0D};
    private static final double[] SALES = {10.0D, 12.0D, 14.0D, 18.0D, 26.0D, 42.0D, 74.0D, 90.0D, 110.0D, 130.0D, 155.0D, 167.0D};

    static {
        String countryCol = " Country:" + toQ(COUNTRIES) + "; ";
        String numCols = "\r\n\t Population:" + toQ(POPULATION) + ";\r\n\t GDP:" + toQ(GDP) + "; \r\n\tGDPperCapita:" + toQ(GDP_PER_CAPITA) + ";  \r\n\tLifeExpectancy:" + toQ(LIFE_EXP) + ")";

        String countryQuery = "([] Continent:" + toQ(CONTINENT) + ";\r\n\t" + countryCol + numCols;
        String[] colTitles = {"Continent", "Country", "Population", "GDP", "GDPperCapita", "LifeExpectancy"};
        Object[] colValues = {CONTINENT, COUNTRIES, POPULATION, GDP, GDP_PER_CAPITA, LIFE_EXP};
        ResultSet rs = new SimpleResultSet(colTitles, colValues);
        COUNTRY_STATS = new TestCase("COUNTRY_STATS", rs, countryQuery);

        String query = "update GDPperCapita%20 from " + countryQuery;
        colValues = new Object[]{CONTINENT, COUNTRIES, POPULATION, GDP, KdbFunctions.mul(GDP_PER_CAPITA, 0.05D), LIFE_EXP};

        rs = new SimpleResultSet(colTitles, colValues);
        COUNTRY_STATS_ADJUSTED_POP = new TestCase("COUNTRY_STATS", rs, query);

        query = "([] " + countryCol + numCols;
        colTitles = new String[]{"Country", "Population", "GDP", "GDPperCapita"};
        rs = new SimpleResultSet(colTitles, new Object[]{COUNTRIES, POPULATION, GDP, GDP_PER_CAPITA});
        COUNTRY_STATS_WITHOUT_CONTINENT = new TestCase("COUNTRY_STATS_WITHOUT_CONTINENT", rs, query);

        query = "([] Country:" + toQ(COUNTRIES) + "; \r\n\t GDP:" + toQ(GDP) + ")";
        colTitles = new String[]{"Country", "GDP"};
        rs = new SimpleResultSet(colTitles, new Object[]{COUNTRIES, GDP});
        COUNTRY_STATS_GDP_ONLY = new TestCase("COUNTRY_STATS_GDP_ONLY", rs, query);

        query = "([Month:2000.01m + til 12]  \r\n\t Costs:" + toQ(COSTS) + "; \r\n\t Sales:" + toQ(SALES) + ")";

        colTitles = new String[]{"Month", "Costs", "Sales"};
        rs = new SimpleResultSet(colTitles, new Object[]{MONTHS, COSTS, SALES});
        MONTHLY_COSTS_SALES = new TestCase("MONTHLY_COSTS_SALES", rs, query);

        query = "([Month:2000.01m + til 36]  \r\n\t Costs:36#" + toQ(COSTS) + "; \r\n\t Sales:raze 0 10 20+\\:" + toQ(SALES) + ")";

        colTitles = new String[]{"Month", "Costs", "Sales"};

        double[] threeYearCosts = new double[COSTS.length * 3];
        for (int i = 0; i < threeYearCosts.length; i++) {
            threeYearCosts[i] = COSTS[i % COSTS.length];
        }
        double[] increasingSales = new double[COSTS.length * 3];
        for (int j = 0; j < 3; j++) {
            for (int k = 0; k < COSTS.length; k++) {
                increasingSales[j * COSTS.length + k] = SALES[k] + (j * 10);
            }
        }

        rs = new SimpleResultSet(colTitles, new Object[]{getMonths(2000, 0, 36), threeYearCosts, increasingSales});
        MONTHLY_COSTS_SALES_OVER_MANY_YEARS = new TestCase("MONTHLY_COSTS_SALES_OVER_MANY_YEARS", rs, query);
    }

    private static String[] getMonths(int year, int month, int count) {
        String[] r = new String[count];
        for (int i = 0; i < count; i++) {
            int m = (month + i) % 12;
            r[i] = (year + i / 12) + "-" + ((m < 9) ? "0" : "") + (m + 1);
        }
        return r;
    }

    static Date[] getDays(int year, int month, int day, int count) {
        Date[] r = new Date[count];
        for (int i = 0; i < count; i++) {
            r[i] = new Date(year - 1900, month - 1, day + i);
        }
        return r;
    }

    private static String format(Date d) {
        return (new SimpleDateFormat("yyyy-MM-dd")).format(d);
    }

    static Date[] getWeekDays(int year, int month, int day, int count) {
        Preconditions.checkArgument((count >= 0));
        Calendar cal = Calendar.getInstance();
        Date[] r = new Date[count];
        int i = 0;
        Date dt = new Date(year - 1900, month - 1, day + i - 1);
        cal.setTime(dt);
        while (i < count) {
            for (int j = 0; j < 5 && i < count; j++, i++) {
                cal.add(Calendar.DATE, 1);
                r[i] = new Date(cal.getTimeInMillis());
            }
            cal.add(Calendar.DATE, 2);
        }
        return r;
    }

    static String[] getTimes(int hour, int min, int minStep, int count) {
        String[] r = new String[count];
        for (int i = 0; i < r.length; i++) {
            int m = (min + i * minStep) % 60;
            int h = hour + (min + i * minStep) / 60;
            r[i] = ((h < 10) ? "0" : "") + h + ":" + ((m < 10) ? "0" : "") + m + ":00";
        }
        return r;
    }

    private static String toQ(double[] nums) {
        StringBuilder sb = new StringBuilder();
        for (double d : nums) {
            sb.append(d);
            sb.append(' ');
        }
        return sb.toString();
    }

    private static String toQ(String[] symSafeStrings) {
        StringBuilder sb = new StringBuilder();
        for (String s : symSafeStrings) {
            sb.append('`');
            sb.append(s);
        }
        return sb.toString();
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\ExampleTestCases.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */