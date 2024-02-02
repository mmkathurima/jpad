package com.timestored.sqldash.chart;

import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.DefaultDrawingSupplier;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Paint;
import java.awt.Point;


class ColorToolTipListener
        implements ChartMouseListener {
    private static final ColorToolTipListener INSTANCE = new ColorToolTipListener();
    private final Paint[] PAINTS = DefaultDrawingSupplier.DEFAULT_PAINT_SEQUENCE;
    private final int PADDING = 5;

    private XYItemEntity lastSeenEntity = null;
    private JPopupMenu lastPopupMenu = null;


    public static final ColorToolTipListener getInstance() {
        return INSTANCE;
    }


    public void chartMouseClicked(ChartMouseEvent arg0) {
    }


    public void chartMouseMoved(ChartMouseEvent cme) {
        if (cme != null && cme.getEntity() instanceof XYItemEntity) {

            XYItemEntity xyEntity = (XYItemEntity) cme.getEntity();

            if (!xyEntity.equals(this.lastSeenEntity)) {
                if (this.lastPopupMenu != null) {
                    this.lastPopupMenu.setVisible(false);
                    this.lastPopupMenu = null;
                }

                JLabel label = new JLabel(xyEntity.getToolTipText());
                this.lastPopupMenu = new JPopupMenu();
                this.lastPopupMenu.add(label);
                label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

                int seriesIdx = xyEntity.getSeriesIndex();
                Color bgColor = (Color) this.PAINTS[seriesIdx % this.PAINTS.length];
                this.lastPopupMenu.setBorder(BorderFactory.createLineBorder(bgColor));

                cme.getTrigger();
                Point loc = MouseInfo.getPointerInfo().getLocation();
                this.lastPopupMenu.setLocation(10 + (int) loc.getX(), 20 + (int) loc.getY());
                this.lastPopupMenu.setVisible(true);
                this.lastSeenEntity = xyEntity;
            }

        } else if (this.lastPopupMenu != null) {
            this.lastPopupMenu.setVisible(false);
            this.lastPopupMenu = null;

            this.lastSeenEntity = null;
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\ColorToolTipListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */