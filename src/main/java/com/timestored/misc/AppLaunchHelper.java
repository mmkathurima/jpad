package com.timestored.misc;

import com.google.common.io.Files;

import javax.swing.UIManager;
import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class AppLaunchHelper {
    private static final Logger LOG = Logger.getLogger(AppLaunchHelper.class.getName());

    public static void setMacAndWindowsAppearance(String title) {
        if (System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", title);
        }

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
        }
    }

    public static void logToUsersFolder(String folder) {
        try {
            String p = System.getProperty("user.home") + File.separator + folder + File.separator + "a";

            Files.createParentDirs(new File(p));
            FileHandler fh = new FileHandler("%h/" + folder + "/log%g.log", 1048576, 1, true);
            fh.setFormatter(new SimpleFormatter());
            Logger.getLogger("").addHandler(fh);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        } catch (SecurityException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\misc\AppLaunchHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */