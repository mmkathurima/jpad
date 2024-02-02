package com.timestored.jgrowl;

import javax.swing.JFrame;


public class GrowlerFactory {
    static Growler getGrowler(JFrame parent, Theme theme) {
        return new GrowlerFacade(new FadingGrowler(parent, theme));
    }


    public static Growler getGrowler(JFrame parent) {
        return getGrowler(parent, new StandardTheme());
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\jgrowl\GrowlerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */