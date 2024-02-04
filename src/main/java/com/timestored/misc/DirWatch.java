package com.timestored.misc;

import com.google.common.base.Preconditions;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;
import java.io.FileFilter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DirWatch {
    private static final Logger LOG = Logger.getLogger(DirWatch.class.getName());
    private final List<DirWatchListener> listeners = new CopyOnWriteArrayList<>();

    private final FileFilter fileFilter;
    private final ChangedFileAlterationListener fileAlterationListener;
    private final long refreshTimer;
    private FileAlterationMonitor monitor;
    private FileAlterationObserver fao;

    public DirWatch(long refreshTimer, FileFilter fileFilter) {
        this.fileFilter = fileFilter;
        this.fileAlterationListener = new ChangedFileAlterationListener();
        this.refreshTimer = refreshTimer;
    }

    public void setRoot(File root) throws Exception {
        Preconditions.checkNotNull(root);
        this.stop();

        this.fao = new FileAlterationObserver(root, this.fileFilter);
        this.fao.addListener(this.fileAlterationListener);
        this.monitor = new FileAlterationMonitor(this.refreshTimer, this.fao);
        this.monitor.start();
    }

    public void stop() {
        if (this.fao != null) {
            this.fao.removeListener(this.fileAlterationListener);
        }
        if (this.monitor != null) {
            this.monitor.removeObserver(this.fao);
            this.fao = null;
            try {
                this.monitor.stop();
            } catch (Exception e) {
                LOG.log(Level.WARNING, "problem stopping", e);
            }
            this.monitor = null;
        }
    }

    public void addListener(DirWatchListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(DirWatchListener listener) {
        this.listeners.remove(listener);
    }

    public interface DirWatchListener {
        void changeOccurred();
    }

    private class ChangedFileAlterationListener
            implements FileAlterationListener {
        private ChangedFileAlterationListener() {
        }

        public void onStart(FileAlterationObserver observer) {
            LOG.fine("The WindowsFileListener has started on " + observer.getDirectory().getAbsolutePath());
        }

        public void onDirectoryCreate(File directory) {
            this.n();
        }

        private void n() {
            for (DirWatch.DirWatchListener l : DirWatch.this.listeners)
                l.changeOccurred();
        }

        public void onDirectoryChange(File directory) {
            this.n();
        }

        public void onDirectoryDelete(File directory) {
            this.n();
        }

        public void onFileCreate(File file) {
            this.n();
        }

        public void onFileChange(File file) {
            this.n();
        }

        public void onFileDelete(File file) {
            this.n();
        }

        public void onStop(FileAlterationObserver observer) {
            LOG.fine("The WindowsFileListener has stopped on " + observer.getDirectory().getAbsolutePath());
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\misc\DirWatch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */