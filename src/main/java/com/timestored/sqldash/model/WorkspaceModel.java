package com.timestored.sqldash.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

public class WorkspaceModel
        implements Widget.Listener {
    private static final Logger LOG = Logger.getLogger(WorkspaceModel.class.getName());

    private final List<DesktopModelListener> listeners;
    private final List<Widget> apps = new CopyOnWriteArrayList<>();
    private Widget selectedApp;
    private String title;
    private String javaLayoutXml;

    public WorkspaceModel(String title, List<DesktopModelListener> listeners) {
        this.title = title;
        List<DesktopModelListener> l = listeners;
        if (l == null) {
            l = Collections.emptyList();
        }
        this.listeners = l;
    }

    void addApp(Widget app) {
        LOG.info("addApp: " + app.toString());
        this.apps.add(app);
        app.addListener(this);
        for (DesktopModelListener l : this.listeners) {
            l.appAdded(this, app);
        }
        this.setSelectedApp(app);
    }

    void removeApp(Widget app) {
        Preconditions.checkNotNull(app);
        LOG.info("removeApp: " + app);
        app.removeListener(this);

        if (app.equals(this.selectedApp)) {
            this.setSelectedApp(null);
        }

        boolean removed = this.apps.remove(app);
        if (removed) {
            for (DesktopModelListener l : this.listeners) {
                l.appRemoved(this, app);
            }
        }

        if (this.apps.size() > 0) {
            this.setSelectedApp(this.apps.iterator().next());
        }
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        LOG.info("setTitle: " + title);
        Preconditions.checkNotNull(title);
        if (!title.equals(this.title)) {
            this.title = title;
            for (DesktopModelListener l : this.listeners) {
                l.workspaceTitleChanged(this);
            }
        }
    }

    public Widget getSelectedApp() {
        return this.selectedApp;
    }

    void setSelectedApp(Widget selectedApp) {
        LOG.info("setSelectedApp: " + selectedApp);
        this.selectedApp = selectedApp;
        for (DesktopModelListener l : this.listeners) {
            l.appSelected(this, selectedApp);
        }
    }

    public Collection<Widget> getApps() {
        return this.apps;
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("title", this.title).add("selectedApp", this.selectedApp).add("apps", this.apps).add("hasJavaLayoutXml", (this.javaLayoutXml != null && this.javaLayoutXml.length() > 0)).toString();
    }

    public void configChanged(Widget app) {
        for (DesktopModelListener l : this.listeners) {
            l.appEdited(this, app);
        }
    }

    public int hashCode() {
        return Objects.hashCode(this.apps, this.selectedApp, this.title, this.javaLayoutXml);
    }

    public boolean equals(Object object) {
        if (object instanceof WorkspaceModel) {
            WorkspaceModel that = (WorkspaceModel) object;
            return (Objects.equal(this.apps, that.apps) && Objects.equal(this.selectedApp, that.selectedApp) && Objects.equal(this.javaLayoutXml, that.javaLayoutXml) && Objects.equal(this.title, that.title));
        }

        return false;
    }

    public String getJavaLayoutXml() {
        return this.javaLayoutXml;
    }

    public void setJavaLayoutXml(String javaLayoutXml) {
        this.javaLayoutXml = javaLayoutXml;
    }

    public String getDescription() {
        StringBuilder sb = new StringBuilder(this.title);
        sb.append(" containing " + this.apps.size() + " apps: \r\n");
        for (Widget w : this.apps) {
            sb.append(", ").append(w.getTitle());
        }
        return sb.toString();
    }

    public Collection<Queryable> getQueryables() {
        List<Queryable> r = new ArrayList<>();
        for (Widget w : this.getApps()) {
            r.addAll(w.getQueryables());
        }
        return r;
    }

    public Collection<Queryable> getQueryablesWithArgs(Set<String> keySet) {
        return QueryTranslator.filterByKeys(this.getQueryables(), keySet);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\model\WorkspaceModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */