package io.jpad;

import com.timestored.tscore.persistance.KeyInterface;
import com.timestored.tscore.persistance.PersistanceInterface;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;


public enum JPPersistance
        implements PersistanceInterface {
    INSTANCE;
    private static final Preferences PREF;

    static {
        PREF = Preferences.userNodeForPackage(JPPersistance.class);
    }

    boolean isLoginSet() {
        return (get(Key.USERNAME, "").length() > 0 && get(Key.PASSWORD, "").length() > 0);
    }

    public void put(KeyInterface key, String value) {
        PREF.put(key.name(), value);
    }

    public String get(KeyInterface key, String def) {
        return PREF.get(key.name(), def);
    }

    public void putBoolean(KeyInterface key, boolean value) {
        PREF.putBoolean(key.name(), value);
    }

    public boolean getBoolean(KeyInterface key, boolean def) {
        return PREF.getBoolean(key.name(), def);
    }

    public void putInt(KeyInterface key, int value) {
        PREF.putInt(key.name(), value);
    }

    public int getInt(KeyInterface key, int def) {
        return PREF.getInt(key.name(), def);
    }

    public void putLong(KeyInterface key, long val) {
        PREF.putLong(key.name(), val);
    }

    public long getLong(KeyInterface key, long def) {
        return PREF.getLong(key.name(), def);
    }

    public void clear(boolean wipeLicense) throws BackingStoreException {
        if (wipeLicense) {
            PREF.clear();
        }
        for (Key k : Key.values()) {
            if (!k.equals(Key.FERD)) {
                PREF.remove(k.name());
            }
        }
    }

    public Preferences getPref() {
        return PREF;
    }

    public enum Key implements KeyInterface {
        WINDOW_POSITIONS, OPEN_DOCS, FRAME_HEIGHT, FRAME_WIDTH, RECENT_DOCS,
        FIRST_OPEN, LAST_AD, LAST_OPENED_FOLDER,
        SIGNED_LICENSE, USERNAME, PASSWORD,

        FERD
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\JPPersistance.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */