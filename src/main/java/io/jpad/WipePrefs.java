package io.jpad;

import javax.swing.JOptionPane;
import java.util.prefs.BackingStoreException;


public class WipePrefs {
    public static void main(String... args) throws BackingStoreException {
        String message = "First make sure JPad is closed!\r\n\r\nThen clicking yes below will remove:\r\n- all saved settings and \r\n- open file history. \r\nAre you sure you want to continue?";


        int choice = JOptionPane.showConfirmDialog(null, message, "Delete all settings?", 0, 2);


        if (choice == 0) {
            try {
                JPadFrame.resetDefaults(true);
            } catch (BackingStoreException e1) {
                String errMsg = "Problem accessing registry, please report as bug";
                JOptionPane.showMessageDialog(null, errMsg);
            }
        } else if (choice == 1) {
            System.out.println("no");
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\WipePrefs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */