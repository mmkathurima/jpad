package com.timestored.jgrowl;

import javax.swing.ImageIcon;
import javax.swing.UIManager;
import java.util.logging.Level;
import java.util.logging.Logger;


class GrowlerFacade
        extends AbstractGrowler {
    private static final ImageIcon INFO_ICON;
    private static final ImageIcon WARNING_ICON;
    private static final ImageIcon ERROR_ICON;
    private static final Logger LOG = Logger.getLogger(GrowlerFacade.class.getName());

    static {
        ImageIcon ii = null;
        ImageIcon wi = null;
        ImageIcon ei = null;
        try {
            ii = (ImageIcon) UIManager.getIcon("OptionPane.informationIcon");
            wi = (ImageIcon) UIManager.getIcon("OptionPane.warningIcon");
            ei = (ImageIcon) UIManager.getIcon("OptionPane.errorIcon");
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Could not get builtin icons for growlers", e);
        }

        try {
            if (ii == null) {
                ii = StandardTheme.Icon.INFO.get();
            }
            if (ei == null) {
                ei = StandardTheme.Icon.SEVERE.get();
            }
            if (wi == null) {
                wi = StandardTheme.Icon.WARNING.get();
            }
        } catch (Exception e) {
            LOG.log(Level.FINE, "Could not get user createed icons for growlers", e);
        }
        INFO_ICON = ii;
        WARNING_ICON = wi;
        ERROR_ICON = ei;
    }

    private final FadingGrowler gc;


    public GrowlerFacade(FadingGrowler growlerCore) {
        this.gc = growlerCore;
    }


    public void show(Level logLevel, String message, String title, ImageIcon imageIcon, boolean sticky) {
        ImageIcon ii = null;
        if (logLevel.equals(Level.SEVERE)) {
            ii = ERROR_ICON;
        } else if (logLevel.equals(Level.WARNING)) {
            ii = WARNING_ICON;
        } else if (logLevel.equals(Level.INFO)) {
            ii = INFO_ICON;
        }


        this.gc.show(message, title, ii, sticky, logLevel);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\jgrowl\GrowlerFacade.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */