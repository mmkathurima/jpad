package io.jpad;

import com.timestored.misc.HtmlUtils;
import com.timestored.theme.Theme;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

class LoginConfigureDialog
        extends JDialog {
    private static final long serialVersionUID = 1L;
    private static final int PAD = 10;
    private final int WIDTH = 200;
    private final int HEIGHT = 200;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JLabel validLabel;
    private Listener listener;

    public LoginConfigureDialog() {
        this.setTitle("JPad Upload Login Details");
        this.setSize(200, 200);
        this.setLayout(new BorderLayout());
        this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

        JPanel panel = Theme.getVerticalBoxPanel();
        panel.setBorder(BorderFactory.createBevelBorder(1));
        final String passTooltip = "These jpad.io credentials are used for uploading code etc.";
        this.usernameField = new JTextField(20);
        this.passwordField = new JPasswordField(20);
        Action regAction = HtmlUtils.getWWWaction("Register", "http://jpad.io/register");
        this.validLabel = new JLabel("                ");

        this.usernameField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                LoginConfigureDialog.this.validLabel.setText("                ");
            }
        });
        this.passwordField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                LoginConfigureDialog.this.validLabel.setText("                ");
            }
        });

        panel.add(new JButton(regAction));
        panel.add(Theme.getFormRow(this.usernameField, "Username:", passTooltip));
        panel.add(Theme.getFormRow(this.passwordField, "Password:", passTooltip));
        panel.add(this.validLabel);

        JPanel butPanel = new JPanel(new FlowLayout(10));
        butPanel.add(Theme.makeButton("Save", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LoginConfigureDialog.this.save();
            }
        }));
        butPanel.add(Theme.makeButton("Test", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (WebsiteUploader.validLogin(LoginConfigureDialog.this.getUsername(), LoginConfigureDialog.this.getPassword())) {
                        LoginConfigureDialog.this.validLabel.setText("valid");
                        LoginConfigureDialog.this.validLabel.setVisible(true);
                    } else {
                        LoginConfigureDialog.this.validLabel.setText("INVALID LOGIN");
                    }
                } catch (IOException e1) {
                    LoginConfigureDialog.this.validLabel.setText("Problem Connecting");
                }
            }
        }));
        butPanel.add(Theme.makeButton("Cancel", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LoginConfigureDialog.this.dispose();
            }
        }));

        this.add(panel, "North");
        this.add(butPanel, "South");
        this.pack();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private void save() {
        this.dispose();
        if (this.listener != null)
            this.listener.onSave();
    }

    public String getUsername() {
        return this.usernameField.getText();
    }

    public void setUsername(String username) {
        this.usernameField.setText(username);
    }

    public char[] getPassword() {
        return this.passwordField.getPassword();
    }

    public void setPassword(String password) {
        this.passwordField.setText(password);
    }

    interface Listener {
        void onSave();
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\LoginConfigureDialog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */