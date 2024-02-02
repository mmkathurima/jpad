package io.jpad;

import com.google.common.base.Preconditions;
import com.timestored.misc.HtmlUtils;
import com.timestored.swingxx.SwingUtils;
import com.timestored.theme.Theme;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SnipUploadDialog
        extends JDialog {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(SnipUploadDialog.class.getName());

    private final JTextField titleTextField;

    private final JTextArea descriptionTextField;

    private final JTextField tagsTextField;

    private final Frame pparent;

    private final Snip snip;

    private final String username;
    private final String password;
    private UploadResult uploadResult;

    public SnipUploadDialog(Frame parent, Snip snip, String username, String password) {
        super(parent, "Snip Uploader");
        this.pparent = parent;
        this.snip = Preconditions.checkNotNull(snip);
        this.username = Preconditions.checkNotNull(username);
        this.password = Preconditions.checkNotNull(password);

        setIconImage(Theme.CIcon.UP_CLOUD.get().getImage());


        setLocationRelativeTo(parent);
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        SwingUtils.addEscapeCloseListener(this);

        Theme.InputLabeller IL = Theme.getInputLabeller(80, 20);
        this.titleTextField = new JTextField(30);
        this.descriptionTextField = new JTextArea(5, 40);
        this.descriptionTextField.setFocusTraversalKeys(0, null);
        this.descriptionTextField.setFocusTraversalKeys(1, null);


        this.tagsTextField = new JTextField(30);

        setLayout(new BorderLayout());
        JPanel c = Theme.getVerticalBoxPanel();
        c.setBorder(BorderFactory.createBevelBorder(0));
        add(c, "Center");

        String helpMsg = "By adding a good title and tags this will make your snippets searchable in future.\r\n";
        c.add(new JLabel(helpMsg));
        c.add(IL.get("Title:", this.titleTextField, "titleTextField"));
        c.add(IL.get("Description:", this.descriptionTextField, "descriptionTextField"));

        JLabel helpLabel = new JLabel("e.g. java,sql,guava");
        c.add(IL.get("Tags:", this.tagsTextField, "tagsTextField", helpLabel, "Comma separated list of tags."));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, 2));

        buttonPanel.add(Theme.makeButton("Upload", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SnipUploadDialog.this.upload();
            }
        }));
        buttonPanel.add(Theme.makeButton("Cancel", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SnipUploadDialog.this.dispose();
            }
        }));

        c.add(buttonPanel);

        refreshGui();
        pack();
    }


    public UploadResult getUploadResult() {
        return this.uploadResult;
    }

    private void refreshGui() {
        this.titleTextField.setText(this.snip.getTitle());
        this.descriptionTextField.setText(this.snip.getDescription());
        this.tagsTextField.setText(this.snip.getTags());
    }


    private void upload() {
        this.snip.setTitle(this.titleTextField.getText());
        this.snip.setDescription(this.descriptionTextField.getText());
        this.snip.setTags(this.tagsTextField.getText());

        String msg = "Upload Failed";
        this.uploadResult = null;
        try {
            this.uploadResult = WebsiteUploader.post(this.username, this.password, this.snip);
        } catch (IOException e) {
            LOG.log(Level.WARNING, msg, e);
        }

        if (this.uploadResult != null && this.uploadResult.getType().equals(UploadResult.ResultType.SUCCESS)) {
            HtmlUtils.browse(this.uploadResult.getUrl());
            dispose();
        } else {
            if (this.uploadResult != null) {
                msg = this.uploadResult.getMessage();
            }
            JOptionPane.showMessageDialog(this.pparent, msg, "Upload Failed", 0);
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\SnipUploadDialog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */