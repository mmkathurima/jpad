package com.timestored.command;

import javax.swing.Icon;
import javax.swing.KeyStroke;

public interface Command {
    Icon getIcon();

    String getTitle();

    String getDetailHtml();

    KeyStroke getKeyStroke();

    void perform();

    String getTitleAdditional();
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\command\Command.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */