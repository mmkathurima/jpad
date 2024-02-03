package com.timestored.sqldash.model;

import javax.swing.Action;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

public abstract class AbstractWidget
        implements Widget {
    private static final Logger LOG = Logger.getLogger(AbstractWidget.class.getName());
    private static int count;
    protected final transient DesktopModel desktopModel;
    private final transient List<Widget.Listener> listeners = new CopyOnWriteArrayList<Widget.Listener>();
    private final int id;
    protected String title = "untitled";

    public AbstractWidget(DesktopModel desktopModel, int id) {
        synchronized (this) {
            this.desktopModel = desktopModel;
            this.id = id;
            count = id + 1;
        }
    }

    public AbstractWidget(DesktopModel desktopModel, String title) {
        synchronized (this) {
            this.id = count++;
        }
        this.title = title;
        this.desktopModel = desktopModel;
    }

    public AbstractWidget(DesktopModel desktopModel) {
        this(desktopModel, "");
    }

    public AbstractWidget(DesktopModel desktopModel, AbstractWidget app) {
        this(desktopModel, app.getTitle());
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
        this.configChanged();
    }

    protected void configChanged() {
        LOG.info("Widget " + this.id + " configChanged");
        for (Widget.Listener l : this.listeners) {
            l.configChanged(this);
        }
    }

    public void addListener(Widget.Listener widgetListener) {
        this.listeners.add(widgetListener);
    }

    public boolean removeListener(Widget.Listener widgetListener) {
        return this.listeners.remove(widgetListener);
    }

    public int getId() {
        return this.id;
    }

    public AbstractWidget setServerName(String serverName) {
        for (Queryable q : this.getQueryables()) {
            q.setServerName(serverName);
        }
        return this;
    }

    public String toString() {
        return "Widget [title=" + this.title + ", id=" + this.id + "]";
    }

    public DesktopModel getDesktopModel() {
        return this.desktopModel;
    }

    public Collection<Action> getActions() {
        return Collections.emptyList();
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\model\AbstractWidget.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */