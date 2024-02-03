package io.jpad;

import java.io.File;

public class JPadConfig {
    public static final String VERSION = "1.07";
    public static final File APP_HOME = new File(System.getProperty("user.home") + File.separator + "JPad".toLowerCase());
    public static final String FILE_EXTENSION = "jpad";
    static final String APP_TITLE = "JPad";
    static final File PLUGINS_FOLDER = new File(APP_HOME, "plugins");
    public static File SCRIPTS_FOLDER = new File(APP_HOME, "scripts");
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\JPadConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */