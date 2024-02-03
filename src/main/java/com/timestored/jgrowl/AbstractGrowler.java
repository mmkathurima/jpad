package com.timestored.jgrowl;

import javax.swing.ImageIcon;
import java.util.logging.Level;

public abstract class AbstractGrowler
        implements Growler {
    public void show(String message, String title, ImageIcon imageIcon) {
        this.show(Level.INFO, message, title, imageIcon, false);
    }

    public void show(String message, String title) {
        this.show(Level.INFO, message, title);
    }

    public void show(String message) {
        this.show(Level.INFO, message);
    }

    public void showInfo(String message, String title) {
        this.show(Level.INFO, message, title);
    }

    public void showWarning(String message, String title) {
        this.show(Level.WARNING, message, title);
    }

    public void showSevere(String message, String title) {
        this.show(Level.SEVERE, message, title);
    }

    public void show(Level level, String message) {
        this.show(level, message, null);
    }

    public void show(Level level, String message, String title) {
        this.show(level, message, title, null, false);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\jgrowl\AbstractGrowler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */