package com.timestored.sqldash.model;

import com.google.common.base.Preconditions;
import com.timestored.connections.ConnectionManager;
import com.timestored.connections.ServerConfig;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;


public class AppModel {
    private static final Logger LOG = Logger.getLogger(AppModel.class.getName());
    private static int desktopCounter = 1;
    public final List<Listener> listeners = new CopyOnWriteArrayList<Listener>();
    private final QueryEngine queryEngine;
    private final Set<DesktopModelListener> openDeskListeners = new HashSet<DesktopModelListener>();
    private DesktopModel selectedDesktopModel;
    private ConnectionManager connMan;


    public AppModel(ConnectionManager connectionManager) {
        this.connMan = connectionManager;
        this.selectedDesktopModel = new DesktopModel(this.connMan);
        this.queryEngine = QueryEngine.newQueryEngine(this.connMan);
        this.queryEngine.setQueryTranslator(new QueryTranslator(this.selectedDesktopModel.getArgMap()));


        addOpenedDesktopListener(new DesktopModelAdapter() {
            public void argChange(Map<String, Object> changes) {
                WorkspaceModel wsm = AppModel.this.getSelectedWorkspaceModel();
                Collection<Queryable> qs = wsm.getQueryablesWithArgs(changes.keySet());
                AppModel.this.queryEngine.addToPriorityQueue(qs);
            }

            public void workspaceSelected(WorkspaceModel wsm) {
                AppModel.this.setQueryablesToSelectedWorkspace();
                AppModel.this.queryEngine.addToPriorityQueue(wsm.getQueryables());
            }

            public void appRemoved(WorkspaceModel wsm, Widget w) {
                AppModel.this.setQueryablesToSelectedWorkspace();
            }

            public void appEdited(WorkspaceModel wsm, Widget w) {
                AppModel.this.queryEngine.addToPriorityQueue(w.getQueryables());
                AppModel.this.setQueryablesToSelectedWorkspace();
            }

            public void appAdded(WorkspaceModel wsm, Widget w) {
                AppModel.this.queryEngine.addToPriorityQueue(w.getQueryables());
                AppModel.this.setQueryablesToSelectedWorkspace();
            }
        });


        this.queryEngine.addListener(new QueryEngine.QueryEngineListener() {
            public void tabChanged(Queryable queryable, ResultSet qTab) {
                WorkspaceModel wsm = AppModel.this.getSelectedWorkspaceModel();
                for (Widget w : wsm.getApps()) {
                    w.tabChanged(queryable, qTab);
                }
            }

            public void queryError(Queryable queryable, Exception e) {
                WorkspaceModel wsm = AppModel.this.getSelectedWorkspaceModel();
                for (Widget w : wsm.getApps()) {
                    w.queryError(queryable, e);
                }
            }
        });
    }

    private void setQueryablesToSelectedWorkspace() {
        Collection<Queryable> qs = this.selectedDesktopModel.getSelectedWorkspace().getQueryables();
        this.queryEngine.setQueryables(qs);
        this.queryEngine.addToPriorityQueue(qs);
    }

    public void requestQueryRefresh(Widget widget) {
        this.queryEngine.addToPriorityQueue(widget.getQueryables());
    }


    private void changeDesktop(DesktopModel newDesktop) {
        this.queryEngine.setQueryTranslator(new QueryTranslator(newDesktop.getArgMap()));

        if (this.selectedDesktopModel != null) {
            for (DesktopModelListener dl : this.openDeskListeners) {
                this.selectedDesktopModel.removeListener(dl);
            }
        }


        if (newDesktop != null) {
            for (DesktopModelListener dl : this.openDeskListeners) {
                newDesktop.addListener(dl);
            }
        }

        this.selectedDesktopModel = newDesktop;
        for (Listener l : this.listeners) {
            l.desktopChanged(newDesktop);
        }

        setQueryablesToSelectedWorkspace();
    }


    public DesktopModel newDesktop() {
        DesktopModel dm = new DesktopModel(this.connMan);
        dm.setTitle("Unknown Desktop " + desktopCounter++);
        changeDesktop(dm);
        return dm;
    }


    public DesktopModel getSelectedDesktopModel() {
        return this.selectedDesktopModel;
    }


    public WorkspaceModel getSelectedWorkspaceModel() {
        if (this.selectedDesktopModel != null) {
            return this.selectedDesktopModel.getSelectedWorkspace();
        }
        return null;
    }

    public ConnectionManager getConnectionManager() {
        return this.connMan;
    }

    public void setConnectionManager(ConnectionManager connMan) {
        this.connMan = Preconditions.checkNotNull(connMan);
        this.queryEngine.setConnectionManager(connMan);
        LOG.info("setConnectionManager: " + connMan);
    }

    public void addListener(Listener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        this.listeners.remove(listener);
    }


    public String getTitle() {
        if (this.selectedDesktopModel != null) {
            return this.selectedDesktopModel.getTitle();
        }
        return "";
    }


    public void addOpenedDesktopListener(DesktopModelListener desktopModelListener) {
        if (this.selectedDesktopModel != null) {
            this.selectedDesktopModel.addListener(desktopModelListener);
        }
        this.openDeskListeners.add(desktopModelListener);
    }


    public void removeOpenedDesktopListener(DesktopModelListener desktopModelListener) {
        if (this.selectedDesktopModel != null) {
            this.selectedDesktopModel.addListener(desktopModelListener);
        }
        this.openDeskListeners.remove(desktopModelListener);
    }


    public Widget getApp(int id) {
        for (Widget w : getSelectedDesktopModel().getApps()) {
            if (w.getId() == id) {
                return w;
            }
        }
        return null;
    }


    public boolean changeToDesktop(DasFile desFileDTO, boolean addConnections) {
        if (addConnections) {
            this.connMan.removeServers();
            this.connMan.addServer(desFileDTO.getConnections());
        }
        changeDesktop(new DesktopModel(desFileDTO.getDesktopDTO(), this.connMan));

        return true;
    }


    public void startQueryEngine() {
        this.queryEngine.startUp();
    }

    public void stopQueryEngine() {
        this.queryEngine.shutDown();
    }


    public void openFile(File dasFile) throws IOException {
        DasFile desFile = new DasFile(dasFile);
        changeToDesktop(desFile, true);
    }


    public void saveToFile(File file) throws IOException {
        DasFile desFile = new DasFile(new DesktopDTO(this.selectedDesktopModel), this.connMan.getServerConnections());
        desFile.save(file);
    }


    public void setAllQueryServersTo(ServerConfig sc) {
        this.connMan.addServer(sc);
        for (WorkspaceModel ws : this.selectedDesktopModel.getWorkspaces()) {
            for (Widget widget : ws.getApps()) {
                for (Queryable q : widget.getQueryables())
                    q.setServerName(sc.getName());
            }
        }
    }

    public interface Listener {
        void desktopChanged(DesktopModel param1DesktopModel);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\model\AppModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */