package com.timestored.misc;

import com.timestored.theme.Icon;
import org.simplericity.macify.eawt.ApplicationListener;
import org.simplericity.macify.eawt.DefaultApplication;


public class Mac {
    public static void configureIfMac(ApplicationListener applicationListener, Icon icon) {
        if (System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0) {
            DefaultApplication defaultApplication = new DefaultApplication();
            defaultApplication.addApplicationListener(applicationListener);
            defaultApplication.setApplicationIconImage(icon.getBufferedImage());
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\misc\Mac.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */