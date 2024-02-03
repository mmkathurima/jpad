package com.timestored.sqldash.chart;

import com.google.common.base.Preconditions;
import com.google.common.io.Files;
import com.timestored.kdb.KdbTestHelper;
import com.timestored.misc.HtmlUtils;
import com.timestored.misc.IOUtils;
import com.timestored.swingxx.SaveableFrame;

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class ChartPhpHelpGenerator {
    private static final Logger LOG = Logger.getLogger(ChartPhpHelpGenerator.class.getName());
    private static final int VIEW_WIDTH = 410;
    private static final int VIEW_HEIGHT = 320;
    private static final ViewStrategy TAB_VS = DataTableViewStrategy.getInstance(false);

    private static String generateBody(ChartTheme chartTheme, String title) {
        StringBuilder sb = new StringBuilder();
        sb.append("<h1>" + title + "</h1>");
        sb.append("<p>For each of the chart types their data format expected together with some examples are shown below</p>");

        sb.append("<ul class='contentListing'>");
        for (ViewStrategy vs : ViewStrategyFactory.getStrategies()) {
            sb.append("<li><a href='#").append(HtmlUtils.clean(vs.getDescription())).append("'>").append(HtmlUtils.clean(vs.getDescription())).append("</a>").append("</li>");
        }

        sb.append("</ul>");

        for (ViewStrategy vs : ViewStrategyFactory.getStrategies()) {

            sb.append("<div class='viewStrategy'>");
            sb.append("<h1 id='");
            sb.append(HtmlUtils.clean(vs.getDescription()));
            sb.append("'>");
            sb.append(vs.getDescription()).append("</h1>");
            sb.append("<h3>").append("Format").append("</h3>");
            sb.append("<p>").append(vs.getFormatExplainationHtml()).append("</p>");

            sb.append("<div class='exampleView'>");
            for (ExampleView exampleView : vs.getExamples()) {
                sb.append("<h4>").append(exampleView.getName()).append("</h4>");
                sb.append("<p>").append(exampleView.getDescription()).append("</p>");

                String imgSrc = "images/" + getImgPath(vs, exampleView, chartTheme);
                HtmlUtils.appendImage(sb, imgSrc, exampleView.getDescription(), 320, 410);

                if (!vs.equals(TAB_VS)) {
                    imgSrc = "images//" + getTabImgPath(TAB_VS, exampleView);
                    HtmlUtils.appendImage(sb, imgSrc, exampleView.getDescription(), 320, 410);
                }

                sb.append("<p>KDB Code </p>");
                String kdbCode = exampleView.getTestCase().getKdbQuery();
                HtmlUtils.appendQCodeArea(sb, kdbCode);
            }
            sb.append("</div>");

            sb.append("</div>");
        }

        sb.append("\r\n\r\n");
        return sb.toString();
    }

    private static String getImgPath(ViewStrategy viewStrategy, ExampleView exampleView, ChartTheme chartTheme) {
        return HtmlUtils.clean(viewStrategy.getDescription()) + "-" + HtmlUtils.clean(exampleView.getName()) + "-" + HtmlUtils.clean(chartTheme.getTitle()) + ".png";
    }

    private static String getTabImgPath(ViewStrategy viewStrategy, ExampleView exampleView) {
        return HtmlUtils.clean(viewStrategy.getDescription()) + "-" + HtmlUtils.clean(exampleView.getName()) + ".png";
    }

    public static void generate(File outdir) throws IOException, InterruptedException, InvocationTargetException, SQLException {
        Preconditions.checkArgument(outdir.isDirectory());
        for (ChartTheme ct : ViewStrategyFactory.getThemes()) {
            String name = "chart-examples-" + HtmlUtils.clean(ct.getTitle()).toLowerCase();
            String title = "sqlDashboards Example Charts " + ct.getTitle() + " Theme";

            LOG.info("Generating html body for theme: " + ct.getTitle());
            String body = generateBody(ct, title);

            String html = HtmlUtils.getXhtmlTop(title) + body + HtmlUtils.getXhtmlBottom();
            IOUtils.writeStringToFile(html, new File(outdir, name + ".html"));

            String php = HtmlUtils.getTSTemplateTop(title) + body + HtmlUtils.getTSTemplateBottom();
            IOUtils.writeStringToFile(php, new File(outdir, name + ".php"));
        }
        generateImages(new File(outdir, "images"));
    }

    private static void generateImages(File outDir) throws IOException, InterruptedException, InvocationTargetException, SQLException {
        Connection conn = KdbTestHelper.getNewConn();
        for (ViewStrategy vs : ViewStrategyFactory.getStrategies()) {

            LOG.info("Generating Images for ViewStrategy:" + vs.getDescription());

            for (ExampleView exampleView : vs.getExamples()) {

                String query = exampleView.getTestCase().getKdbQuery();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("q)" + query);

                JdbcChartPanel jdbcChartPanel = ViewStrategyFactory.getJdbcChartpanel();
                jdbcChartPanel.update(rs);
                EventQueue.invokeAndWait(new Runnable() {
                    public void run() {
                        jdbcChartPanel.validate();
                        jdbcChartPanel.repaint();
                    }
                });

                jdbcChartPanel.setViewStrategy(TAB_VS);
                clearEventQueue();
                if (!vs.equals(TAB_VS)) {
                    File tabFile = new File(outDir, getTabImgPath(TAB_VS, exampleView));
                    Files.createParentDirs(tabFile);
                    SaveableFrame.saveComponentImage(jdbcChartPanel, 410, 320, tabFile, false);
                }

                for (ChartTheme chartTheme : ViewStrategyFactory.getThemes()) {

                    jdbcChartPanel.setTheme(chartTheme);
                    jdbcChartPanel.setViewStrategy(vs);
                    clearEventQueue();

                    File file = new File(outDir, getImgPath(vs, exampleView, chartTheme));
                    Files.createParentDirs(file);
                    LOG.info("Generating Image: " + file.getAbsolutePath());
                    SaveableFrame.saveComponentImage(jdbcChartPanel, 410, 320, file, false);
                }

                st.close();
            }
        }

        KdbTestHelper.killAnyOpenProcesses();
    }

    public static void clearEventQueue() throws InterruptedException, InvocationTargetException {
        EventQueue.invokeAndWait(new Runnable() {
            public void run() {
                System.out.println("clearing EDT");
            }
        });
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\ChartPhpHelpGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */