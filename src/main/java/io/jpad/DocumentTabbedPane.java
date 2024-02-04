package io.jpad;

import com.google.common.collect.Lists;
import com.timestored.docs.*;
import com.timestored.swingxx.TabbedPaneRightClickBlocker;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

class DocumentTabbedPane
        extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final int SPLITPANE_WIDTH = 200;
    private final JTabbedPane tabbedPane;
    private final OpenDocumentsModel openDocumentsModel;
    private final DocumentActions documentActions;
    private final JFrame parentFrame;
    private final List<MyEditor> displayedEditors = Lists.newArrayList();

    private boolean iAmChangingSelection;

    DocumentTabbedPane(DocumentActions documentActions, OpenDocumentsModel openDocumentsModel, JFrame parentFrame) {
        this.openDocumentsModel = openDocumentsModel;
        this.documentActions = documentActions;
        this.parentFrame = parentFrame;

        FileDropDocumentHandler fileDropHandler = new FileDropDocumentHandler();
        fileDropHandler.addListener(documentActions);
        this.setTransferHandler(fileDropHandler);

        this.tabbedPane = new JTabbedPane();
        this.tabbedPane.setName("serverDocsTabbedPane");
        this.tabbedPane.setMinimumSize(new Dimension(400, 1));
        this.tabbedPane.setMaximumSize(new Dimension(2147483647, 2147483647));

        for (Document document : openDocumentsModel.getDocuments()) {
            this.addDocToTabs(document);
        }

        TabbedPaneRightClickBlocker.install(this.tabbedPane);

        this.tabbedPane.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                this.maybeHandleClick(e);
            }

            public void mouseReleased(MouseEvent e) {
                this.maybeHandleClick(e);
            }

            private void maybeHandleClick(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    (new DocumentsPopupMenu(documentActions, null)).show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        this.setLayout(new BorderLayout());
        this.add(this.tabbedPane);
        this.refreshTabAppearance();
        openDocumentsModel.addListener(new UpdateTabsListener());

        this.tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (!DocumentTabbedPane.this.iAmChangingSelection) {
                    int idx = DocumentTabbedPane.this.tabbedPane.getSelectedIndex();
                    if (idx > 0) {
                        Document d = DocumentTabbedPane.this.displayedEditors.get(idx).getDocument();
                        openDocumentsModel.setSelectedDocument(d);
                    }
                }
            }
        });
    }

    private void addDocToTabs(Document document) {
        MyEditor myEditor = MyEditor.getEditorComponent(document);
        JTextArea textArea = myEditor.getJTextArea();

        FileDropDocumentHandler fileDropHandler = new FileDropDocumentHandler(textArea.getTransferHandler());
        fileDropHandler.addListener(this.documentActions);
        textArea.setTransferHandler(fileDropHandler);

        this.documentActions.addActionsToEditor(textArea, document);

        textArea.addCaretListener(new CaretListener() {
            private boolean modifyingSelf;

            public void caretUpdate(CaretEvent e) {
                if (!this.modifyingSelf) {
                    this.modifyingSelf = true;
                    String t = textArea.getText();
                    if (!t.equals(document.getContent())) {
                        document.setContent(t);
                    }

                    document.setSelection(textArea.getSelectionStart(), textArea.getSelectionEnd(), e.getDot());
                    textArea.requestFocusInWindow();
                    this.modifyingSelf = false;
                }
            }
        });

        this.displayedEditors.add(myEditor);
        this.tabbedPane.add(myEditor.getP());
    }

    public boolean requestFocusInWindow() {
        Component c = this.tabbedPane.getSelectedComponent();
        if (c != null)
            return c.requestFocusInWindow();
        return false;
    }

    public void requestFocus() {
        Component c = this.tabbedPane.getSelectedComponent();
        if (c != null)
            c.requestFocus();
    }

    private int indexOfDocument(Document d) {
        for (int i = 0; i < this.displayedEditors.size(); i++) {
            MyEditor m = this.displayedEditors.get(i);
            if (m.getDocument().equals(d))
                return i;
        }
        return -1;
    }

    private void refreshTabAppearance() {
        List<Document> documents = this.openDocumentsModel.getDocuments();

        int i;

        for (i = this.displayedEditors.size() - 1; i >= 0; i--) {
            Document d = this.displayedEditors.get(i).getDocument();
            if (!documents.contains(d)) {
                this.displayedEditors.remove(i);
                this.tabbedPane.remove(i);
            }
        }

        for (i = documents.size() - 1; i >= 0; i--) {
            Document d = documents.get(i);
            if (this.indexOfDocument(d) == -1) {
                this.addDocToTabs(d);
            }
        }

        for (i = this.displayedEditors.size() - 1; i >= 0; i--) {
            MyEditor me = this.displayedEditors.get(i);
            String t = me.getDocument().getContent();
            if (!t.equals(me.getJTextArea().getText())) {
                me.getJTextArea().setText(t);
            }
            JPanel p = this.getTabComponent(me.getDocument());
            this.tabbedPane.setTabComponentAt(i, p);
        }
    }

    private JPanel getTabComponent(Document doc) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        p.setName("tab-" + doc.getTitle());

        JLabel label = new JLabel(doc.getTitle(), doc.getIcon().get16(), SwingConstants.LEFT);
        label.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    DocumentTabbedPane.this.openDocumentsModel.setSelectedDocument(doc);
                } else if (SwingUtilities.isMiddleMouseButton(e)) {
                    DocumentTabbedPane.this.documentActions.getCloseFileAction(doc).actionPerformed(null);
                }
                this.mouseClicked(e);
            }

            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    (new DocumentsPopupMenu(DocumentTabbedPane.this.documentActions, doc)).show(e.getComponent(), e.getX(), e.getY());
                }
                super.mouseReleased(e);
            }
        });
        label.setToolTipText(doc.getFilePath());
        label.setOpaque(false);
        p.add(label);
        p.setOpaque(true);
        int h = Math.abs(doc.getTitle().hashCode());
        int r = 192 + h % 64;
        int g = 192 + h / 64 % 64;
        int b = 192 + h / 64 * 64 % 64;
        p.setBackground(new Color(r, g, b, 0));

        if (doc.equals(this.openDocumentsModel.getSelectedDocument())) {
            JButton closeButton = new JButton("x");
            closeButton.setMaximumSize(new Dimension(16, 16));
            closeButton.setPreferredSize(new Dimension(16, 16));
            closeButton.setMargin(new Insets(0, 0, 0, 0));
            closeButton.setBorder(BorderFactory.createEmptyBorder());
            closeButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    DocumentTabbedPane.this.documentActions.getCloseFileAction(doc).actionPerformed(null);
                }
            });

            p.add(closeButton);
        }
        return p;
    }

    public String getText() {
        int idx = this.indexOfDocument(this.openDocumentsModel.getSelectedDocument());
        JTextArea ta = this.displayedEditors.get(idx).getJTextArea();
        String selText = ta.getSelectedText();
        if (selText == null || selText.length() == 0) {
            selText = ta.getText();
        }
        return selText;
    }

    public void updateErrorDisplay(String code, List<Diagnostic<? extends JavaFileObject>> diagnostics) {
        int idx = this.indexOfDocument(this.openDocumentsModel.getSelectedDocument());
        if (idx != -1) {
            MyEditor myEditor = this.displayedEditors.get(idx);
            myEditor.removeAllErrorHighlights();
            if (diagnostics != null && myEditor.getJTextArea().getText().equals(code))
                for (Diagnostic<? extends JavaFileObject> d : diagnostics) {
                    long p = d.getLineNumber();
                    if (p != -1L)
                        myEditor.addHighlight((int) p, ErrorLevel.get(d.getKind()), d.getMessage(null));
                }
        }
    }

    private class UpdateTabsListener implements OpenDocumentsModel.Listener {
        private UpdateTabsListener() {
        }

        public void docClosed(Document document) {
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    DocumentTabbedPane.this.iAmChangingSelection = true;
                    int index = DocumentTabbedPane.this.indexOfDocument(document);
                    if (index >= 0) {
                        DocumentTabbedPane.this.tabbedPane.remove(index);
                        DocumentTabbedPane.this.displayedEditors.remove(index);
                    }
                    DocumentTabbedPane.this.iAmChangingSelection = false;
                }
            });
        }

        public void docSelected(Document document) {
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    int index = DocumentTabbedPane.this.indexOfDocument(document);
                    if (DocumentTabbedPane.this.tabbedPane.getSelectedIndex() != index) {
                        DocumentTabbedPane.this.refreshTabAppearance();
                        DocumentTabbedPane.this.iAmChangingSelection = true;
                        int idx = DocumentTabbedPane.this.indexOfDocument(document);
                        DocumentTabbedPane.this.tabbedPane.setSelectedIndex(idx);
                        DocumentTabbedPane.this.iAmChangingSelection = false;
                    }
                    DocumentTabbedPane.this.requestFocusInWindow();
                }
            });
        }

        public void docAdded(Document document) {
            EventQueue.invokeLater(DocumentTabbedPane.this::refreshTabAppearance);
        }

        public void docSaved() {
            this.refresh();
        }

        public void docContentModified() {
            this.refresh();
        }

        private void refresh() {
            EventQueue.invokeLater(DocumentTabbedPane.this::refreshTabAppearance);
        }

        public void docCaratModified() {
        }

        public void folderSelected(File selectedFolder) {
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\DocumentTabbedPane.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */