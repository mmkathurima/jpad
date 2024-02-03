package com.timestored.sqldash;

import com.timestored.connections.*;
import com.timestored.misc.FifoBuffer;
import com.timestored.misc.IOUtils;
import com.timestored.sqldash.chart.ViewStrategy;
import com.timestored.sqldash.chart.ViewStrategyFactory;
import com.timestored.sqldash.forms.FormWidget;
import com.timestored.sqldash.model.*;
import com.timestored.sqldash.stockdb.DemoFactory;
import com.timestored.sqldash.stockdb.FinanceDataDemo;
import com.timestored.sqldash.stockdb.FinanceDemoFrame;
import com.timestored.swingxx.SwingUtils;
import com.timestored.theme.ShortcutAction;
import com.timestored.theme.Theme;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppActions {
    private static final Logger LOG = Logger.getLogger(AppActions.class.getName());

    private final List<Action> addChartActions = new ArrayList<Action>();

    private final Action addWorkspaceAction;

    private final Action removeWorkspaceAction;
    private final Action renameWorkspaceAction;
    private final Action openFileAction;
    private final Action saveFileAction;
    private final Action saveAsFileAction;
    private final Action newFileAction;
    private final Action closeFileAction;
    private final Action addFormWidgetAction;
    private final FifoBuffer<File> recentOpenPaths = new FifoBuffer(9);
    private final List<Listener> listeners = new CopyOnWriteArrayList<Listener>();

    private final AppModel appModel;

    private final Component parent;
    private final List<Action> demoLaunchActions;
    private final AbstractAction mainDashDemoAction;
    private File currentFile;

    public AppActions(AppModel appModel, Component parent) {
        this.appModel = appModel;
        this.parent = parent;

        for (ViewStrategy vs : ViewStrategyFactory.getStrategies()) {
            if (vs.getIcon() != null) {
                String desc = "Add " + vs.getDescription();
                ShortcutAction a = new ShortcutAction(desc, vs.getIcon(), desc + " to the current workspace.") {
                    public void actionPerformed(ActionEvent arg0) {
                        DesktopModel dm = appModel.getSelectedDesktopModel();
                        if (dm != null) {
                            Queryable q = new Queryable("NO SERVERS.", "");
                            ConnectionManager connMan = appModel.getConnectionManager();
                            if (connMan != null) {
                                List<ServerConfig> conns = connMan.getServerConnections();
                                if (conns.size() > 0) {
                                    ServerConfig sc = conns.get(0);
                                    q.setServerName(sc.getName());
                                    String qry = vs.getQueryEg(sc.getJdbcType());
                                    if (qry != null) {
                                        q.setQuery(qry);
                                    }
                                }
                            }
                            ChartWidget cw = new ChartWidget(appModel.getSelectedDesktopModel());
                            cw.setQueryable(q);
                            cw.setViewStrategy(vs);
                            cw.setTitle(vs.getDescription());
                            dm.add(cw);
                        }
                    }
                };
                this.addChartActions.add(a);
            }
        }

        this.addFormWidgetAction = new ShortcutAction("Add Form", Theme.CIcon.LAYOUT_ADD, "Add a form that allows submitting Arguments to customize chart queries.") {
            public void actionPerformed(ActionEvent arg0) {
                DesktopModel dm = appModel.getSelectedDesktopModel();
                if (dm != null) {
                    dm.add(new FormWidget(dm));
                }
            }
        };

        this.addWorkspaceAction = new ShortcutAction("Add Worksheet", Theme.CIcon.LAYOUT_ADD, "Insert a new Worksheet") {
            public void actionPerformed(ActionEvent arg0) {
                DesktopModel dm = appModel.getSelectedDesktopModel();
                if (dm != null) {
                    dm.addWorkspace();
                }
            }
        };

        this.removeWorkspaceAction = new ShortcutAction("Remove Worksheet", Theme.CIcon.LAYOUT_DELETE, "Remove the current Worksheet") {
            public void actionPerformed(ActionEvent e) {
                int r = JOptionPane.showConfirmDialog(parent, "Delete the current workspace?");
                if (r == 0) {
                    DesktopModel dm = appModel.getSelectedDesktopModel();
                    if (dm != null) {
                        dm.remove(dm.getSelectedWorkspace());
                    }
                }
            }
        };

        this.renameWorkspaceAction = new ShortcutAction("Rename Worksheet", Theme.CIcon.LAYOUT_EDIT, "Rename the current Worksheet") {
            public void actionPerformed(ActionEvent e) {
                WorkspaceModel ws = appModel.getSelectedWorkspaceModel();
                final String msg = "Enter the new name:";
                String r = JOptionPane.showInputDialog(parent, msg, ws.getTitle());
                if (r != null && r.length() > 0) {
                    ws.setTitle(r);
                }
            }
        };

        this.openFileAction = new ShortcutAction("Open File...", Theme.CIcon.DOCUMENT_OPEN, 79) {
            public void actionPerformed(ActionEvent e) {
                if (SDLicenser.requestPermission()) {
                    AppActions.this.askUserToChooseFileOpen();
                }
            }
        };

        this.saveFileAction = new ShortcutAction("Save", Theme.CIcon.DOCUMENT_SAVE, 83) {
            public void actionPerformed(ActionEvent e) {
                if (SDLicenser.requestPermission()) {
                    if (AppActions.this.currentFile != null) {
                        AppActions.this.save(AppActions.this.currentFile);
                    } else {
                        AppActions.this.letUserChooseFileAndSave();
                    }
                }
            }
        };
        this.saveFileAction.putValue("MnemonicKey", Integer.valueOf(83));

        this.saveAsFileAction = new ShortcutAction("Save As...", Theme.CIcon.DOCUMENT_SAVE_AS, null) {
            public void actionPerformed(ActionEvent e) {
                if (SDLicenser.requestPermission()) {
                    AppActions.this.letUserChooseFileAndSave();
                }
            }
        };
        this.saveAsFileAction.putValue("MnemonicKey", Integer.valueOf(65));

        this.newFileAction = new ShortcutAction("New File", Theme.CIcon.DOCUMENT_NEW, 78) {
            public void actionPerformed(ActionEvent e) {
                appModel.newDesktop();
            }
        };

        this.closeFileAction = new ShortcutAction("Close", null, "Close", Integer.valueOf(67), 115) {
            public void actionPerformed(ActionEvent e) {
                appModel.newDesktop();
                File f = AppActions.this.currentFile;
                AppActions.this.currentFile = null;
                for (Listener l : AppActions.this.listeners) {
                    l.fileClosed(f);
                }
                f = null;
            }
        };

        this.demoLaunchActions = new ArrayList<Action>();

        this.demoLaunchActions.add(new AbstractAction("MySQL Yahoo Finance Dashboard", Theme.CIcon.DAS_FILE.get16()) {
            public void actionPerformed(ActionEvent e) {
                List<ServerConfig> sconns = appModel.getConnectionManager().getServerConnections();
                ServerConfig mySC = null;
                for (ServerConfig sc : sconns) {
                    if (sc.getJdbcType().equals(JdbcTypes.MYSQL)) {
                        mySC = sc;
                        break;
                    }
                }
                if (mySC != null) {

                    String msg = "This demo creates tables and insert data onto your MySQL database server:.\r\n" + mySC.getShortName() + "\r\n\r\nAre you sure you want to proceed?";

                    int choice = JOptionPane.showConfirmDialog(parent, msg, null, 0);

                    if (choice == 0) {
                        AppActions.this.openDemoDialog(mySC, "stock-watch-h2.das");
                    }
                } else {

                    JOptionPane.showMessageDialog(parent, "I could not find a MySQL connection in the server list.\r\nPlease create one for your database.");
                }
            }
        });

        this.mainDashDemoAction = new AbstractAction("Yahoo Finance SQL Dashboard", Theme.CIcon.DAS_FILE.get16()) {
            public void actionPerformed(ActionEvent e) {
                final String msg = "This demo uses an in-memory H2 database that stays running as long as it's window is open.\r\n";

                DBTestRunner dbRunner = DBTestRunnerFactory.getDbRunner(JdbcTypes.H2);
                try {
                    dbRunner.start();
                    ServerConfig sc = dbRunner.getServerConfig();
                    FinanceDemoFrame fdFrame = AppActions.this.openDemoDialog(sc, "stock-watch-h2.das");
                    fdFrame.addWindowListener(new WindowAdapter() {
                        public void windowClosed(WindowEvent e) {
                            dbRunner.stop();
                        }
                    });
                } catch (SQLException se) {
                    String message = "Unable to initialise database:\r\n" + se.getMessage();
                    JOptionPane.showMessageDialog(parent, message, "Sql Intiialisation Error", 2);
                }
            }
        };

        this.demoLaunchActions.add(this.mainDashDemoAction);

        this.demoLaunchActions.add(new AbstractAction("Yahoo Finance Kdb Dashboard", Theme.CIcon.DAS_FILE.get16()) {
            public void actionPerformed(ActionEvent e) {
                final String msg = "This demo creates tables and insert data onto a kdb database server on the local machine with port 5000.\r\n\r\nHave you started the kdb server on port 5000 and is it ok to proceed?";

                int choice = JOptionPane.showConfirmDialog(parent, msg, null, 0);

                if (choice == 0) {
                    ServerConfig sc = new ServerConfig("localhost", 5000);
                    AppActions.this.openDemoDialog(sc, "stock-watch.das");
                }
            }
        });

        this.demoLaunchActions.add(new AbstractAction("Simple kdb Dashboard", Theme.CIcon.DAS_FILE.get16()) {
            public void actionPerformed(ActionEvent e) {
                if (AppActions.this.openExample("example.das", new ServerConfig("localhost", 5000))) {
                    final String message = "This example relies on a kdb database server being present on localhost port 5000.\r\nIf you are not running a server on that port, edit the server configuration to point at an existing server.";

                    JOptionPane.showMessageDialog(parent, message, "Example Dashboard", -1);
                }
            }
        });

        appModel.addListener(new AppModel.Listener() {
            public void desktopChanged(DesktopModel selectedDesktopModel) {
                AppActions.this.refreshActions();
            }
        });

        appModel.addOpenedDesktopListener(new DesktopModelAdapter() {
            public void appSelected(WorkspaceModel wsm, Widget selectedApp) {
                AppActions.this.refreshActions();
            }

            public void appRemoved(WorkspaceModel wsm, Widget w) {
                AppActions.this.refreshActions();
            }

            public void appAdded(WorkspaceModel wsm, Widget w) {
                AppActions.this.refreshActions();
            }

            public void workspaceSelected(WorkspaceModel wsm) {
                AppActions.this.refreshActions();
            }
        });
    }

    private void askUserToChooseFileOpen() {
        JFileChooser fc;
        if (this.currentFile != null) {
            fc = new JFileChooser(this.currentFile.getParentFile());
        } else {
            fc = new JFileChooser();
        }

        fc.setFileFilter(new FileFilter() {
                             public String getDescription() {
                                 return "Dashboard files";
                             }

                             public boolean accept(File f) {
                                 return (f.isDirectory() || f.getName().toLowerCase().endsWith("das"));
                             }
                         }
        );

        fc.setMultiSelectionEnabled(false);
        if (fc.showOpenDialog(null) == 0) {
            File file = fc.getSelectedFile();
            this.openFile(file);
        } else {
            LOG.info("Open command cancelled by user.");
        }
    }

    void openFile(File file) {
        try {
            this.appModel.openFile(file);
            this.currentFile = file;
            for (Listener l : this.listeners) {
                l.fileOpened(this.currentFile);
            }
            this.recentOpenPaths.add(file);
        } catch (IOException e) {
            final String msg = "Error could not understand the file format.\r\nIf you are sure this is a valid .das file please contact\r\ntech@timestored.com";

            JOptionPane.showMessageDialog(this.parent, msg, "File Open Error", 2);
            LOG.info(msg);
            JOptionPane.showMessageDialog(this.parent, msg, "Error Opening", 0);
        }
    }

    private void letUserChooseFileAndSave() {
        File file = SwingUtils.askUserSaveLocation("das", this.currentFile);
        if (file != null) {
            this.save(file);
        } else {
            LOG.info("Save command cancelled by user.");
        }
    }

    private void save(File file) {
        try {
            this.appModel.saveToFile(file);

            this.currentFile = file;
            for (Listener l : this.listeners) {
                l.fileSaved(this.currentFile);
            }
        } catch (IOException e) {
            String msg = "Error saving file: " + file;
            LOG.info(msg);
            JOptionPane.showMessageDialog(this.parent, msg, "Error Saving", 0);
        }
    }

    private void refreshActions() {
        boolean hasWS = (this.appModel.getSelectedWorkspaceModel() != null);
        DesktopModel desktop = this.appModel.getSelectedDesktopModel();
        for (Action a : this.addChartActions) {
            a.setEnabled(hasWS);
        }
        this.addWorkspaceAction.setEnabled((desktop != null));
        this.removeWorkspaceAction.setEnabled(hasWS);
        this.renameWorkspaceAction.setEnabled(hasWS);
    }

    public Action getOpenFileAction() {
        return this.openFileAction;
    }

    public List<Action> getAddChartActions() {
        return this.addChartActions;
    }

    public Action getAddFormAction() {
        return this.addFormWidgetAction;
    }

    public Action getAddWorkspaceAction() {
        return this.addWorkspaceAction;
    }

    public Action getRemoveWorkspaceAction() {
        return this.removeWorkspaceAction;
    }

    public Action getSaveFileAction() {
        return this.saveFileAction;
    }

    public Action getSaveAsFileAction() {
        return this.saveAsFileAction;
    }

    public Action getNewFileAction() {
        return this.newFileAction;
    }

    public Action getCloseFileAction() {
        return this.closeFileAction;
    }

    public Action getRenameWorkspaceAction() {
        return this.renameWorkspaceAction;
    }

    public Action getMainDashDemoAction() {
        return this.mainDashDemoAction;
    }

    public List<Action> getDemoLaunchActions() {
        return this.demoLaunchActions;
    }

    public List<JMenuItem> getOpenRecentFileActions() {
        List<JMenuItem> l = new ArrayList<JMenuItem>();
        List<File> fps = this.recentOpenPaths.getAll();

        for (int i = 0; i < fps.size(); i++) {
            File f = fps.get(i);
            String path = f.getAbsolutePath();
            if (path.length() > 31) {
                path = path.subSequence(0, 30) + "...";
            }
            String s = (i + 1) + ". " + f.getName() + " [" + path + "]";
            JMenuItem mi = new JMenuItem(new AbstractAction(s) {
                public void actionPerformed(ActionEvent e) {
                    AppActions.this.openFile(f);
                }
            });
            mi.setToolTipText(f.getAbsolutePath());
            mi.setMnemonic(49 + i);
            l.add(mi);
        }
        return l;
    }

    public boolean addListener(Listener appActionslistener) {
        return this.listeners.add(appActionslistener);
    }

    public File getCurrentFile() {
        return this.currentFile;
    }

    private FinanceDemoFrame openDemoDialog(ServerConfig sc, String exampleFileName) {
        FinanceDataDemo fdDemo = null;
        FinanceDemoFrame fdDialog = null;
        try {
            fdDemo = DemoFactory.getFinanceDataDemo(sc);
        } catch (IOException e1) {
        }

        if (fdDemo == null) {
            JOptionPane.showMessageDialog(this.parent, "Could not find demo for the database type you requested.", "Sql Intiialisation Error", 2);
        } else {

            fdDialog = new FinanceDemoFrame(sc.getShortName(), fdDemo);
            fdDialog.setLocationRelativeTo(this.parent);
            try {
                fdDemo.start();
                fdDialog.setVisible(true);

                Runnable runnable = new Runnable() {
                    public void run() {
                        EventQueue.invokeLater(new Runnable() {
                            public void run() {
                                AppActions.this.openExample(exampleFileName, sc);
                            }
                        });
                    }
                };
                Executors.newScheduledThreadPool(1).schedule(runnable, 4L, TimeUnit.SECONDS);
            } catch (SQLException se) {
                fdDialog.dispose();
                String message = "Unable to initialise database:\r\n" + se.getMessage();
                JOptionPane.showMessageDialog(this.parent, message, "Sql Intiialisation Error", 2);
            }
        }

        return fdDialog;
    }

    private boolean openExample(String packageFilename, ServerConfig serverConfigToUseForAllQueries) {
        boolean openedExample = false;
        try {
            InputStream is = SqlDashFrame.class.getResourceAsStream(packageFilename);
            File f = IOUtils.createTempCopy(packageFilename, is);
            this.openFile(f);
            if (serverConfigToUseForAllQueries != null) {
                this.appModel.setAllQueryServersTo(serverConfigToUseForAllQueries);
            }
            openedExample = true;
        } catch (IOException ioe) {
            String msg = "Probelem opening " + packageFilename;
            LOG.log(Level.SEVERE, msg);
            JOptionPane.showMessageDialog(null, msg);
        }
        return openedExample;
    }

    public interface Listener {
        void fileOpened(File param1File);

        void fileSaved(File param1File);

        void fileClosed(File param1File);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\AppActions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */