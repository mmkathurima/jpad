package com.timestored.jgrowl;

import javax.swing.JWindow;


class DisplayedItem {
    public final Growl message;
    public final JWindow frame;
    public float lifeLeft;

    public DisplayedItem(JWindow frame, Growl message) {
        this.frame = frame;
        this.message = message;
        this.lifeLeft = 1.0F;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\jgrowl\DisplayedItem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */