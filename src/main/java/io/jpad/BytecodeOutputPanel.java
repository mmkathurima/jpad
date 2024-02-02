package io.jpad;

import com.timestored.theme.Theme;
import io.jpad.model.JPadCode;
import io.jpad.model.PanelResultRenderer;
import io.jpad.model.RunResult;

import java.awt.BorderLayout;


public class BytecodeOutputPanel
        extends PanelResultRenderer {
    public static final String NAME = "bytecodeOut";
    private static final long serialVersionUID = 1L;
    private final ReadOnlyTextPanel javapPanel = new ReadOnlyTextPanel("text/java");
    private String s = "";

    public BytecodeOutputPanel() {
        super("bytecodeOut", Theme.CIcon.EDIT_COMMENT.get16());
        setLayout(new BorderLayout());
        add(this.javapPanel, "Center");
    }

    public void compiling(JPadCode code) {
        this.s = "";
        this.javapPanel.setText(this.s);
    }

    public void compiled(RunResult runResult) {
        this.s = runResult.getJavapOutput();
        this.javapPanel.setText(this.s);
    }

    public String getLatestRendering() {
        return this.s;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\BytecodeOutputPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */