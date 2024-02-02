package io.jpad.model;


import com.google.common.collect.Lists;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.Component;
import java.util.Collections;
import java.util.List;


public abstract class PanelResultRenderer
        extends JPanel
        implements ResultRenderer {
    private static final long serialVersionUID = 1L;
    private final String tabName;
    private final ImageIcon imageIcon;
    private final List<Action> actions = Lists.newArrayList();


    public PanelResultRenderer(String tabName, ImageIcon imageIcon, Action... actions) {

        this.tabName = tabName;

        this.imageIcon = imageIcon;

        Collections.addAll(this.actions, actions);

    }


    public String getTabName() {

        return this.tabName;
    }

    public ImageIcon getImageIcon() {

        return this.imageIcon;

    }

    public List<Action> getActions() {
        return this.actions;
    }


    public void compiling(JPadCode code) {
    }


    public void running(JPadCode code) {
    }


    public void resultReturned(RunResult runResult) {
    }


    public void compiled(RunResult runResult) {
    }


    public void displayRequested(Component component, String name) {
    }


    public String getLatestRendering() {

        return null;
    }

    public Component getComponent() {

        return this;

    }

}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\model\PanelResultRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */