package com.timestored.docs;

import javax.swing.JComponent;
import javax.swing.TransferHandler;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class FileDropDocumentHandler
        extends TransferHandler {
    private static final long serialVersionUID = 1L;
    private static final DataFlavor MY_DATA_FLAVOR = DataFlavor.javaFileListFlavor;
    private final TransferHandler fallbackHandler;
    private final List<Listener> listeners = new CopyOnWriteArrayList<Listener>();


    public FileDropDocumentHandler() {
        this(null);
    }

    public FileDropDocumentHandler(TransferHandler fallbackHandler) {
        this.fallbackHandler = fallbackHandler;
    }

    public FileDropDocumentHandler addListener(Listener listener) {
        this.listeners.add(listener);
        return this;
    }

    public FileDropDocumentHandler removeListener(Listener listener) {
        this.listeners.remove(listener);
        return this;
    }


    public boolean canImport(TransferHandler.TransferSupport tSupp) {
        return ((this.fallbackHandler != null && this.fallbackHandler.canImport(tSupp)) || (tSupp.isDrop() && tSupp.isDataFlavorSupported(MY_DATA_FLAVOR)));
    }


    public boolean importData(TransferHandler.TransferSupport tSupp) {
        if (!canImport(tSupp)) {
            return false;
        }

        if (tSupp.isDrop() && tSupp.isDataFlavorSupported(MY_DATA_FLAVOR)) {
            Transferable t = tSupp.getTransferable();

            try {
                List<File> files = (List<File>) t.getTransferData(MY_DATA_FLAVOR);
                for (Listener l : this.listeners) {
                    l.filesDropped(files);
                }
            } catch (IOException e1) {
                return false;
            } catch (UnsupportedFlavorException e) {
                return false;
            }
        }
        if (this.fallbackHandler != null) {
            return this.fallbackHandler.importData(tSupp);
        }

        return true;
    }


    public void exportToClipboard(JComponent comp, Clipboard clip, int action) throws IllegalStateException {
        if (this.fallbackHandler != null)
            this.fallbackHandler.exportToClipboard(comp, clip, action);
    }

    public interface Listener {
        void filesDropped(List<File> param1List);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\docs\FileDropDocumentHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */