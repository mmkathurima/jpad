package io.jpad;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class JPadLtd {
    public static final String TECH_EMAIL_ADDRESS = "tech@jpad.io";
    public static final String URL = "http://jpad.io";
    private static final String NICE_URL = "http://JPad.io";
    private static final String TRACKER_PARAMS = "?utm_source=jpad1&utm_medium=app&utm_campaign=jpad1";
    private static final Random R = ThreadLocalRandom.current();

    @NotNull
    public static String getRedirectPage(@NotNull String url, @Nullable String purpose) {
        return "http://jpad.io/r?" + encode("url", url) + encode("purpose", purpose) + "&source=jpad";
    }

    @NotNull
    private static String encode(@NotNull String name, @Nullable String val) {
        return (val == null) ? "" : ("&" + name + "=" + URLEncoder.encode(val, StandardCharsets.UTF_8));
    }

    @NotNull
    public static String getContactUrl(@Nullable String subject, @Nullable String details) {
        return "http://jpad.io/contact?" + encode("subject", subject) + encode("details", details);
    }

    @NotNull
    public static String getContactUrl(@Nullable String subject) {
        return getContactUrl(subject, null);
    }

    public enum Page {
        HELP("help"),
        REPL("repl"),
        CONTACT("contact");
        private final String loc;

        Page(String loc) {
            this.loc = loc;
        }

        public String niceUrl() {
            return "http://JPad.io/" + this.loc;
        }

        public String url() {
            return "http://jpad.io/" + this.loc + "?utm_source=jpad1&utm_medium=app&utm_campaign=jpad1";
        }

        public String toAnchor() {
            return "<a href='" + this.url() + "'>" + this.niceUrl() + "</a>";
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\JPadLtd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */