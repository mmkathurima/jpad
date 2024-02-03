package io.jpad;

import com.timestored.misc.HtmlUtils;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.JPanel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.BorderLayout;

public class ReadOnlyTextPanel
        extends JPanel {
    private final RSyntaxTextArea rTextArea;
    private final RTextScrollPane rTextScrollPane;

    public ReadOnlyTextPanel(String syntaxConstant) {
        this.rTextArea = new RSyntaxTextArea();
        this.rTextArea.setSyntaxEditingStyle(syntaxConstant);
        this.rTextArea.setEditable(false);
        this.rTextScrollPane = new RTextScrollPane(this.rTextArea, true);
        this.setLayout(new BorderLayout());
        this.add(this.rTextScrollPane);
        this.rTextArea.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                HtmlUtils.browse(JPadLtd.getRedirectPage(e.getURL().toString(), "editorHyperlink"));
            }
        });
    }

    public void setText(String text) {
        this.rTextArea.setText(text);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\ReadOnlyTextPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */