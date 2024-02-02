package com.timestored.swingxx;

import com.google.common.base.Preconditions;
import com.timestored.StringUtils;
import com.timestored.messages.Msg;
import com.timestored.misc.IOUtils;
import com.timestored.theme.Theme;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;


public class SwingUtils {
    public static final KeyStroke ESC_KEYSTROKE = KeyStroke.getKeyStroke(27, 0);
    private static final Logger LOG = Logger.getLogger(SwingUtils.class.getName());


    public static void addEscapeCloseListener(final JDialog dialog) {
        putEscapeAction(dialog.getRootPane(), new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                WindowEvent wev = new WindowEvent(dialog, 201);
                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
            }
        });

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


    public static void forceToFront(JFrame frame) {
        frame.setState(0);
        frame.toFront();
        frame.setAlwaysOnTop(true);
        frame.setAlwaysOnTop(false);
        frame.repaint();
    }


    public static JDialog showSplashDialog(URL r, Color bgColor, String txt) {
        JDialog dialog = null;
        try {
            if (r != null) {
                dialog = new JDialog();

                dialog.setUndecorated(true);
                JPanel cp = new JPanel(new BorderLayout());
                ImageIcon ii = new ImageIcon(r);
                cp.add(new JLabel(ii), "Center");
                JLabel l = new JLabel(txt);
                l.setForeground(Color.WHITE);
                l.setBackground(bgColor);
                l.setOpaque(true);
                cp.add(l, "South");
                cp.setBorder(BorderFactory.createLineBorder(bgColor));
                dialog.add(cp);
                dialog.pack();
                dialog.setLocationRelativeTo(null);

                dialog.setVisible(true);
                dialog.validate();
                dialog.paintAll(dialog.getGraphics());
                dialog.repaint();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dialog;
    }


    public static JFrame getPopupFrame(Component parent, String title, Component content, Image icon) {
        JFrame f = new JFrame(StringUtils.abbreviate(title, 80));
        f.setIconImage(icon);
        f.setLayout(new BorderLayout());
        f.add(content);
        setSensibleDimensions(parent, f);
        return f;
    }


    public static void showAppDialog(Component parent, String title, JPanel contentPanel, Image icon) {
        JDialog d = new JDialog();

        addEscapeCloseListener(d);

        d.setIconImage(icon);
        d.setTitle(title);
        d.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        d.setLayout(new BorderLayout());
        d.add(contentPanel);
        setSensibleDimensions(parent, d);


        d.setLocationRelativeTo(parent);

        d.setVisible(true);
    }

    private static void setSensibleDimensions(Component parent, Window w) {
        Toolkit tk = Toolkit.getDefaultToolkit();
        int defWidth = 400;
        int defHeight = 400;
        if (parent != null) {
            Insets si = tk.getScreenInsets(parent.getGraphicsConfiguration());
            defWidth = Math.max(700, ((tk.getScreenSize()).width - si.left - si.right) * 3 / 5);
            defHeight = ((tk.getScreenSize()).height - si.top - si.bottom) * 4 / 5;
        }
        w.setSize(defWidth, defHeight);
    }


    private static void putEscapeAction(JComponent com, Action action) {
        ActionMap am = com.getActionMap();
        InputMap im = com.getInputMap(1);
        am.put("escapeAction", action);
        im.put(ESC_KEYSTROKE, "escapeAction");
    }


    public static void showMessageDialog(Component parentComponent, String message, String title, int messageType) {
        String[] st = message.split("\r");
        int lineCount = st.length;

        int maxLineLength = 0;
        for (String r : st) {
            if (r.length() > maxLineLength) {
                maxLineLength = r.length();
            }
        }


        String style = (maxLineLength > 100) ? " style='width: 600px;'" : "";
        Object msg = "<html><body><p" + style + ">" + message + "</body></html>";


        if (lineCount > 15 || message.length() > 800) {

            JTextArea textArea = new JTextArea(15, 70);
            textArea.setText(message);
            textArea.setEditable(false);
            msg = new JScrollPane(textArea);
        }

        JOptionPane.showMessageDialog(parentComponent, msg, title, messageType);
    }


    public static void offerToOpenFile(String message, File file, String optionOpen, String optionClose) {
        String[] options = {optionOpen, optionClose};
        int option = JOptionPane.showOptionDialog(null, message, "Open File?", 2, 1, Theme.CIcon.TEXT_HTML.get32(), options, options[0]);


        if (option == 0) {
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Problem opening output file, try browsing to folder manually and opening there");
            }
        }
    }


    public static File askUserSaveLocation(String filetypeExtension, File fileOrFolder) {
        JFileChooser fc = null;
        if (fileOrFolder != null && fileOrFolder.isDirectory()) {
            fc = new JFileChooser(fileOrFolder);
        } else {
            fc = new JFileChooser();
        }

        if (fileOrFolder != null && fileOrFolder.isFile()) {
            fc.setSelectedFile(fileOrFolder);
        }

        if (filetypeExtension != null) {
            fc.setFileFilter(new FileNameExtensionFilter(filetypeExtension, filetypeExtension));
        }
        fc.setApproveButtonText(Msg.get(Msg.Key.SAVE));
        fc.setDialogTitle(Msg.get(Msg.Key.SAVE_FILE));
        if (fc.showOpenDialog(null) == 0) {
            File f = fc.getSelectedFile();
            if (filetypeExtension != null && !f.getName().contains(".")) {
                f = new File(f.getAbsolutePath() + "." + filetypeExtension);
            }
            return f;
        }
        LOG.info(Msg.get(Msg.Key.SAVE_CANCELLED));

        return null;
    }


    public static File askUserAndSave(String filetypeExtension, File file, String content) {
        Preconditions.checkNotNull(content);
        File f = askUserSaveLocation(filetypeExtension, file);
        if (f != null) {
            try {
                IOUtils.writeStringToFile(content, f);
            } catch (IOException e) {
                String msg = Msg.get(Msg.Key.ERROR_SAVING) + ": " + f;
                LOG.info(msg);
                JOptionPane.showMessageDialog(null, msg, Msg.get(Msg.Key.ERROR_SAVING), 0);
            }
        }
        return f;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\swingxx\SwingUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */