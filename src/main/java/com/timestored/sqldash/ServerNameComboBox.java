package com.timestored.sqldash;

import com.google.common.base.Preconditions;
import com.timestored.connections.ConnectionManager;
import com.timestored.connections.ServerConfig;
import com.timestored.sqldash.model.Queryable;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;


public class ServerNameComboBox
        extends JComboBox {
    private static final long serialVersionUID = 1L;
    private final ConnectionManager connMan;
    private final Queryable app;
    private boolean modelChanging = false;

    public ServerNameComboBox(ConnectionManager connMan, Queryable app) {
        this.connMan = Preconditions.checkNotNull(connMan);
        this.app = app;
        addActionListener(this);

        connMan.addListener(new ConnectionManager.Listener() {
            public void statusChange(ServerConfig serverConfig, boolean connected) {
                ServerNameComboBox.this.refresh();
            }

            public void prefChange() {
                ServerNameComboBox.this.refresh();
            }
        });

        refresh();
    }

    private void refresh() {
        this.modelChanging = true;
        if (this.app != null) {

            List<String> displayedNames = this.connMan.getServerNames();
            if (!displayedNames.contains(this.app.getServerName())) {
                displayedNames = new ArrayList<String>(displayedNames);
                displayedNames.add(this.app.getServerName());
            }
            String[] items = displayedNames.toArray(new String[0]);
            setModel(new DefaultComboBoxModel(items));
            setSelectedItem(this.app.getServerName());
        } else {
            setEnabled(false);
        }
        this.modelChanging = false;
    }

    public void actionPerformed(ActionEvent e) {
        if (!this.modelChanging && this.app != null)
            this.app.setServerName((String) getSelectedItem());
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\ServerNameComboBox.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */