package io.jpad;

import bibliothek.gui.DockFrontend;
import bibliothek.gui.Dockable;
import bibliothek.gui.dock.DefaultDockable;
import bibliothek.gui.dock.SplitDockStation;
import bibliothek.gui.dock.action.DefaultDockActionSource;
import bibliothek.gui.dock.action.actions.SimpleButtonAction;
import bibliothek.gui.dock.station.split.DockableSplitDockTree;
import bibliothek.gui.dock.station.split.SplitDockTree;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.timestored.docs.Document;
import com.timestored.docs.DocumentActions;
import com.timestored.docs.FileDropDocumentHandler;
import com.timestored.docs.OpenDocumentsModel;
import com.timestored.messages.Msg;
import com.timestored.misc.AbstractApplicationListener;
import com.timestored.misc.HtmlUtils;
import com.timestored.misc.Mac;
import com.timestored.sqldash.swingxx.DockerHelper;
import com.timestored.sqldash.swingxx.ToggleDockableMenuItem;
import com.timestored.swingxx.FileTreePanel;
import com.timestored.swingxx.JToolBarWithBetterTooltips;
import com.timestored.swingxx.QueryStatusBar;
import com.timestored.theme.AboutDialog;
import com.timestored.theme.Theme;
import com.timestored.tscore.persistance.KeyInterface;
import com.timestored.tscore.persistance.OpenDocumentPersister;
import com.timestored.tscore.persistance.PersistenceInterface;
import io.jpad.model.*;
import org.fife.ui.rtextarea.RTextArea;
import org.simplericity.macify.eawt.ApplicationEvent;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;

public class JPadFrame
        extends JFrame {
    static final com.timestored.theme.Icon APP_ICON = Theme.CIcon.JPAD;
    private static final Logger LOG = Logger.getLogger(JPadFrame.class.getName());
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();
    private static final File SCRATCH_DIR = new File(JPadConfig.APP_HOME, "scratch");
    private static final long serialVersionUID = 1L;
    private static final boolean DEVELOPMENT_FLAG = true;
    private final JEngine jEngine;
    private final RunConfig runConfig = new RunConfig();
    private final OpenDocumentsModel openDocumentsModel;
    private final DockFrontend frontend;
    private final DocumentActions documentActions;
    private final String defaultLayoutXml;
    private final JMenuBar menuBar;
    private OpenDocumentPersister persistDocListener;
    private JMenu fileMenu;

    public JPadFrame(OpenDocumentsModel openDocumentsModel, JEngine jEngine) {
        LOG.info("Starting JPadFrame Constructor");
        this.openDocumentsModel = Preconditions.checkNotNull(openDocumentsModel);
        this.jEngine = Preconditions.checkNotNull(jEngine);
        this.documentActions = new DocumentActions(openDocumentsModel, "jpad");

        Mac.configureIfMac(new JavaNotepadAppListener(), APP_ICON);
        this.setTransferHandler((new FileDropDocumentHandler()).addListener(this.documentActions));
        openDocumentsModel.addListener(new OpenDocumentsModel.Adapter() {
            public void docSelected(Document document) {
                JPadFrame.this.setFrameTitle();
            }
        });
        final JPPersistence persistance = JPPersistence.INSTANCE;

        boolean firstEverOpen = persistance.getBoolean(JPPersistence.Key.FIRST_OPEN, true);
        if (firstEverOpen) {
            persistance.putBoolean(JPPersistence.Key.FIRST_OPEN, false);
        }

        this.setTitle("JPad");
        this.setLayout(new BorderLayout(4, 4));
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setIconImage(APP_ICON.get().getImage());

        openDocumentsModel.forceKeyboardShortcutOverrides();

        this.frontend = new DockFrontend(this);
        SplitDockStation station = new SplitDockStation();
        this.frontend.addRoot("JPad", station);
        this.frontend.setShowHideAction(true);

        JPanel codeEditorPanel = new JPanel(new BorderLayout());
        DocumentTabbedPane documentTabbedPane = new DocumentTabbedPane(this.documentActions, openDocumentsModel, this);
        codeEditorPanel.add(documentTabbedPane, "Center");

        jEngine.addListener(new JEngineAdapter() {
            public void resultReturned(RunResult runResult) {
                documentTabbedPane.updateErrorDisplay(runResult.getRawCode(), runResult.getDiagnostics());
            }
        });

        DefaultDockable documentsDockable = this.createDockable(Msg.get(Msg.Key.DOCUMENTS), Theme.CIcon.PAGE_CODE
                .get16(), codeEditorPanel);

        File egDir = new File("examples");
        if (!egDir.exists() && this.isWindows()) {
            egDir = new File("C:\\Program Files (x86)\\TimeStored.com\\JPad\\examples");
        }
        DefaultDockable egFileTreeDockable = this.getFilePanel(egDir, Msg.get(Msg.Key.EXAMPLE_SCRIPTS), false);
        //JPadConfig.SCRIPTS_FOLDER = Path.of(FileSystemView.getFileSystemView().getDefaultDirectory().getPath(), "EspressoPad").toFile();
        if (!JPadConfig.SCRIPTS_FOLDER.exists()) {
            try {
                Files.createDirectory(JPadConfig.SCRIPTS_FOLDER.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        DefaultDockable myDocsFileTreeDockable = this.getFilePanel(JPadConfig.SCRIPTS_FOLDER, Msg.get(Msg.Key.MY_SCRIPTS), true);

        DefaultDockable consoleDockable = this.addRenderer(Msg.get(Msg.Key.CONSOLE), new ConsolePanel());
        DefaultDockable javapDockable = this.addRenderer(Msg.get(Msg.Key.BYTE_CODE), new BytecodeOutputPanel());
        DefaultDockable htmlDockable = this.addRenderer(Msg.get(Msg.Key.RESULT), new HtmlResultPanel());
        DefaultDockable chartResultDockable = this.addRenderer(Msg.get(Msg.Key.CHART), new ChartResultPanel());

        jEngine.registerResultRenderer(new JpadCodeResultRenderer());

        List<Class<ResultRenderer>> pluggedinRenderers = ClassFinder.findClasses(clsName -> clsName.endsWith("JPadPlugin"), ResultRenderer.class);
        List<DefaultDockable> pluggedinDockables = Lists.newArrayListWithExpectedSize(pluggedinRenderers.size());
        for (Class<ResultRenderer> cls : pluggedinRenderers) {
            try {
                ResultRenderer rr = cls.newInstance();
                pluggedinDockables.add(this.addRenderer(rr.getTabName(), rr));
            } catch (InstantiationException | IllegalAccessException e) {
                LOG.log(Level.SEVERE, "Could not load plugin from class: " + cls.getName(), e);
            }
        }

        DefaultDockable generatedCodeDockable;

        generatedCodeDockable = this.addRenderer(Msg.get(Msg.Key.FILE), new GeneratedCodePanel());

        JPadActions commonActions = new JPadActions(jEngine, documentTabbedPane, persistance, this, openDocumentsModel, this.runConfig);

        JMenu panelsMenu = getJMenu(Msg.get(Msg.Key.WINDOWS), 87);
        panelsMenu.add(new ToggleDockableMenuItem(documentsDockable, this.frontend, "documentsDockableMI"));
        panelsMenu.add(new ToggleDockableMenuItem(htmlDockable, this.frontend, "htmlDockableMI"));
        panelsMenu.add(new ToggleDockableMenuItem(consoleDockable, this.frontend, "consoleDockableMI"));
        panelsMenu.add(new ToggleDockableMenuItem(chartResultDockable, this.frontend, "chartResultDockableMI"));
        panelsMenu.add(new ToggleDockableMenuItem(javapDockable, this.frontend, "javapDockableMI"));
        panelsMenu.add(new ToggleDockableMenuItem(generatedCodeDockable, this.frontend, "generatedCodeDockableMI"));
        if (egFileTreeDockable != null) {
            panelsMenu.add(new ToggleDockableMenuItem(egFileTreeDockable, this.frontend, "egFileTreeDockableMI"));
        }
        if (myDocsFileTreeDockable != null) {
            panelsMenu.add(new ToggleDockableMenuItem(myDocsFileTreeDockable, this.frontend, "myDocsFileTreeDockableMI"));
        }
        panelsMenu.addSeparator();
        panelsMenu.add(new AbstractAction("Restore Default Layout") {
                           private static final long serialVersionUID = 1L;

                           public void actionPerformed(ActionEvent e) {
                               DockerHelper.loadLayout(JPadFrame.this.defaultLayoutXml, JPadFrame.this.frontend);
                           }
                       }
        );

        this.menuBar = this.getBasicMenuBar(commonActions, this.documentActions);
        this.menuBar.add(panelsMenu);
        this.menuBar.add(this.getHelpMenu());
        this.setJMenuBar(this.menuBar);

        DockableSplitDockTree tree = new DockableSplitDockTree();

        SplitDockTree<Dockable>.Key docsGroup = this.putTree(tree, documentsDockable);

        List<Dockable> outputGrp = Lists.newArrayList(new Dockable[]{htmlDockable, consoleDockable, chartResultDockable, javapDockable, generatedCodeDockable});
        outputGrp.addAll(pluggedinDockables);
        SplitDockTree<Dockable>.Key consoleGroup = this.putTree(tree, outputGrp.toArray(new Dockable[0]));
        SplitDockTree<Dockable>.Key editorTree = tree.vertical(docsGroup, consoleGroup, 0.75D);

        SplitDockTree<Dockable>.Key root = editorTree;

        SplitDockTree<Dockable>.Key sourceBrowserTree = this.putTree(tree, myDocsFileTreeDockable, egFileTreeDockable);
        if (sourceBrowserTree != null) {
            root = tree.horizontal(sourceBrowserTree, editorTree, 0.25D);
        }

        tree.root(root);
        station.dropTree(tree);

        this.defaultLayoutXml = DockerHelper.getLayout(this.frontend);

        this.setLayout(new BorderLayout());
        this.add(this.getToolbar(commonActions, this.documentActions), "North");
        this.add(station, "Center");
        this.add(getStatusBar(jEngine), "South");

        this.restoreBounds(persistance, JPPersistence.Key.FRAME_WIDTH, JPPersistence.Key.FRAME_HEIGHT);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                JPadFrame.this.persistDocListener.storeDocumentsScratch();
                persistance.putInt(JPPersistence.Key.FRAME_WIDTH, JPadFrame.this.getWidth());
                persistance.putInt(JPPersistence.Key.FRAME_HEIGHT, JPadFrame.this.getHeight());
                try {
                    persistance.getPref().flush();
                } catch (BackingStoreException e1) {
                    LOG.severe("problem flushing to persistanct");
                }

                System.exit(0);
            }
        });

        EXECUTOR.execute(new Runnable() {
            public void run() {
                JPadFrame.this.persistDocListener = new OpenDocumentPersister(openDocumentsModel, persistance, SCRATCH_DIR, JPPersistence.Key.RECENT_DOCS, JPPersistence.Key.LAST_OPENED_FOLDER);

                File folder = JPadFrame.this.persistDocListener.getOpenFolder(persistance);
                openDocumentsModel.setSelectedFolder(folder);

                if (!firstEverOpen) {

                    JPadFrame.this.persistDocListener.restoreDocuments();
                }
                JPadFrame.this.documentActions.setDefaultFolder(JPadConfig.SCRIPTS_FOLDER);
                openDocumentsModel.addListener(JPadFrame.this.persistDocListener);
            }
        });

        jEngine.addListener(new JEngineAdapter() {
            public void compiling(JPadCode code) {
                if (JPadFrame.this.persistDocListener != null) {
                    JPadFrame.this.persistDocListener.storeDocumentsScratch();
                }
            }
        });

        jEngine.runJPadsFromFolder(JPadConfig.PLUGINS_FOLDER);

        LOG.info("Finished JPadFrame Constructor");
    }

    private static QueryStatusBar getStatusBar(JEngine jEngine) {
        QueryStatusBar queryStatusBar = new QueryStatusBar();
        jEngine.addListener(new JEngineAdapter() {
            public void compiling(JPadCode code) {
                queryStatusBar.startQuery(code.getRawCode());
            }

            public void resultReturned(RunResult runResult) {
                String statusText = runResult.isCompletedOk() ? "OK" : "Error";
                int count = runResult.getDumps().size();
                queryStatusBar.endQuery(statusText, count);
            }
        });
        return queryStatusBar;
    }

    private static JMenu getJMenu(String title, int mnemonic) {
        JMenu m = new JMenu(title);
        m.setName(title + "Menu");
        m.setMnemonic(mnemonic);
        return m;
    }

    private static Action getAction(String title, com.timestored.theme.Icon icon, Action action, int acceleratorKey, int modifier) {
        action.putValue("Name", title);
        action.putValue("SmallIcon", icon.get16());

        KeyStroke k = KeyStroke.getKeyStroke(acceleratorKey, 0);
        action.putValue("AcceleratorKey", k);
        return action;
    }

    private static Action getAction(String title, com.timestored.theme.Icon icon, Action action, int acceleratorKey) {
        return getAction(title, icon, action, acceleratorKey, 0);
    }

    private static Action getCtrlAction(String title, com.timestored.theme.Icon icon, Action action, int acceleratorKey) {
        return getAction(title, icon, action, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
    }

    public static void resetDefaults(boolean wipeLicense) throws BackingStoreException {
        JPPersistence.INSTANCE.clear(wipeLicense);
    }

    private SplitDockTree<Dockable>.Key putTree(DockableSplitDockTree tree, Dockable... dockables) {
        List<Dockable> ds = Lists.newArrayList();
        for (Dockable d : dockables) {
            if (d != null) {
                ds.add(d);
            }
        }

        if (ds.size() > 0) {
            return tree.put(ds.toArray(new Dockable[0]), ds.get(0));
        }
        return null;
    }

    private void restoreBounds(PersistenceInterface persistance, KeyInterface frameWidthKey, KeyInterface frameHeightKey) {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Insets si = tk.getScreenInsets(this.getGraphicsConfiguration());
        int defWidth = ((tk.getScreenSize()).width - si.left - si.right) * 4 / 5;
        int defHeight = ((tk.getScreenSize()).height - si.top - si.bottom) * 4 / 5;
        int w = persistance.getInt(frameWidthKey, defWidth);
        int h = persistance.getInt(frameHeightKey, defHeight);
        this.setBounds(0, 0, w, h);
    }

    private boolean isWindows() {
        return (System.getProperty("os.name").toLowerCase().contains("win"));
    }

    private DefaultDockable getFilePanel(File exampleRoot, String uniqTitle, boolean rightClickMenuShown) {
        if (exampleRoot.exists() && exampleRoot.isDirectory()) {
            FileTreePanel fileTreePanel = new FileTreePanel();
            fileTreePanel.setRightClickMenuShown(rightClickMenuShown);
            fileTreePanel.setName(uniqTitle);
            fileTreePanel.setRoot(exampleRoot);
            fileTreePanel.addListener(new FileTreePanel.Listener() {
                public void fileSelected(File selectedFile) {
                    if (selectedFile != null && selectedFile.isFile()) {
                        JPadFrame.this.documentActions.openFile(selectedFile);
                    }
                }
            });

            Action refreshAction = new AbstractAction(Msg.get(Msg.Key.REFRESH), Theme.CIcon.ARROW_REFRESH.get16()) {
                private static final long serialVersionUID = 1L;

                public void actionPerformed(ActionEvent e) {
                    fileTreePanel.refreshGui();
                }
            };
            return this.createDockable(uniqTitle, Theme.CIcon.DOCUMENT_OPEN.get16(), fileTreePanel, refreshAction);
        }
        return null;
    }

    private DefaultDockable addRenderer(String uniqueTitle, ResultRenderer renderer) {
        this.jEngine.registerResultRenderer(renderer);
        Action[] actions = renderer.getActions().toArray(new Action[0]);
        return this.createDockable(uniqueTitle, renderer.getImageIcon(), renderer.getComponent(), actions);
    }

    private DefaultDockable createDockable(String uniqueTitle, ImageIcon icon, Component component, Action... dockerActions) {
        DefaultDockable d = new DefaultDockable(component, uniqueTitle, icon);

        this.frontend.addDockable(uniqueTitle, d);
        this.frontend.setHideable(d, true);

        DefaultDockActionSource actions = new DefaultDockActionSource();

        for (Action a : dockerActions) {
            if (a != null) {
                SimpleButtonAction button = new SimpleButtonAction();
                button.setText((String) a.getValue("Name"));
                button.setIcon((Icon) a.getValue("SmallIcon"));
                button.addActionListener(a);
                actions.add(button);
            }
        }
        d.setActionOffers(actions);
        return d;
    }

    private void setFrameTitle() {
        String docTitle = this.openDocumentsModel.getSelectedDocument().getTitle();
        this.setTitle(docTitle + " - " + "JPad");
    }

    private JToolBar getToolbar(JPadActions commonActions, DocumentActions dh) {
        JToolBarWithBetterTooltips jToolBarWithBetterTooltips = new JToolBarWithBetterTooltips("Common Actions");
        jToolBarWithBetterTooltips.add(dh.getNewFileAction());
        jToolBarWithBetterTooltips.add(dh.getOpenFileAction());
        jToolBarWithBetterTooltips.add(dh.getSaveFileAction());
        jToolBarWithBetterTooltips.add(commonActions.getRunAction());
        jToolBarWithBetterTooltips.add(commonActions.getUploadSnipAction());
        jToolBarWithBetterTooltips.addSeparator();
        jToolBarWithBetterTooltips.add(new ArgsEditorPanel(this.runConfig));
        return jToolBarWithBetterTooltips;
    }

    private JMenuBar getBasicMenuBar(JPadActions commonActions, DocumentActions dh) {
        JMenu editMenu = getJMenu(Msg.get(Msg.Key.EDIT), 69);
        editMenu.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent e) {
                editMenu.removeAll();
                editMenu.add(new JMenuItem(RTextArea.getAction(RTextArea.UNDO_ACTION)));
                editMenu.add(new JMenuItem(RTextArea.getAction(RTextArea.REDO_ACTION)));
                editMenu.addSeparator();
                for (Action a : JPadFrame.this.documentActions.getEditorActions())
                    editMenu.add(new JMenuItem(a));
                editMenu.add(new JMenuItem(RTextArea.getAction(RTextArea.DELETE_ACTION)));
                editMenu.add(new JMenuItem(RTextArea.getAction(RTextArea.SELECT_ALL_ACTION)));
            }

            public void menuCanceled(MenuEvent e) {
            }

            public void menuDeselected(MenuEvent e) {
            }
        });
        this.fileMenu = getJMenu(Msg.get(Msg.Key.FILE), 70);
        this.fileMenu.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent e) {
                JPadFrame.this.rebuildFileMenu();
            }

            public void menuCanceled(MenuEvent e) {
            }

            public void menuDeselected(MenuEvent e) {
            }
        });
        this.rebuildFileMenu();

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(this.fileMenu);
        menuBar.add(editMenu);
        JMenu toolsMenu = getJMenu(Msg.get(Msg.Key.TOOLS), 84);
        toolsMenu.add(commonActions.getRunAction());
        toolsMenu.add(commonActions.getUploadSnipAction());
        toolsMenu.add(commonActions.getPreferencesAction());
        menuBar.add(toolsMenu);
        return menuBar;
    }

    private JMenu getHelpMenu() {
        JMenu helpMenu = getJMenu("Help", 72);
        Action a = HtmlUtils.getWWWaction("Welcome", JPadLtd.Page.HELP.url());
        a.putValue("MnemonicKey", 72);
        a.putValue("AcceleratorKey", KeyStroke.getKeyStroke("F1"));
        helpMenu.add(a);
        String bugUrl = JPadLtd.getContactUrl("JPad Bug Report");
        helpMenu.add(HtmlUtils.getWWWaction("Report a Bug", bugUrl));
        helpMenu.addSeparator();
        helpMenu.add(new AbstractAction(Msg.get(Msg.Key.ABOUT)) {
            public void actionPerformed(ActionEvent e) {
                JPadFrame.this.showAboutDialog(null);
            }
        });
        return helpMenu;
    }

    private void rebuildFileMenu() {
        this.fileMenu.removeAll();
        for (Action a : this.documentActions.getFileActions()) {
            this.fileMenu.add(new JMenuItem(a));
        }

        if (this.persistDocListener != null) {
            List<String> filePaths = this.persistDocListener.getRecentFilePaths();
            this.fileMenu.addSeparator();
            for (Action a : this.documentActions.getOpenRecentActions(filePaths)) {
                this.fileMenu.add(a);
            }
            this.fileMenu.addSeparator();
            this.fileMenu.add(this.documentActions.openAllAction(filePaths));
        }

        this.fileMenu.addSeparator();

        JMenuItem exit = new JMenuItem(Msg.get(Msg.Key.EXIT));
        exit.setMnemonic('x');
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                WindowEvent wev = new WindowEvent(JPadFrame.this, 201);
                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
            }
        });
        this.fileMenu.add(exit);
    }

    private void showAboutDialog(String licenseTxt) {
        final Theme.CIcon cIcon = Theme.CIcon.JPAD;
        final String htmlTitle = "<h1><font color='#2580A2'>J</font><font color='#25A230'>Pad</font></h1>";
        (new AboutDialog(this, "JPad", cIcon, htmlTitle, "1.07", licenseTxt))
                .setVisible(true);
    }

    public class JavaNotepadAppListener
            extends AbstractApplicationListener {
        public JavaNotepadAppListener() {
            super(JPadFrame.this);
        }

        public void handleOpenFile(ApplicationEvent event) {
            JPadFrame.this.documentActions.openFile(new File(event.getFilename()));
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\JPadFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */