package com.timestored.command;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.KeyStroke;

class ActionCommand
        implements Command {
    private final Action a;

    public ActionCommand(Action a) {
        this.a = a;
    }

    public Icon getIcon() {
        return (Icon) this.a.getValue("SmallIcon");
    }

    public String getTitle() {
        return (String) this.a.getValue("Name");
    }

    public String getDetailHtml() {
        return (String) this.a.getValue("ShortDescription");
    }

    public KeyStroke getKeyStroke() {
        return (KeyStroke) this.a.getValue("AcceleratorKey");
    }

    public void perform() {
        this.a.actionPerformed(null);
    }

    public String toString() {
        return this.getTitle();
    }

    public String getTitleAdditional() {
        return "";
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\command\ActionCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */