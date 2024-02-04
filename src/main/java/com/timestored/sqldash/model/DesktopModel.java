package com.timestored.sqldash.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.timestored.connections.ConnectionManager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

public class DesktopModel {
    private static final Logger LOG = Logger.getLogger(DesktopModel.class.getName());

    private static final String DEFAULT_TITLE = "Untitled";

    private static int workspaceCounter = 1;

    private final Map<String, Object> argMap = new ConcurrentHashMap<>();
    private final List<DesktopModelListener> listeners = new CopyOnWriteArrayList<>();
    private final List<WorkspaceModel> workspaces = new CopyOnWriteArrayList<>();
    private final ConnectionManager connMan;
    private WorkspaceModel selectedWorkspace;
    private String title = "Untitled";

    DesktopModel(DesktopDTO desktopDTO, ConnectionManager connMan) {
        Preconditions.checkNotNull(desktopDTO);
        this.connMan = Preconditions.checkNotNull(connMan);
        if (desktopDTO.title != null) {
            this.title = desktopDTO.title;
        }
        Preconditions.checkArgument((desktopDTO.workspaces != null && desktopDTO.workspaces.size() > 0));
        for (WorkspaceDTO w : desktopDTO.workspaces) {
            String wsTitle = (w.title == null) ? "Untitled Workspace" : w.title;
            WorkspaceModel ws = this.addWorkspace(wsTitle);
            ws.setJavaLayoutXml(w.jlayout);
            for (WidgetDTO a : w.widgets) {
                ws.addApp(a.newInstance(this));
            }
        }
        this.selectedWorkspace = this.workspaces.get(0);
    }

    public DesktopModel(ConnectionManager connMan) {
        this.selectedWorkspace = this.addWorkspace("Untitled");
        this.connMan = Preconditions.checkNotNull(connMan);
    }

    public WorkspaceModel getSelectedWorkspace() {
        return this.selectedWorkspace;
    }

    public void setSelectedWorkspace(WorkspaceModel selectedWorkspace) {
        LOG.info("setSelectedWorkspace: " + selectedWorkspace);
        if (selectedWorkspace == null || !this.workspaces.contains(selectedWorkspace)) {
            throw new IllegalArgumentException("invalid workspaceModel Selected");
        }
        if (selectedWorkspace != this.selectedWorkspace) {
            this.selectedWorkspace = selectedWorkspace;
            for (DesktopModelListener l : this.listeners) {
                l.workspaceSelected(selectedWorkspace);
            }
        }
    }

    public Widget getSelectedApp() {
        return this.selectedWorkspace.getSelectedApp();
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        LOG.info("setTitle: " + title);
        this.title = title;
    }

    public void setArg(String key, Object value) {
        LOG.info("setArg: " + key + "->" + ((value instanceof String) ? (String) value : "OBJ"));
        if (value == null) {
            this.argMap.remove(key);
        } else {
            this.argMap.put(key, value);
        }
        Map<String, Object> m = new HashMap<>(1);
        m.put(key, value);
        for (DesktopModelListener l : this.listeners) {
            l.argChange(m);
        }

        for (Widget w : this.selectedWorkspace.getApps()) {
            w.argChange(m);
        }
    }

    public Object getArg(String key) {
        return this.argMap.get(key);
    }

    public Set<String> getArgKeys() {
        return this.argMap.keySet();
    }

    public Collection<Queryable> getQueryableWidgets() {
        return this.selectedWorkspace.getQueryables();
    }

    public List<Widget> getApps() {
        List<Widget> r = Lists.newArrayList();
        for (WorkspaceModel wsm : this.workspaces) {
            r.addAll(wsm.getApps());
        }
        return Collections.unmodifiableList(r);
    }

    public void setSelectedApp(WorkspaceModel workspaceModel, Widget selectedApp) {
        Preconditions.checkNotNull(workspaceModel);
        Preconditions.checkArgument(this.workspaces.contains(workspaceModel));
        Preconditions.checkArgument((selectedApp == null || workspaceModel.getApps().contains(selectedApp)));
        LOG.info("setSelectedApp: " + selectedApp);

        if (!workspaceModel.equals(this.selectedWorkspace)) {
            this.setSelectedWorkspace(workspaceModel);
        }
        this.selectedWorkspace.setSelectedApp(selectedApp);

        for (DesktopModelListener l : this.listeners) {
            l.appSelected(workspaceModel, selectedApp);
        }
    }

    public WorkspaceModel addWorkspace(String title) {
        WorkspaceModel workspaceModel = new WorkspaceModel(title, this.listeners);
        this.workspaces.add(workspaceModel);
        this.setSelectedWorkspace(workspaceModel);
        return workspaceModel;
    }

    public void addWorkspace() {
        this.addWorkspace("Untitled " + workspaceCounter++);
    }

    public void remove(WorkspaceModel workspaceModel) {
        boolean removed = this.workspaces.remove(workspaceModel);
        if (removed) {
            if (this.workspaces.size() == 0) {
                this.workspaces.add(new WorkspaceModel("Untitled " + workspaceCounter++, this.listeners));
            }
            this.setSelectedWorkspace(this.workspaces.get(0));
        }
    }

    public void add(Widget widget) {
        this.selectedWorkspace.addApp(widget);
    }

    public void remove(Widget widget) {
        LOG.info("remove: " + widget);
        this.selectedWorkspace.removeApp(widget);
    }

    public void addListener(DesktopModelListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(DesktopModelListener listener) {
        this.listeners.remove(listener);
    }

    public List<WorkspaceModel> getWorkspaces() {
        return this.workspaces;
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("title", this.title).add("selectedWorkspace", this.selectedWorkspace).add("workspaces", this.workspaces).toString();
    }

    public int hashCode() {
        return Objects.hashCode(this.argMap, this.listeners, this.workspaces, this.selectedWorkspace, this.title);
    }

    public boolean equals(Object object) {
        if (object instanceof DesktopModel) {
            DesktopModel that = (DesktopModel) object;
            return (Objects.equal(this.argMap, that.argMap) && Objects.equal(this.listeners, that.listeners) && Objects.equal(this.workspaces, that.workspaces) && Objects.equal(this.selectedWorkspace, that.selectedWorkspace) && Objects.equal(this.title, that.title));
        }

        return false;
    }

    public String getArgS(String argKey) {
        Object o = this.getArg(argKey);
        return (o == null) ? "" : (String) o;
    }

    public Collection<Queryable> getQueryables() {
        Collection<Queryable> r = new ArrayList<>();
        for (WorkspaceModel wsm : this.workspaces) {
            r.addAll(wsm.getQueryables());
        }
        return r;
    }

    public Map<String, Object> getArgMap() {
        return this.argMap;
    }

    public ConnectionManager getConnectionManager() {
        return this.connMan;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\model\DesktopModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */