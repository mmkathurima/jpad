package com.timestored.command;

import com.timestored.docs.DocumentActions;
import com.timestored.messages.Msg;
import com.timestored.swingxx.FileTreePanel;

import javax.swing.Icon;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FileOpenCommandProvider
        implements CommandProvider {
    private static final FileSystemView FSV = FileSystemView.getFileSystemView();
    private final DocumentActions documentActions;
    private final FileTreePanel fileTreePanel;

    public FileOpenCommandProvider(DocumentActions documentActions, FileTreePanel fileTreePanel) {
        this.documentActions = documentActions;
        this.fileTreePanel = fileTreePanel;
    }

    public Collection<Command> getCommands() {
        Collection<File> fcache = this.fileTreePanel.getFileCache();
        if (fcache.size() > 0) {
            List<Command> cmds = new ArrayList<>(fcache.size());
            for (File f : fcache) {
                cmds.add(new FileOpenCommand(f));
            }
            return cmds;
        }
        return Collections.emptyList();
    }

    private class FileOpenCommand
            implements Command {
        private final File f;

        private FileOpenCommand(File f) {
            this.f = f;
        }

        public Icon getIcon() {
            return FSV.getSystemIcon(this.f);
        }

        public String getTitle() {
            return Msg.get(Msg.Key.OPEN_FILE) + ": " + this.f.getName();
        }

        public String getDetailHtml() {
            return this.f.getAbsolutePath();
        }

        public KeyStroke getKeyStroke() {
            return null;
        }

        public String toString() {
            return this.getTitle();
        }

        public String getTitleAdditional() {
            return this.f.getAbsolutePath();
        }

        public void perform() {
            FileOpenCommandProvider.this.documentActions.openFile(this.f);
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\command\FileOpenCommandProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */