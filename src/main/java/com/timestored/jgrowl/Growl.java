package com.timestored.jgrowl;

import javax.swing.ImageIcon;
import java.util.logging.Level;

class Growl {
    private final String message;
    private final String title;
    private final ImageIcon imageIcon;
    private final boolean sticky;
    private final Level logLevel;

    public Growl(String message, String title, ImageIcon imageIcon, boolean sticky, Level logLevel) {
        if (message == null) {
            throw new IllegalArgumentException("message cannot be null");
        }
        this.message = message;
        this.title = title;
        this.imageIcon = imageIcon;
        this.sticky = sticky;
        this.logLevel = logLevel;
    }

    public String getMessage() {
        return this.message;
    }

    public String getTitle() {
        return this.title;
    }

    public ImageIcon getImageIcon() {
        return this.imageIcon;
    }

    public boolean isSticky() {
        return this.sticky;
    }

    public Level getLogLevel() {
        return this.logLevel;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\jgrowl\Growl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */