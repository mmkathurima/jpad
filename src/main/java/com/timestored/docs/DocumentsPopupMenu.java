package com.timestored.docs;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DocumentsPopupMenu
        extends JPopupMenu {
    private static final Logger log = Logger.getLogger(DocumentsPopupMenu.class.getName());
    private static final long serialVersionUID = 1L;

    public DocumentsPopupMenu(DocumentActions documentActions, Document document) {
        this.setName("DocumentsPopupMenu");

        Action closeOthersAction = documentActions.getCloseOtherFilesAction(document);
        this.add(closeOthersAction);
        closeOthersAction.setEnabled((document != null));
        this.add(documentActions.getCloseFileAction(document));
        this.add(documentActions.getCloseAllFileAction());

        String p = null;
        String fp = (document != null) ? document.getFilePath() : null;
        if (fp != null) {
            p = (new File(fp)).getParentFile().getAbsolutePath();
        }
        String parentPath = p;
        Action copyFilePathToClipboard = new AbstractAction("Open Containing Folder") {
            public void actionPerformed(ActionEvent e) {
                File parentFolder = new File(parentPath);
                if (parentFolder.exists()) {
                    try {
                        Desktop.getDesktop().open(parentFolder);
                    } catch (IOException ioe) {
                        final String message = "Could not open folder";
                        log.log(Level.WARNING, message, ioe);
                        JOptionPane.showMessageDialog(null, message);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Folder no longer exists");
                }
            }
        };
        copyFilePathToClipboard.setEnabled((parentPath != null));
        this.add(copyFilePathToClipboard);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\docs\DocumentsPopupMenu.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */