package io.jpad;

import com.google.common.collect.Lists;
import com.timestored.swingxx.ScrollingTextArea;
import com.timestored.theme.Theme;
import io.jpad.model.JPadCode;
import io.jpad.model.PanelResultRenderer;
import io.jpad.model.RunResult;

import javax.swing.AbstractAction;
import javax.swing.Action;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseWheelListener;
import java.util.List;


class ConsolePanel
        extends PanelResultRenderer {
    public static final String NAME = "consoleOut";
    private static final long serialVersionUID = 1L;
    private static final int FONT_SIZE = 12;
    private static final Color FG_COLOR = new Color(240, 240, 240);
    private final StringBuilder latestResult = new StringBuilder();

    private final ScrollingTextArea scText;
    private final Action clearAction = new AbstractAction("Clear", Theme.CIcon.TABLE_DELETE.get16()) {
        private static final long serialVersionUID = 1L;

        public void actionPerformed(ActionEvent e) {
            ConsolePanel.this.latestResult.setLength(0);
            ConsolePanel.this.scText.clear();
        }
    };

    public ConsolePanel() {
        super("consoleOut", Theme.CIcon.TERMINAL.get16());
        setLayout(new BorderLayout());

        this.scText = new ScrollingTextArea(FG_COLOR, Color.BLACK);
        this.scText.setTextareaFont(new Font("Monospaced", 0, 12));
        add(this.scText, "Center");
    }

    private void app(String msg) {
        this.scText.appendMessage(msg);
        this.latestResult.append(msg + '\n');
    }

    public List<Action> getActions() {
        return Lists.newArrayList(this.clearAction);
    }

    public void setCodeFont(Font f) {
        this.scText.setTextareaFont(f);
    }

    public void setMaxLength(int maxConsoleLength) {
        this.scText.setMaxLength(maxConsoleLength);
    }

    public void running(JPadCode code) {
        app("### Running.");
    }

    public String getLatestRendering() {
        return this.latestResult.toString();
    }

    public synchronized void addMouseWheelListener(MouseWheelListener l) {
        this.scText.addMouseWheelListener(l);
    }

    public void compiled(RunResult runResult) {
        app("### " + (runResult.isCompileSuccessful() ? "compiled." : "compilation Failed"));
        String compileResult = runResult.getCompileResult();
        if (!compileResult.trim().isEmpty()) {
            app(compileResult);
        }
    }

    public void compiling(JPadCode code) {
        this.latestResult.setLength(0);
        app("");
        app("###### Compiling. ######");
    }

    public void resultReturned(RunResult runResult) {
        if (runResult.isCompletedOk()) {
            app("### Run Complete.");
            String o = runResult.getOutput();
            if (o != null) {

                this.latestResult.setLength(0);

                app(runResult.getOutput());
            }
        } else {
            app("### Runtime Error.");
            app(runResult.getError());
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\ConsolePanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */