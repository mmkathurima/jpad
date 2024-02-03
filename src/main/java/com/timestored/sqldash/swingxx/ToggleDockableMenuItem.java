package com.timestored.sqldash.swingxx;

import bibliothek.gui.DockFrontend;
import bibliothek.gui.Dockable;
import bibliothek.gui.dock.event.DockFrontendAdapter;

import javax.swing.JCheckBoxMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ToggleDockableMenuItem
        extends JCheckBoxMenuItem {
    private static final long serialVersionUID = 1L;

    public ToggleDockableMenuItem(Dockable observed, DockFrontend frontend, String name) {
        super(observed.getTitleText(), observed.getTitleIcon());
        this.setName(name);

        frontend.addFrontendListener(new DockFrontendAdapter() {
            public void shown(DockFrontend f, Dockable dockable) {
                if (dockable == observed) {
                    ToggleDockableMenuItem.this.setSelected(true);
                }
            }

            public void hidden(DockFrontend f, Dockable dockable) {
                if (dockable == observed) {
                    ToggleDockableMenuItem.this.setSelected(false);
                }
            }
        });

        this.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (ToggleDockableMenuItem.this.isSelected()) {
                    frontend.show(observed);
                } else {
                    frontend.hide(observed);
                }
            }
        });

        this.setSelected(frontend.isShown(observed));
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\swingxx\ToggleDockableMenuItem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */