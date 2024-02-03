package com.timestored.sqldash.theme;

import com.timestored.theme.Icon;
import com.timestored.theme.IconHelper;

import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;

public enum DBIcons
        implements Icon {
    TEXTFIELD("textfield.png"),
    CHECKBOX("checkbox.png"),
    COMBOBOX("combobox.png"),
    LIST("list.png"),
    RADIOBUTTON("radiobutton.png"),
    SLIDER("slider.png"),
    SPINNER("spinner.png"),
    TEXTAREA("textarea.png"),
    GO_BOTTOM("go-bottom.png"),
    GO_DOWN("go-down.png"),
    GO_TOP("go-top.png"),
    GO_UP("go-up.png"),
    CHART_HEATMAP("heatmap.png"),
    CHART_CANDLESTICK("candlestick.png"),
    CHART_LINE("chart-line.png"),
    CHART_AREA("chart-area.png"),
    CHART_SCATTER_PLOT("chart-scatter.png"),
    CHART_BUBBLE("chart-bubble.png"),
    CHART_COLUMN("chart-column.png"),
    CHART_PIE("chart-pie.png"),
    CHART_CURVE("chart-curve.png"),
    CHART_BAR("chart-bar.png"),
    CHART_HISTOGRAM("chart-histogram.png");

    public final ImageIcon imageIcon32;
    private final ImageIcon imageIcon;
    private final ImageIcon imageIcon16;

    DBIcons(String loc) {
        ImageIcon[] icons = IconHelper.getDiffSizesOfIcon(DBIcons.class.getResource(loc));
        this.imageIcon = icons[0];
        this.imageIcon16 = icons[1];
        this.imageIcon32 = icons[2];
    }

    public ImageIcon get() {
        return this.imageIcon;
    }

    public ImageIcon get16() {
        return this.imageIcon16;
    }

    public ImageIcon get32() {
        return this.imageIcon32;
    }

    public BufferedImage getBufferedImage() {
        return IconHelper.getBufferedImage(this.imageIcon);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\theme\DBIcons.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */