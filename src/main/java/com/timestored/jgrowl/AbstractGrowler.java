package com.timestored.jgrowl;

import javax.swing.ImageIcon;
import java.util.logging.Level;


public abstract class AbstractGrowler
        implements Growler {
    public void show(String message, String title, ImageIcon imageIcon) {
        show(Level.INFO, message, title, imageIcon, false);
    }


    public void show(String message, String title) {
        show(Level.INFO, message, title);
    }


    public void show(String message) {
        show(Level.INFO, message);
    }


    public void showInfo(String message, String title) {
        show(Level.INFO, message, title);
    }


    public void showWarning(String message, String title) {
        show(Level.WARNING, message, title);
    }


    public void showSevere(String message, String title) {
        show(Level.SEVERE, message, title);
    }


    public void show(Level level, String message) {
        show(level, message, null);
    }


    public void show(Level level, String message, String title) {
        show(level, message, title, null, false);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\jgrowl\AbstractGrowler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */