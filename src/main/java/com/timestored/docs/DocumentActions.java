package com.timestored.docs;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.timestored.messages.Msg;
import com.timestored.swingxx.SwingUtils;
import com.timestored.theme.Icon;
import com.timestored.theme.ShortcutAction;
import com.timestored.theme.Theme;
import jsyntaxpane.actions.*;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DocumentActions
        implements FileDropDocumentHandler.Listener {
    private static final Logger LOG = Logger.getLogger(DocumentActions.class.getName());

    private static final long FILE_WARNING_SIZE_MB = 1L;
    private static final int shortModifier = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
    private static final KeyStroke OUTLINE_DOC_KS = KeyStroke.getKeyStroke(73, shortModifier);
    private final OpenDocumentsModel openDocumentsModel;
    private final Action openFolderAction;
    private final Action closeFolderAction;
    private final Action openFilesAction;
    private final Action saveFileAction;
    private final Action closeFileAction;
    private final Action closeAllFileAction = new CloseAllFileAction();
    private final Action saveAsFileAction = new SaveAsFileAction();

    private final Action newFileAction;

    private final List<Action> fileActions;

    private final Action nextDocumentAction;
    private final Action prevDocumentAction;
    private final Action cutAction;
    private final Action copyAction;
    private final List<Action> editorActions;
    private final Action generateDocumentationAction;
    private final String defaultFiletypeExtension;
    private File lastSelectedOpenFileFolder;
    private File lastSelectedSaveFolder;

    public DocumentActions(OpenDocumentsModel openDocumentsModel, String defaultFiletypeExtension) {
        this(openDocumentsModel, defaultFiletypeExtension, null);
    }

    public DocumentActions(OpenDocumentsModel openDocumentsModel, String defaultFiletypeExtension, Action generateDocumentationAction) {
        this.openDocumentsModel = Preconditions.checkNotNull(openDocumentsModel);
        this.defaultFiletypeExtension = defaultFiletypeExtension;

        this.generateDocumentationAction = generateDocumentationAction;
        if (generateDocumentationAction != null) {
            generateDocumentationAction.putValue("MnemonicKey", 68);
        }

        this.openFilesAction = new ShortcutAction(Msg.get(Msg.Key.OPEN_FILE) + "...", Theme.CIcon.DOCUMENT_OPEN, 79) {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent arg0) {
                JFileChooser fc = getFileChooser(DocumentActions.this.lastSelectedOpenFileFolder);
                fc.setMultiSelectionEnabled(true);
                if (fc.showOpenDialog(null) == 0) {
                    DocumentActions.this.openFiles(Arrays.asList(fc.getSelectedFiles()));
                } else {
                    LOG.info(Msg.get(Msg.Key.OPEN_CANCELLED));
                }
            }
        };

        String oFolder = Msg.get(Msg.Key.OPEN_FOLDER);
        this.openFolderAction = new AbstractAction(oFolder + "...") {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent arg0) {
                JFileChooser fc = getFileChooser(DocumentActions.this.lastSelectedOpenFileFolder);
                fc.setFileSelectionMode(1);
                fc.setMultiSelectionEnabled(false);
                fc.setDialogTitle(Msg.get(Msg.Key.BROWSE_FOLDER));
                fc.setApproveButtonText(oFolder);

                if (fc.showOpenDialog(null) == 0) {
                    File f = fc.getSelectedFile();
                    if (f.isDirectory()) {
                        DocumentActions.this.lastSelectedOpenFileFolder = f;
                        openDocumentsModel.setSelectedFolder(f);
                    } else {
                        String message = Msg.get(Msg.Key.INVALID_DIRECTORY);
                        JOptionPane.showMessageDialog(null, message);
                    }
                } else {
                    LOG.info(Msg.get(Msg.Key.OPEN_CANCELLED));
                }
            }
        };

        this.closeFolderAction = new AbstractAction(Msg.get(Msg.Key.CLOSE_FOLDER)) {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent arg0) {
                openDocumentsModel.setSelectedFolder(null);
            }
        };

        this.newFileAction = new ShortcutAction(Msg.get(Msg.Key.NEW_FILE), Theme.CIcon.DOCUMENT_NEW, 78) {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent arg0) {
                openDocumentsModel.addDocument();
            }
        };

        this.closeFileAction = new ShortcutAction(Msg.get(Msg.Key.CLOSE), null, Msg.get(Msg.Key.CLOSE), 67, 115) {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent arg0) {
                DocumentActions.this.closeWithConfirmation(openDocumentsModel.getSelectedDocument());
            }
        };

        this.saveFileAction = new ShortcutAction(Msg.get(Msg.Key.SAVE_FILE), Theme.CIcon.DOCUMENT_SAVE, 83) {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent ae) {
                try {
                    Document selDoc = openDocumentsModel.getSelectedDocument();
                    if (selDoc.getFilePath() != null) {
                        openDocumentsModel.saveDocument();
                    } else {
                        DocumentActions.this.letUserChooseFileAndSave();
                    }
                } catch (IOException e) {
                    String msg = Msg.get(Msg.Key.SAVE_FILE_ERROR);
                    LOG.info(msg);
                    JOptionPane.showMessageDialog(null, msg, Msg.get(Msg.Key.SAVE_ERROR), JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        this.nextDocumentAction = new ShortcutAction(Msg.get(Msg.Key.NEXT_DOCUMENT), null, 34) {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent ae) {
                openDocumentsModel.gotoNextDocument();
            }
        }
        ;

        this.prevDocumentAction = new ShortcutAction(Msg.get(Msg.Key.PREV_DOCUMENT), null, 33) {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent ae) {
                openDocumentsModel.gotoPrevDocument();
            }
        }
        ;

        this.fileActions = Collections.unmodifiableList(Arrays.asList(this.newFileAction, this.openFilesAction, this.openFolderAction, this.closeFileAction, this.closeAllFileAction, this.closeFolderAction, this.saveFileAction, this.saveAsFileAction));

        this.cutAction = getAction(Msg.get(Msg.Key.CUT), Theme.CIcon.EDIT_CUT, new DefaultEditorKit.CutAction(), 88);
        this.copyAction = getAction(Msg.get(Msg.Key.COPY), Theme.CIcon.EDIT_COPY, new DefaultEditorKit.CopyAction(), 67);
        Action findNextAction = getAction(Msg.get(Msg.Key.FIND_NEXT), Theme.CIcon.EDIT_FIND_NEXT, new FindNextAction(), 114);
        Action toggleCommentAction = getAction(Msg.get(Msg.Key.TOGGLE_COMMENTS), Theme.CIcon.EDIT_COMMENT, new ToggleCommentsAction(), 47);

        toggleCommentAction.putValue("LineComments", "/ ");
        findNextAction.putValue("AcceleratorKey", KeyStroke.getKeyStroke("F3"));

        List<Action> acts = new ArrayList<>();
        acts.add(this.cutAction);
        acts.add(this.copyAction);
        acts.add(getAction(Msg.get(Msg.Key.PASTE), Theme.CIcon.EDIT_PASTE, new DefaultEditorKit.PasteAction(), 86));
        acts.add(toggleCommentAction);
        acts.add(getAction(Msg.get(Msg.Key.FIND) + "...", Theme.CIcon.EDIT_FIND, new FindReplaceAction(), 72));
        acts.add(findNextAction);
        acts.add(getAction(Msg.get(Msg.Key.GOTO_LINE) + "...", Theme.CIcon.EDIT_GOTO_LINE, new GotoLineAction(), 71));
        this.editorActions = Collections.unmodifiableList(acts);

        openDocumentsModel.addListener(new OpenDocumentsModel.Adapter() {
            public void docSelected(Document document) {
                DocumentActions.this.refresh();
            }

            public void docSaved() {
                DocumentActions.this.refresh();
            }

            public void docContentModified() {
                DocumentActions.this.refresh();
            }

            public void docCaratModified() {
                DocumentActions.this.refresh();
            }
        });

        this.refresh();
    }

    private static Action getAction(String title, Icon icon, Action action, int acceleratorKey) {
        action.putValue("Name", title);
        action.putValue("SmallIcon", icon.get16());

        KeyStroke k = KeyStroke.getKeyStroke(acceleratorKey, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        action.putValue("AcceleratorKey", k);
        return action;
    }

    private static JFileChooser getFileChooser(File lastSelection) {
        if (lastSelection != null) {
            return new JFileChooser(lastSelection);
        }
        return new JFileChooser();
    }

    public Action configureAction(Action action, KeyStroke keyStroke, String shortDescription) {
        action.putValue("ShortDescription", shortDescription);
        action.putValue("AcceleratorKey", keyStroke);
        return action;
    }

    public void openFile(File file) {
        this.openFiles(Collections.singletonList(file));
    }

    public void openFiles(List<File> files) {
        String errMsg = "";
        for (File f : files) {

            boolean proceed = true;
            double fileSizeMb = f.length() / 1048576.0D;
            if (fileSizeMb > 1.0D) {
                DecimalFormat df = new DecimalFormat("##.00");
                String msg = f.getName() + " file is " + df.format(fileSizeMb) + " MB and may take some time to open. Proceed?";
                proceed = (0 == JOptionPane.showConfirmDialog(null, msg));
            }

            try {
                if (proceed) {
                    this.openDocumentsModel.openDocument(f);
                    this.lastSelectedOpenFileFolder = f.getParentFile();
                }
            } catch (IOException e) {
                errMsg = String.format("%s\r\n%s", errMsg, f.getAbsolutePath());
                LOG.log(Level.WARNING, "openFiles exception", e);
            }
        }
        if (errMsg.length() > 0) {
            String msg = Msg.get(Msg.Key.EROR_OPENING_FILES) + errMsg;
            JOptionPane.showMessageDialog(null, msg, Msg.get(Msg.Key.ERROR_OPENING), JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<Action> getEditorActions() {
        this.refresh();
        return this.editorActions;
    }

    public Action getOpenFileAction() {
        return this.openFilesAction;
    }

    public Action getOpenFolderAction() {
        return this.openFolderAction;
    }

    public Action getNewFileAction() {
        return this.newFileAction;
    }

    public Action getSaveFileAction() {
        return this.saveFileAction;
    }

    public Action getSaveAsFileAction() {
        return this.saveAsFileAction;
    }

    public Action getCloseFileAction() {
        return this.closeFileAction;
    }

    public Action getCloseFileAction(Document document) {
        String t = (document == null) ? "" : document.getTitle();
        Action a = new AbstractAction(Msg.get(Msg.Key.CLOSE) + " " + t) {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent arg0) {
                DocumentActions.this.closeWithConfirmation(document);
            }
        };
        a.setEnabled((document != null && this.openDocumentsModel.getDocuments().contains(document)));

        return a;
    }

    public Action getCloseOtherFilesAction(Document document) {
        Action closeOthersAction = new AbstractAction("Close Other Tabs") {
            public void actionPerformed(ActionEvent e) {
                for (Document d : DocumentActions.this.openDocumentsModel.getDocuments()) {
                    if (!d.equals(document)) {
                        DocumentActions.this.getCloseFileAction(d).actionPerformed(null);
                    }
                }
            }
        };
        closeOthersAction.putValue("MnemonicKey", 79);
        return closeOthersAction;
    }

    public Action getCloseAllFileAction() {
        return this.closeAllFileAction;
    }

    private void closeWithConfirmation(Document document) {
        LOG.info("closeWithConfirmation: " + document);
        try {
            if (document.hasUnsavedChanges()) {
                String message = Msg.get(Msg.Key.DOCUMENT) + document.getTitle() + Msg.get(Msg.Key.UNSAVED_CHANGES_CONFIRM);

                int choice = JOptionPane.showConfirmDialog(null, message);
                switch (choice) {
                    case 0:
                        if (document.getFilePath() != null)
                            this.openDocumentsModel.saveDocument();
                        else
                            this.letUserChooseFileAndSave();
                        break;
                    case 1:
                        this.openDocumentsModel.closeDocument(document);
                        break;
                }
            } else {
                this.openDocumentsModel.closeDocument(document);
            }
        } catch (IOException e) {
            String msg = Msg.get(Msg.Key.ERROR_SAVING);
            LOG.info(msg);
            JOptionPane.showMessageDialog(null, msg, msg, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void letUserChooseFileAndSave() {
        File file = SwingUtils.askUserSaveLocation(this.defaultFiletypeExtension, this.lastSelectedSaveFolder);
        if (file != null) {
            try {
                this.openDocumentsModel.saveAs(file);
                this.lastSelectedSaveFolder = file.getParentFile();
            } catch (IOException e) {
                String msg = Msg.get(Msg.Key.ERROR_SAVING) + ": " + file;
                LOG.info(msg);
                JOptionPane.showMessageDialog(null, msg, Msg.get(Msg.Key.ERROR_SAVING), JOptionPane.ERROR_MESSAGE);
            }
        } else {
            LOG.info(Msg.get(Msg.Key.SAVE_CANCELLED));
        }
    }

    private void refresh() {
        Document d = this.openDocumentsModel.getSelectedDocument();

        this.saveFileAction.setEnabled(d.hasUnsavedChanges());

        this.cutAction.setEnabled(d.isTextSelected());
        this.copyAction.setEnabled(d.isTextSelected());
    }

    public Action getUndoAction() {
        return getAction(Msg.get(Msg.Key.UNDO_TYPING), Theme.CIcon.EDIT_UNDO, new UndoAction(), 90);
    }

    public Action getRedoAction() {
        return getAction(Msg.get(Msg.Key.REDO), Theme.CIcon.EDIT_REDO, new RedoAction(), 89);
    }

    public List<Action> getFileActions() {
        return this.fileActions;
    }

    public Action getGenerateDocumentationAction() {
        return this.generateDocumentationAction;
    }

    public void filesDropped(List<File> files) {
        this.openFiles(files);
    }

    public List<Action> getOpenRecentActions(List<String> filePaths) {
        List<Action> r = Lists.newArrayListWithExpectedSize(filePaths.size());
        int i = 1;
        for (String fp : filePaths) {
            r.add(new AbstractAction(i + " " + fp) {
                private static final long serialVersionUID = 1L;

                public void actionPerformed(ActionEvent e) {
                    DocumentActions.this.openFile(new File(fp));
                }
            });
            i++;
        }
        return r;
    }

    public Action openAllAction(List<String> filePaths) {
        return new AbstractAction(Msg.get(Msg.Key.OPEN_ALL_RECENT)) {
            public void actionPerformed(ActionEvent e) {
                List<File> files = Lists.newArrayList();
                for (String fp : filePaths) {
                    files.add(new File(fp));
                }
                DocumentActions.this.openFiles(files);
            }
        };
    }

    public void setDefaultFolder(File folder) {
        if (folder != null && folder.isDirectory()) {
            this.lastSelectedOpenFileFolder = folder;
            this.lastSelectedSaveFolder = folder;
        }
    }

    public void addActionsToEditor(JComponent component, Document document) {
        this.addAction(component, this.nextDocumentAction);
        this.addAction(component, this.prevDocumentAction);
    }

    private void addAction(JComponent component, Action action) {
        String mapkey = (String) action.getValue("Name");
        component.getActionMap().put(mapkey, action);
        KeyStroke keyStroke = (KeyStroke) action.getValue("AcceleratorKey");
        component.getInputMap().put(keyStroke, mapkey);
    }

    private class CloseAllFileAction
            extends AbstractAction {
        private static final long serialVersionUID = 1L;

        public CloseAllFileAction() {
            super(Msg.get(Msg.Key.CLOSE_ALL));
            this.putValue("ShortDescription", Msg.get(Msg.Key.CLOSE_ALL));
            this.putValue("MnemonicKey", 65);
        }

        public void actionPerformed(ActionEvent arg0) {
            for (Document d : DocumentActions.this.openDocumentsModel.getDocuments()) {
                DocumentActions.this.closeWithConfirmation(d);
            }
        }
    }

    private class SaveAsFileAction
            extends AbstractAction {
        private static final long serialVersionUID = 1L;

        public SaveAsFileAction() {
            super(Msg.get(Msg.Key.SAVE_AS) + "...", Theme.CIcon.DOCUMENT_SAVE.get16());
            this.putValue("ShortDescription", Msg.get(Msg.Key.SAVE_AS) + "...");
            this.putValue("MnemonicKey", 65);
        }

        public void actionPerformed(ActionEvent arg0) {
            DocumentActions.this.letUserChooseFileAndSave();
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\docs\DocumentActions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */