package com.timestored.sqldash;

import com.timestored.TimeStored;
import com.timestored.connections.ConnectionManager;
import com.timestored.connections.JdbcTypes;
import com.timestored.misc.AppLaunchHelper;
import com.timestored.misc.ErrorReporter;
import com.timestored.misc.KeyedPrefs;
import com.timestored.plugins.PluginLoader;
import com.timestored.sqldash.model.AppModel;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class SqlDashboardLauncher {
    private static SqlDashFrame appFrame;

    public static KeyedPrefs<SqldKey> getStoredPrefs() {
        return new KeyedPrefs<>("com.timestored.sqldashboard");
    }

    public static void main(String[] args) {
        PluginLoader.loadPlugins();

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final String title = "sqlDashboards";
                AppLaunchHelper.setMacAndWindowsAppearance(title);
                AppLaunchHelper.logToUsersFolder("sqlDash");

                KeyedPrefs<SqldKey> storedPrefs = getStoredPrefs();

                String websiteUrl = TimeStored.getContactUrl("sqlDashboards Error Report");
                final String email = "tech@timestored.com";
                final String emailTitle = "sqlDashboards Bug Report 1.41";
                final int minutesBetweenErrMessage = 720;
                ErrorReporter errRep = new ErrorReporter(websiteUrl, email, emailTitle, minutesBetweenErrMessage);
                Thread.setDefaultUncaughtExceptionHandler(errRep.getUncaughtExceptionHandler());

                AppModel appModel = new AppModel(ConnectionManager.newInstance());
                appModel.startQueryEngine();
                List<JdbcTypes> jdbcTypesSupported = Arrays.asList(JdbcTypes.values());
                appFrame = new SqlDashFrame(appModel, jdbcTypesSupported);
                appFrame.setExtendedState(6);
                appFrame.setVisible(true);
                if (args.length > 0) {
                    File f = new File(args[0]);
                    if (f.isFile() && f.exists()) {
                        appFrame.openFile(f);
                    } else {
                        JOptionPane.showMessageDialog(null, "No valid file was selected", "Error Opening File", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\SqlDashboardLauncher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */