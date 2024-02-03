package com.timestored.sqldash.forms;

import com.timestored.sqldash.model.DesktopModel;
import com.timestored.sqldash.theme.DBIcons;

import javax.swing.*;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.List;

class WRadioButtonsWListSelector
        extends AbstractWListSelector {
    private static final long serialVersionUID = 1L;
    private ButtonGroup bgroup;
    private JPanel radioPanel;

    public WRadioButtonsWListSelector(ListSelectionWidget lsw, DesktopModel desktopModel) {
        super(lsw, desktopModel);
        this.refresh();
    }

    public Component getInputComponent(DesktopModel desktopModel) {
        this.radioPanel = new JPanel();
        this.radioPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        this.bgroup = new ButtonGroup();

        return this.radioPanel;
    }

    public void setSelection(List<String> value) {
        synchronized (this) {
            Enumeration<AbstractButton> options = this.bgroup.getElements();
            while (options.hasMoreElements()) {
                AbstractButton b = options.nextElement();
                if (value.contains(b.getText())) {
                    b.setSelected(true);
                }
            }
        }
    }

    public void setOptionsShown(List<String> v) {
        synchronized (this) {
            if (!this.radioPanel.isFocusOwner()) {
                this.radioPanel.removeAll();
                this.radioPanel.setLayout(new GridLayout(v.size(), 1));
                for (String s : v) {
                    JRadioButton rb = new JRadioButton(s, false);
                    rb.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            WRadioButtonsWListSelector.this.desktopModel.setArg(WRadioButtonsWListSelector.this.cbw.getArgKey(), s);
                        }
                    });
                    this.bgroup.add(rb);
                    this.radioPanel.add(rb);
                }
            }
        }
    }

    public ImageIcon getIcon() {
        return DBIcons.RADIOBUTTON.get16();
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\forms\WRadioButtonsWListSelector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */