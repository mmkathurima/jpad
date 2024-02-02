package com.timestored.sqldash.chart;

import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.labels.StandardXYZToolTipGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.labels.XYZToolTipGenerator;

import java.text.DecimalFormat;


class Tooltip {
    public static String LABEL_XY_FORMAT = "<html><b>{0}:</b><br>{1}<br>{2}</html>";
    public static String LABEL_XYZ_FORMAT = "<html><b>{0}:</b><br>{1}<br>{2}</html>";


    public static XYToolTipGenerator getXYNumbersGenerator() {
        return new StandardXYToolTipGenerator(LABEL_XY_FORMAT, new DecimalFormat("#,###.##"), new DecimalFormat("#,###.##"));
    }


    public static XYZToolTipGenerator getXYZNumbersGenerator() {
        return new StandardXYZToolTipGenerator(LABEL_XYZ_FORMAT, new DecimalFormat("#,###.##"), new DecimalFormat("#,###.##"), new DecimalFormat("#,###.##"));
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\Tooltip.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */