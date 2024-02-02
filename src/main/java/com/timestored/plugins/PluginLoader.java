package com.timestored.plugins;

import com.timestored.misc.DynamicClassLoader;

import java.io.File;
import java.util.List;


public class PluginLoader {
    private static DatabaseAuthenticationService databaseAuthenticationService;

    public static boolean loadPlugins() {
        String curDir = System.getProperty("user.dir");
        curDir = curDir + "/libs";
        File dir = new File(curDir);
        List<DatabaseAuthenticationService> instances = DynamicClassLoader.loadInstances(dir, DatabaseAuthenticationService.class, true);
        if (!instances.isEmpty()) {
            databaseAuthenticationService = instances.get(0);
        }


        if (databaseAuthenticationService != null) {
            writePropIfNotExist("jdbc.isKDB", "true");
            writePropIfNotExist("jdbc.dbRequired", "false");
            writePropIfNotExist("jdbc.driver", "kx.jdbc");
            writePropIfNotExist("jdbc.urlFormat", "jdbc:q:@HOST@:@PORT@");
            writePropIfNotExist("jdbc.niceName", "Kdb with " + databaseAuthenticationService.getName());
            writePropIfNotExist("jdbc.authenticator", databaseAuthenticationService.getClass().getCanonicalName());
        }

        return (databaseAuthenticationService != null);
    }


    private static void writePropIfNotExist(String key, String value) {
        if (System.getProperty(key) == null)
            System.setProperty(key, value);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\plugins\PluginLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */