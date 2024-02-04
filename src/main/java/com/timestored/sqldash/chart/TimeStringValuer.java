package com.timestored.sqldash.chart;

import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.renderer.StringValues;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.*;
import java.util.HashMap;
import java.util.Map;

public class TimeStringValuer
        implements StringValue {
    private static final long serialVersionUID = 1L;
    private final Map<Class<?>, Format> formatters;

    public TimeStringValuer() {
        this.formatters = new HashMap<>();
        this.formatters.put(Time.class, new SimpleDateFormat("HH:mm:ss.SSS"));
        this.formatters.put(Date.class, new SimpleDateFormat("yyyy-MM-dd"));
        this.formatters.put(Date.class, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS"));
        this.formatters.put(Timestamp.class, new TimestampFormat());
    }

    public boolean isSupported(Class<?> key) {
        return this.formatters.containsKey(key);
    }

    public String getString(Object o) {
        if (o != null) {
            Format f = this.formatters.get(o.getClass());
            if (f != null) {
                return f.format(o);
            }
        }
        return StringValues.TO_STRING.getString(o);
    }

    private static class TimestampFormat
            extends Format {
        private static final long serialVersionUID = 1L;

        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        DecimalFormat df = new DecimalFormat("000");

        private TimestampFormat() {
        }

        public StringBuffer format(Object o, StringBuffer toAppendTo, FieldPosition pos) {
            if (o instanceof Timestamp) {
                Timestamp ts = (Timestamp) o;
                String s = this.f.format(ts) + this.df.format((ts.getNanos() % 1000000 / 1000));
                return toAppendTo.append(s);
            }
            return this.f.format(o, toAppendTo, pos);
        }

        public Object parseObject(String source, ParsePosition pos) {
            return this.f.parseObject(source, pos);
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\TimeStringValuer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */