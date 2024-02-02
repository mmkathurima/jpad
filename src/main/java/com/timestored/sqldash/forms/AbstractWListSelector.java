package com.timestored.sqldash.forms;

import com.timestored.sqldash.model.DesktopModel;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.util.List;


abstract class AbstractWListSelector
        extends JPanel
        implements WListSelector {
    private static final long serialVersionUID = 1L;
    protected final ListSelectionWidget cbw;
    protected final DesktopModel desktopModel;
    private final JLabel lbl;
    private final JPanel inputPanel;
    private Component inputComponent;

    public AbstractWListSelector(ListSelectionWidget listSelectionWidget, DesktopModel desktopModel) {
        this.cbw = listSelectionWidget;
        this.desktopModel = desktopModel;


        this.lbl = new JLabel();
        JPanel p = new JPanel(new FlowLayout(0));
        this.inputPanel = new JPanel(new BorderLayout());
        p.add(this.lbl);
        p.add(this.inputPanel);

        add(p);
    }


    public void refresh() {
        if (EventQueue.isDispatchThread()) {
            doRefresh();
        } else {
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    AbstractWListSelector.this.doRefresh();
                }
            });
        }
    }


    private void doRefresh() {
        if (this.inputComponent == null || !this.inputComponent.isFocusOwner()) {
            this.inputPanel.removeAll();
            this.inputComponent = getInputComponent(this.desktopModel);
            this.inputComponent.setName("ListSelector-" + this.cbw.getId());
            this.inputPanel.add(this.inputComponent, "North");
            this.lbl.setLabelFor(this.inputComponent);

            this.lbl.setText(this.cbw.getTitle());
            this.lbl.setToolTipText(this.cbw.getTooltip());

            setOptionsShown(this.cbw.getOptions());
            setSelection(this.cbw.getSelections());
            this.inputPanel.revalidate();
        }
    }


    public JPanel getDisplay() {
        return this;
    }

    abstract void setSelection(List<String> paramList);

    abstract void setOptionsShown(List<String> paramList);

    public abstract Component getInputComponent(DesktopModel paramDesktopModel);
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\forms\AbstractWListSelector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */