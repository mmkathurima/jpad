package com.timestored.sqldash.forms;

import com.timestored.sqldash.model.DesktopModel;
import com.timestored.sqldash.theme.DBIcons;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

class WComboBoxWListSelector
        extends AbstractWListSelector {
    private static final long serialVersionUID = 1L;
    private JComboBox cb;

    public WComboBoxWListSelector(ListSelectionWidget lsw, DesktopModel desktopModel) {
        super(lsw, desktopModel);
        this.refresh();
    }

    public JComboBox getInputComponent(DesktopModel desktopModel) {
        this.cb = new JComboBox();
        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String s = (String) WComboBoxWListSelector.this.cb.getSelectedItem();
                String argKey = WComboBoxWListSelector.this.cbw.getArgKey();
                if (s != null && !s.equals(desktopModel.getArg(argKey))) {
                    desktopModel.setArg(argKey, WComboBoxWListSelector.this.cb.getSelectedItem());
                }
            }
        };
        this.cb.addActionListener(al);
        return this.cb;
    }

    public synchronized void setSelection(List<String> value) {
        Object sel = this.cb.getSelectedItem();
        if (sel != null && value.size() > 0 && !sel.equals(value.get(0))) {
            this.cb.setSelectedItem(value.get(0));
        }
    }

    public synchronized void setOptionsShown(List<String> v) {
        if (!this.cb.isFocusOwner()) {
            this.cb.setModel(new DefaultComboBoxModel(v.toArray()));
            Object o = this.desktopModel.getArg(this.cbw.getArgKey());
            if (o instanceof String) {
                this.cb.setSelectedItem(o);
            }
        }
    }

    public ImageIcon getIcon() {
        return DBIcons.COMBOBOX.get16();
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\forms\WComboBoxWListSelector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */