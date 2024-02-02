package com.timestored.tscore.persistance;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public interface PersistanceInterface {
    String PATH_SPLIT = ";";

    void put(KeyInterface paramKeyInterface, String paramString);

    String get(KeyInterface paramKeyInterface, String paramString);

    void putBoolean(KeyInterface paramKeyInterface, boolean paramBoolean);

    boolean getBoolean(KeyInterface paramKeyInterface, boolean paramBoolean);

    void putInt(KeyInterface paramKeyInterface, int paramInt);

    int getInt(KeyInterface paramKeyInterface, int paramInt);

    void putLong(KeyInterface paramKeyInterface, long paramLong);

    long getLong(KeyInterface paramKeyInterface, long paramLong);

    void clear(boolean paramBoolean) throws BackingStoreException;

    Preferences getPref();
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\tscore\persistance\PersistanceInterface.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */