package com.timestored.jgrowl;

import javax.swing.JFrame;
import javax.swing.JWindow;

public interface Theme {
    long getFadeTimerDelay();

    int getSpaceBetweenItems();

    int getMoveSpeed();

    float getFadeRate();

    int getFadeRangeMinimum();

    JWindow getWindow(Growl paramGrowl, JFrame paramJFrame);

    int getLeftRuler(JFrame paramJFrame);

    int getTopSpacer();
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\jgrowl\Theme.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */