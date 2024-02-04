package com.timestored.sqldash;

import bibliothek.gui.DockFrontend;
import bibliothek.gui.DockStation;
import bibliothek.gui.Dockable;
import bibliothek.gui.dock.DefaultDockable;
import bibliothek.gui.dock.SplitDockStation;
import bibliothek.gui.dock.action.DefaultDockActionSource;
import bibliothek.gui.dock.action.actions.SimpleButtonAction;
import bibliothek.gui.dock.dockable.DockableStateEvent;
import bibliothek.gui.dock.dockable.DockableStateListener;
import bibliothek.gui.dock.event.DockFrontendAdapter;
import bibliothek.gui.dock.event.DockStationListener;
import bibliothek.util.xml.XElement;
import bibliothek.util.xml.XIO;
import com.google.common.base.Preconditions;
import com.timestored.sqldash.model.*;
import com.timestored.theme.Theme;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DockingAppPanel
        extends JPanel {
    public static final String FACTORY_ID = "REARDEN";
    private static final int EDITOR_PANEL_HEIGHT = 250;
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(DockingAppPanel.class.getName());
    private static final Dimension EDITOR_DIMENSION = new Dimension(200, 250);
    private final AppActions appActions;
    private final AppModel appModel;
    private final DockFrontend frontend;
    private final SplitDockStation station;
    private final JPanel appEditorPanel;
    private final Map<Integer, DefaultDockable> apptoDockable = new ConcurrentHashMap<>();
    private final Map<Dockable, Widget> dockableToWidget = new ConcurrentHashMap<>();
    private final JPanel stationHolder;
    private boolean refreshing;

    public DockingAppPanel(String uniqueId, JFrame parentFrame, AppModel appModel, AppActions appActions) {
        Preconditions.checkNotNull(parentFrame);
        this.appModel = Preconditions.checkNotNull(appModel);
        this.appActions = Preconditions.checkNotNull(appActions);

        this.frontend = new DockFrontend(parentFrame);
        this.frontend.setShowHideAction(true);
        this.frontend.addFrontendListener(new DockFrontendAdapter() {
            public void hidden(DockFrontend f, Dockable dockable) {
                Widget app = DockingAppPanel.this.dockableToWidget.get(dockable);
                if (app != null) {
                    appModel.getSelectedDesktopModel().remove(app);
                }
            }
        });

        IdLookupFactory dockFactory = new IdLookupFactory("REARDEN") {
            public int getId(Dockable d) {
                String name = DockingAppPanel.this.frontend.getNameOf(d);
                return Integer.parseInt(name);
            }

            public Dockable getDockable(int id) {
                Widget w = appModel.getApp(id);
                return DockingAppPanel.this.createDockable(appModel.getSelectedDesktopModel().getSelectedWorkspace(), w);
            }
        };

        this.frontend.registerFactory(dockFactory, true);

        this.station = new SplitDockStation();
        this.stationHolder = new JPanel(new GridLayout());
        this.frontend.addRoot(uniqueId, this.station);
        this.stationHolder.add(this.station);

        this.appEditorPanel = new JPanel(new BorderLayout());
        this.appEditorPanel.setBorder(BorderFactory.createRaisedBevelBorder());

        this.setLayout(new BorderLayout());
        this.add(this.appEditorPanel, "South");
        this.add(this.stationHolder, "Center");

        appModel.addListener(new AppModel.Listener() {
            public void desktopChanged(DesktopModel selectedDesktopModel) {
                DockingAppPanel.this.refreshDesktop();
            }
        });

        this.station.addDockStationListener(new DockStationListener() {
            public void dockablesRepositioned(DockStation arg0, Dockable[] arg1) {
                this.saveChangedLayout();
            }

            private void saveChangedLayout() {
                if (!DockingAppPanel.this.refreshing) {
                    synchronized (this) {
                        if (!DockingAppPanel.this.refreshing) {
                            LOG.info("saveChangedLayout");
                            WorkspaceModel wsm = appModel.getSelectedWorkspaceModel();
                            if (wsm != null) {
                                XElement xroot = new XElement("jlayout");
                                DockingAppPanel.this.frontend.writeXML(xroot);
                                wsm.setJavaLayoutXml(xroot.toString());
                            }
                        }
                    }
                }
            }

            public void dockableShowingChanged(DockStation arg0, Dockable arg1, boolean arg2) {
                this.saveChangedLayout();
            }

            public void dockableSelected(DockStation arg0, Dockable arg1, Dockable arg2) {
            }

            public void dockableRemoving(DockStation arg0, Dockable arg1) {
            }

            public void dockableAdding(DockStation arg0, Dockable arg1) {
            }

            public void dockableRemoved(DockStation arg0, Dockable arg1) {
                this.saveChangedLayout();
            }

            public void dockableAdded(DockStation arg0, Dockable arg1) {
                this.saveChangedLayout();
            }
        });

        appModel.addOpenedDesktopListener(new RefreshOnDesktopChangesListener());

        this.refreshDesktop();
    }

    private static SimpleButtonAction getActionButton(Action a) {
        SimpleButtonAction button = new SimpleButtonAction();
        button.setText("" + a.getValue("Name"));
        Icon ic = (Icon) a.getValue("SmallIcon");
        button.setIcon(ic);
        button.addActionListener(a);
        return button;
    }

    private void refreshDesktop() {
        LOG.info("refreshDesktop()");
        synchronized (this) {
            this.refreshing = true;
            DesktopModel dm = this.appModel.getSelectedDesktopModel();

            this.station.removeAllDockables();
            this.apptoDockable.clear();
            for (Dockable d : this.frontend.getDockables()) {
                this.frontend.remove(d);
            }

            if (dm != null) {
                WorkspaceModel workspaceShown = dm.getSelectedWorkspace();
                if (workspaceShown != null) {
                    for (Widget app : workspaceShown.getApps()) {
                        this.addDockableApp(workspaceShown, app);
                    }
                }
            }

            this.stationHolder.removeAll();
            if (this.apptoDockable.size() == 0) {
                this.stationHolder.add(this.getHelpComponent());
            } else {
                this.stationHolder.add(this.station);
            }

            this.refreshlayout();

            this.refreshAppEditor();
            this.refreshing = false;
        }
    }

    private void refreshlayout() {
        WorkspaceModel ws = this.appModel.getSelectedWorkspaceModel();
        if (ws != null) {
            String layoutXML = ws.getJavaLayoutXml();

            if (layoutXML != null) {

                try {

                    Set<String> layouts = this.frontend.getSettings();
                    String[] keys = layouts.toArray(new String[0]);
                    for (String key : keys) {
                        this.frontend.delete(key);
                    }
                    this.frontend.readXML(XIO.read(layoutXML));
                } catch (IOException e) {
                    LOG.log(Level.SEVERE, "Error restoring layout", e);
                }
            }
        }
    }

    private Component getHelpComponent() {
        JPanel p = Theme.getVerticalBoxPanel();
        final String helpHtml = "<html><p>To create a dashboard you must:<ol><li>Create atleast one server connection.</li><li>Add a widget and specify the server / query.</li><li>Set the chart type to one that supports the given query.</li></ol><br />For help see: <a href='http://www.timestored.com/sqlDashboards/help'>sqlDashboards Help</a><br /><br /><br />Or to start an interactive demo using a built-in datebase click the button below:</html>";

        p.add(Theme.getHeader("Getting Started"));
        p.add(Theme.getHtmlText(helpHtml));
        JButton demoButton = new JButton("Launch Demo sqlDashboard");
        demoButton.addActionListener(this.appActions.getMainDashDemoAction());
        demoButton.setIcon(Theme.CIcon.DAS_FILE.get16());
        JPanel bp = new JPanel();
        bp.add(demoButton);
        p.add(bp);
        return new JScrollPane(p);
    }

    private void refreshAppEditor() {
        DesktopModel dm = this.appModel.getSelectedDesktopModel();
        if (dm != null) {
            Widget widget = dm.getSelectedApp();
            LOG.fine("RefreshAppEditor -> " + widget);

            JPanel editor = null;
            if (widget != null) {
                editor = widget.getEditorPanel();
                this.appEditorPanel.setPreferredSize(EDITOR_DIMENSION);
            } else {
                this.appEditorPanel.setPreferredSize(new Dimension(0, 0));
            }

            this.appEditorPanel.removeAll();
            if (editor != null) {
                this.appEditorPanel.add(editor, "Center");
            }
            this.appEditorPanel.revalidate();
            this.appEditorPanel.repaint();
        }
    }

    private void addDockableApp(WorkspaceModel workspaceModel, Widget app) {
        if (this.apptoDockable.size() == 0) {
            this.stationHolder.removeAll();
            this.stationHolder.add(this.station);
        }

        DefaultDockable dockable = this.createDockable(workspaceModel, app);

        this.frontend.addDockable("" + app.getId(), dockable);
        this.frontend.setHideable(dockable, true);
        this.station.drop(dockable);
    }

    public void removeDockable(Widget app) {
        app.invalidatePanelCache();
        Dockable d = this.apptoDockable.remove(app.getId());
        this.dockableToWidget.remove(d);
        if (d != null) {
            this.frontend.hide(d);
            this.frontend.remove(d);
        }

        if (this.apptoDockable.size() == 0) {
            this.stationHolder.removeAll();
            this.stationHolder.add(this.getHelpComponent());
        }
    }

    public DefaultDockable createDockable(WorkspaceModel workspaceModel, Widget app) {
        DefaultDockable dockable = new DefaultDockable();
        dockable.setTitleText(app.getTitle());
        dockable.setFactoryID("REARDEN");
        dockable.setTitleIcon(app.getIcon());

        DefaultDockActionSource dockActions = new DefaultDockActionSource();
        Collection<Action> actions = app.getActions();
        if (!actions.isEmpty()) {
            for (Action a : app.getActions()) {
                dockActions.add(getActionButton(a));
            }
        }
        Action refreshAction = new AbstractAction("Refresh", Theme.CIcon.ARROW_REFRESH.get16()) {
            public void actionPerformed(ActionEvent e) {
                DockingAppPanel.this.appModel.requestQueryRefresh(app);
            }
        };
        dockActions.add(getActionButton(refreshAction));

        dockable.setActionOffers(dockActions);

        JPanel kdbChartPanel = app.getPanel();

        dockable.add(kdbChartPanel);

        dockable.addDockableStateListener(new DockableStateListener() {
            public void changed(DockableStateEvent dockStateEvent) {
                DesktopModel dm = DockingAppPanel.this.appModel.getSelectedDesktopModel();
                if (dm != null && DockingAppPanel.this.frontend.getController().isFocused(dockable))
                    if (((!DockingAppPanel.this.frontend.isHidden(dockable) ? 1 : 0) & (!app.equals(dm.getSelectedApp()) ? 1 : 0)) != 0) {
                        dm.setSelectedApp(workspaceModel, app);
                    }
            }
        });
        this.apptoDockable.put(app.getId(), dockable);
        this.dockableToWidget.put(dockable, app);
        return dockable;
    }

    public boolean isEditorVisible() {
        return this.appEditorPanel.isVisible();
    }

    public void setEditorVisible(boolean visible) {
        this.appEditorPanel.setVisible(visible);
    }

    private class RefreshOnDesktopChangesListener
            implements DesktopModelListener {
        private RefreshOnDesktopChangesListener() {
        }

        public void workspaceSelected(WorkspaceModel selectedWorkspace) {
            DockingAppPanel.this.refreshDesktop();
        }

        public void argChange(Map<String, Object> changes) {
        }

        public void appRemoved(WorkspaceModel workspaceModel, Widget app) {
            DockingAppPanel.this.removeDockable(app);
        }

        public void appAdded(WorkspaceModel workspaceModel, Widget app) {
            DockingAppPanel.this.addDockableApp(workspaceModel, app);
        }

        public void appSelected(WorkspaceModel workspaceModel, Widget selectedApp) {
            DockingAppPanel.this.refreshAppEditor();
            if (selectedApp != null) {
                DefaultDockable d = DockingAppPanel.this.apptoDockable.get(selectedApp.getId());
                if (d != null && !DockingAppPanel.this.frontend.getController().isFocused(d)) {
                    DockingAppPanel.this.frontend.getController().setFocusedDockable(d, true);
                }
            }
        }

        public void workspaceTitleChanged(WorkspaceModel workspaceModel) {
        }

        public void appEdited(WorkspaceModel workspaceModel, Widget widget) {
            DefaultDockable dockable = DockingAppPanel.this.apptoDockable.get(widget.getId());
            if (dockable != null) {
                String appTitle = widget.getTitle();
                if (!dockable.getTitleText().equals(appTitle)) {
                    dockable.setTitleText(appTitle);
                }
                dockable.setTitleIcon(widget.getIcon());
            }
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\DockingAppPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */