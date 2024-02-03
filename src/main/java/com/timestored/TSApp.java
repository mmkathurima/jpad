package com.timestored;

import com.timestored.misc.ErrorReporter;
import com.timestored.theme.AboutDialog;

import javax.swing.JFrame;

public class TSApp {
    private static final int MINS_BETWEEN = 720;
    private final AppDescription appDescription;

    public TSApp(AppDescription appDescription) {
        this.appDescription = appDescription;
    }

    public void showAboutDialog(JFrame frame, String licenseTxt) {
        (new AboutDialog(frame, this.appDescription.getAppTitle(), this.appDescription.getIcon(), this.appDescription.getHtmlTitle(), this.appDescription.getVersion(), licenseTxt)).setVisible(true);
    }

    public ErrorReporter getErrorReporter() {
        return new ErrorReporter(this.appDescription.getErrorURL(), this.appDescription.getTechEmail(), this.appDescription.getAppTitle() + " Bug Report " + this.appDescription.getVersion(), 720);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\TSApp.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */