package com.timestored.sqldash;

import com.google.common.base.Joiner;
import com.google.common.io.Files;
import com.timestored.connections.ConnectionManager;
import com.timestored.connections.ServerConfig;
import com.timestored.misc.CmdRunner;
import com.timestored.sqldash.chart.ChartFormatException;
import com.timestored.sqldash.chart.ChartUtils;
import joptsimple.OptionException;
import joptsimple.OptionSet;

import javax.sql.rowset.CachedRowSet;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class SqlChart {
    private static final Logger LOG = Logger.getLogger(SqlChart.class.getName());

    public static void main(String... args) {
        Logger globalLogger = Logger.getLogger("");
        Handler[] handlers = globalLogger.getHandlers();
        for (Handler handler : handlers) {
            globalLogger.removeHandler(handler);
        }

        System.exit(run(args));
    }

    public static int run(String... args) {
        int exitCode = 0;

        if (args.length > 0) {

            Exception ex = null;
            try {
                LOG.info("Generating Chart for args: " + Joiner.on(" ").join(args));
                OptionSet o = ChartParams.parse(args);
                if (o.has("?")) {
                    try {
                        ChartParams.printHelpOn(System.out);
                    } catch (IOException e) {
                        throw new IOException("Error displaying help.");
                    }
                } else {
                    generate(ChartParams.getChartParams(o));
                }
            } catch (IOException | IllegalArgumentException e) {
                ex = e;
            } catch (SQLException e) {
                ex = new SQLException("SQL Error: " + e.getMessage());
            } catch (OptionException e) {
            }

            if (ex != null) {
                System.err.println(ex.getMessage());
                exitCode = 1;
            }
        } else {

            try {
                ChartParams.printHelpOn(System.out);
            } catch (IOException e) {
                System.err.println("Error printing help.");
            }
        }

        return exitCode;
    }

    public static int testGenerate(String arg) {
        return run(CmdRunner.parseCommand(arg));
    }

    public static void generate(ChartParams chartParams) throws IOException, SQLException {
        Connection conn;
        ServerConfig sc = chartParams.serverConfig;
        try {
            Class.forName(sc.getJdbcType().getDriver());
        } catch (ClassNotFoundException e) {
            throw new IOException("Database driver could not be loaded.");
        }

        try {
            conn = DriverManager.getConnection(sc.getUrl(), sc.getUsername(), sc.getPassword());
        } catch (SQLException e) {
            throw new IOException("Error getting database connection: " + e.getMessage());
        }
        CachedRowSet rs = ConnectionManager.executeQuery(sc, chartParams.query, conn);
        boolean showWatermark = !SDLicenser.isPermissioned(SDLicenser.Section.PRO);
        File file = chartParams.file;
        try {
            Files.createParentDirs(file);
            ChartUtils.save(chartParams.viewStrategy, rs, file, chartParams.width, chartParams.height, showWatermark, chartParams.chartTheme);
        } catch (ChartFormatException e) {
            String msg = "Error: " + e.getMessage() + "\r\n\r\nFormat Expected:\r\n";
            msg = msg + chartParams.viewStrategy.getFormatExplaination();
            throw new IOException(msg);
        } catch (IOException e) {
            throw new IOException("Error creating necessary output folders/files: " + file.getPath());
        }
        try {
            conn.close();
        } catch (SQLException e) {
        }

        System.out.println(file.getAbsolutePath());
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\SqlChart.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */