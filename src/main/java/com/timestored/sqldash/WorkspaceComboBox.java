package com.timestored.sqldash;

import com.timestored.sqldash.model.AppModel;
import com.timestored.sqldash.model.DesktopModel;
import com.timestored.sqldash.model.DesktopModelAdapter;
import com.timestored.sqldash.model.WorkspaceModel;

import javax.swing.*;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


class WorkspaceComboBox
        extends JComboBox {
    private static final long serialVersionUID = 1L;
    private final AppModel appModel;

    public WorkspaceComboBox(final AppModel appModel) {
        this.appModel = appModel;
        setEditable(false);

        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                WorkspaceModel wsm = appModel.getSelectedWorkspaceModel();
                WorkspaceModel selection = (WorkspaceModel) WorkspaceComboBox.this.getSelectedItem();
                if (wsm != selection) {
                    appModel.getSelectedDesktopModel().setSelectedWorkspace(selection);
                }
            }
        });

        setRenderer(new ListCellRenderer<>() {
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel l = new JLabel();
                l.setOpaque(true);
                l.setText((value != null) ? value.toString() : "");
                if (value instanceof WorkspaceModel) {
                    WorkspaceModel ws = (WorkspaceModel) value;
                    l.setText(ws.getTitle());
                    l.setToolTipText(ws.getDescription());
                }
                if (isSelected) {
                    l.setBackground(list.getSelectionBackground());
                    l.setForeground(list.getSelectionForeground());
                } else {
                    l.setBackground(list.getBackground());
                    l.setForeground(list.getForeground());
                }
                return l;
            }
        });

        appModel.addListener(new AppModel.Listener() {
            public void desktopChanged(DesktopModel selectedDesktopModel) {
                WorkspaceComboBox.this.refresh();
            }
        });

        appModel.addOpenedDesktopListener(new DesktopModelAdapter() {
            public void workspaceSelected(WorkspaceModel wsm) {
                WorkspaceComboBox.this.refresh();
            }

            public void workspaceTitleChanged(WorkspaceModel wsm) {
                WorkspaceComboBox.this.refresh();
            }
        });


        refresh();
    }

    private void refresh() {
        DesktopModel dm = this.appModel.getSelectedDesktopModel();
        WorkspaceModel selectedWSM = this.appModel.getSelectedWorkspaceModel();
        boolean enabled = false;

        if (dm != null) {
            List<WorkspaceModel> workspaces = dm.getWorkspaces();
            enabled = (workspaces.size() > 0);
            setModel(new DefaultComboBoxModel<>(workspaces.toArray()));
            if (selectedWSM != null) {
                setSelectedItem(selectedWSM);
            }
        } else {
            setModel(new DefaultComboBoxModel<>());
        }
        setEnabled(enabled);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\WorkspaceComboBox.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */