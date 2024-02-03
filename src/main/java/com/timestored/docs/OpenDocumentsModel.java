package com.timestored.docs;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

public class OpenDocumentsModel {
    private static final Logger LOG = Logger.getLogger(OpenDocumentsModel.class.getName());

    private final List<Document> documents = new CopyOnWriteArrayList<Document>();
    private final CopyOnWriteArrayList<Listener> listeners;
    private final Document.Listener selectedDocListener;
    private Document selectedDocument;
    private File selectedFolder;

    private OpenDocumentsModel() {
        this.listeners = new CopyOnWriteArrayList<Listener>();

        this.selectedDocListener = new Document.Listener() {
            public void docContentModified() {
                for (Document.Listener l : OpenDocumentsModel.this.listeners) {
                    l.docContentModified();
                }
            }

            public void docCaratModified() {
                for (Document.Listener l : OpenDocumentsModel.this.listeners) {
                    l.docCaratModified();
                }
            }

            public void docSaved() {
                for (Document.Listener l : OpenDocumentsModel.this.listeners) {
                    l.docSaved();
                }
            }
        };

        this.selectedDocument = this.addDocument();
        this.selectedDocument.addListener(this.selectedDocListener);
    }

    public static OpenDocumentsModel newInstance() {
        return new OpenDocumentsModel();
    }

    public void forceKeyboardShortcutOverrides() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            boolean down;

            public boolean dispatchKeyEvent(KeyEvent e) {
                int kc = e.getKeyCode();
                boolean ctrl = e.isControlDown();
                if (!this.down && ctrl && kc == 115) {
                    OpenDocumentsModel.this.closeDocument();
                    this.down = true;
                } else if (!this.down && ctrl && e.isShiftDown() && kc == 9) {
                    OpenDocumentsModel.this.gotoPrevDocument();
                    this.down = true;
                } else if (!this.down && ctrl && kc == 9) {
                    OpenDocumentsModel.this.gotoNextDocument();
                    this.down = true;
                } else {
                    this.down = false;
                }
                if (this.down) {
                    e.consume();
                }
                return false;
            }
        });
    }

    public Document openDocument(String filepath) throws IOException {
        Preconditions.checkNotNull(filepath);
        return this.openDocument(new File(filepath));
    }

    public void openDocuments(List<String> filepaths) {
        for (String fp : filepaths) {
            try {
                this.openDocument(new File(fp));
            } catch (IOException e) {
                LOG.warning("Couldn't open previously opened file location:" + fp);
            }
        }
        if (this.documents.size() > 0) {
            this.changeSelectedDocTo(this.documents.get(this.documents.size() - 1));
        } else {
            this.changeSelectedDocTo(this.addDocument());
        }
        this.selectedDocument.addListener(this.selectedDocListener);

        for (Listener l : this.listeners) {
            l.docSelected(this.selectedDocument);
        }
    }

    public void changeSelectedDocTo(Document d) {
        if (this.selectedDocument != null) {
            this.selectedDocument.removeListener(this.selectedDocListener);
        }
        this.selectedDocument = d;
        this.selectedDocument.addListener(this.selectedDocListener);
    }

    public Document addDocument() {
        Document d = new Document();
        LOG.info("addDocument: " + d.getTitle());
        this.documents.add(d);
        this.changeSelectedDocTo(d);

        for (Listener l : this.listeners) {
            l.docAdded(d);
            l.docSelected(d);
        }
        return d;
    }

    public Document openDocument(File file) throws IOException {
        LOG.info("openDocument: " + file.getName());

        for (Document document : this.documents) {
            if (document.getFilePath() != null && document.getFilePath().equals(file.getAbsolutePath())) {
                LOG.info("openDocument: was already open, reselecting->" + file.getName());
                for (Listener l : this.listeners) {
                    l.docSelected(document);
                }
                return document;
            }
        }

        Document d = new Document(file);
        this.documents.add(d);
        for (Listener l : this.listeners) {
            l.docAdded(d);
        }
        this.setSelectedDocument(d);
        return d;
    }

    public void closeDocument() {
        this.closeDocument(this.selectedDocument);
    }

    public void closeDocument(Document document) {
        LOG.info("closeDocument: " + document.getTitle());
        this.documents.remove(document);
        if (this.selectedDocument == document) {
            if (this.documents.size() > 0) {
                this.changeSelectedDocTo(this.documents.get(0));
            } else {
                this.changeSelectedDocTo(this.addDocument());
            }
        }

        for (Listener l : this.listeners) {
            l.docClosed(document);
            l.docSelected(this.selectedDocument);
        }
    }

    public void saveAs(File file) throws IOException {
        LOG.info("saveAs: " + this.selectedDocument.getTitle() + " as " + file.getAbsolutePath());
        this.selectedDocument.saveAs(file);
    }

    public void saveDocument() throws IOException {
        LOG.info("saveDocument: " + this.selectedDocument.getTitle());
        this.selectedDocument.save();
    }

    public void gotoNextDocument() {
        LOG.info("gotoNextDocument");
        int i = (this.documents.indexOf(this.selectedDocument) + 1) % this.documents.size();
        this.setSelectedDocument(this.documents.get(i));
    }

    public void gotoPrevDocument() {
        LOG.info("gotoPrevDocument");
        int i = this.documents.indexOf(this.selectedDocument) - 1;
        i = (i < 0) ? (this.documents.size() - 1) : i;
        this.setSelectedDocument(this.documents.get(i));
    }

    public void setContent(String content) {
        LOG.info("setContent carat=" + this.selectedDocument.getCaratPosition());
        this.selectedDocument.setContent(content);
    }

    public List<Document> getDocuments() {
        return this.documents;
    }

    public Document getSelectedDocument() {
        return this.selectedDocument;
    }

    public void setSelectedDocument(Document document) {
        LOG.info("setSelectedDocument: " + document.getTitle());

        if (this.documents.contains(document)) {
            if (!document.equals(this.selectedDocument)) {
                this.changeSelectedDocTo(document);
            }
            for (Listener l : this.listeners) {
                l.docSelected(document);
            }
        } else {
            String msg = "I dont have doc: " + document.getTitle();
            LOG.warning(msg);
            throw new IllegalArgumentException(msg);
        }
    }

    public File getSelectedFolder() {
        return this.selectedFolder;
    }

    public void setSelectedFolder(File selectedFolder) {
        LOG.info("setSelectedFolder: " + selectedFolder);

        boolean noChange = (java.util.Objects.equals(selectedFolder, this.selectedFolder));

        if (!noChange) {
            if (selectedFolder == null || selectedFolder.isDirectory()) {
                this.selectedFolder = selectedFolder;
                for (Listener l : this.listeners) {
                    l.folderSelected(selectedFolder);
                }
            } else {
                String msg = "not a directory: " + selectedFolder;
                LOG.warning(msg);
                throw new IllegalArgumentException(msg);
            }
        }

        this.selectedFolder = selectedFolder;
    }

    public boolean hasAnyUnsavedChanges() {
        for (Document d : this.documents) {
            if (d.hasUnsavedChanges()) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("documentsSize", this.documents.size()).add("selectedDocument", this.selectedDocument).add("listenersSize", this.listeners.size()).toString();
    }

    public void insertSelectedText(String text) {
        this.selectedDocument.insertSelectedText(text);
    }

    public void insertText(String text) {
        this.selectedDocument.insertText(text);
    }

    public void closeAll() {
        List<Document> docs = new ArrayList<Document>(this.documents);
        LOG.info("closeAll");
        this.documents.clear();
        this.changeSelectedDocTo(this.addDocument());

        for (Listener l : this.listeners) {
            l.docSelected(this.selectedDocument);
        }
        for (Document closedDoc : docs) {
            for (Listener l : this.listeners)
                l.docClosed(closedDoc);
        }
    }

    public void addListener(Listener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        this.listeners.remove(listener);
    }

    public interface Listener extends Document.Listener {
        void docAdded(Document param1Document);

        void docClosed(Document param1Document);

        void docSelected(Document param1Document);

        void folderSelected(File param1File);
    }

    public abstract static class Adapter implements Listener {
        public void docAdded(Document document) {
        }

        public void docClosed(Document document) {
        }

        public void docSelected(Document document) {
        }

        public void docContentModified() {
        }

        public void docCaratModified() {
        }

        public void docSaved() {
        }

        public void folderSelected(File selectedFolder) {
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\docs\OpenDocumentsModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */