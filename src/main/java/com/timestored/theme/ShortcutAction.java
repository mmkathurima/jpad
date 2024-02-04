package com.timestored.theme;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

public abstract class ShortcutAction
        extends AbstractAction {
    private static final long serialVersionUID = 1L;

    public ShortcutAction(String text, Icon icon, int mnemonicAccel) {
        this(text, icon, text, mnemonicAccel, mnemonicAccel);
    }

    public ShortcutAction(String text, Icon icon, String desc) {
        this(text, icon, desc, null, 0);
    }

    public ShortcutAction(String text, Icon icon, String desc, Integer mnemonic, int acceleratorKey) {
        super(text, (icon != null) ? icon.get16() : null);
        this.putValue("ShortDescription", desc);
        if (mnemonic != null) {
            this.putValue("MnemonicKey", mnemonic);
        }

        if (0 != acceleratorKey) {
            KeyStroke k = KeyStroke.getKeyStroke(acceleratorKey, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());

            this.putValue("AcceleratorKey", k);
        }
    }

    public abstract void actionPerformed(ActionEvent paramActionEvent);
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\theme\ShortcutAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */