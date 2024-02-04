package com.timestored.sqldash.forms;

import com.timestored.sqldash.model.DesktopModel;
import com.timestored.sqldash.model.Widget;
import com.timestored.sqldash.theme.DBIcons;
import com.timestored.theme.Theme;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Objects;

class FormWidgetEditorPanel
        extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final int LIST_EDITOR_WIDTH = 250;
    private final FormWidget formWidget;
    private final JPanel subEditorPanel;
    private final Action deleteAction;
    private final Action moveWidgetUpAction;
    private final Action moveWidgetDownAction;
    private final JList l;
    private final DesktopModel desktopModel;
    private boolean ignoreSelectEvents;

    public FormWidgetEditorPanel(FormWidget formWidget, DesktopModel desktopModel) {
        this.formWidget = formWidget;
        this.desktopModel = desktopModel;

        this.setLayout(new BorderLayout(20, 4));

        this.deleteAction = new AbstractAction("Delete Form Component", Theme.CIcon.DELETE.get()) {
            public void actionPerformed(ActionEvent e) {
                formWidget.removeWidget((Widget) FormWidgetEditorPanel.this.l.getSelectedValue());
            }
        };

        this.moveWidgetUpAction = new AbstractAction("Move Component Up the Form", DBIcons.GO_UP.get16()) {
            public void actionPerformed(ActionEvent e) {
                formWidget.moveWidgetUp((Widget) FormWidgetEditorPanel.this.l.getSelectedValue());
            }
        };

        this.moveWidgetDownAction = new AbstractAction("Move Component Down the Form", DBIcons.GO_DOWN.get16()) {
            public void actionPerformed(ActionEvent e) {
                formWidget.moveWidgetDown((Widget) FormWidgetEditorPanel.this.l.getSelectedValue());
            }
        };

        JPanel listEditorPanel = new JPanel(new BorderLayout());
        listEditorPanel.setBorder(BorderFactory.createTitledBorder("Form Components"));
        Dimension d = new Dimension(250, 0);
        listEditorPanel.setPreferredSize(d);
        listEditorPanel.setMinimumSize(d);

        JPanel ep = new JPanel(new BorderLayout());
        Theme.InputLabeller il = Theme.getInputLabeller();

        JTextField titleTextField = new JTextField(20);
        titleTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                formWidget.setTitle(titleTextField.getText());
            }
        });

        ep.add(il.get("Title", titleTextField, "titleField"), "North");
        ep.add(il.get("Layout", getLayoutComboBox(formWidget), "layoutCB"), "Center");
        ep.add(il.get("Add Component", this.createAddToolBar(), "addComToolbar"), "South");

        JToolBar rightToolbar = new JToolBar(1);
        rightToolbar.add(this.moveWidgetUpAction);
        rightToolbar.add(this.moveWidgetDownAction);
        rightToolbar.add(this.deleteAction);

        this.l = new JList();
        this.l.setCellRenderer(FormWidget.WidgetListCellRenderer.INSTANCE);
        listEditorPanel.add(ep, "North");
        listEditorPanel.add(new JScrollPane(this.l), "Center");
        listEditorPanel.add(rightToolbar, "East");

        Color fgColor = new Color(242, 242, 242);
        Color bgColor = new Color(133, 133, 170);
        this.add(Theme.getSubHeader("Form Editor", fgColor, bgColor), "North");
        this.add(listEditorPanel, "West");
        this.subEditorPanel = new JPanel(new GridLayout(1, 1));
        this.subEditorPanel.setBorder(BorderFactory.createTitledBorder("Component Editor"));
        this.add(this.subEditorPanel, "Center");

        this.l.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!FormWidgetEditorPanel.this.ignoreSelectEvents) {
                    formWidget.setSelectedWidget((Widget) FormWidgetEditorPanel.this.l.getSelectedValue());
                }
            }
        });

        formWidget.addListener(new Widget.Listener() {
            public void configChanged(Widget widget) {
                FormWidgetEditorPanel.this.refresh(true);
            }
        });
        this.refresh(true);
    }

    private static JComboBox getLayoutComboBox(FormWidget formWidget) {
        JComboBox<String> layoutCB = new JComboBox<>(new String[]{"Horizontal", "Vertical"});
        layoutCB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Objects.equals(layoutCB.getSelectedItem(), "Horizontal")) {
                    formWidget.setLayout(3);
                } else {
                    formWidget.setLayout(2);
                }
            }
        });
        return layoutCB;
    }

    private JToolBar createAddToolBar() {
        JToolBar tb = new JToolBar(0);
        tb.add(this.mkListAction("Add ComboBox", ListSelectionWidget.SELECTOR_TYPE.COMBOBOX));
        tb.add(this.mkListAction("Add Radio Buttons", ListSelectionWidget.SELECTOR_TYPE.RADIOBUTTON));
        tb.add(this.mkListAction("Add Checkboxes", ListSelectionWidget.SELECTOR_TYPE.CHECKBOX));
        tb.add(this.mkListAction("Add List", ListSelectionWidget.SELECTOR_TYPE.LIST));
        tb.setFloatable(false);
        return tb;
    }

    private Action mkListAction(String txt, ListSelectionWidget.SELECTOR_TYPE selType) {
        return new AbstractAction(txt, selType.getImageIcon()) {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                Widget w = new ListSelectionWidget(FormWidgetEditorPanel.this.desktopModel, selType);
                FormWidgetEditorPanel.this.formWidget.addWidget(w);
            }
        };
    }

    private void refresh(boolean resetModel) {
        List<Widget> wdgts = this.formWidget.getWidgets();
        Widget w = this.formWidget.getSelectedWidget();

        this.ignoreSelectEvents = true;
        if (resetModel) {
            this.l.setListData(wdgts.toArray());
        }

        this.subEditorPanel.removeAll();
        if (w != null) {
            this.subEditorPanel.add(w.getEditorPanel());
            this.l.setSelectedIndex(wdgts.indexOf(w));
        }
        this.subEditorPanel.revalidate();
        this.subEditorPanel.repaint();

        this.ignoreSelectEvents = false;
        int si = this.l.getSelectedIndex();
        this.moveWidgetUpAction.setEnabled((si > 0));
        this.moveWidgetDownAction.setEnabled((si > -1 && si < wdgts.size() - 1));
        this.deleteAction.setEnabled((si != -1));
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\forms\FormWidgetEditorPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */