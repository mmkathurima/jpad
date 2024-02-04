package com.timestored.jgrowl;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

class StandardTheme
        implements Theme {
    private static final Logger LOG = Logger.getLogger(Theme.class.getName());

    private static final int TOP_SPACER = 45;

    private static final int BORDER_WIDTH = 1;

    private static final int MESSAGE_WIDTH = 200;
    private static final int RIGHT_SPACER = 11;
    private static final int PAD = 3;
    private static final int SPACE_HACK = 75;

    private static Border getBorder(Growl growl, boolean hover) {
        Color c = Color.LIGHT_GRAY;
        int ll = growl.getLogLevel().intValue();
        if (ll >= Level.SEVERE.intValue()) {
            c = Color.RED;
        } else if (ll >= Level.WARNING.intValue()) {
            c = Color.ORANGE;
        }
        if (hover) {
            c = c.darker();
        }

        Border b = BorderFactory.createLineBorder(c, 1);
        if (growl.getTitle() != null) {
            b = BorderFactory.createTitledBorder(b, growl.getTitle());
        }
        return b;
    }

    public long getFadeTimerDelay() {
        return 50L;
    }

    public int getSpaceBetweenItems() {
        return 5;
    }

    public int getMoveSpeed() {
        return 10;
    }

    public float getFadeRate() {
        return 0.01F;
    }

    public int getTopSpacer() {
        return 45;
    }

    public int getFadeRangeMinimum() {
        return 200;
    }

    public int getLeftRuler(JFrame parentFrame) {
        return Math.max(0, parentFrame.getX() + parentFrame.getWidth() - 211);
    }

    public JWindow getWindow(Growl message, JFrame parentFrame) {
        JWindow frame = new JWindow();
        new BoxLayout(frame, BoxLayout.PAGE_AXIS);
        frame.setAlwaysOnTop(parentFrame.isFocused());
        frame.setMaximumSize(new Dimension(200, 2147483647));

        JPanel panel = new JPanel();
        new BoxLayout(panel, BoxLayout.PAGE_AXIS);
        JLabel label = new JLabel("<html><body style='width:124px'>" + message.getMessage() + "</body></html>");

        label.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        label.setMaximumSize(new Dimension(199, 2147483647));

        Border border = getBorder(message, false);
        Border hoverBorder = getBorder(message, true);
        panel.setBorder(border);

        frame.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                parentFrame.toFront();
                super.mousePressed(e);
            }

            public void mouseEntered(MouseEvent e) {
                panel.setBorder(hoverBorder);
                super.mouseEntered(e);
            }

            public void mouseExited(MouseEvent e) {
                panel.setBorder(border);
                super.mouseExited(e);
            }
        });

        panel.add(label);

        frame.add(panel);
        frame.pack();
        frame.setMinimumSize(new Dimension(200, 0));
        frame.setVisible(true);
        return frame;
    }

    public enum Icon {
        INFO("dialog-information.png"),
        WARNING("dialog-warning.png"),
        SEVERE("dialog-error.png");

        public final ImageIcon imageIcon32;
        private final ImageIcon imageIcon;
        private final ImageIcon imageIcon16;

        Icon(String loc) {
            ImageIcon ii = null;
            ImageIcon ii16 = null;
            ImageIcon ii32 = null;
            try {
                ii = new ImageIcon(Theme.class.getResource(loc));
                Image i = ii.getImage();
                ii16 = new ImageIcon(i.getScaledInstance(16, 16, 16));
                ii32 = new ImageIcon(i.getScaledInstance(32, 32, 16));
            } catch (Exception e) {
                LOG.log(Level.WARNING, "missing icon image", e);
            }
            this.imageIcon = ii;
            this.imageIcon16 = ii16;
            this.imageIcon32 = ii32;
        }

        public ImageIcon get() {
            return this.imageIcon;
        }

        public ImageIcon get16() {
            return this.imageIcon16;
        }

        public ImageIcon get32() {
            return this.imageIcon32;
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\jgrowl\StandardTheme.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */