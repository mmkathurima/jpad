package com.timestored.docs;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.timestored.misc.IOUtils;
import com.timestored.theme.Icon;
import com.timestored.theme.Theme;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class Document {
    private static final Logger LOG = Logger.getLogger(Document.class.getName());
    private static final AtomicInteger counter = new AtomicInteger(0);

    private final List<Listener> listeners = new CopyOnWriteArrayList<Listener>();

    private String title;
    private File file;
    private String savedContent = "";
    private String content = "";
    private int selectionStart;
    private int selectionEnd;
    private int caretPosition;

    public Document() {
        int v = counter.incrementAndGet();
        this.title = "new " + v;
    }

    public Document(File file) throws IOException {
        this.file = Preconditions.checkNotNull(file);
        this.content = IOUtils.toString(file, StandardCharsets.UTF_8).replace("\r", "");
        this.savedContent = this.content;
        this.title = file.getName();
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean hasUnsavedChanges() {
        return !this.savedContent.equals(this.content);
    }

    public boolean isReadOnly() {
        return (this.file != null && !this.file.canWrite());
    }

    public String getFilePath() {
        return (this.file == null) ? null : this.file.getAbsolutePath();
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("title", this.title).add("file", this.file).add("selectionStart", this.selectionStart).add("selectionEnd", this.selectionEnd).add("unsavedChanges", this.hasUnsavedChanges()).toString();
    }

    public boolean isQKfileSuffix() {
        if (this.file != null) {
            String fp = this.file.getName().toUpperCase();
            return (fp.endsWith(".Q") || fp.endsWith(".K"));
        }
        return false;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content.replace("\r", "");
        int l = content.length();
        if (this.selectionEnd > l) {
            this.selectionEnd = l;
        }
        if (this.selectionStart > l) {
            this.selectionStart = l;
        }
        this.notifyListenersContentModified();
    }

    public int getSelectionEnd() {
        return this.selectionEnd;
    }

    public int getSelectionStart() {
        return this.selectionStart;
    }

    public String getSelectedText() {
        return this.content.substring(this.selectionStart, this.selectionEnd);
    }

    public boolean isTextSelected() {
        return (this.selectionEnd > this.selectionStart);
    }

    public String getCurrentLine() {
        final char LS = '\n';

        int i = Math.max(this.caretPosition - 1, 0);
        while (i > 0 && i < this.content.length() && this.content.charAt(i) != '\n') {
            i--;
        }

        int j = Math.min(this.caretPosition, this.content.length());
        while (j < this.content.length() && this.content.charAt(j) != '\n') {
            j++;
        }

        return this.content.substring(i, j).trim();
    }

    public String getTextBeforeCarat() {
        final char LS = '\n';
        int i = Math.max(this.caretPosition - 1, 0);

        while (i > 0 && i < this.content.length() && this.content.charAt(i) != '\n' && this.content.charAt(i) != ' ') {
            i--;
        }

        return this.content.substring(i, this.caretPosition).trim();
    }

    public void saveAs(File file) throws IOException {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            if (this.isWindows()) {
                bw.write(this.content.replace("\n", "\r\n"));
            } else {
                bw.write(this.content);
            }
            bw.close();
            this.file = file;
            this.title = file.getName();
            this.savedContent = this.content;
            for (Listener l : this.listeners) {
                l.docSaved();
            }
        } catch (IOException ex) {
            throw new IOException(ex);
        }
    }

    private boolean isWindows() {
        return (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0);
    }

    public void save() throws IOException {
        try {
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(this.file), StandardCharsets.UTF_8);
            BufferedWriter bw = new BufferedWriter(osw);
            if (this.isWindows()) {
                bw.write(this.content.replace("\n", "\r\n"));
            } else {
                bw.write(this.content);
            }
            bw.close();
            this.savedContent = this.content;

            for (Listener l : this.listeners) {
                l.docSaved();
            }
        } catch (IOException ex) {
            throw new IOException(ex);
        }
    }

    public void setSelection(int selectionStart, int selectionEnd, int caretPosition) {
        Preconditions.checkArgument((selectionStart >= 0 && selectionEnd >= selectionStart));

        Preconditions.checkArgument((selectionEnd <= this.content.length()));

        this.selectionStart = selectionStart;
        this.selectionEnd = selectionEnd;
        this.caretPosition = caretPosition;
        LOG.finest("(selectionStart - selectionEnd) -> " + selectionStart + " - " + selectionEnd);
        LOG.finest("setSelection -> getSelectedText() = " + this.getSelectedText());
        this.notifyListenersCaratMoved();
    }

    public int getCaratPosition() {
        return this.caretPosition;
    }

    public void setCaratPosition(int caratPosition) {
        Preconditions.checkArgument((caratPosition <= this.content.length()));
        this.caretPosition = caratPosition;
        LOG.finest("caratPosition = " + caratPosition);
        this.notifyListenersCaratMoved();
    }

    public void insertSelectedText(String text) {
        this.content = this.content.substring(0, this.selectionStart) + "\n" + text + this.content.substring(this.selectionEnd);

        if (this.caretPosition > this.content.length()) {
            this.caretPosition = this.content.length();
        }
        int pos = this.selectionStart + text.length() + 1;
        this.notifyListenersContentModified();
        this.setCaratPosition(pos);
    }

    public void insertText(String text) {
        this.content = this.content.substring(0, this.selectionEnd) + text + this.content.substring(this.selectionEnd);

        this.notifyListenersContentModified();
    }

    public void gotoNextLine() {
        int i = this.caretPosition;
        boolean found = false;
        while (i < this.content.length() - 1) {
            if (this.content.charAt(i) == '\n') {
                found = true;
                break;
            }
            i++;
        }
        if (found) {
            this.setCaratPosition(i + 1);
        }
    }

    public void addListener(Listener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        this.listeners.remove(listener);
    }

    private void notifyListenersContentModified() {
        for (Listener l : this.listeners) {
            l.docContentModified();
        }
    }

    private void notifyListenersCaratMoved() {
        for (Listener l : this.listeners) {
            l.docCaratModified();
        }
    }

    public Icon getIcon() {
        Theme.CIcon cIcon = Theme.CIcon.PAGE;
        if (this.isReadOnly()) {
            cIcon = Theme.CIcon.PAGE_WHITE_ZIP;
        } else if (this.hasUnsavedChanges()) {
            cIcon = Theme.CIcon.PAGE_RED;
        } else if (this.isQKfileSuffix()) {
            cIcon = Theme.CIcon.PAGE_CODE;
        }
        return cIcon;
    }

    public interface Listener {
        void docContentModified();

        void docCaratModified();

        void docSaved();
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\docs\Document.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */