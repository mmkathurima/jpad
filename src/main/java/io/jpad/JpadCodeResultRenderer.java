package io.jpad;

import io.jpad.model.JPadCode;
import io.jpad.model.ResultRenderer;
import io.jpad.model.RunResult;

import javax.swing.Action;
import javax.swing.ImageIcon;
import java.awt.Component;
import java.util.List;

public class JpadCodeResultRenderer
        implements ResultRenderer {
    public static final String NAME = "jpadCode";
    private JPadCode code;

    public void running(JPadCode code) {
    }

    public void resultReturned(RunResult runResult) {
    }

    public void compiling(JPadCode code) {
        this.code = code;
    }


    public void compiled(RunResult runResult) {
    }

    public String getLatestRendering() {
        return (this.code == null) ? "" : this.code.getRawCode();
    }

    public void displayRequested(Component component, String name) {
    }

    public String getTabName() {
        return "jpadCode";
    }

    public Component getComponent() {
        return null;
    }

    public ImageIcon getImageIcon() {
        return null;
    }

    public List<Action> getActions() {
        return null;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\JpadCodeResultRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */