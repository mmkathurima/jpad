package com.timestored.command;

import com.timestored.swingxx.SwingUtils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;


class Utils {
    private static final Logger LOG = Logger.getLogger(Utils.class.getName());
    private static final boolean browseSupported;
    private static final int GAP = 4;
    private static final Border CENTRE_BORDER = BorderFactory.createEmptyBorder(4, 4, 4, 4);

    static {
        boolean browsey = false;
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                browsey = true;
            }
        }
        browseSupported = browsey;
    }

    public static void addEscapeCloseListener(final JDialog dialog) {
        dialog.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 27) {

                    WindowEvent wev = new WindowEvent(dialog, 201);
                    Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
                }
                super.keyPressed(e);
            }
        });
    }


    public static void putEscapeAction(JComponent searchTextField, Action action) {
        ActionMap am = searchTextField.getActionMap();
        InputMap im = searchTextField.getInputMap(1);
        am.put("escapeAction", action);
        im.put(SwingUtils.ESC_KEYSTROKE, "escapeAction");
    }


    public static boolean browse(String url) {
        if (browseSupported) {
            try {
                Desktop.getDesktop().browse(new URI(url));
                return true;
            } catch (IOException e) {
                LOG.log(Level.WARNING, "couldn't open browser", e);
            } catch (URISyntaxException e) {
                LOG.log(Level.WARNING, "couldn't open browser", e);
            }
        }
        return false;
    }


    public static JPanel getSubHeader(String title, Color foregroundColor, Color backgroundColor) {
        JPanel outPanel = new JPanel(new BorderLayout());
        outPanel.setBorder(BorderFactory.createEmptyBorder(4, 0, 6, 0));
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel(title);
        Font cf = titleLabel.getFont();
        titleLabel.setFont(new Font(cf.getName(), 1, cf.getSize() + 3));
        if (foregroundColor != null) {
            titleLabel.setForeground(foregroundColor);
        }
        titleLabel.setBorder(CENTRE_BORDER);
        headerPanel.add(titleLabel, "Center");
        if (backgroundColor != null) {
            headerPanel.setBackground(backgroundColor);
        }
        outPanel.add(headerPanel, "North");
        return outPanel;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\command\Utils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */