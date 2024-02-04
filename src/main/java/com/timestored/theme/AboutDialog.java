package com.timestored.theme;

import com.timestored.misc.InfoLink;

import javax.swing.*;
import java.awt.Container;
import java.awt.Dimension;

public class AboutDialog
        extends JDialog {
    private static final long serialVersionUID = 1L;

    public AboutDialog(JFrame parentFrame, String title, Icon icon, String htmlTitle, String version, String licenseText) {
        super(parentFrame, title);
        this.setIconImage(icon.get().getImage());

        JPanel logoPanel = new JPanel();
        logoPanel.add(new JLabel(icon.get()));
        logoPanel.add(Theme.getHtmlText(htmlTitle));
        logoPanel.setAlignmentX(0.5F);

        JLabel label = new JLabel("<html><h4>Version: " + version + "</h4></html>");
        label.setHorizontalAlignment(0);
        final String txt = "Homepage: TimeStored.com";

        JPanel timestoredLinkPanel = Theme.getVerticalBoxPanel();
        timestoredLinkPanel.add(label);
        timestoredLinkPanel.add(InfoLink.getLabel(txt, txt, "http://www.timestored.com", true));
        timestoredLinkPanel.setAlignmentX(0.5F);

        Container cp = this.getContentPane();

        cp.setLayout(new BoxLayout(cp, BoxLayout.PAGE_AXIS));
        cp.add(logoPanel);
        cp.add(timestoredLinkPanel);

        if (licenseText != null && licenseText.trim().length() > 0) {
            cp.add(new JLabel("License: "));
            JTextArea ta = Theme.getTextArea("asd", licenseText);
            ta.setBorder(BorderFactory.createLoweredBevelBorder());
            JScrollPane sp = new JScrollPane(ta);
            sp.setPreferredSize(new Dimension(300, 100));
            sp.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            sp.setVerticalScrollBarPolicy(22);
            sp.setHorizontalScrollBarPolicy(31);
            cp.add(sp);
        }
        this.pack();
        this.setLocationRelativeTo(parentFrame);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\theme\AboutDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */