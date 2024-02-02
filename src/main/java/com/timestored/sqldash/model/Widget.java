package com.timestored.sqldash.model;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JPanel;
import java.util.Collection;

public interface Widget extends QueryEngine.QueryEngineListener, DesktopModelArgListener {
    String getTitle();

    void setTitle(String paramString);

    int getId();

    void addListener(Listener paramListener);

    boolean removeListener(Listener paramListener);

    JPanel getPanel();

    JPanel getEditorPanel();

    void invalidatePanelCache();

    Collection<Queryable> getQueryables();

    Collection<Action> getActions();

    Icon getIcon();

    DesktopModel getDesktopModel();

    WidgetDTO getDTO();

    interface Listener {
        void configChanged(Widget param1Widget);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\model\Widget.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */