package com.timestored.jgrowl;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JWindow;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

class FadingGrowler {
    private static final Logger LOG = Logger.getLogger(FadingGrowler.class.getName());
    private static ExecutorService executor;
    private final Theme theme;
    private final JFrame parentFrame;
    private final List<DisplayedItem> displayedItems = new ArrayList<DisplayedItem>();
    private int lastSeenY;

    public FadingGrowler(JFrame parentFrame, Theme theme) {
        this.parentFrame = parentFrame;
        this.lastSeenY = parentFrame.getY();
        this.theme = theme;

        parentFrame.addWindowFocusListener(new WindowFocusListener() {
            public void windowLostFocus(WindowEvent e) {
                synchronized (FadingGrowler.this.displayedItems) {
                    for (FadingGrowler.DisplayedItem d : FadingGrowler.this.displayedItems) {
                        d.frame.setAlwaysOnTop(false);
                    }
                }
            }

            public void windowGainedFocus(WindowEvent e) {
                synchronized (FadingGrowler.this.displayedItems) {
                    for (FadingGrowler.DisplayedItem d : FadingGrowler.this.displayedItems) {
                        d.frame.setAlwaysOnTop(true);
                    }
                }
            }
        });
    }

    private static void setAlpha(float alpha, JWindow win) {
        try {
            Class<?> awtutil = Class.forName("com.sun.awt.AWTUtilities");

            Method setWindowOpaque = awtutil.getMethod("setWindowOpacity", Window.class, float.class);
            setWindowOpaque.invoke(null, win, Float.valueOf(alpha));
        } catch (Exception ex) {
            win.getRootPane().putClientProperty("Window.alpha", new Float(alpha));
        }
    }

    private void startFadingThread() {
        if (executor == null) {
            synchronized (this) {
                if (executor == null) {
                    executor = Executors.newCachedThreadPool();
                    executor.execute(new Runnable() {
                                         public void run() {
                                             while (true) {
                                                 try {
                                                     while (true) {
                                                         Thread.sleep(FadingGrowler.this.theme.getFadeTimerDelay());
                                                         EventQueue.invokeLater(new Runnable() {
                                                             public void run() {
                                                                 synchronized (FadingGrowler.this.displayedItems) {
                                                                     FadingGrowler.this.updateDisplayItems();
                                                                 }
                                                             }
                                                         });
                                                     }
                                                     //break;
                                                 } catch (InterruptedException ex) {
                                                     LOG.log(Level.SEVERE, null, ex);
                                                 }
                                             }
                                         }
                                     }
                    );
                }
            }
        }
    }

    public synchronized void show(String message, String title, ImageIcon imageIcon, boolean sticky, Level logLevel) {
        this.startFadingThread();

        LOG.log(logLevel, title + ": " + message);
        if (sticky) {
            throw new UnsupportedOperationException("cant do sticky");
        }
        this.addItem(new Growl(message, title, imageIcon, sticky, logLevel));
    }

    private void addItem(Growl message) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                JWindow frame = FadingGrowler.this.theme.getWindow(message, FadingGrowler.this.parentFrame);
                synchronized (FadingGrowler.this.displayedItems) {
                    int top = FadingGrowler.this.parentFrame.getY() + FadingGrowler.this.theme.getTopSpacer();
                    if (FadingGrowler.this.displayedItems.size() > 0) {
                        JWindow lastFrame = (FadingGrowler.this.displayedItems.get(FadingGrowler.this.displayedItems.size() - 1)).frame;
                        top = lastFrame.getY() + lastFrame.getHeight() + FadingGrowler.this.theme.getSpaceBetweenItems();
                    }
                    frame.setLocation(FadingGrowler.this.theme.getLeftRuler(FadingGrowler.this.parentFrame), top);
                    FadingGrowler.this.displayedItems.add(new FadingGrowler.DisplayedItem(frame, message));
                }
            }
        });
    }

    private void updateDisplayItems() {
        int parentTop = this.parentFrame.getY() + this.theme.getTopSpacer();
        int parentYmove = 0;

        if (this.parentFrame.getY() != this.lastSeenY) {
            parentYmove = this.parentFrame.getY() - this.lastSeenY;
            this.lastSeenY = this.parentFrame.getY();
        }

        Iterator<DisplayedItem> it = this.displayedItems.iterator();
        int prevTop = parentTop;
        while (it.hasNext()) {
            DisplayedItem dispItem = it.next();
            JWindow fm = dispItem.frame;
            int y = fm.getY();

            Point p = fm.getMousePosition();
            if (p != null && fm.contains(p)) {
                dispItem.lifeLeft = 1.0F;
                setAlpha((dispItem.lifeLeft > 1.0D) ? 1.0F : dispItem.lifeLeft, fm);

                break;
            }
            if (!dispItem.message.isSticky()) {
                int fadeLevel = parentTop + Math.min(this.theme.getFadeRangeMinimum(), this.parentFrame.getHeight() / 2);

                if (y < fadeLevel) {
                    dispItem.lifeLeft -= this.theme.getFadeRate();
                }
            }

            if (y > prevTop) {
                y -= this.theme.getMoveSpeed();
            }
            prevTop = y + this.theme.getSpaceBetweenItems() + fm.getHeight();

            if (dispItem.lifeLeft > 0.0F) {
                setAlpha((dispItem.lifeLeft > 1.0D) ? 1.0F : dispItem.lifeLeft, fm);
                fm.setLocation(this.theme.getLeftRuler(this.parentFrame), y + parentYmove);
                continue;
            }
            it.remove();
            fm.setVisible(false);
        }
    }

    private static class DisplayedItem {
        private final Growl message;

        private final JWindow frame;
        private float lifeLeft;

        public DisplayedItem(JWindow frame, Growl message) {
            this.frame = frame;
            this.message = message;
            this.lifeLeft = 1.0F;
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\jgrowl\FadingGrowler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */