package com.timestored.sqldash.chart;

import java.awt.Color;

public class DarkColorScheme
        implements ColorScheme {
    private static final Color TXT_COLOR = Color.decode("#DDDDDD");
    private static final Color FG_COLOR = Color.decode("#EEEEEE");
    private static final Color BG_COLOR = Color.decode("#111111");
    private static final Color ALT_BG_COLOR = Color.decode("#333333");
    private static final Color SELECTED_BG_COLOR = Color.decode("#630802");

    public Color getBG() {
        return BG_COLOR;
    }

    public Color getAltBG() {
        return ALT_BG_COLOR;
    }

    public Color getSelectedBG() {
        return SELECTED_BG_COLOR;
    }

    public Color getFG() {
        return FG_COLOR;
    }

    public Color getText() {
        return TXT_COLOR;
    }

    public Color getGridlines() {
        return Color.LIGHT_GRAY;
    }

    public Color[] getColorArray() {
        return LightColorScheme.SERIES_COLORS;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\DarkColorScheme.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */