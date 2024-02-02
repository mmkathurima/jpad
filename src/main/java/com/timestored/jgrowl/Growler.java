package com.timestored.jgrowl;

import javax.swing.ImageIcon;
import java.util.logging.Level;

public interface Growler {
    void show(String paramString1, String paramString2, ImageIcon paramImageIcon);

    void show(String paramString1, String paramString2);

    void show(String paramString);

    void showInfo(String paramString1, String paramString2);

    void showWarning(String paramString1, String paramString2);

    void showSevere(String paramString1, String paramString2);

    void show(Level paramLevel, String paramString1, String paramString2);

    void show(Level paramLevel, String paramString);

    void show(Level paramLevel, String paramString1, String paramString2, ImageIcon paramImageIcon, boolean paramBoolean);
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\jgrowl\Growler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */