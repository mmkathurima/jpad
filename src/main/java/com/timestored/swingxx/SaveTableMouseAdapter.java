package com.timestored.swingxx;

import org.jdesktop.swingx.renderer.StringValue;

import javax.swing.*;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SaveTableMouseAdapter
        extends MouseAdapter {
    private static final Logger LOG = Logger.getLogger(SaveTableMouseAdapter.class.getName());

    private static final String NL = "\r\n";
    private final JTable table;
    private final StringValue stringValue;
    private final ImageIcon csvIcon;

    public SaveTableMouseAdapter(JTable table, ImageIcon csvIcon) {
        this(table, csvIcon, null);
    }

    public SaveTableMouseAdapter(JTable table, ImageIcon csvIcon, StringValue stringValue) {
        this.table = table;
        this.stringValue = stringValue;
        this.csvIcon = csvIcon;

        String property = (stringValue != null) ? stringValue.toString() : null;
        table.setTransferHandler(new TransferHandler(property) {
            public void exportToClipboard(JComponent comp, Clipboard clip, int action) throws IllegalStateException {
                if (action == 1) {
                    boolean areaSelected = (table.getSelectedRow() != -1);
                    StringSelection selection = new StringSelection(SaveTableMouseAdapter.this.getTable(areaSelected, false, "\t"));
                    clip.setContents(selection, selection);
                    this.exportDone(comp, selection, action);
                } else {
                    super.exportToClipboard(comp, clip, action);
                }
            }
        });
    }

    public void mouseReleased(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            JPopupMenu menu = new JPopupMenu();
            boolean areaSelected = (this.table.getSelectedRow() != -1);

            menu.add(new CopyToClipboardAction("Copy Table", false, true));
            menu.add(new CopyToClipboardAction("Copy Selection", true, false)).setEnabled(areaSelected);
            menu.add(new CopyToClipboardAction("Copy Selection with Column Titles", true, true)).setEnabled(areaSelected);

            menu.add(new ExportAsCsvAction("Export Table", false, true));
            menu.add(new ExportAsCsvAction("Export Selection", true, false)).setEnabled(areaSelected);
            menu.add(new ExportAsCsvAction("Export Selection with Column Titles", true, true)).setEnabled(areaSelected);

            menu.show(e.getComponent(), e.getX(), e.getY());
            menu.setVisible(true);
        }
        super.mouseReleased(e);
    }

    private String getTable(boolean selectedAreaOnly, boolean includeHeaders, String separator) {
        int c = 0;
        int r = 0;
        int cEnd = this.table.getColumnCount();
        int rEnd = this.table.getRowCount();

        if (selectedAreaOnly) {
            c = this.table.getSelectedColumn();
            cEnd = c + this.table.getSelectedColumnCount();
            r = this.table.getSelectedRow();
            rEnd = r + this.table.getSelectedRowCount();
            if (c == -1) {
                return "";
            }
        }

        StringBuilder sb = new StringBuilder();

        if (includeHeaders) {
            for (int ci = c; ci < cEnd; ci++) {
                sb.append(this.table.getColumnName(ci));
                if (ci != cEnd - 1) {
                    sb.append(separator);
                }
            }
            sb.append("\r\n");
        }

        for (int ri = r; ri < rEnd; ri++) {
            for (int ci = c; ci < cEnd; ci++) {

                int modelCi = this.table.convertColumnIndexToModel(ci);
                int modelRi = this.table.convertRowIndexToModel(ri);
                Object o = this.table.getModel().getValueAt(modelRi, modelCi);
                String s = "";
                if (o != null) {
                    s = (this.stringValue == null) ? o.toString() : this.stringValue.getString(o);

                    if (o instanceof Number && !s.trim().isEmpty()) {
                        s = "" + o;
                    }
                }
                sb.append(s);
                if (ci != cEnd - 1) {
                    sb.append(separator);
                }
            }
            if (ri != rEnd - 1) {
                sb.append("\r\n");
            }
        }
        return sb.toString();
    }

    class ExportAsCsvAction
            extends AbstractAction {
        private static final long serialVersionUID = 1L;
        private final boolean selectedAreaOnly;
        private final boolean includeHeaders;

        public ExportAsCsvAction(String name, boolean selectedAreaOnly, boolean includeHeaders) {
            super(name, SaveTableMouseAdapter.this.csvIcon);
            this.selectedAreaOnly = selectedAreaOnly;
            this.includeHeaders = includeHeaders;
        }

        public void actionPerformed(ActionEvent arg0) {
            try {
                File f = File.createTempFile("document", ".csv");
                LOG.info("writing out to: " + f);
                FileWriter out = new FileWriter(f);
                out.write(SaveTableMouseAdapter.this.getTable(this.selectedAreaOnly, this.includeHeaders, ","));
                out.close();
                Desktop.getDesktop().open(f);
            } catch (IOException e) {
                final String msg = "Error saving file: ";
                LOG.log(Level.INFO, msg, e);
                JOptionPane.showMessageDialog(null, msg, "Error Saving", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class CopyToClipboardAction
            extends AbstractAction {
        private static final long serialVersionUID = 1L;
        private final boolean selectedAreaOnly;
        private final boolean includeHeaders;

        private CopyToClipboardAction(String name, boolean selectedAreaOnly, boolean includeHeaders) {
            super(name);
            this.selectedAreaOnly = selectedAreaOnly;
            this.includeHeaders = includeHeaders;
        }

        public void actionPerformed(ActionEvent arg0) {
            StringSelection selection = new StringSelection(SaveTableMouseAdapter.this.getTable(this.selectedAreaOnly, this.includeHeaders, "\t"));
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\swingxx\SaveTableMouseAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */