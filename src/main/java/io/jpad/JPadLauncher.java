package io.jpad;

import com.timestored.TimeStored;
import com.timestored.docs.OpenDocumentsModel;
import com.timestored.misc.AppLaunchHelper;
import com.timestored.misc.ErrorReporter;
import com.timestored.swingxx.ApplicationInstanceListener;
import com.timestored.swingxx.ApplicationInstanceManager;
import com.timestored.swingxx.SwingUtils;
import io.jpad.model.JEngine;
import io.jpad.scratch.JPad;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.SplashScreen;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StreamTokenizer;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;


public class JPadLauncher {
    private static final Logger LOG = Logger.getLogger(JPadLauncher.class.getName());
    private static final Color BLUE_LOGO_BG = new Color(0, 124, 195);
    private static final String ERR_URL = TimeStored.getContactUrl("qStudio Error Report");
    public static final ErrorReporter ERR_REPORTER = new ErrorReporter(ERR_URL, "tech@timestored.com", "JPad Bug Report 1.07", 720);
    private static final int MINS_BETWEEN = 720;
    private static JPadFrame appFrame;
    private static OpenDocumentsModel openDocumentsModel;

    public static void main(String... args) throws InterruptedException, InvocationTargetException {
        JPad.isGUI = true;


        boolean firstInstance = ApplicationInstanceManager.registerInstance(args);
        if (!firstInstance) {
            System.out.println("Not the first instance.");

            if (args.length > 0) {

                System.out.println("I had arguments, they were handled so EXIT.");

                return;
            }
        }
        openDocumentsModel = OpenDocumentsModel.newInstance();


        ApplicationInstanceManager.setApplicationInstanceListener(new ApplicationInstanceListener() {
            public void newInstanceCreated(List<String> args) {
                System.out.println("New instance detected...");
                if (args.size() > 0 && JPadLauncher.appFrame != null) {
                    JPadLauncher.handleArgs(args);
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            SwingUtils.forceToFront(JPadLauncher.appFrame);
                        }
                    });
                }
            }
        });

        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                try {
                    JPadLauncher.launch(args);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }


    private static void launch(String... args) throws IOException {
        String title = "JPad";

        AppLaunchHelper.setMacAndWindowsAppearance(title);
        AppLaunchHelper.logToUsersFolder("JPad" + File.separator + "logs");
        LOG.info("Starting JPadLauncher  launch() ###################################");

        Thread.setDefaultUncaughtExceptionHandler(ERR_REPORTER.getUncaughtExceptionHandler());


        JDialog dialog = null;
        if (SplashScreen.getSplashScreen() == null) {
            URL r = JPadLauncher.class.getResource("splash.png");
            String u = JPPersistance.INSTANCE.get(JPPersistance.Key.USERNAME, "");
            dialog = SwingUtils.showSplashDialog(r, BLUE_LOGO_BG, "http://jpad.io   " + u);
        }

        JEngine jEngine = new JEngine();

        appFrame = new JPadFrame(openDocumentsModel, jEngine);
        appFrame.setExtendedState(6);
        appFrame.setVisible(true);


        if (dialog != null) {
            dialog.setVisible(false);
            dialog.dispose();
        }

        handleArgs(Arrays.asList(args));
        /*System.setIn(new InputStream() {
            private final StringBuilder buffer = new StringBuilder();

            @Override
            public int read() {
                if (this.buffer.length() == 0) {
                    this.buffer.append(JOptionPane.showInputDialog("User input:"));
                    this.buffer.append("\n");
                } else return -1;

                char charToRead = this.buffer.charAt(0);
                this.buffer.deleteCharAt(0);
                System.out.print(charToRead);
                return charToRead;
            }
        });*/
        LOG.info("Finished JPadLauncher  launch()");
    }


    private static void handleArgs(List<String> args) {
        openDocumentsModel.openDocuments(args);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\JPadLauncher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */