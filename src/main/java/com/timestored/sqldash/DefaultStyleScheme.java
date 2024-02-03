package com.timestored.sqldash;

import com.timestored.sqldash.chart.ChartTheme;
import com.timestored.sqldash.chart.ViewStrategyFactory;

public class DefaultStyleScheme
        implements StyleScheme {
    private static final ChartTheme vsTheme = ViewStrategyFactory.getThemes().get(0);

    private static final DefaultStyleScheme INSTANCE = new DefaultStyleScheme();

    public static DefaultStyleScheme getInstance() {
        return INSTANCE;
    }

    public ChartTheme getViewStrategyTheme() {
        return vsTheme;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\DefaultStyleScheme.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */