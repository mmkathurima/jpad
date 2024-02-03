package com.timestored.swingxx;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SaveableFrame
        extends JFrame {
    private static final Logger LOG = Logger.getLogger(SaveableFrame.class.getName());
    private static final long serialVersionUID = 1L;

    public SaveableFrame(Component cp, int width, int height) {
        this.setLayout(new BorderLayout());
        if (cp != null) {
            this.add(cp, "Center");
        }
        this.setSize(new Dimension(width, height));
        this.setVisible(true);
    }

    public static void saveComponentImage(Component a, int width, int height, File file, boolean includeWatermark) throws IOException {
        Dimension d = new Dimension(width, height);
        a.setSize(d);
        a.setPreferredSize(d);
        layoutComponent(a);

        BufferedImage bi = new BufferedImage(width, height, 2);
        Graphics2D g = bi.createGraphics();
        a.paint(g);

        if (includeWatermark) {
            BufferedImage waterMark = ImageIO.read(SaveableFrame.class.getResourceAsStream("/com/timestored/swingxx/timestored-small.png"));

            g.setComposite(AlphaComposite.getInstance(3, 0.5F));
            g.drawImage(waterMark, 50, 10, null);
        }

        g.dispose();

        ImageIO.write(bi, "png", file);
    }

    private static void layoutComponent(Component component) {
        synchronized (component.getTreeLock()) {
            component.doLayout();

            if (component instanceof Container) {
                for (Component child : ((Container) component).getComponents()) {
                    layoutComponent(child);
                }
            }
        }
    }

    public static void saveFrame(SaveableFrame f, File file) {
        saveFrame(f, file, false);
    }

    public static void saveFrame(SaveableFrame f, File file, boolean includeWatermark) {
        AtomicBoolean finished = new AtomicBoolean(false);

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                f.requestFocus();
                try {
                    saveComponentImage(f, (f.getSize()).width, (f.getSize()).height, file, false);
                } catch (IOException e) {
                    LOG.log(Level.SEVERE, "eframe.saveToFile(filepath)", e);
                }
                f.close();
                synchronized (finished) {
                    finished.set(true);
                    finished.notifyAll();
                }
            }
        });

        while (!finished.get()) {
            try {
                synchronized (finished) {
                    finished.wait();
                }
            } catch (InterruptedException e) {
            }
        }
    }

    public void close() {
        WindowEvent wev = new WindowEvent(this, 201);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\swingxx\SaveableFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */