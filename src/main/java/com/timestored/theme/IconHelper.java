package com.timestored.theme;

import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;


public class IconHelper {
    public static BufferedImage getBufferedImage(ImageIcon ii) {
        BufferedImage bi = new BufferedImage(ii.getIconWidth(), ii.getIconHeight(), 2);


        Graphics g = bi.createGraphics();

        ii.paintIcon(null, g, 0, 0);
        g.dispose();
        return bi;
    }


    public static ImageIcon[] getDiffSizesOfIcon(URL resourceUrl) {
        ImageIcon ii = null;
        ImageIcon ii16 = null;
        ImageIcon ii32 = null;
        try {
            ii = new ImageIcon(resourceUrl);
            Image i = ii.getImage();
            ii16 = new ImageIcon(i.getScaledInstance(16, 16, 16));
            ii32 = new ImageIcon(i.getScaledInstance(32, 32, 16));
        } catch (Exception e) {
        }


        return new ImageIcon[]{ii, ii16, ii32};
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\theme\IconHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */