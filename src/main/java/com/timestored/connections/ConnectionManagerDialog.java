package com.timestored.connections;

import com.google.common.base.Preconditions;
import com.timestored.misc.TextWrapper;
import com.timestored.swingxx.ColorChooserPanel;
import com.timestored.swingxx.SwingUtils;
import com.timestored.theme.Theme;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ConnectionManagerDialog
        extends JDialog
        implements ActionListener {
    private static final int DIALOG_HEIGHT = 450;
    private static final int DIALOG_WIDTH = 400;
    private static final Logger LOG = Logger.getLogger(ConnectionManagerDialog.class.getName());

    private static final long serialVersionUID = 1L;
    private static final Theme.InputLabeller INPUT_LABELLER = Theme.getInputLabeller(80, 20);

    private static final int DEF_COLUMNS = 20;

    private final ConnectionManager conMan;

    private final JTextField hostTextField;

    private final JTextField portTextField;

    private final JPasswordField passwordTextField;

    private final JTextField usernameTextField;

    private final JTextField nameTextField;

    private final JComboBox serverTypeComboBox;

    private final List<JdbcTypes> jdbcTypesShown;

    private final ColorChooserPanel colorChooserPanel;

    private final JTextField databaseTextField;

    private final JPanel databasePanel;
    private final String serverName;
    private final JComboBox folderComboBox;
    private ServerConfig serverConfig;

    public ConnectionManagerDialog(ConnectionManager connectionManager, JFrame parent, String serverName, final List<JdbcTypes> jdbcTypesShown) {
        super(parent, "Server Properties");
        setIconImage(Theme.CIcon.SERVER_EDIT.get().getImage());
        this.serverName = serverName;
        this.conMan = Preconditions.checkNotNull(connectionManager);
        this.jdbcTypesShown = jdbcTypesShown;


        this.serverConfig = null;
        if (serverName != null) {
            this.serverConfig = this.conMan.getServer(serverName);
            if (this.serverConfig == null) {
                throw new IllegalArgumentException("Server not found in Connection Manager");
            }
        }


        setResizable(false);
        setSize(400, 450);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        JPanel cp = new JPanel();
        cp.setLayout(new BoxLayout(cp, 3));
        SwingUtils.addEscapeCloseListener(this);


        JPanel connPanel = new JPanel();
        connPanel.setBorder(new TitledBorder(null, "Connection", 4, 2, null, null));
        connPanel.setLayout(new BoxLayout(connPanel, 3));

        Component verticalBox = Box.createVerticalStrut(10);
        connPanel.add(verticalBox);

        this.hostTextField = new HighlightTextField("localhost");
        this.hostTextField.setColumns(20);
        String hostLbl = "<html><b>" + Msg.get(Msg.Key.HOST) + ":</b></html>";
        JPanel panel = INPUT_LABELLER.get(hostLbl, this.hostTextField, "hostField");
        connPanel.add(panel);

        this.portTextField = new HighlightTextField("5000");
        this.portTextField.setColumns(10);
        JPanel panel_1 = INPUT_LABELLER.get("<html><b>" + Msg.get(Msg.Key.PORT) + ":</b></html>", this.portTextField, "portField");
        connPanel.add(panel_1);

        String[] names = new String[jdbcTypesShown.size()];
        for (int i = 0; i < names.length; i++) {
            names[i] = jdbcTypesShown.get(i).getNiceName();
        }
        this.serverTypeComboBox = new JComboBox<String>(names);


        String stLbl = "Server Type:";
        connPanel.add(INPUT_LABELLER.get(stLbl, this.serverTypeComboBox, "serverTypeDropdown"));


        this.databasePanel = new JPanel(new FlowLayout(0));
        this.serverTypeComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                JdbcTypes t = jdbcTypesShown.get(ConnectionManagerDialog.this.serverTypeComboBox.getSelectedIndex());
                ConnectionManagerDialog.this.databasePanel.setVisible(t.isDatabaseRequired());
                ConnectionManagerDialog.this.portTextField.setText("" + t.getDefaultPort());
            }
        });
        JdbcTypes t = jdbcTypesShown.get(this.serverTypeComboBox.getSelectedIndex());
        this.databasePanel.setVisible(t.isDatabaseRequired());


        connPanel.add(this.databasePanel);

        this.databaseTextField = new HighlightTextField("");
        this.databasePanel.add(INPUT_LABELLER.get(Msg.get(Msg.Key.DATABASE) + ":", this.databaseTextField, "dbField"));


        JPanel loginPanel = new JPanel();
        loginPanel.setBorder(new TitledBorder(null, "Login", 4, 2, null, null));
        loginPanel.setLayout(new BoxLayout(loginPanel, 3));

        this.usernameTextField = new HighlightTextField("");
        loginPanel.add(INPUT_LABELLER.get(Msg.get(Msg.Key.USERNAME) + ":", this.usernameTextField, "usernameField"));

        this.passwordTextField = new JPasswordField(20);
        loginPanel.add(INPUT_LABELLER.get(Msg.get(Msg.Key.PASSWORD) + ":", this.passwordTextField, "passwordField"));
        this.passwordTextField.setColumns(20);


        JPanel nameColorPanel = new JPanel();
        nameColorPanel.setBorder(new EtchedBorder(1, null, null));
        nameColorPanel.setLayout(new BoxLayout(nameColorPanel, 3));


        this.nameTextField = new HighlightTextField("");
        String nameLbl = "<html><b>Name:</b></html>";
        nameColorPanel.add(INPUT_LABELLER.get(nameLbl, this.nameTextField, "serverNameField"));

        this.colorChooserPanel = new ColorChooserPanel(this);
        nameColorPanel.add(INPUT_LABELLER.get("Background:", this.colorChooserPanel, "colorButton"));


        this.folderComboBox = new JComboBox();
        this.folderComboBox.setEditable(true);

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(Theme.makeButton((this.serverConfig == null) ? Msg.get(Msg.Key.ADD) : Msg.get(Msg.Key.SAVE), this));


        if (this.serverConfig != null) {
            ActionListener deleteListener = new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    Component parent = ConnectionManagerDialog.this;
                    try {
                        int reply = JOptionPane.showConfirmDialog(parent, "Are you sure you want to delete this server?");

                        if (reply == 0) {
                            ConnectionManagerDialog.this.conMan.removeServer(ConnectionManagerDialog.this.serverConfig);
                            ConnectionManagerDialog.this.closeDialog();
                        }
                    } catch (Exception e) {
                        String msg = "Possible problem deleting server.";
                        JOptionPane.showMessageDialog(parent, msg, "Delete error", 0);

                        ConnectionManagerDialog.LOG.log(Level.SEVERE, msg, e);
                    }
                }
            };
            buttonPanel.add(Theme.makeButton(Msg.get(Msg.Key.DELETE), deleteListener));
        }


        Action dispatchTest = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                Component parent = ConnectionManagerDialog.this;

                try {
                    ConnectionManagerDialog.this.conMan.testConnection(ConnectionManagerDialog.this.getServerConfig());
                    String message = "Connection works";
                    JOptionPane.showMessageDialog(parent, message);
                } catch (IOException ioe) {
                    String message = "Connection does not work.";
                    String fullMsg = TextWrapper.forWidth(80).hard().wrap(message + " " + ioe);


                    JOptionPane.showMessageDialog(parent, fullMsg, message, 2);

                    ConnectionManagerDialog.LOG.log(Level.INFO, message);
                }
            }
        };
        buttonPanel.add(Theme.makeButton("Test", dispatchTest));


        Action dispatchClose = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                JDialog d = ConnectionManagerDialog.this;
                d.dispatchEvent(new WindowEvent(d, 201));
            }
        };


        buttonPanel.add(Theme.makeButton(Msg.get(Msg.Key.CANCEL), dispatchClose));

        cp.add(connPanel);
        cp.add(loginPanel);
        cp.add(nameColorPanel);
        cp.add(INPUT_LABELLER.get("Folder:", this.folderComboBox, "folderComboBox"));


        add(cp, "Center");
        buttonPanel.setAlignmentX(0.5F);
        add(buttonPanel, "South");


        SwingUtils.addEscapeCloseListener(this);
        showConnection(this.serverConfig);
    }

    protected void closeDialog() {
        WindowEvent wev = new WindowEvent(this, 201);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
    }

    private void showConnection(ServerConfig sc) {
        if (sc != null) {


            int idx = this.jdbcTypesShown.indexOf(sc.getJdbcType());
            if (idx >= 0) {
                this.serverTypeComboBox.setSelectedIndex(idx);
            }

            this.hostTextField.setText(sc.getHost());
            this.portTextField.setText("" + sc.getPort());
            this.usernameTextField.setText(sc.getUsername());
            this.passwordTextField.setText(sc.getPassword());
            this.nameTextField.setText(sc.getShortName());
            this.databaseTextField.setText(sc.getDatabase());
            this.colorChooserPanel.setColor(sc.getColor());
            this.databasePanel.setVisible(sc.getJdbcType().isDatabaseRequired());
            setFolder(sc.getFolder());
        } else {

            this.folderComboBox.setSelectedItem("");
        }
    }

    private ServerConfig getServerConfig() {
        JdbcTypes jdbcType = this.jdbcTypesShown.get(this.serverTypeComboBox.getSelectedIndex());
        String host = this.hostTextField.getText();
        int port = Integer.parseInt(this.portTextField.getText());
        String username = this.usernameTextField.getText();
        String password = new String(this.passwordTextField.getPassword());

        String folder = (this.folderComboBox != null) ? (String) this.folderComboBox.getSelectedItem() : "";
        String name = this.nameTextField.getText();


        String database = this.databaseTextField.getText();
        Color c = this.colorChooserPanel.getColor();

        return new ServerConfig(host, port, username, password, name, jdbcType, c, database, folder);
    }

    public void actionPerformed(ActionEvent e) {
        try {
            if (this.serverConfig != null) {
                this.conMan.updateServer(this.serverName, getServerConfig());
            } else {
                this.conMan.addServer(getServerConfig());
            }
            closeDialog();
        } catch (IllegalArgumentException ex) {
            String msg = "Error saving server changes. \r\n" + ex.getMessage();
            JOptionPane.showMessageDialog(this, msg, "Save error", 0);

            LOG.info(msg);
        }
    }

    public void setFolder(String selectedFolder) {
        Set<String> foldSet = new HashSet<String>(this.conMan.getFolders());
        foldSet.add("");
        foldSet.add(selectedFolder);
        String[] folders = foldSet.toArray(new String[0]);

        this.folderComboBox.setModel(new DefaultComboBoxModel<String>(folders));
        this.folderComboBox.setSelectedItem(selectedFolder);
    }

    private static class HighlightTextField
            extends JTextField {
        public HighlightTextField(String text) {
            setText(text);
            setColumns(20);
        }

        protected void processFocusEvent(FocusEvent e) {
            super.processFocusEvent(e);
            if (e.getID() == 1004)
                selectAll();
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\connections\ConnectionManagerDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */