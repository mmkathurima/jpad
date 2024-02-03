package com.timestored.connections;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import java.util.List;

public class ServerNameComboBox
        extends JComboBox {
    private static final long serialVersionUID = 1L;
    private final ConnectionManager connectionManager;

    public ServerNameComboBox(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;

        connectionManager.addListener(new ConnectionManager.Listener() {
            public void statusChange(ServerConfig serverConfig, boolean connected) {
                ServerNameComboBox.this.refresh();
            }

            public void prefChange() {
                ServerNameComboBox.this.refresh();
            }
        });

        this.refresh();
    }

    private void refresh() {
        String selectedServer = (String) this.getSelectedItem();
        List<String> serverNames = this.connectionManager.getServerNames();
        String[] names = serverNames.toArray(new String[0]);
        this.setModel(new DefaultComboBoxModel(names));
        if (serverNames.contains(selectedServer)) {
            this.setSelectedItem(selectedServer);
        }
    }

    public ServerConfig getSelectedServer() {
        return this.connectionManager.getServer((String) this.getSelectedItem());
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\connections\ServerNameComboBox.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */