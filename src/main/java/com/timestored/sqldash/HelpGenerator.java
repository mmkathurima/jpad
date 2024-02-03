package com.timestored.sqldash;

import com.google.common.collect.Lists;
import com.timestored.misc.HtmlUtils;
import com.timestored.misc.IOUtils;
import com.timestored.sqldash.exampledb.ExampleChartDB;
import com.timestored.sqldash.exampledb.ExampleDbHtmlGenerator;
import com.timestored.sqldash.exampledb.StockDBAdapter;

import javax.swing.JFileChooser;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

public class HelpGenerator {
    private static final Logger LOG = Logger.getLogger(HelpGenerator.class.getName());

    public static void main(String... args) throws IOException, InterruptedException, InvocationTargetException, SQLException {
        File targetDir = null;

        if (args.length > 0) {
            targetDir = new File(args[0]);
        } else {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(1);
            if (fc.showOpenDialog(null) == 0) {
                targetDir = fc.getSelectedFile();
            }
        }

        if (targetDir != null) {
            generate(targetDir);
        } else {
            LOG.info("No valid targetDir specified");
        }
        System.exit(0);
    }

    public static void generate(File outDir) throws IOException, InterruptedException, InvocationTargetException, SQLException {
        File helpFolder = new File(outDir, "help");
        helpFolder.mkdir();

        File parent = new File(helpFolder, "database");
        parent.mkdir();

        List<String> folderLinks = Lists.newArrayList();

        Collection<ExampleChartDB> exampleDBs = StockDBAdapter.getStockExampleChartDBs();

        for (ExampleChartDB egDB : exampleDBs) {
            String fname = HtmlUtils.clean(egDB.getDbType().getNiceName() + "-" + egDB.getName());
            folderLinks.add("<a href='" + fname + "'>" + fname + "</a>");
            ExampleDbHtmlGenerator.generatePages(egDB, new File(parent, fname));
        }

        String sb = HtmlUtils.getXhtmlTop("Database Charting Examples") +
                HtmlUtils.toList(folderLinks) +
                HtmlUtils.getXhtmlBottom();
        IOUtils.writeStringToFile(sb, new File(parent, "index.html"));
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\HelpGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */