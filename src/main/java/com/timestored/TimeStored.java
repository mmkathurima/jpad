package com.timestored;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Random;


public class TimeStored {
    public static final String TECH_EMAIL_ADDRESS = "tech@timestored.com";
    public static final String URL = "http://www.timestored.com";
    public static final String PUBLIC_LICENSE_KEY = "MIHwMIGoBgcqhkjOOAQBMIGcAkEA/KaCzo4Syrom78z3EQ5SbbB4sF7ey80etKII864WF64B81uRpH5t9jQTxeEu0ImbzRMqzVDZkVG9xD7nN1kuFwIVAJYu3cw2nLqOuyYO5rahJtk0bjjFAkBnhHGyepz0TukaScUUfbGpqvJE8FpDTWSGkx0tFCcbnjUDC3H9c9oXkGmzLik1Yw4cIGI1TQ2iCmxBblC+eUykA0MAAkBDhrGYHGc6FoB5ljIjrrRPST5alK4SAzxEMZrxvcqb8OSGyz3G8VD1vNV2Aa9XoUqZPizlgBPylDMjUl7nnqoz";
    private static final String NICE_URL = "http://www.TimeStored.com";
    private static final String TRACKER_PARAMS = "?utm_source=qstudio&utm_medium=app&utm_campaign=qstudio";
    private static final Random R = new Random();
    private static final String[] LINKS = new String[]{"<a href='" + Page.TRAINING.url() + "'>Perhaps some training would help... </a>", "<a href='" + Page.TRAINING.url() + "'>TimeStored.com - Provide onsite and public kdb training courses.</a>", "<a href='" + Page.CONSULTING.url() + "'>TimeStored.com - Offer Kdb Consultants of various experience levels at competitive rates.</a>", "<a href='" + Page.TRAINING.url() + "'>TimeStored offer Kdb Training and Consulting services worldwide.</a>", "<a href='" + Page.CONSULTING.url() + "'>Need architectural advice or guidance on system design? TimeStored provide kdb experts.</a>", "TimeStored.com has <a href='" + Page.TUTORIALS.url() + "'>Free Kdb Video Tutorials.</a>"};
    private static final String[] ADVERTS = new String[]{"<html><p>Try pressing <b>Ctrl+P</b></p><p>It brings up a <a href='" + Page.COMMAND_BAR_HELP.url() + "'>command bar</a> that allows smart matching on:</p>" + "<ul>" + "<li>Server name to change your selected server.</li>" + "<li>File names in the File Tree to open that document.</li>" + "</ul>" + "</html>", "<html><p>TimeStored provide a three day <a href='" + Page.TRAINING_INTRO.url() + "'>Introduction to Kdb Training course</a>:</p>" + "<p>It is a practical introduction to Kdb+ from KX. Intended for those that have no prior experience and " + "want to become competent at selecting data, writing scripts and creating functions. " + "From the philosophy behind kdb, through the core building-blocks and up to " + "writing advanced analytics that use parallel processing we will help you master kdb. " + "On completion, a comprehensive set of notes, examples, exercises and example scripts " + "are provided to take away.</p>" + "</html>", "<html><p>Check out our <a href='" + Page.QSTUDIO_PRO.url() + "'>advanced features</a> for:</p>" + "<ul>" + "<li><a href='" + Page.QSTUDIO_HELP_DBMANAGE.url() + "'>Database Management</a></li>" + "<li><a href='" + Page.QSTUDIO_HELP_LOADCSV.url() + "'>Loading CSV's</a></li>" + "<li><a href='" + Page.QSTUDIO_HELP_QUNIT.url() + "'>Unit Testing</a></li>" + "</ul>" + "</html>", "<html><p>TimeStored provide <a href='" + Page.TRAINING_CUSTOM.url() + "'>customized onsite training courses</a>:</p>" + "<p>Our instructors can come on site and tailor a course specifically to meet the needs of your team. " + "You can choose a list of modules from our full course outline or " + "if you have something else entirely you want covered we have an extensive range " + "of in-house expertise that covers many areas.</p>" + "</html>", "<html><p><a href='" + Page.TUTORIALS.url() + "'>Free Kdb video tutorials</a> are available on:</p>" + "<ul>" + "<li><a href='" + Page.TUTE_IPC.url() + "'>Inter-Process Communication</a> - client/server, (a)synchronous, message handlers.</li>" + "<li><a href='" + Page.TUTE_PEACH.url() + "'>Parallel Processing</a> - peach and .Q.fc</li>" + "<li><a href='" + Page.TUTE_PYTHON.url() + "'>Using Kdb from Python.</a></li>" + "<li><a href=" + Page.TUTE_DEBUG.url() + "'>Debugging in Kdb</a> - breakpoints, suspending on client errors, debug outputting.</li>" + "</ul>" + "</html>"};

    public static String getContactUrl(String subject, String details) {
        String d = "";
        String s = "";

        s = (subject == null) ? "" : ("&subject=" + URLEncoder.encode(subject, StandardCharsets.UTF_8));
        d = (details == null) ? "" : ("&details=" + URLEncoder.encode(details, StandardCharsets.UTF_8));

        return "http://www.timestored.com/contact?" + s + d;
    }

    public static String getContactUrl(String subject) {
        return getContactUrl(subject, null);
    }

    public static String getRandomAdvertLink() {
        return LINKS[R.nextInt(LINKS.length)];
    }

    public static String getRandomLongAdvertHtml() {
        return ADVERTS[R.nextInt(ADVERTS.length)];
    }

    public static String getRedirectPage(String url, String purpose) {
        return "http://www.timestored.com/r?url=" + URLEncoder.encode(url) + "&source=qstudio&purpose=" + URLEncoder.encode(purpose);
    }


    public enum Page {
        TRAINING("kdb-training"),
        CONSULTING("consulting"),
        TUTORIALS("kdb-guides"),
        TUTE_IPC("kdb-guides/interprocess-communication"),
        TUTE_PEACH("kdb-guides/parallel-peach"),
        TUTE_PYTHON("kdb-guides/python-api"),
        TUTE_DEBUG("kdb-guides/debugging-kdb"),
        TUTE_HARDWARE("kdb-guides/hardware-planning"),
        TUTE_MEM("kdb-guides/memory-management"),
        QSTUDIO("qstudio"),
        QSTUDIO_PRO("qstudio/pro"),
        QSTUDIO_HELP("qstudio/help"),
        QSTUDIO_BUY("qstudio/buy"),
        QSTUDIO_REGISTER("qstudio/register"),
        QSTUDIO_HELP_USER("qstudio/help/user-permissions"),
        QSTUDIO_HELP_DBMANAGE("qstudio/help/database-management"),
        QSTUDIO_HELP_LOADCSV("qstudio/help/load-csv-data-file-into-kdb"),
        QSTUDIO_HELP_QUNIT("qstudio/help/qunit"),
        SQLDASH("sqldashboards"),
        SQLDASH_HELP_EG("sqldashboards/help/chart-examples"),
        SQLDASH_BUY("sqldashboards/buy"),
        QDOC("qstudio/help/qdoc"),
        TRAINING_INTRO("kdb-training/intro-to-kdb"),
        COMMAND_BAR_HELP("qstudio/help/keyboard-shortcuts"),
        TRAINING_CUSTOM("kdb-training/customized-onsite-course"),
        CONTACT("contact"),
        QMODULES("kdb-modules"),
        NEWS("qStudioNews"),
        QUNIT_HELP("kdb-guides/kdb-regression-unit-tests");

        private final String loc;

        Page(String loc) {
            this.loc = loc;
        }


        public String niceUrl() {
            return "http://www.TimeStored.com/" + this.loc;
        }

        public String url() {
            return "http://www.timestored.com/" + this.loc + "?utm_source=qstudio&utm_medium=app&utm_campaign=qstudio";
        }

        public String toAnchor() {
            return "<a href='" + url() + "'>" + niceUrl() + "</a>";
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\TimeStored.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */