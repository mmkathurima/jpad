package com.timestored.connections;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.*;

class Msg {
    private static final String BUNDLE_NAME = "com.timestored.connections.messages";
    private static ResourceBundle rb;

    static {
        setLocale(Locale.getDefault());
    }

    public static String get(Key key) {
        return rb.getString(key.toString());
    }

    public static void setLocale(Locale locale) {
        rb = ResourceBundle.getBundle("com.timestored.connections.messages", locale);
    }

    static boolean checkAllKeysSpecified() {
        for (Key k : Key.values()) {
            if (get(k) == null || get(k).trim().length() < 1) {
                return false;
            }
        }
        return true;
    }

    static Set<String> getSuperfluosResourceBundleEntries() {
        Set<String> extraKeys = Sets.newHashSet();
        Enumeration<String> keys = rb.getKeys();
        while (keys.hasMoreElements()) {
            String k = keys.nextElement();
            if (Key.get(k) == null) {
                extraKeys.add(k);
            }
        }
        return extraKeys;
    }

    public enum Key {
        HOST, DATABASE, USERNAME, PASSWORD, ADD, SAVE, DELETE, CANCEL, PORT;

        private static final Map<String, Key> lookup = Maps.newHashMap();

        static {
            for (Key k : values()) lookup.put(k.toString(), k);
        }

        public static Key get(String k) {
            return lookup.get(k);
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\connections\Msg.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */