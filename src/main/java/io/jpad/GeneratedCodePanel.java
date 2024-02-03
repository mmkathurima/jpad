package io.jpad;

import com.timestored.theme.Theme;
import io.jpad.model.JPadCode;
import io.jpad.model.PanelResultRenderer;
import io.jpad.model.RunResult;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.text.BadLocationException;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.logging.Logger;

public class GeneratedCodePanel
        extends PanelResultRenderer {
    private static final Logger log = Logger.getLogger(GeneratedCodePanel.class.getName());

    private static final long serialVersionUID = 1L;

    private final RSyntaxTextArea textArea;

    public GeneratedCodePanel() {
        super("generatedCode", Theme.CIcon.TEXT_HTML.get16());
        this.setLayout(new BorderLayout());

        this.textArea = new RSyntaxTextArea(20, 60);
        this.textArea.setSyntaxEditingStyle("text/java");
        this.textArea.setCodeFoldingEnabled(true);
        RTextScrollPane sp = new RTextScrollPane(this.textArea);
        this.add(sp, "Center");
    }

    public void compiling(JPadCode code) {
        this.textArea.removeAllLineHighlights();
        this.textArea.setText(code.getGeneratedCode());
    }

    public void compiled(RunResult runResult) {
        for (Diagnostic<? extends JavaFileObject> d : runResult.getDiagnostics()) {
            try {
                this.textArea.addLineHighlight((int) d.getLineNumber() - 1, Color.RED);
            } catch (BadLocationException e) {
                log.severe("addLineHighlight failed as BadLocationException:" + e);
            }
        }
    }

    public String getLatestRendering() {
        return this.textArea.getText();
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\GeneratedCodePanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */