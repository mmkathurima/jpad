package com.timestored.sqldash;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.timestored.connections.JdbcTypes;
import com.timestored.connections.ServerConfig;
import com.timestored.sqldash.chart.BarChartViewStrategy;
import com.timestored.sqldash.chart.ChartTheme;
import com.timestored.sqldash.chart.ViewStrategy;
import com.timestored.sqldash.chart.ViewStrategyFactory;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class ChartParams {
    private static final OptionParser p = getParser();
    final String query;
    final ServerConfig serverConfig;
    final ChartTheme chartTheme;
    final ViewStrategy viewStrategy;
    final int width;
    final int height;
    final File file;

    private ChartParams(String query, ServerConfig serverConfig, ChartTheme chartTheme, ViewStrategy viewStrategy, int width, int height, File file) {
        if (query == null || query.trim().length() < 1) {
            throw new IllegalArgumentException("Must specify sql execute statement or enter sql at console.");
        }
        Preconditions.checkArgument((width > 0));
        Preconditions.checkArgument((height > 0));

        this.query = query;
        this.serverConfig = Preconditions.checkNotNull(serverConfig);
        this.chartTheme = Preconditions.checkNotNull(chartTheme);
        this.viewStrategy = Preconditions.checkNotNull(viewStrategy);
        this.width = width;
        this.height = height;
        this.file = Preconditions.checkNotNull(file);
    }

    public static OptionParser getOptionParser() {
        return p;
    }

    public static OptionSet parse(String... arguments) {
        return p.parse(arguments);
    }

    private static OptionParser getParser() {
        OptionParser p = new OptionParser();

        p.acceptsAll(List.of(new String[]{"h", "host"}), "SQL server host that will be queried.").withRequiredArg().describedAs("host_name").defaultsTo("localhost", new String[0]).ofType(String.class);


        p.acceptsAll(List.of(new String[]{"P", "port"}), "The TCP/IP port number to use for the SQL Server connection.").withRequiredArg().describedAs("port_num");


        p.acceptsAll(List.of(new String[]{"D", "database"}), "The database to use.").withRequiredArg().describedAs("db_name").ofType(String.class);


        p.acceptsAll(List.of(new String[]{"u", "user"}), "Username used to connect to SQL server.").withRequiredArg().describedAs("user_name").ofType(String.class);


        p.acceptsAll(List.of(new String[]{"p", "password"}), "Password used to connect to SQL server.").withRequiredArg().describedAs("password").ofType(String.class);


        p.acceptsAll(List.of(new String[]{"e", "execute"}), "Execute the selected sql statement.").withRequiredArg().describedAs("sql_statement").ofType(String.class);


        p.acceptsAll(List.of(new String[]{"t", "theme"}), "Set the color theme for the chart. Options available: light,dark,pastel").withRequiredArg().describedAs("color_theme").defaultsTo("light", new String[0]).ofType(String.class);


        p.acceptsAll(List.of(new String[]{"s", "servertype"}), "The type of sql server being queried. Valid values include:kdb,mysql,postgres,mssql,h2.").withRequiredArg().describedAs("server_type").required().ofType(String.class);


        String desc = "Set the selected chart type. Options available: " + Joiner.on(", ").join(getChartTypes());

        p.acceptsAll(List.of(new String[]{"c", "chart"}), desc).withRequiredArg().describedAs("chart_type").defaultsTo("barchart");


        p.acceptsAll(List.of(new String[]{"W", "width"}), "Set the width of the chart output").withRequiredArg().describedAs("output_width").defaultsTo("400");


        p.acceptsAll(List.of(new String[]{"H", "height"}), "Set the height of the chart output").withRequiredArg().describedAs("output_height").defaultsTo("300");


        p.acceptsAll(List.of(new String[]{"o", "out"}), "The name of the destination image file.").withRequiredArg().describedAs("file_name").defaultsTo("out.png");


        p.acceptsAll(List.of(new String[]{"?", "help"}), "Display a help message and exit.").forHelp();

        p.allowsUnrecognizedOptions();
        return p;
    }


    public static ChartParams getChartParams(OptionSet o) throws IOException {
        String qry = "";
        if (o.has("execute")) {
            qry = "" + o.valueOf("execute");
        } else {
            qry = readConsole();
        }

        JdbcTypes jdbcTypes = getJdbcType(("" + o.valueOf("servertype")).toLowerCase());
        if (jdbcTypes == null) {
            throw new IllegalArgumentException("Server Type must be one of: kdb,mysql,postgres,mssql.");
        }

        int port = jdbcTypes.getDefaultPort();
        if (o.hasArgument("port")) {
            port = Integer.parseInt("" + o.valueOf("port"));
        }
        if (port < 0) {
            throw new NumberFormatException("port must be positive");
        }

        String ctype = ("" + o.valueOf("chart")).toLowerCase();
        ViewStrategy vs = getChartType(ctype);
        if (vs == null) {
            throw new IllegalArgumentException("Valid chart type must be selected.");
        }

        ChartTheme ct = getChartTheme(("" + o.valueOf("theme")).toLowerCase());


        File file = new File("" + o.valueOf("out"));

        String database = null;
        if (o.hasArgument("database")) {
            database = "" + o.valueOf("database");
        }
        ServerConfig sc = new ServerConfig("" + o.valueOf("host"), port, (String) o.valueOf("user"), (String) o.valueOf("password"), "servername", jdbcTypes, null, database);


        return (new ChartParamsBuilder()).query(qry).file(file).serverConfig(sc).chartTheme(ct).viewStrategy(vs).height(Integer.parseInt("" + o.valueOf("height"))).width(Integer.parseInt("" + o.valueOf("width"))).build();
    }

    private static ViewStrategy getChartType(String ctype) {
        ViewStrategy vs = null;
        for (ViewStrategy v : ViewStrategyFactory.getStrategies()) {
            if (v.getDescription().toLowerCase().replace(" ", "").equals(ctype)) {
                vs = v;
            }
        }
        return vs;
    }

    public static String getChartType(ViewStrategy viewStrategy) {
        return viewStrategy.getDescription().toLowerCase().replace(" ", "");
    }

    private static ChartTheme getChartTheme(String s) {
        for (ChartTheme ct : ViewStrategyFactory.getThemes()) {
            if (ct.getTitle().equalsIgnoreCase(s)) {
                return ct;
            }
        }
        return ViewStrategyFactory.LIGHT_THEME;
    }

    private static Collection<String> getChartThemes() {
        List<ChartTheme> ts = ViewStrategyFactory.getThemes();
        Collection<String> r = new ArrayList<String>(ts.size());
        for (ChartTheme ct : ts) {
            r.add(ct.getTitle().toLowerCase());
        }
        return r;
    }

    private static Collection<String> getChartTypes() {
        List<ViewStrategy> strats = ViewStrategyFactory.getStrategies();
        Collection<String> r = new ArrayList<String>(strats.size());
        for (ViewStrategy v : strats) {
            r.add(v.getDescription().toLowerCase().replace(" ", ""));
        }
        return r;
    }

    private static JdbcTypes getJdbcType(String s) {
        JdbcTypes stype = null;
        if (s.equals("kdb")) {
            stype = JdbcTypes.KDB;
        } else if (s.equals("mysql")) {
            stype = JdbcTypes.MYSQL;
        } else if (s.equals("postgres")) {
            stype = JdbcTypes.POSTGRES;
        } else if (s.equals("mssql")) {
            stype = JdbcTypes.MSSERVER;
        } else if (s.equals("h2")) {
            stype = JdbcTypes.H2;
        }
        return stype;
    }

    public static String readConsole() throws IOException {
        BufferedReader f = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        String s = "";
        while ((s = f.readLine()) != null) {
            sb.append(s);
        }
        String q = sb.toString();
        return q;
    }

    public static void printHelpOn(PrintStream out) throws IOException {
        p.printHelpOn(out);
    }

    public static String toArgs(ServerConfig sc) {
        String st = sc.isKDB() ? "kdb" : "h2";
        return " --host " + sc.getHost() + " --port " + sc.getPort() + getArg("user", sc.getUsername()) + getArg("pass", sc.getPassword()) + getArg("servertype", st) + getArg("database", sc.getDatabase());
    }

    private static String getArg(String name, String val) {
        if (val != null && val.trim().length() > 0) {
            return " --" + name + " " + val;
        }
        return "";
    }

    public static ChartParamsBuilder builder() {
        return new ChartParamsBuilder();
    }

    public static class ChartParamsBuilder {
        ChartTheme chartTheme = ViewStrategyFactory.LIGHT_THEME;
        ViewStrategy viewStrategy = BarChartViewStrategy.INSTANCE;
        int width = 400, height = 300;
        private String query;
        private ServerConfig serverConfig;
        private File file;

        public ChartParamsBuilder query(String query) {
            this.query = query;
            return this;
        }

        public ChartParamsBuilder serverConfig(ServerConfig serverConfig) {
            this.serverConfig = serverConfig;
            return this;
        }

        public ChartParamsBuilder chartTheme(ChartTheme chartTheme) {
            this.chartTheme = chartTheme;
            return this;
        }

        public ChartParamsBuilder viewStrategy(ViewStrategy viewStrategy) {
            this.viewStrategy = viewStrategy;
            return this;
        }

        public ChartParamsBuilder width(int width) {
            this.width = width;
            return this;
        }

        public ChartParamsBuilder height(int height) {
            this.height = height;
            return this;
        }

        public ChartParamsBuilder file(File file) {
            this.file = file;
            return this;
        }

        public ChartParams build() {
            return new ChartParams(this.query, this.serverConfig, this.chartTheme, this.viewStrategy, this.width, this.height, this.file);
        }

        public String toString() {
            return "ChartParams.ChartParamsBuilder(query=" + this.query + ", serverConfig=" + this.serverConfig + ", chartTheme=" + this.chartTheme + ", viewStrategy=" + this.viewStrategy + ", width=" + this.width + ", height=" + this.height + ", file=" + this.file + ")";
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\ChartParams.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */