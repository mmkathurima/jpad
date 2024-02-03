package com.timestored.sqldash.exampledb;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.timestored.connections.ConnectionManager;
import com.timestored.connections.DBTestRunner;
import com.timestored.connections.DBTestRunnerFactory;
import com.timestored.connections.ServerConfig;
import com.timestored.misc.HtmlUtils;
import com.timestored.misc.IOUtils;
import com.timestored.sqldash.ChartParams;
import com.timestored.sqldash.SqlChart;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Logger;

public class ExampleDbHtmlGenerator {
    private static final Logger LOG = Logger.getLogger(ExampleDbHtmlGenerator.class.getName());

    private static final String NL = "\r\n";

    private static final int IMG_WIDTH = 500;

    private static final int IMG_HEIGHT = 300;

    public static void generatePages(ExampleChartDB exampleChartDB, File outDir) throws IOException, SQLException {
        outDir.mkdirs();
        Map<String, ExampleChartQuery> charts = generate(outDir, exampleChartDB);

        String title = exampleChartDB.getName() + " Database Example sqlDashboards Charts";

        String sqlFilename = exampleChartDB.getName().replace(' ', '-').toLowerCase() + ".sql";

        String html = generateHtml(exampleChartDB, charts, title, sqlFilename);

        String initSql = Joiner.on("\r\n").join(exampleChartDB.getInitSQL(true));
        IOUtils.writeStringToFile(initSql, new File(outDir, sqlFilename));

        String htmlFile = HtmlUtils.getXhtmlTop(title) + html + HtmlUtils.getXhtmlBottom();
        String phpFile = HtmlUtils.getTSTemplateTop(title) + html + HtmlUtils.getXhtmlBottom();

        IOUtils.writeStringToFile(htmlFile, new File(outDir, "index.html"));
        IOUtils.writeStringToFile(phpFile, new File(outDir, "index.php"));
    }

    private static String generateHtml(ExampleChartDB exampleChartDB, Map<String, ExampleChartQuery> charts, String title, String sqlFilename) {
        LOG.info("generateHtml for " + exampleChartDB.getName());

        StringBuilder sb = new StringBuilder();
        sb.append("\r\n");

        String dbName = exampleChartDB.getDbType().getNiceName();

        sb.append("<h1>").append(title).append("</h1>");
        sb.append("\r\n");
        sb.append("<p>").append(exampleChartDB.getDescription()).append("</p>");
        sb.append("\r\n");

        String initSql = Joiner.on("\r\n").join(exampleChartDB.getInitSQL(true));
        if (initSql != null && initSql.trim().length() > 0) {
            sb.append("<a href='" + sqlFilename + "'>Download " + sqlFilename + "</a>");
            HtmlUtils.appendQCodeArea(sb, initSql.substring(0, Math.min(initSql.length(), 1000)));
        }

        sb.append("<div class='conListing'> <h4>Contents</h4><ol>");
        for (Map.Entry<String, ExampleChartQuery> e : charts.entrySet()) {
            ExampleChartQuery ecq = e.getValue();
            String lnk = ecq.getSupportedViewStrategy().getDescription() + " of " + ecq.getName();
            sb.append("<li><a href='#").append(HtmlUtils.cleanAtt(HtmlUtils.clean(ecq.getName()))).append("'>").append(lnk).append("</a></li>");
        }

        sb.append("</ol></div>");

        sb.append("<div id='chart-container'>").append("\r\n");
        for (Map.Entry<String, ExampleChartQuery> e : charts.entrySet()) {

            String imgPath = e.getKey();
            ExampleChartQuery ecq = e.getValue();

            sb.append("<div class='qeg' id='" + HtmlUtils.cleanAtt(HtmlUtils.clean(ecq.getName())) + "'>");
            String v = ecq.getSupportedViewStrategy().getDescription() + " of ";
            sb.append("<h2>").append(v).append(ecq.getName()).append("</h2>").append("\r\n");

            HtmlUtils.appendImage(sb, imgPath, ecq.getName(), 300, 500);

            HtmlUtils.appendQCodeArea(sb, ecq.getSqlQuery());
            sb.append("<p>").append(ecq.getDescription()).append("</p>").append("\r\n");
            sb.append("</div>").append("\r\n");
        }

        sb.append("</div>").append("\r\n");
        return sb.toString();
    }

    private static Map<String, ExampleChartQuery> generate(File parentFolder, ExampleChartDB exampleChartDB) throws SQLException, IOException {
        Preconditions.checkArgument(parentFolder.isDirectory());

        DBTestRunner dbRunner = DBTestRunnerFactory.getDbRunner(exampleChartDB.getDbType());
        if (dbRunner == null) {
            throw new IllegalArgumentException("DB type not supported to run");
        }
        ConnectionManager connMan = dbRunner.start();
        ServerConfig sc = dbRunner.getServerConfig();

        Map<String, ExampleChartQuery> generatedCharts = Maps.newHashMap();

        try {
            for (String initSql : exampleChartDB.getInitSQL(false)) {
                LOG.fine("sending initSql: " + initSql.substring(0, Math.min(initSql.length(), 33)));
                boolean ranOk = connMan.execute(sc, initSql);
                if (!ranOk) {
                    LOG.warning("Could not run initSQL: " + initSql);
                }
            }

            String PRE = HtmlUtils.clean(exampleChartDB.getDbType().name()) + "-chart-";
            for (ExampleChartQuery exq : exampleChartDB.getQueries()) {

                String filename = PRE + HtmlUtils.clean(exq.getName()) + ".png";

                if (generatedCharts.containsKey(filename)) {
                    LOG.severe("Filename overlap between charts within ExampleChartDb");
                }

                ChartParams chartParams = (new ChartParams.ChartParamsBuilder()).serverConfig(sc).height(300).width(500).file(new File(parentFolder, filename)).viewStrategy(exq.getSupportedViewStrategy()).query(exq.getSqlQuery()).build();

                SqlChart.generate(chartParams);
                generatedCharts.put(filename, exq);
            }
        } finally {

            dbRunner.stop();
        }

        return generatedCharts;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\exampledb\ExampleDbHtmlGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */