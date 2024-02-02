package io.jpad;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.timestored.docs.Document;
import com.timestored.misc.HtmlUtils;
import com.timestored.theme.Theme;
import org.fife.ui.rsyntaxtextarea.ErrorStrip;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.Gutter;
import org.fife.ui.rtextarea.GutterIconInfo;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;
import java.util.logging.Logger;


class MyEditor {
    private static final Logger log = Logger.getLogger(MyEditor.class.getName());
    private final JPanel p;
    private final Document document;
    private final RSyntaxTextArea rTextArea;
    private final RTextScrollPane rTextScrollPane;
    private final ErrorStrip errorStrip;
    private final List<GutterIconInfo> gutterIconsShown = Lists.newArrayList();

    private MyEditor(Document document) {
        this.document = Preconditions.checkNotNull(document);
        this.rTextArea = new RSyntaxTextArea();
        this.rTextArea.setSyntaxEditingStyle("text/java");
        this.rTextArea.setCodeFoldingEnabled(true);
        this.rTextScrollPane = new RTextScrollPane(this.rTextArea, true);

        this.errorStrip = new ErrorStrip(this.rTextArea);
        this.rTextScrollPane.setIconRowHeaderEnabled(true);
        this.rTextScrollPane.getGutter().setBookmarkIcon(Theme.CIcon.LAMBDA_ELEMENT.get16());
        this.rTextScrollPane.getGutter().setBookmarkingEnabled(true);
        this.p = new JPanel(new BorderLayout());
        this.p.add(this.rTextScrollPane);
        this.p.add(this.errorStrip, "After");
        this.rTextArea.setText(document.getContent());

        this.rTextArea.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                HtmlUtils.browse(JPadLtd.getRedirectPage(e.getURL().toString(), "editorHyperlink"));
            }
        });
    }

    public static MyEditor getEditorComponent(Document document) {
        return new MyEditor(document);
    }

    public JPanel getP() {
        return this.p;
    }

    public Document getDocument() {
        return this.document;
    }

    public JTextArea getJTextArea() {
        return this.rTextArea;
    }

    public void removeAllErrorHighlights() {
        this.rTextArea.removeAllLineHighlights();
        Gutter gutter = this.rTextScrollPane.getGutter();
        for (GutterIconInfo gii : this.gutterIconsShown) {
            gutter.removeTrackingIcon(gii);
        }
        this.gutterIconsShown.clear();
    }

    public void addHighlight(int lineNumber, ErrorLevel errorLevel, String msg) {
        try {
            Gutter gutter = this.rTextScrollPane.getGutter();
            GutterIconInfo gi = gutter.addLineTrackingIcon(lineNumber, errorLevel.getcIcon().get16(), msg);
            this.gutterIconsShown.add(gi);
            Color color = errorLevel.getColor();
            if (color != null) {
                this.rTextArea.addLineHighlight(lineNumber, color);
            }
        } catch (BadLocationException e) {
            log.severe("addLineHighlight failed as BadLocationException:" + e);
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\MyEditor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */