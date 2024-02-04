package io.jpad;

import com.google.common.io.Resources;
import com.timestored.swingxx.SwingUtils;
import com.timestored.theme.Theme;
import io.jpad.model.JPadCode;
import io.jpad.model.PanelResultRenderer;
import io.jpad.model.RunResult;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.web.WebView;
import org.jetbrains.annotations.Nullable;

import javax.swing.JToolBar;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HtmlResultPanel extends PanelResultRenderer {
    public static final String NAME = "htmlOut";
    private static final Logger log = Logger.getLogger(HtmlResultPanel.class.getName());
    private static final long serialVersionUID = 1L;
    private final String css;
    private JFXPanel jfxPanel;
    private String html;
    private RunResult runResult;
    private File htmlLastSaveFile;
    private File csvLastSaveFile;

    public HtmlResultPanel() {
        super("htmlOut", Theme.CIcon.TEXT_HTML.get16());
        String c;
        this.html = "";
        this.htmlLastSaveFile = null;
        this.csvLastSaveFile = null;
        this.setLayout(new java.awt.BorderLayout());
        JToolBar toolBar = new JToolBar("htmlResultBar");

        try {
            c = Resources.toString(Resources.getResource("s.css"), StandardCharsets.UTF_8);
        } catch (Exception e) {
            c = "";
        }
        this.css = c;
    }

    @Nullable
    private static File askUserToSaveAs(@Nullable File file, String content, String filetypeExtension) {
        File f = SwingUtils.askUserAndSave(filetypeExtension, file, content);
        if (f != null) {
            try {
                Desktop.getDesktop().open(f);
            } catch (IOException e) {
                log.log(Level.WARNING, "problem opening file", e);
            }
        }
        return f;
    }

    public void compiling(JPadCode code) {
        this.display("Compiling...");
    }

    public void resultReturned(RunResult runResult) {
        this.runResult = runResult;
        this.display(runResult.toHtml());
    }

    private synchronized void display(String body) {
        this.html = "<html><head><style>" + this.css + "</style></head><body>" + body + "</body></html>";

        if (this.jfxPanel == null) {
            this.jfxPanel = new JFXPanel();
            this.add(this.jfxPanel, "Center");
        }

        Platform.runLater(() -> {
            WebView webView = new WebView();
            webView.setContextMenuEnabled(false);
            this.createContextMenu(webView);
            this.jfxPanel.setScene(new Scene(webView));
            webView.getEngine().loadContent(this.html);
        });
    }

    private void createContextMenu(WebView webView) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem reload = new MenuItem("Reload");
        reload.setOnAction(e -> webView.getEngine().reload());

        MenuItem savePage = new MenuItem("Save Page...");
        savePage.setOnAction(e -> this.htmlLastSaveFile = askUserToSaveAs(this.htmlLastSaveFile, this.html, "html"));

        MenuItem saveCsv = new MenuItem("Export as csv...");
        if (this.runResult != null) {
            saveCsv.setOnAction(e -> this.csvLastSaveFile = askUserToSaveAs(this.csvLastSaveFile, this.runResult.toCsv(), "csv"));
        } else {
            saveCsv.setDisable(true);
        }

        MenuItem print = new MenuItem("Print");
        print.setOnAction(e -> webView.getEngine().print(PrinterJob.createPrinterJob()));

        contextMenu.getItems().addAll(reload, savePage, saveCsv, print);

        webView.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(webView, e.getScreenX(), e.getScreenY());
            } else {
                contextMenu.hide();
            }
        });
    }

    public String getLatestRendering() {
        return this.html;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\HtmlResultPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */