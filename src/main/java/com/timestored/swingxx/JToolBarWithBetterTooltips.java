package com.timestored.swingxx;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;


public class JToolBarWithBetterTooltips
        extends JToolBar {
    private static final long serialVersionUID = 1L;

    public JToolBarWithBetterTooltips(String title) {
        super(title);
    }

    public JButton add(Action a) {
        JButton b = super.add(a);

        Object o = a.getValue("AcceleratorKey");
        Object deso = a.getValue("ShortDescription");
        String t = "";
        if (deso != null && deso instanceof String) {
            t = (String) deso;
        }
        if (o != null && o instanceof KeyStroke) {
            KeyStroke ks = (KeyStroke) o;
            t = t + " (" + ks.toString().replace("pressed", "+") + ")";
        }
        b.setToolTipText(t);

        return b;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\swingxx\JToolBarWithBetterTooltips.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */