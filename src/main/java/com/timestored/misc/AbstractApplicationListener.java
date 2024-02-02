package com.timestored.misc;

import org.simplericity.macify.eawt.ApplicationEvent;
import org.simplericity.macify.eawt.ApplicationListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;


public abstract class AbstractApplicationListener
        implements ApplicationListener {
    private final JFrame frame;

    public AbstractApplicationListener(JFrame frame) {
        this.frame = frame;
    }


    public void handleReOpenApplication(ApplicationEvent event) {
    }

    public void handleQuit(ApplicationEvent event) {
        WindowEvent wev = new WindowEvent(this.frame, 201);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
    }

    public void handleOpenApplication(ApplicationEvent event) {
    }

    public void handlePrintFile(ApplicationEvent event) {
        JOptionPane.showMessageDialog(this.frame, "Sorry, printing not implemented");
    }

    public void handlePreferences(ApplicationEvent event) {
        JOptionPane.showMessageDialog(this.frame, "No settings necessary");
    }

    public void handleAbout(ApplicationEvent event) {
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\misc\AbstractApplicationListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */