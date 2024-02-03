package com.timestored.sqldash.model;

import com.google.common.base.Preconditions;
import com.timestored.connections.ConnectionManager;
import com.timestored.sqldash.ChartControlPanel;
import com.timestored.theme.Theme;

import javax.swing.Box;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;

class ChartEditorPanel
        extends JPanel {
    private static final long serialVersionUID = 1L;
    private final ConnectionManager connectionManager;

    public ChartEditorPanel(ConnectionManager connectionManager) {
        this.connectionManager = Preconditions.checkNotNull(connectionManager);
        this.setLayout(new BorderLayout(4, 4));
    }

    public void display(ChartWidget app) {
        this.removeAll();
        QueryableEditorPanel appQueryEditorPanel = new QueryableEditorPanel(this.connectionManager);
        appQueryEditorPanel.setDesktopModel(app.getDesktopModel());
        appQueryEditorPanel.display(app.getQ());
        Box b = Box.createVerticalBox();
        b.add(new ChartControlPanel(app));

        Color fgColor = new Color(242, 242, 242);
        Color bgColor = new Color(133, 133, 170);
        this.add(Theme.getSubHeader("Control Panel", fgColor, bgColor), "North");
        this.add(b, "West");
        this.add(appQueryEditorPanel, "Center");
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\model\ChartEditorPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */