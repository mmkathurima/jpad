package com.timestored.misc;

import com.google.common.base.Preconditions;
import com.timestored.theme.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ErrorReporter {
    private static final Logger LOG = Logger.getLogger(ErrorReporter.class.getName());

    private final String websiteUrl;

    private final String email;
    private final String emailTitle;
    private final long timeDelayBetweenReports;
    private long lastErrTime;

    public ErrorReporter(String websiteUrl, String email, String emailTitle, int delayBetweenReports) {
        Preconditions.checkNotNull(websiteUrl);
        Preconditions.checkNotNull(email);
        Preconditions.checkNotNull(emailTitle);
        Preconditions.checkArgument(websiteUrl.contains("?"));
        Preconditions.checkArgument((delayBetweenReports >= 0));

        this.websiteUrl = websiteUrl;
        this.email = email;
        this.emailTitle = emailTitle;

        this.timeDelayBetweenReports = (60000L * delayBetweenReports);
    }

    private static String getEncoded(String s, int maxUnencodedLength) {
        String t = s.substring(0, Math.min(s.length(), maxUnencodedLength));
        return URLEncoder.encode(t, StandardCharsets.UTF_8);
    }

    public UncaughtErrorReporter getUncaughtExceptionHandler() {
        return new UncaughtErrorReporter();
    }

    public void showReportErrorDialog(Throwable e, String description) {
        this.showReportErrorDialog(this.getErrDetails(e, description));
    }

    private String getErrDetails(Throwable e, String description) {
        String stackTrace = "..";
        if (e != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            stackTrace = sw.toString();
        }

        String version = System.getProperty("java.version");
        String os = System.getProperty("os.name");

        return "\r\n\r\nDetails:\r\n\r\nOS=" + ((os == null) ? "unknown" : os) + "\r\nJava=" + ((version == null) ? "unknown" : version) + "\r\nDes=" + ((description == null) ? "unknown" : description) + "\r\nStack=" + stackTrace;
    }

    public void showReportErrorDialog(String errDetails) {
        boolean enoughTimeDelay = (System.currentTimeMillis() - this.lastErrTime > this.timeDelayBetweenReports);

        if (enoughTimeDelay) {
            this.lastErrTime = System.currentTimeMillis();

            String msg = "An error occurred, to allow us to fix the problem please click report below which will contact us via the website or email  " + this.email + "\r\n\r\nTechnical Details:\r\n";

            JPanel b = new JPanel(new BorderLayout());

            b.add(Theme.getTextArea("repError", msg), "North");

            JTextArea errTA = Theme.getTextArea("errDetails", errDetails);
            errTA.setFont(new Font("Verdana", 1, 12));
            errTA.setForeground(Color.GRAY);
            errTA.setWrapStyleWord(false);
            errTA.setLineWrap(false);

            b.add(new JScrollPane(errTA), "Center");
            b.setPreferredSize(new Dimension(400, 300));

            String[] options = {"Report", "Close"};

            int choice = JOptionPane.showOptionDialog(null, b, "Error", 2, 0, Theme.CIcon.ERROR.get(), options, options[0]);

            if (choice == 0) {
                HtmlUtils.browse(this.websiteUrl + "&details=" + URLEncoder.encode(errDetails, StandardCharsets.UTF_8));
            }
        }
    }

    public Component getErrorReportLink(Throwable e, String description) {
        String txt = "" + description + "<br /><font color='red'>" + e.toString() + "<br /></font>";
        return this.getErrorReportLink(txt, this.getErrDetails(e, description));
    }

    public Component getErrorReportLink(String shortDescription, String errDetails) {
        String encodedSubject = getEncoded(this.emailTitle, 30);
        String encodedDetails = getEncoded(errDetails, 435);

        JEditorPane editPane = Theme.getHtmlText("<html>" + shortDescription + "<br />If you believe this is a bug contact:" + "<br /><a href='mailto:" + this.email + "?Subject=" + encodedSubject + "&Body=" + encodedDetails + "'>" + this.email + "</a> to report the problem please.</html>");

        JButton reportButton = new JButton("Report via Website", Theme.CIcon.TEXT_HTML.get16());

        reportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                HtmlUtils.browse(ErrorReporter.this.websiteUrl + "&details=" + encodedDetails);
            }
        });

        JPanel p = new JPanel(new BorderLayout());
        p.add(editPane, "Center");
        p.add(reportButton, "East");
        JPanel c = new JPanel();
        c.add(p);
        return c;
    }

    private class UncaughtErrorReporter implements Thread.UncaughtExceptionHandler {
        private UncaughtErrorReporter() {
        }

        public void uncaughtException(Thread t, Throwable e) {
            LOG.log(Level.WARNING, "uncaught error", e);
            ErrorReporter.this.showReportErrorDialog(e, null);
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\misc\ErrorReporter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */