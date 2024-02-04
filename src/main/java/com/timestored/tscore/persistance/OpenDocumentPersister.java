package com.timestored.tscore.persistance;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.timestored.docs.Document;
import com.timestored.docs.OpenDocumentsModel;
import com.timestored.misc.FifoBuffer;
import com.timestored.misc.IOUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OpenDocumentPersister
        implements OpenDocumentsModel.Listener {
    private static final Logger log = Logger.getLogger(OpenDocumentPersister.class.getName());

    private static final String CONTENT_MARKER = "\r\nCONTENT:\r\n";

    private static final String PATH_MARKER = "\r\nPATH:\r\n";
    private final OpenDocumentsModel openDocumentsModel;
    private final FifoBuffer<String> recentFilePaths = new FifoBuffer<>(9);

    private final PersistenceInterface persistance;

    private final File scratchDir;

    private final KeyInterface recentDocsKey;

    private final KeyInterface lastOpenedFolderKey;

    public OpenDocumentPersister(OpenDocumentsModel openDocumentsModel, PersistenceInterface persistance, File scratchDir, KeyInterface recentDocsKey, KeyInterface lastOpenedFolderKey) {
        this.openDocumentsModel = Preconditions.checkNotNull(openDocumentsModel);
        this.persistance = Preconditions.checkNotNull(persistance);
        this.scratchDir = Preconditions.checkNotNull(scratchDir);
        this.recentDocsKey = Preconditions.checkNotNull(recentDocsKey);
        this.lastOpenedFolderKey = Preconditions.checkNotNull(lastOpenedFolderKey);

        this.recentFilePaths.addAll(getFilePaths(persistance, recentDocsKey));
        scratchDir.mkdir();
    }

    private static List<String> getFilePaths(PersistenceInterface persistance, KeyInterface filekey) {
        List<String> filepaths = Lists.newArrayList();
        String recent = persistance.get(filekey, "");
        String[] recDocs = recent.split(";");
        for (String filepath : recDocs) {
            if (!filepath.trim().isEmpty()) {
                filepaths.add(filepath);
            }
        }
        return filepaths;
    }

    private void persistOpenDocuments() {
        this.storeDocumentsScratch();

        String recent = Joiner.on(";").join(this.recentFilePaths.getAll());
        this.persistance.put(this.recentDocsKey, recent);
    }

    public void storeDocumentsScratch() {
        int i = 0;

        File[] existingFiles = this.scratchDir.listFiles();
        if (existingFiles != null) {
            for (File ef : existingFiles) {
                ef.delete();
            }
        }
        List<Document> documents = this.openDocumentsModel.getDocuments();
        for (Document d : documents) {

            File f;
            do {
                i++;
                f = new File(this.scratchDir, i + "-" + d.getTitle());
            } while (f.exists() && i++ < 1000);
            String firstLine = (d.getFilePath() == null) ? "" : d.getFilePath();
            try {
                StringBuilder sb = new StringBuilder(d.getTitle() + "\r\nPATH:\r\n" + firstLine);
                if (d.hasUnsavedChanges()) {
                    sb.append("\r\nCONTENT:\r\n").append(d.getContent());
                }
                IOUtils.writeStringToFile(sb.toString(), f);
            } catch (IOException e) {
                log.log(Level.SEVERE, "Could not save scratch files.", e);
            }
        }
    }

    public void restoreDocuments() {
        List<Document> startingDocuments = Lists.newArrayList(this.openDocumentsModel.getDocuments());

        File[] files = this.scratchDir.listFiles();
        List<File> scratchFiles = Collections.emptyList();
        if (files != null) {
            scratchFiles = Lists.newArrayList(files);
        }

        for (File f : scratchFiles) {
            try {
                String c = IOUtils.toString(f);
                int pathPos = c.indexOf("\r\nPATH:\r\n");
                int contentPos = c.indexOf("\r\nCONTENT:\r\n");
                if (pathPos > 0) {
                    String title = c.substring(0, pathPos);
                    String path = c.substring(pathPos + "\r\nPATH:\r\n".length());
                    String content = null;
                    if (contentPos > pathPos) {
                        path = c.substring(pathPos + "\r\nPATH:\r\n".length(), contentPos);
                        content = c.substring(contentPos + "\r\nCONTENT:\r\n".length());
                    }

                    Document d;
                    if (path.trim().length() > 0) {
                        log.info("attempting to restore existing known document");
                        File pFile = new File(path);
                        if (pFile.exists() && pFile.canRead()) {
                            try {
                                d = this.openDocumentsModel.openDocument(pFile);
                                if (content != null) {
                                    d.setContent(content);
                                }
                            } catch (IOException ioe) {
                            }
                        }

                        continue;
                    }
                    d = this.openDocumentsModel.addDocument();
                    d.setTitle(title);
                    if (content != null) {
                        d.setContent(content);
                    }
                    continue;
                }
                log.log(Level.WARNING, "found scratch file I dont know how to restore: " + f.getAbsolutePath());
            } catch (IOException e) {
                log.log(Level.WARNING, "error restoring files from scratch: " + e);
            }
        }

        if (startingDocuments.size() == 1) {
            Document onlyDoc = startingDocuments.get(0);
            if (onlyDoc.getContent().trim().isEmpty()) {
                this.openDocumentsModel.closeDocument(onlyDoc);
            }
        }
    }

    public List<String> getRecentFilePaths() {
        return this.recentFilePaths.getAll();
    }

    public File getOpenFolder(PersistenceInterface persistance) {
        String path = persistance.get(this.lastOpenedFolderKey, "");
        if (!path.equals("")) {
            File f = new File(path);
            if (f.isDirectory()) {
                return f;
            }
        }
        return null;
    }

    public void docClosed(Document document) {
        if (document.getFilePath() != null) {
            this.recentFilePaths.add(document.getFilePath());
        }
        this.persistOpenDocuments();
    }

    public void docAdded(Document document) {
        if (document.getFilePath() != null) {
            this.recentFilePaths.add(document.getFilePath());
        }
        this.persistOpenDocuments();
    }

    public void docSaved() {
        this.persistOpenDocuments();
    }

    public void docSelected(Document document) {
    }

    public void folderSelected(File selectedFolder) {
        String path = (selectedFolder == null) ? "" : selectedFolder.getAbsolutePath();
        this.persistance.put(this.lastOpenedFolderKey, path);
    }

    public void docContentModified() {
    }

    public void docCaratModified() {
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\tscore\persistance\OpenDocumentPersister.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */