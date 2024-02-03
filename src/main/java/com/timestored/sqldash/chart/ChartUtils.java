package com.timestored.sqldash.chart;

import com.timestored.swingxx.SaveableFrame;

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ChartUtils {
    private static final int HEIGHT = 600;
    private static final int WIDTH = 800;

    static void save(ViewStrategy viewStrategy, ResultSet resultSet, File file) throws IOException {
        save(viewStrategy, resultSet, file, 800, 600, false);
    }

    public static void save(ViewStrategy viewStrategy, ResultSet resultSet, File file, int width, int height, boolean watermark) throws IOException {
        save(viewStrategy, resultSet, file, width, height, watermark, null);
    }

    public static void save(ViewStrategy viewStrategy, ResultSet resultSet, File file, int width, int height, boolean watermark, ChartTheme chartTheme) throws IOException {
        JdbcChartPanel jdbcChartPanel = ViewStrategyFactory.getJdbcChartpanel(viewStrategy);
        if (chartTheme != null) {
            jdbcChartPanel.setTheme(chartTheme);
        }
        jdbcChartPanel.update(resultSet);

        try {
            EventQueue.invokeAndWait(new Runnable() {
                public void run() {
                    jdbcChartPanel.validate();
                    jdbcChartPanel.repaint();
                }
            });
        } catch (InterruptedException e) {
        } catch (InvocationTargetException e) {
        }
        ChartFormatException cfe = jdbcChartPanel.getLastChartFormatException();
        if (cfe != null) {
            throw cfe;
        }
        SaveableFrame.saveComponentImage(jdbcChartPanel, width, height, file, watermark);
    }

    public static void queryKdbAndSave(ViewStrategy viewStrategy, String query, File file, Connection conn) throws IOException {
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("q)" + query);
            save(viewStrategy, rs, file, 800, 600, false);
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\ChartUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */