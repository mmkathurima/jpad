package com.timestored.sqldash.chart;

import java.awt.Color;

public class InverseColorScheme
        implements ColorScheme {
    private final Color bg;
    private final Color fg;
    private final Color text;
    private final Color gridLines;
    private final Color[] seriesColors;
    private final Color altBgColor;
    private final Color selectedBgColor;

    public InverseColorScheme(ColorScheme colorScheme) {
        this.bg = invert(colorScheme.getBG());
        this.fg = invert(colorScheme.getFG());
        this.text = invert(colorScheme.getText());
        this.gridLines = invert(colorScheme.getGridlines());
        this.altBgColor = invert(colorScheme.getAltBG());
        this.selectedBgColor = invert(colorScheme.getSelectedBG());

        Color[] originalColors = colorScheme.getColorArray();
        this.seriesColors = new Color[originalColors.length];
        for (int i = 0; i < originalColors.length; i++) {
            this.seriesColors[i] = invert(originalColors[i]);
        }
    }

    private static Color invert(Color c) {
        return new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue());
    }

    public Color getBG() {
        return this.bg;
    }

    public Color getAltBG() {
        return this.altBgColor;
    }

    public Color getSelectedBG() {
        return this.selectedBgColor;
    }

    public Color getFG() {
        return this.fg;
    }

    public Color getGridlines() {
        return this.gridLines;
    }

    public Color[] getColorArray() {
        return this.seriesColors;
    }

    public Color getText() {
        return this.text;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\InverseColorScheme.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */