package com.timestored.misc;

import com.timestored.TimeStored;
import com.timestored.theme.Theme;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class InfoLink
        extends JLabel {
    private static final long serialVersionUID = 1L;

    public static JLabel getLabel(String title, String text, String webUrl, boolean showTitleText) {
        return getLabel(title, text, webUrl, addHttp(webUrl), showTitleText);
    }

    private static String addHttp(String webUrl) {
        boolean hasHttp = webUrl.toLowerCase().startsWith("http://");
        String actualUrl = hasHttp ? webUrl : ("http://" + webUrl);
        return actualUrl;
    }


    public static JLabel getLabel(String title, String text, TimeStored.Page webPage, boolean showTitleText) {
        return getLabel(title, text, webPage.niceUrl(), webPage.url(), true);
    }


    private static JLabel getLabel(String title, String text, String niceWebUrl, final String actualWebUrl, boolean showTitleText) {
        JLabel l = new JLabel(Theme.CIcon.INFO.get());
        if (text != null && text.length() > 0) {
            l.setToolTipText("<html><b>" + title + "</b>" + "<br><br>" + text + "<br><br><a href='" + actualWebUrl + "' >" + niceWebUrl + "</a></html>");
        }


        if (showTitleText) {
            l.setText(title);
        }

        if (HtmlUtils.isBrowseSupported()) {
            l.setCursor(Cursor.getPredefinedCursor(12));
            l.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    HtmlUtils.browse(actualWebUrl);
                }
            });
        }
        return l;
    }


    public static JButton getButton(String title, String text, TimeStored.Page webPage) {
        return getButton(title, text, webPage.niceUrl(), webPage.url());
    }


    private static JButton getButton(String title, String text, String niceWebUrl, final String actualWebUrl) {
        JButton b = new JButton(Theme.CIcon.INFO.get());
        if (text != null && text.length() > 0) {
            b.setToolTipText("<html><b>" + title + "</b>" + "<br><br>" + text + "<br><br><a href='" + actualWebUrl + "' >" + niceWebUrl + "</a></html>");
        }


        b.setText(title);


        if (HtmlUtils.isBrowseSupported()) {
            b.setCursor(Cursor.getPredefinedCursor(12));
            b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    HtmlUtils.browse(actualWebUrl);
                }
            });
        }
        return b;
    }


    public static void showMessageDialog(Component parent, String htmlBody, String title) {
        JLabel label = new JLabel();
        Font font = label.getFont();


        String style = "font-family:" + font.getFamily() + ";" + "font-weight:" + (font.isBold() ? "bold" : "normal") + ";" +
                "font-size:" + font.getSize() + "pt;";

        String html = "<html><body style=\"" + style + "\">" + htmlBody + "</body></html>";

        JEditorPane ep = Theme.getHtmlText(html);

        JOptionPane.showMessageDialog(parent, ep, title, 1);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\misc\InfoLink.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */