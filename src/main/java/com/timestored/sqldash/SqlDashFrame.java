package com.timestored.sqldash;

import com.google.common.base.Preconditions;
import com.timestored.TimeStored;
import com.timestored.connections.ConnectionManager;
import com.timestored.connections.ConnectionManagerDialog;
import com.timestored.connections.JdbcTypes;
import com.timestored.connections.ServerConfig;
import com.timestored.misc.AbstractApplicationListener;
import com.timestored.misc.HtmlUtils;
import com.timestored.misc.Mac;
import com.timestored.sqldash.model.AppModel;
import com.timestored.sqldash.model.DesktopModel;
import com.timestored.sqldash.model.WorkspaceModel;
import com.timestored.theme.AboutDialog;
import com.timestored.theme.ShortcutAction;
import com.timestored.theme.Theme;
import org.simplericity.macify.eawt.ApplicationEvent;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;
import java.util.logging.Logger;


public class SqlDashFrame
        extends JFrame {
    public static final String APP_TITLE = "sqlDashboards";
    public static final String UNIQUE_ID = "DbVisFrame";
    public static final String VERSION = "1.41";
    public static final String FILE_EXTENSION = "das";
    static final String HELP_URL = "http://www.timestored.com/sqlDashboards/help";
    private static final Logger LOG = Logger.getLogger(SqlDashFrame.class.getName());
    private static final long serialVersionUID = 1L;
    private static final String APP_URL = "http://www.timestored.com/sqlDashboards";
    public final List<JdbcTypes> jdbcTypesSupported;
    private final DockingAppPanel dockingAppPanel;
    private final AppModel appModel;
    private final JToolBar toolbar;
    private final AppActions appActions;

    public SqlDashFrame(AppModel appModel, List<JdbcTypes> jdbcTypesSupported) {
        this.appModel = Preconditions.checkNotNull(appModel);
        this.jdbcTypesSupported = jdbcTypesSupported;
        this.appActions = new AppActions(appModel, this);
        this.dockingAppPanel = new DockingAppPanel("DbVisFrame", this, appModel, this.appActions);


        Mac.configureIfMac(new SqlDashFrameAppListener(), Theme.CIcon.SQLDASH_LOGO);

        this.appActions.addListener(new AppActions.Listener() {
            public void fileSaved(File f) {
                SqlDashFrame.this.updateTitle();
            }

            public void fileOpened(File f) {
                SqlDashFrame.this.updateTitle();
            }

            public void fileClosed(File f) {
                SqlDashFrame.this.updateTitle();
            }
        });
        appModel.addListener(new AppModel.Listener() {
            public void desktopChanged(DesktopModel selectedDesktopModel) {
                SqlDashFrame.this.updateTitle();
            }
        });

        setLayout(new BorderLayout());
        setIconImage(Theme.CIcon.SQLDASH_LOGO.get().getImage());
        setDefaultCloseOperation(3);


        this.toolbar = getToolbar(this.appActions, appModel);
        add(this.toolbar, "North");
        setJMenuBar(getMenuBar(this.appActions));

        add(this.dockingAppPanel, "Center");

        setSize(new Dimension(640, 480));
        setLocationRelativeTo(null);
        updateTitle();
    }

    private static JToolBar getToolbar(AppActions aa, AppModel appModel) {
        Dimension SEP_DIST = new Dimension(20, 1);
        JToolBar t = new JToolBar("Common Actions");
        t.add(new JLabel("Add Chart: "));
        for (Action addChart : aa.getAddChartActions()) {
            String name = "tool-" + addChart.getValue("ShortDescription");
            JButton but = getBut(addChart, name, false);
            t.add(but);
        }
        t.addSeparator();
        t.addSeparator(SEP_DIST);
        t.add(getBut(aa.getAddFormAction(), "tool-getAddFormAction", true));


        t.addSeparator();
        t.addSeparator(SEP_DIST);

        WorkspaceComboBox workspaceComboBox = new WorkspaceComboBox(appModel);
        workspaceComboBox.setPreferredSize(new Dimension(200, 23));

        JPanel p = new JPanel(new BorderLayout());
        JLabel l = new JLabel("Worksheet:");
        l.setMinimumSize(l.getPreferredSize());
        p.add(l, "West");
        p.add(workspaceComboBox, "East");
        p.setMaximumSize(new Dimension(280, 28));
        t.add(p);

        t.add(getBut(aa.getAddWorkspaceAction(), "tool-addWorkspace", false));
        t.add(getBut(aa.getRemoveWorkspaceAction(), "tool-removeWorkspace", false));
        t.add(getBut(aa.getRenameWorkspaceAction(), "tool-renameWorkspace", false));

        t.addSeparator();
        t.addSeparator(SEP_DIST);
        return t;
    }

    public static JButton getBut(Action a, String name, boolean showText) {
        JButton b = new JButton(a);
        b.setName(name);
        if (!showText) {
            b.setText("");
        }
        return b;
    }

    private static JMenu getJMenu(String title, int mnemonic) {
        JMenu m = new JMenu(title);
        m.setName(title + "Menu");
        m.setMnemonic(mnemonic);
        return m;
    }

    private void updateTitle() {
        File f = this.appActions.getCurrentFile();
        if (this.appModel.getTitle().length() > 0) {
            setTitle(this.appModel.getTitle() + " - " + "sqlDashboards");
        } else if (this.appActions.getCurrentFile() != null) {
            setTitle(f.getName() + " - " + "sqlDashboards");
        } else {
            setTitle("sqlDashboards");
        }
    }

    public AppModel getAppModel() {
        return this.appModel;
    }


    private JMenuBar getMenuBar(final AppActions appActions) {
        final JMenu fileMenu = getJMenu("File", 70);
        fileMenu.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent e) {
                SqlDashFrame.this.rebuildFileMenu(appActions, fileMenu);
            }

            public void menuCanceled(MenuEvent e) {
            }

            public void menuDeselected(MenuEvent e) {
            }
        });
        rebuildFileMenu(appActions, fileMenu);

        JMenu viewMenu = getViewMenu();


        final JMenu serverMenu = getJMenu("Server", 83);
        serverMenu.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent e) {
                ConnectionManager connMan = SqlDashFrame.this.appModel.getConnectionManager();
                serverMenu.removeAll();
                JMenuItem addServerItem = new JMenuItem(new SqlDashFrame.AddServerAction());
                serverMenu.add(addServerItem);
                serverMenu.addSeparator();
                for (ServerConfig sc : connMan.getServerConnections()) {
                    serverMenu.add(new SqlDashFrame.EditServerAction(sc, connMan));
                }
            }

            public void menuCanceled(MenuEvent e) {
            }

            public void menuDeselected(MenuEvent e) {
            }
        });
        JMenu appMenu = getJMenu("Actions", 65);

        for (Action action : appActions.getAddChartActions()) {
            appMenu.add(action);
        }
        appMenu.addSeparator();
        appMenu.add(appActions.getAddWorkspaceAction());
        appMenu.add(appActions.getRemoveWorkspaceAction());
        appMenu.add(appActions.getRenameWorkspaceAction());


        final JMenu workspaceMenu = getJMenu("Workspace", 87);
        workspaceMenu.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent e) {
                workspaceMenu.removeAll();
                JMenuItem addServerItem = new JMenuItem(appActions.getAddWorkspaceAction());
                workspaceMenu.add(addServerItem);
                workspaceMenu.addSeparator();
                DesktopModel dm = SqlDashFrame.this.appModel.getSelectedDesktopModel();
                if (dm != null) {
                    List<WorkspaceModel> workspaces = dm.getWorkspaces();
                    for (WorkspaceModel w : workspaces) {
                        workspaceMenu.add(new AbstractAction("Show " + w.getTitle()) {
                            public void actionPerformed(ActionEvent e) {
                                DesktopModel dm = SqlDashFrame.this.appModel.getSelectedDesktopModel();
                                if (dm != null) {
                                    dm.setSelectedWorkspace(w);
                                }
                            }
                        });
                    }
                }
            }


            public void menuCanceled(MenuEvent e) {
            }


            public void menuDeselected(MenuEvent e) {
            }
        });
        JMenu helpMenu = getJMenu("Help", 72);
        Action a = HtmlUtils.getWWWaction("Welcome", "http://www.timestored.com/sqlDashboards/help/");
        a.putValue("MnemonicKey", Integer.valueOf(72));
        a.putValue("AcceleratorKey", KeyStroke.getKeyStroke("F1"));
        helpMenu.add(a);

        JMenu demosMenu = getJMenu("Demo", 68);
        demosMenu.setIcon(Theme.CIcon.SCRIPT_GO.get());
        for (Action demoAct : appActions.getDemoLaunchActions()) {
            demosMenu.add(demoAct);
        }
        helpMenu.add(demosMenu);


        helpMenu.addSeparator();
        helpMenu.add(HtmlUtils.getWWWaction("Launch TimeStored.com", "http://www.timestored.com"));
        String bugUrl = TimeStored.getContactUrl("sqlDashboards Bug Report");
        helpMenu.add(HtmlUtils.getWWWaction("Report a Bug", bugUrl));

        helpMenu.addSeparator();
        helpMenu.add(HtmlUtils.getWWWaction("Purchase sqlDashboards Pro", TimeStored.Page.SQLDASH_BUY.url()));
        helpMenu.add(new AbstractAction("Enter License Key") {
            public void actionPerformed(ActionEvent e) {
                String helpMsg = "Please enter your license key: ";
                String txt = Theme.getTextFromDialog(SqlDashFrame.this, "License Key", "", helpMsg);
                if (txt != null) {
                    String msg = "Sorry that license doesn't appear to be a valid signed license. \r\nPlease contact tech@timestored.com for help";
                    if (SDLicenser.setSignedLicense(txt)) {
                        msg = "Congratulations you have sucessfully registered your license";
                    }

                    JOptionPane.showMessageDialog(SqlDashFrame.this, msg);
                }
            }
        });
        helpMenu.addSeparator();
        helpMenu.add(new AbstractAction("About") {
            public void actionPerformed(ActionEvent e) {
                SqlDashFrame.this.showAboutDialog(SDLicenser.getLicenseXml());
            }
        });


        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(serverMenu);
        menuBar.add(workspaceMenu);
        menuBar.add(appMenu);
        menuBar.add(helpMenu);
        return menuBar;
    }


    public JMenu getViewMenu() {
        JMenu viewMenu = getJMenu("View", 86);
        ShortcutAction shortcutAction = new ShortcutAction("Full Screen", null, "Hide all toolbars and maximize the chart viewing area.", Integer.valueOf(122), 65535) {
            public void actionPerformed(ActionEvent e) {
                boolean showing = (SqlDashFrame.this.toolbar.isVisible() || SqlDashFrame.this.dockingAppPanel.isEditorVisible());
                SqlDashFrame.this.toolbar.setVisible(!showing);
                SqlDashFrame.this.dockingAppPanel.setEditorVisible(!showing);
            }
        };
        KeyStroke ks = KeyStroke.getKeyStroke("F11");
        shortcutAction.putValue("AcceleratorKey", ks);

        JMenu toolbarMenu = getJMenu("Toolbars", 84);

        Action toggleCommonAction = new AbstractAction("Common Actions") {
            public void actionPerformed(ActionEvent e) {
                SqlDashFrame.this.toolbar.setVisible(!SqlDashFrame.this.toolbar.isVisible());
            }
        };
        toolbarMenu.add(new JCheckBoxMenuItem(toggleCommonAction) {
            public boolean isSelected() {
                return SqlDashFrame.this.toolbar.isVisible();
            }
        });

        Action toggleEditorAction = new AbstractAction("Editor") {
            public void actionPerformed(ActionEvent e) {
                SqlDashFrame.this.dockingAppPanel.setEditorVisible(!SqlDashFrame.this.dockingAppPanel.isEditorVisible());
            }
        };
        toolbarMenu.add(new JCheckBoxMenuItem(toggleEditorAction) {
            public boolean isSelected() {
                return SqlDashFrame.this.dockingAppPanel.isEditorVisible();
            }
        });

        viewMenu.add(toolbarMenu);
        viewMenu.add(new JMenuItem(shortcutAction));
        return viewMenu;
    }


    public void rebuildFileMenu(AppActions appActions, JMenu fileMenu) {
        fileMenu.removeAll();
        fileMenu.add(appActions.getNewFileAction());
        fileMenu.add(appActions.getOpenFileAction());
        fileMenu.add(appActions.getCloseFileAction());
        fileMenu.add(appActions.getSaveFileAction());
        fileMenu.add(appActions.getSaveAsFileAction());
        fileMenu.addSeparator();
        for (JMenuItem m : appActions.getOpenRecentFileActions()) {
            fileMenu.add(m);
        }
        fileMenu.addSeparator();


        JMenuItem exit = new JMenuItem("Exit");
        exit.setMnemonic('x');
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                WindowEvent wev = new WindowEvent(SqlDashFrame.this, 201);

                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
            }
        });
        fileMenu.add(exit);
    }

    public void openFile(File file) {
        this.appActions.openFile(file);
    }

    public void showAboutDialog(String licenseTxt) {
        String htmlTitle = "<h1><font color='#2580A2'>sql</font><font color='#25A230'>Dashboards</font></h1>";
        Theme.CIcon cIcon = Theme.CIcon.SQLDASH_LOGO;
        (new AboutDialog(this, "sqlDashboards", cIcon, htmlTitle, "1.41", licenseTxt)).setVisible(true);
    }

    public class SqlDashFrameAppListener
            extends AbstractApplicationListener {
        public SqlDashFrameAppListener() {
            super(SqlDashFrame.this);
        }

        public void handleOpenFile(ApplicationEvent event) {
            SqlDashFrame.this.appActions.getOpenFileAction().actionPerformed(null);
        }

        public void handleAbout(ApplicationEvent event) {
            SqlDashFrame.this.showAboutDialog(null);
        }
    }

    public class AddServerAction
            extends AbstractAction {
        private static final long serialVersionUID = 1L;


        public AddServerAction() {
            super("Add Server...", Theme.CIcon.SERVER_ADD.get());
            putValue("ShortDescription", "Add a server to the list of possible connections");

            putValue("MnemonicKey", Integer.valueOf(65));
        }


        public void actionPerformed(ActionEvent arg0) {
            (new ConnectionManagerDialog(SqlDashFrame.this.appModel.getConnectionManager(), SqlDashFrame.this, null, SqlDashFrame.this.jdbcTypesSupported)).setVisible(true);
        }
    }

    public class EditServerAction
            extends AbstractAction {
        private static final long serialVersionUID = 1L;

        private final ServerConfig sc;
        private final ConnectionManager connMan;

        public EditServerAction(ServerConfig sc, ConnectionManager connMan) {
            super(sc.getName());
            this.connMan = connMan;

            if (connMan.isConnected(sc)) {
                putValue("SmallIcon", Theme.CIcon.SERVER_CONNECT.get());
                putValue("SwingLargeIconKey", Theme.CIcon.SERVER_CONNECT.get());
            }
            this.sc = sc;

            putValue("ShortDescription", "Edit the server connection " + sc.getName());
        }


        public void actionPerformed(ActionEvent arg0) {
            (new ConnectionManagerDialog(this.connMan, SqlDashFrame.this, this.sc.getName(), SqlDashFrame.this.jdbcTypesSupported)).setVisible(true);
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\SqlDashFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */