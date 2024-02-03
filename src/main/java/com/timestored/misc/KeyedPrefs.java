package com.timestored.misc;

import java.util.prefs.Preferences;

public class KeyedPrefs<Key extends Enum<Key>> {
    private final Preferences pref;

    public KeyedPrefs(String nodeName) {
        this.pref = Preferences.userRoot().node(nodeName);
    }

    public void put(Key key, String value) {
        this.pref.put(key.name(), value);
    }

    public String get(Key key, String def) {
        return this.pref.get(key.name(), def);
    }

    public void putBoolean(Key key, boolean value) {
        this.pref.putBoolean(key.name(), value);
    }

    public boolean getBoolean(Key key, boolean def) {
        return this.pref.getBoolean(key.name(), def);
    }

    public void putInt(Key key, int value) {
        this.pref.putInt(key.name(), value);
    }

    public int getInt(Key key, int def) {
        return this.pref.getInt(key.name(), def);
    }

    public void putLong(Key key, long val) {
        this.pref.putLong(key.name(), val);
    }

    public long getLong(Key key, long def) {
        return this.pref.getLong(key.name(), def);
    }

    public Preferences getPref() {
        return this.pref;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\misc\KeyedPrefs.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */