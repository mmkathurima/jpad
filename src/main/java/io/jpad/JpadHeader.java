package io.jpad;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

public class JpadHeader {
    private static final String PREFIX = "http://jpad.io/example/";
    private static final String STARTER = "/*";
    private static final String ENDER = "*/";
    @NotNull
    private final String title;
    @NotNull
    private final String description;
    @NotNull
    private final String tags;
    @NotNull
    private final String url;

    private JpadHeader(Snip snip, String url) {
        this.title = snip.getTitle();
        this.description = snip.getDescription();
        this.tags = snip.getTags();
        this.url = url;
    }

    private JpadHeader(String jpadCode) {
        String aTitle = "";
        String aDescription = "";
        String aTags = "";
        String aUrl = "";

        String pc = jpadCode.trim();
        if (pc.startsWith("/*")) {
            int end = pc.indexOf("*/");
            if (end == -1) {
                end = pc.length();
            }
            String[] comments = pc.substring(2, end).split("\n");
            for (String c : comments) {
                c = c.trim();
                if (c.startsWith("*")) {
                    c = c.substring(1).trim();
                }
                int div = c.indexOf(' ');
                if (div > -1) {
                    String tag = c.substring(0, div).toLowerCase();
                    String val = c.substring(div).trim();
                    switch (tag) {
                        case "@title":
                            aTitle = val;
                            break;
                        case "@description":
                            aDescription = val;
                            break;
                        case "@tags":
                            aTags = val;
                            break;
                        case "@url":
                            aUrl = val;
                            break;
                    }
                }
            }
        }
        if (pc.contains("http://jpad.io/example/")) {
            int st = pc.indexOf("http://jpad.io/example/");
            aUrl = pc.substring(st).split("[\\s\\*]+")[0];
        }

        if (aTitle.isEmpty() && !aUrl.isEmpty()) {
            String[] parts = aUrl.substring("http://jpad.io/example/".length()).split("/");
            if (parts.length > 1) {
                aTitle = parts[1];
            }
        }

        this.title = aTitle;
        this.description = aDescription;
        this.tags = aTags;
        this.url = aUrl;
    }

    public static JpadHeader extract(String jpadCode) {
        return new JpadHeader(jpadCode);
    }

    public static JpadHeader build(Snip snip, String url) {
        return new JpadHeader(snip, url);
    }

    private static String getTag(String title, String val) {
        if (!val.isEmpty()) {
            return " * @" + title + " " + val + '\n';
        }
        return "";
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof JpadHeader)) return false;
        JpadHeader other = (JpadHeader) o;
        if (!other.canEqual(this)) return false;
        if (!Objects.equals(this.title, other.title)) return false;
        if (!Objects.equals(this.description, other.description))
            return false;
        if (!Objects.equals(this.tags, other.tags)) return false;
        return Objects.equals(this.url, other.url);
    }

    protected boolean canEqual(Object other) {
        return other instanceof JpadHeader;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        Object $title = this.title;
        result = result * 59 + (($title == null) ? 0 : $title.hashCode());
        Object $description = this.description;
        result = result * 59 + (($description == null) ? 0 : $description.hashCode());
        Object $tags = this.tags;
        result = result * 59 + (($tags == null) ? 0 : $tags.hashCode());
        Object $url = this.url;
        return result * 59 + (($url == null) ? 0 : $url.hashCode());
    }

    public String toString() {
        return "JpadHeader(title=" + this.title + ", description=" + this.description + ", tags=" + this.tags + ", url=" + this.url + ")";
    }

    @NotNull
    public String getTitle() {
        return this.title;
    }

    @NotNull
    public String getDescription() {
        return this.description;
    }

    @NotNull
    public String getTags() {
        return this.tags;
    }

    @NotNull
    public String getUrl() {
        return this.url;
    }

    Optional<String> getSnipID() {
        if (this.url.startsWith("http://jpad.io/example/")) {
            String s = this.url.substring("http://jpad.io/example/".length());
            int p = s.indexOf('/');
            if (p > -1) {
                s = s.substring(0, p);
            }
            return Optional.of(s);
        }
        return Optional.empty();
    }

    String getAsComment() {
        final String N = "\n";
        return "/**" + "\n" +
                getTag("title", this.title) +
                getTag("description", this.description) +
                getTag("tags", this.tags) +
                getTag("url", this.url) +
                " */";
    }

    public String replaceComment(String jpadCodeWithOptionalHeaderComment) {
        if (jpadCodeWithOptionalHeaderComment.startsWith("/*")) {
            int end = jpadCodeWithOptionalHeaderComment.indexOf("*/");
            if (end > -1) {
                return this.getAsComment() + jpadCodeWithOptionalHeaderComment.substring(end + "*/".length());
            }
        }

        String space = "";
        if (jpadCodeWithOptionalHeaderComment.charAt(0) != '\n' && jpadCodeWithOptionalHeaderComment.charAt(1) != '\n') {
            space = "\n";
        }

        return this.getAsComment() + space + jpadCodeWithOptionalHeaderComment;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\JpadHeader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */