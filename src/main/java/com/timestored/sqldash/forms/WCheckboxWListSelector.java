package com.timestored.sqldash.forms;

import com.timestored.sqldash.model.DesktopModel;
import com.timestored.sqldash.theme.DBIcons;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

class WCheckboxWListSelector
        extends AbstractWListSelector {
    private static final long serialVersionUID = 1L;
    private final List<JCheckBox> checkboxes = new CopyOnWriteArrayList<JCheckBox>();
    private JPanel p;

    public WCheckboxWListSelector(ListSelectionWidget lsw, DesktopModel desktopModel) {
        super(lsw, desktopModel);
        this.refresh();
    }

    public JPanel getInputComponent(DesktopModel desktopModel) {
        this.p = new JPanel();
        this.p.setLayout(new BoxLayout(this.p, 3));
        return this.p;
    }

    public synchronized void setSelection(List<String> selections) {
        for (JCheckBox c : this.checkboxes) {
            c.setSelected(selections.contains(c.getText()));
        }
    }

    public synchronized void setOptionsShown(List<String> options) {
        boolean equals = false;
        if (options.size() == this.checkboxes.size()) {
            for (int i = 0; i < options.size(); i++) {
                if (!options.get(i).equals(this.checkboxes.get(i).getText())) {
                    equals = false;

                    break;
                }
            }
        }
        if (!equals) {

            this.p.removeAll();
            this.checkboxes.clear();
            for (String v : options) {
                JCheckBox cb = new JCheckBox(v);
                cb.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        List<String> r = new ArrayList<String>();
                        for (JCheckBox cb : WCheckboxWListSelector.this.checkboxes) {
                            if (cb.isSelected()) {
                                r.add(cb.getText());
                            }
                        }
                        WCheckboxWListSelector.this.desktopModel.setArg(WCheckboxWListSelector.this.cbw.getArgKey(), r);
                    }
                });
                this.checkboxes.add(cb);
                this.p.add(cb);
            }
            this.p.revalidate();
            this.p.repaint();
        }
    }

    public ImageIcon getIcon() {
        return DBIcons.COMBOBOX.get16();
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\forms\WCheckboxWListSelector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */