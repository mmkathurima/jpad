package com.timestored.swingxx;

import com.timestored.messages.Msg;
import com.timestored.misc.DirWatch;
import com.timestored.misc.FifoBuffer;
import com.timestored.misc.IOUtils;
import com.timestored.theme.Theme;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileTreePanel extends JPanel {
    public static final FileFilter IGNORE_SVN_FILTER = new IgnoreGitSvnFileFilter();
    private static final Logger LOG = Logger.getLogger(FileTreePanel.class.getName());
    private static final int MAX_FILES_TO_WATCH = 1000;

    private static final FileSystemView fsv = FileSystemView.getFileSystemView();
    private final CopyOnWriteArrayList<Listener> listeners;
    private final TreeMouseListener treeMouseListener;
    private final FileFilter fileFilter;
    private final DirWatch dirWatch;
    private final FifoBuffer<File> fileCache = new FifoBuffer<>(1000);
    private final FileTreeCellRenderer fileTreeCellRenderer;
    private JTree tree;
    private File root;
    private Component noRootsComponent;
    private boolean rightClickMenuShown = true;

    public FileTreePanel() {
        this.listeners = new CopyOnWriteArrayList<>();
        this.fileFilter = IGNORE_SVN_FILTER;
        this.dirWatch = new DirWatch(30100L, this.fileFilter);
        this.setLayout(new BorderLayout());
        this.noRootsComponent = new JLabel(" No root folder selected");
        this.noRootsComponent.setName("noRootsComponent");

        this.treeMouseListener = new TreeMouseListener();
        this.addMouseListener(new RefreshTreeMouseListener());
        this.refreshGui();
        this.dirWatch.addListener(FileTreePanel.this::refreshGui);

        this.fileTreeCellRenderer = new FileTreeCellRenderer();
        this.fileTreeCellRenderer.addListener(FileTreePanel.this.fileCache::add);
    }

    private static void addChildren(List<File> fs, File[] list, FileFilter fileFilter) {
        if (list != null) {
            fs.addAll(Arrays.asList(list));
            for (File curF : list) {
                if (fs.size() < 1000) {
                    addChildren(fs, getFiles(fileFilter, curF), fileFilter);
                }
            }
        }
    }

    private static File[] getFiles(FileFilter fileFilter, File f) {
        File[] files;
        if (fileFilter == null) {
            files = f.listFiles();
        } else {
            files = f.listFiles(fileFilter);
        }
        return files;
    }

    public Collection<File> getFileCache() {
        return this.fileCache.getAll();
    }

    private JPopupMenu getFileRightClickMenu(File file) {
        JPopupMenu popupMenu = new JPopupMenu("");
        popupMenu.setName("fileRightClickMenu");

        File contextFile = (file == null) ? this.root : file;
        File f = contextFile;
        if (!f.isDirectory()) {
            f = contextFile.getParentFile();
        }
        File nearestDir = f;

        String openText = contextFile.isDirectory() ? "Open Folder" : "Open Containing Folder";
        popupMenu.add(new AbstractAction(openText) {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                if (nearestDir.isDirectory()) {
                    try {
                        Desktop.getDesktop().open(nearestDir);
                    } catch (IOException ioe) {
                        final String msg = "Could not open folder";
                        LOG.log(Level.WARNING, msg, ioe);
                        JOptionPane.showMessageDialog(null, msg);
                    }
                }
            }
        });

        popupMenu.add(new AbstractAction(Msg.get(Msg.Key.CREATE_NEW_FOLDER), Theme.CIcon.FOLDER_ADD.get16()) {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                String newFolderName = JOptionPane.showInputDialog("Enter name for new Folder:", "New Folder");
                if (newFolderName != null) {
                    File f = new File(nearestDir, newFolderName);
                    f.mkdirs();
                    f.mkdir();
                    FileTreePanel.this.refreshGui();
                }
            }
        });

        popupMenu.add(new AbstractAction(Msg.get(Msg.Key.CREATE_NEW_FILE), Theme.CIcon.PAGE.get16()) {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                String newFileName = JOptionPane.showInputDialog("Enter name for new file:", "New File");
                final String errMsg = "Could not create new File";
                if (newFileName != null) {
                    File f = new File(nearestDir, newFileName);
                    boolean success;
                    f.getParentFile().mkdirs();
                    try {
                        success = f.createNewFile();
                    } catch (IOException ioe) {
                        success = false;
                        LOG.log(Level.WARNING, errMsg, ioe);
                    }
                    if (!success) {
                        JOptionPane.showMessageDialog(null, errMsg);
                    }
                    FileTreePanel.this.refreshGui();
                }
            }
        });

        popupMenu.add(new AbstractAction("Refresh Tree") {
            public void actionPerformed(ActionEvent e) {
                FileTreePanel.this.refreshGui();
            }
        });

        return popupMenu;
    }

    public void refreshGui() {
        if (this.root != null && (
                !this.root.isDirectory() || !this.root.canRead())) {
            this.root = null;
        }

        LOG.info("FileTreePanel refreshGui");
        if (this.tree != null) {
            this.tree.removeMouseListener(this.treeMouseListener);
        }
        if (this.root == null) {
            this.removeAll();
            this.add(this.noRootsComponent, "Center");
        } else {
            File[] files = getFiles(this.fileFilter, this.root);

            this.fileCache.addAll(this.generateFileCache(files, this.fileFilter));

            if (files.length > 0) {
                FileTreeNode rootTreeNode = new FileTreeNode(files, this.fileFilter);

                this.tree = new JTree(rootTreeNode);
                this.tree.setName("FileTreePanelTree");
                this.tree.setCellRenderer(this.fileTreeCellRenderer);
                this.tree.setRootVisible(false);
                this.tree.addMouseListener(this.treeMouseListener);
                JScrollPane jsp = new JScrollPane(this.tree);
                jsp.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

                this.removeAll();
                this.add(jsp, "Center");
            } else {

                this.removeAll();
                this.noRootsComponent = new JLabel(" Selected Folder is Empty");
                this.add(this.noRootsComponent, "Center");
            }
            this.revalidate();
            this.repaint();
        }
    }

    private Collection<File> generateFileCache(File[] files, FileFilter fileFilter) {
        if (files != null && files.length > 0) {
            List<File> fs = new ArrayList<>();
            addChildren(fs, files, fileFilter);
            return fs;
        }
        return Collections.emptyList();
    }

    public void setNoRootsComponent(Component noRootsComponent) {
        if (noRootsComponent == null) {
            throw new IllegalArgumentException("noRootsComponent cannot be null");
        }
        this.noRootsComponent = noRootsComponent;
        noRootsComponent.setName("noRootsComponent");
        if (this.root == null) {
            this.refreshGui();
        }
    }

    public void setRoot(File selectedFolder) {
        if (selectedFolder != null && !selectedFolder.isDirectory()) {
            throw new IllegalArgumentException("setRoots file must be a directory");
        }

        boolean rootChanging = ((selectedFolder == null && this.root != null) || (selectedFolder != null && !selectedFolder.equals(this.root)));

        if (rootChanging) {
            LOG.info("root changing");
            this.root = selectedFolder;
            if (selectedFolder == null) {
                this.dirWatch.stop();
            } else {
                try {
                    if (IOUtils.containsMoreThanMaxFiles(selectedFolder, 1000)) {
                        this.dirWatch.stop();
                    } else {
                        this.dirWatch.setRoot(selectedFolder);
                    }
                } catch (Exception e) {
                    LOG.warning("Could not watch root folder");
                }
            }

            if (SwingUtilities.isEventDispatchThread()) {
                this.refreshGui();
            } else {

                try {
                    EventQueue.invokeAndWait(FileTreePanel.this::refreshGui);
                } catch (InterruptedException | InvocationTargetException e) {
                }
            }
        }
    }

    public void addListener(Listener listener) {
        this.listeners.add(listener);
    }

    public boolean isRightClickMenuShown() {
        return this.rightClickMenuShown;
    }

    public void setRightClickMenuShown(boolean rightClickMenuShown) {
        this.rightClickMenuShown = rightClickMenuShown;
    }

    public interface Listener {
        void fileSelected(File param1File);
    }

    private static class FileTreeCellRenderer
            extends DefaultTreeCellRenderer {
        private final Map<String, Icon> iconCache = new HashMap<>();

        private final Map<File, String> rootNameCache = new HashMap<>();

        private final List<Listener> listeners = new CopyOnWriteArrayList<>();

        private FileTreeCellRenderer() {
        }

        public void addListener(Listener listener) {
            this.listeners.add(listener);
        }

        public void clearListeners() {
            this.listeners.clear();
        }

        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            if (value instanceof FileTreePanel.FileTreeNode) {
                FileTreePanel.FileTreeNode ftn = (FileTreePanel.FileTreeNode) value;
                File file = ftn.file;
                String filename = "";
                if (file != null) {
                    for (Listener l : this.listeners) {
                        l.renderedFile(file);
                    }
                    if (ftn.isFileSystemRoot) {

                        filename = this.rootNameCache.get(file);
                        if (filename == null) {
                            filename = fsv.getSystemDisplayName(file);
                            this.rootNameCache.put(file, filename);
                        }
                    } else {

                        filename = file.getName();
                    }
                }
                JLabel result = (JLabel) super.getTreeCellRendererComponent(tree, filename, sel, expanded, leaf, row, hasFocus);

                if (file != null) {
                    Icon icon = this.iconCache.get(filename);
                    if (icon == null) {

                        icon = fsv.getSystemIcon(file);
                        this.iconCache.put(filename, icon);
                    }
                    result.setIcon(icon);
                }
                return result;
            }
            return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        }

        private interface Listener {
            void renderedFile(File param2File);
        }
    }

    private static class FileTreeNode
            implements TreeNode {
        private final File file;
        private final TreeNode parent;
        private final FileFilter fileFilter;
        private File[] children;
        private boolean isFileSystemRoot;

        public FileTreeNode(File file, boolean isFileSystemRoot, TreeNode parent, FileFilter fileFilter) {
            this.file = file;
            this.isFileSystemRoot = isFileSystemRoot;
            this.fileFilter = fileFilter;
            this.parent = parent;
            this.children = getFiles(fileFilter, file);

            if (this.children == null) {
                this.children = new File[0];
            }
            Arrays.sort(this.children, new FileFolderNameComparator());
        }

        public FileTreeNode(File[] children, FileFilter fileFilter) {
            this.file = null;
            this.parent = null;
            Arrays.sort(children, new FileFolderNameComparator());
            this.children = children;
            this.fileFilter = fileFilter;
        }

        public Enumeration children() {
            int elementCount = this.children.length;
            return new Enumeration<File>() {
                int count;

                public boolean hasMoreElements() {
                    return (this.count < elementCount);
                }

                public File nextElement() {
                    if (this.count < elementCount) {
                        return FileTreePanel.FileTreeNode.this.children[this.count++];
                    }
                    throw new NoSuchElementException("Vector Enumeration");
                }
            };
        }

        public boolean getAllowsChildren() {
            return true;
        }

        public TreeNode getChildAt(int childIndex) {
            return new FileTreeNode(this.children[childIndex], (this.parent == null), this, this.fileFilter);
        }

        public int getChildCount() {
            return this.children.length;
        }

        public int getIndex(TreeNode node) {
            FileTreeNode ftn = (FileTreeNode) node;
            for (int i = 0; i < this.children.length; i++) {
                if (ftn.file.equals(this.children[i]))
                    return i;
            }
            return -1;
        }

        public TreeNode getParent() {
            return this.parent;
        }

        public boolean isLeaf() {
            return (this.getChildCount() == 0);
        }

        private static class FileFolderNameComparator implements Comparator<File>, Serializable {
            private FileFolderNameComparator() {
            }

            public int compare(File f1, File f2) {
                if (f1.isDirectory() && f2.isFile())
                    return -1;
                if (f2.isDirectory() && f1.isFile()) {
                    return 1;
                }
                return String.CASE_INSENSITIVE_ORDER.compare(f1.getName(), f2.getName());
            }
        }
    }

    private static class IgnoreGitSvnFileFilter implements FileFilter {
        private IgnoreGitSvnFileFilter() {
        }

        public boolean accept(File f) {
            return (!f.isDirectory() || (!f.getName().equals(".git") && !f.getName().equals(".svn")));
        }
    }

    private class TreeMouseListener
            extends MouseAdapter {
        private TreeMouseListener() {
        }

        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
                int row = FileTreePanel.this.tree.getClosestRowForLocation(e.getX(), e.getY());
                FileTreePanel.this.tree.setSelectionRow(row);
            }

            File f = null;
            TreePath tp = FileTreePanel.this.tree.getSelectionPath();
            Object o = (tp == null) ? null : tp.getLastPathComponent();
            if (o instanceof FileTreeNode) {
                f = ((FileTreePanel.FileTreeNode) o).file;
            }

            if (f != null && !f.canRead()) {
                FileTreePanel.this.refreshGui();
            } else if (f != null) {

                if (SwingUtilities.isRightMouseButton(e) && FileTreePanel.this.rightClickMenuShown) {
                    FileTreePanel.this.getFileRightClickMenu(f).show(e.getComponent(), e.getX(), e.getY());
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    LOG.info("FileTreePanel file selected->" + f);
                    for (FileTreePanel.Listener l : FileTreePanel.this.listeners)
                        l.fileSelected(f);
                }
            }
        }
    }

    private class RefreshTreeMouseListener
            extends MouseAdapter {
        private RefreshTreeMouseListener() {
        }

        public void mouseClicked(MouseEvent e) {
            if (FileTreePanel.this.root != null) {
                if (SwingUtilities.isRightMouseButton(e) && FileTreePanel.this.rightClickMenuShown) {
                    FileTreePanel.this.getFileRightClickMenu(FileTreePanel.this.root).show(e.getComponent(), e.getX(), e.getY());
                }
            }
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\swingxx\FileTreePanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */