package com.timestored.sqldash.forms;

import com.google.common.primitives.Ints;
import com.timestored.sqldash.model.DesktopModel;
import com.timestored.sqldash.theme.DBIcons;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


class WListWListSelector
        extends AbstractWListSelector {
    private static final long serialVersionUID = 1L;
    private boolean ignoreChanges = false;
    private JPanel p;
    private JList l;

    public WListWListSelector(ListSelectionWidget lsw, DesktopModel desktopModel) {
        super(lsw, desktopModel);
        refresh();
    }


    public JPanel getInputComponent(final DesktopModel desktopModel) {
        this.p = new JPanel(new BorderLayout());
        this.l = new JList();
        this.l.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!WListWListSelector.this.ignoreChanges) {
                    List<String> r = new ArrayList<String>();
                    for (Object o : WListWListSelector.this.l.getSelectedValues()) {
                        r.add("" + o);
                    }
                    desktopModel.setArg(WListWListSelector.this.cbw.getArgKey(), r);
                }
            }
        });
        this.p.add(new JScrollPane(this.l));
        return this.p;
    }


    public synchronized void setSelection(List<String> selections) {
        List<Integer> indices = new ArrayList<Integer>();
        ListModel m = this.l.getModel();
        for (int i = 0; i < m.getSize(); i++) {
            if (selections.contains(m.getElementAt(i))) {
                indices.add(Integer.valueOf(i));
            }
        }

        int[] alreadySelected = this.l.getSelectedIndices();
        int[] nowSelected = Ints.toArray(indices);
        this.ignoreChanges = true;
        if (!Arrays.equals(alreadySelected, nowSelected)) {
            this.l.setSelectedIndices(nowSelected);
        }
        this.ignoreChanges = false;
    }


    public synchronized void setOptionsShown(List<String> options) {
        ListModel m = this.l.getModel();

        boolean equals = false;
        if (options.size() == m.getSize()) {
            for (int i = 0; i < options.size(); i++) {
                if (!options.get(i).equals(m.getElementAt(i))) {
                    equals = false;

                    break;
                }
            }
        }
        if (!equals) {
            this.l.setListData(options.toArray());
            this.p.revalidate();
            this.p.repaint();
        }
    }

    public ImageIcon getIcon() {
        return DBIcons.LIST.get16();
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\forms\WListWListSelector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */