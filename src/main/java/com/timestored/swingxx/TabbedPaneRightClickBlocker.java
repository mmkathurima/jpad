package com.timestored.swingxx;

import javax.swing.JComponent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TabbedPaneRightClickBlocker {
    public static void install(JComponent tabbedPane) {
        MouseListener handler = findUIMouseListener(tabbedPane);
        if (handler != null) {
            tabbedPane.removeMouseListener(handler);
            tabbedPane.addMouseListener(new MouseListenerWrapper(handler));
        }
    }

    private static MouseListener findUIMouseListener(JComponent tabbedPane) {
        MouseListener[] listeners = tabbedPane.getMouseListeners();
        for (MouseListener l : listeners) {
            if (l.getClass().getName().contains("$Handler")) {
                return l;
            }
        }
        return null;
    }

    private static class MouseListenerWrapper
            implements MouseListener {
        private final MouseListener delegate;

        public MouseListenerWrapper(MouseListener delegate) {
            this.delegate = delegate;
        }

        public void mouseClicked(MouseEvent e) {
            if (e.isPopupTrigger())
                return;
            this.delegate.mouseClicked(e);
        }

        public void mousePressed(MouseEvent e) {
            if (e.isPopupTrigger())
                return;
            this.delegate.mousePressed(e);
        }

        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger())
                return;
            this.delegate.mouseReleased(e);
        }

        public void mouseEntered(MouseEvent e) {
            if (e.isPopupTrigger())
                return;
            try {
                this.delegate.mouseEntered(e);
            } catch (NullPointerException npe) {
            }
        }

        public void mouseExited(MouseEvent e) {
            if (e.isPopupTrigger())
                return;
            this.delegate.mouseExited(e);
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\swingxx\TabbedPaneRightClickBlocker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */