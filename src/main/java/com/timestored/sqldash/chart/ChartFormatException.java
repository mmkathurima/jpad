package com.timestored.sqldash.chart;

import java.io.IOException;

public class ChartFormatException
        extends IOException {
    private final String details;

    ChartFormatException(String details) {
        this.details = details;
    }

    String getDetails() {
        return this.details;
    }

    public String getMessage() {
        return this.details;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\ChartFormatException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */