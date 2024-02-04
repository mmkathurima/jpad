package com.timestored.misc;

import com.google.common.base.Joiner;
import com.timestored.TimeStored;
import com.timestored.theme.Theme;

import javax.swing.AbstractAction;
import javax.swing.Action;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HtmlUtils {
    public static final String END = "</body></html>";
    public static final String START = "<html><body>";
    private static final Logger LOG = Logger.getLogger(HtmlUtils.class.getName());
    private static final boolean browseSupported;

    private static final String HEAD_PRE = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" ><head><meta http-equiv=\"content-type\" content=\"text/html; charset=iso-8859-1\" />";

    private static final String FAVICON_LINK = "<link rel=\"shortcut icon\" type=\"image/png\" href=\"http://www.timestored.com/favicon.png\" />";
    private static int counter = 13;

    static {
        boolean browsey = false;
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                browsey = true;
            }
        }
        browseSupported = browsey;
    }

    public static String getHead(String title, String headContent) {
        return "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" ><head><meta http-equiv=\"content-type\" content=\"text/html; charset=iso-8859-1\" /><title>" + title + "</title>" + headContent + "</head><body>";
    }

    public static String getTail() {
        return "</body></html>";
    }

    public static String getTSPageHead(String title, String subTitleLink, String headContent, boolean withTSFavicon) {

        return getHead(title + " - TimeStored.com", headContent + "<link rel=\"shortcut icon\" type=\"image/png\" href=\"http://www.timestored.com/favicon.png\" />") +
                "\r\n<div id='wrap'><div id='page'>" +
                "\r\n<div id='header'><h2>" + subTitleLink + " - " + "<a target='a' href='" + "http://www.timestored.com" + "'>TimeStored.com</a></h2></div>" +
                "\r\n<div id='main'>";
    }

    public static String getTSPageTail(String subTitleLink) {

        return "\r\n</div>" +
                "<div id='footer'> <p>&copy; 2013 " +
                subTitleLink +
                " | <a target='a' href='http://www.timestored.com'>TimeStored.com</a> | <a target='a' href='" + TimeStored.Page.TRAINING.url() + "'>KDB Training</a>" + " | <a target='a' href='" + TimeStored.Page.CONSULTING.url() + "'>KDB Consulting</a>" + " | <a target='a' href='" + TimeStored.Page.CONTACT.url() + "'>Contact Us</a></p>" + "</div>" +
                "\r\n</div></div>\r\n" +
                "</body></html>";
    }

    public static String toList(List<String> items) {
        return "<ul><li>" + Joiner.on("</li><li>").join(items) + "</li></ul>";
    }

    public static String toList(String... items) {
        return "<ul><li>" + Joiner.on("</li><li>").join(items) + "</li></ul>";
    }

    public static String toList(Map<String, String> smap, boolean hideEmpty) {
        if (smap.isEmpty()) {
            return "";
        }
        return "<ul>" + expandMapToHtml(smap, hideEmpty, "<li><strong>", "</strong> - ", "", "</li>") + "</ul>";
    }

    public static String toDefinitions(Map<String, String> smap, boolean hideEmpty) {
        if (smap.isEmpty()) {
            return "";
        }
        return "<dl>" + expandMapToHtml(smap, hideEmpty, "<dt>", "</dt>", "<dl>", "</dl>") + "</dl>";
    }

    public static String toTable(Map<String, String> smap, boolean hideEmpty) {
        if (smap.isEmpty()) {
            return "";
        }
        return "<table>" + expandMapToHtml(smap, hideEmpty, "<tr><th>", "</th>", "<td>", "</td></tr>") + "</table>";
    }

    private static String expandMapToHtml(Map<String, String> smap, boolean hideEmpty, String preKey, String postKey, String preVal, String postVal) {
        StringBuilder sb = new StringBuilder();
        List<String> keyList = new ArrayList<>(smap.keySet());
        Collections.sort(keyList);

        for (String key : keyList) {
            String val = smap.get(key);
            if (hideEmpty && (val == null || val.trim().isEmpty())) {
                continue;
            }
            sb.append(preKey).append(key).append(postKey);
            sb.append(preVal).append(val + postVal);
        }

        return sb.toString();
    }

    public static String extractBody(String htmlDoc) {
        if (htmlDoc != null && !htmlDoc.trim().isEmpty()) {
            String t = getTextInsideTag(htmlDoc, "body");
            if (t == null) t = getTextInsideTag(htmlDoc, "html");
            if (t == null) t = htmlDoc;
            return t;
        }
        return "";
    }

    private static String getTextInsideTag(String htmlDoc, String tag) {
        String b = "<" + tag + ">";
        int st = htmlDoc.indexOf(b);
        int end = htmlDoc.lastIndexOf("</" + tag + ">");
        if (st == -1 || end == -1) {
            return null;
        }
        return htmlDoc.substring(st + b.length(), end);
    }

    public static void browse(String url) {
        if (browseSupported) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                LOG.log(Level.WARNING, "couldn't open browser", e);
            }
        }
    }

    public static boolean isBrowseSupported() {
        return browseSupported;
    }

    public static Action getWWWaction(String title, String url) {
        return new WwwAction(title, url);
    }

    public static void appendQCodeArea(StringBuilder sb, String code) {
        sb.append("\r\n<textarea rows='2' cols='80' class='code' id='code");
        sb.append(counter);
        sb.append("'>");
        sb.append(code);
        sb.append("</textarea> <script type='text/javascript'>CodeMirror.fromTextArea(document.getElementById('code");

        sb.append(counter);
        sb.append("'),  {  lineNumbers: true, matchBrackets: true,  mode: \"text/x-plsql\", readOnly:true });</script>");

        counter++;
    }

    public static String getXhtmlTop(String title) {
        return "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" ><head>\r\n<meta http-equiv=\"content-type\" content=\"text/html; charset=iso-8859-1\" />\r\n<title>" + title + "</title></head><body>";
    }

    public static String getXhtmlBottom() {
        return " </body></html>";
    }

    public static String getTSTemplateTop(String title) {
        return "<?php include 'Template.php'; ?><?php echo Template::getTop(\"" + title + "\", \"codemirror.js\", \"codemirror.css\"); ?>" + "<div id='header-small'></div><div id='main'>";
    }

    public static String getTSTemplateBottom() {
        return "</div><?php echo Template::getBottom(); ?>";
    }

    public static String escapeHTML(String txt) {
        StringBuilder out = new StringBuilder(Math.max(16, txt.length()));
        return appendEscapedHtml(out, txt).toString();
    }

    public static StringBuilder appendEscapedHtml(StringBuilder sb, String txt) {
        boolean previousWasASpace = false;
        for (char c : txt.toCharArray()) {
            if (c == ' ') {
                if (previousWasASpace) {
                    sb.append("&nbsp;");
                    previousWasASpace = false;
                } else {
                    previousWasASpace = true;

                    switch (c) {
                        case '<':
                            sb.append("&lt;");
                            break;
                        case '>':
                            sb.append("&gt;");
                            break;
                        case '&':
                            sb.append("&amp;");
                            break;
                        case '"':
                            sb.append("&quot;");
                            break;
                        case '\n':
                            sb.append("\n<br />");
                            break;
                        case '\t':
                            sb.append("&nbsp; &nbsp; &nbsp;");
                            break;
                        default:
                            if (c < '') {
                                sb.append(c);
                                break;
                            }
                            sb.append("&#").append(c).append(";");
                            break;
                    }
                }
            } else {
                previousWasASpace = false;
                switch (c) {
                    case '<':
                        sb.append("&lt;");
                        break;
                    case '>':
                        sb.append("&gt;");
                        break;
                    case '&':
                        sb.append("&amp;");
                        break;
                    case '"':
                        sb.append("&quot;");
                        break;
                    case '\n':
                        sb.append("\n<br />");
                        break;
                    case '\t':
                        sb.append("&nbsp; &nbsp; &nbsp;");
                        break;
                    default:
                        if (c < '') {
                            sb.append(c);
                            break;
                        }
                        sb.append("&#").append(c).append(";");
                        break;
                }
            }
        }
        return sb;
    }

    public static String cleanAtt(String name) {
        return name.replace("'", "&lsquo;");
    }

    public static String clean(String s) {
        return s.trim().replace(",", "-").replace(":", "-").replace(" ", "-").replace("'", "-").toLowerCase();
    }

    public static void appendImage(StringBuilder sb, String imgFilename, String alt, int height, int width) {
        sb.append("<img src='").append(imgFilename).append("'");
        if (alt != null && alt.trim().length() > 0) {
            sb.append(" alt='").append(cleanAtt(alt)).append("' ");
        }
        sb.append(" height='").append(height).append("' width='").append(width).append("'");

        sb.append(" />");
    }

    private static class WwwAction
            extends AbstractAction {
        private final String url;

        WwwAction(String title, String url) {
            super(title, Theme.CIcon.TEXT_HTML.get16());
            this.url = url;
            this.setEnabled(isBrowseSupported());
        }

        public void actionPerformed(ActionEvent e) {
            browse(this.url);
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\misc\HtmlUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */