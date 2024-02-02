package com.timestored.connections;

import com.google.common.base.Preconditions;
import com.timestored.plugins.DatabaseAuthenticationService;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


public enum JdbcTypes {
    KDB("Kdb", "kx.jdbc", 5000, false) {
        private static final String URL_PREFIX = "jdbc:q:";

        public String getURL(ServerConfig sc) {
            return "jdbc:q:" + sc.getHost() + ":" + sc.getPort();
        }

        public String getComment(String commentContent) {
            return "/ " + commentContent;
        }

        public boolean isKDB() {
            return true;
        }
    },
    POSTGRES("Postgres", "org.postgresql.Driver", 5432, true) {
        private static final String URL_PREFIX = "jdbc:postgresql://";


        public String getURL(ServerConfig sc) {
            return "jdbc:postgresql://" + sc.getHost() + ":" + sc.getPort() + "/" + sc.getDatabase() + "?";
        }
    },


    CUSTOM(getProperty("jdbc.niceName", "Custom JDBC Driver"), getProperty("jdbc.driver", "DriverNotSpecified"), getProperty("jdbc.port", 5000), getProperty("jdbc.dbRequired", true)) {
        private final String JDBC_URL_FORMAT = getProperty("jdbc.urlFormat", "DriverUrlPrefixNotSpecified");
        private volatile DatabaseAuthenticationService dbAuthenticatorService;
        private volatile boolean init = false;
        private final boolean isKDB = getProperty("jdbc.isKDB", false);

        public DatabaseAuthenticationService getAuthenticator() {
            synchronized (this) {
                if (!this.init) {

                    try {
                        String className = getProperty("jdbc.authenticator", null);
                        Class<?> clazz = Class.forName(className);
                        Constructor<?> ctor = clazz.getConstructor();
                        Object object = ctor.newInstance();
                        if (object instanceof DatabaseAuthenticationService) {
                            this.dbAuthenticatorService = (DatabaseAuthenticationService) object;
                        }
                    } catch (ClassNotFoundException e) {
                    } catch (NoSuchMethodException e) {
                    } catch (SecurityException e) {
                    } catch (InstantiationException e) {
                    } catch (IllegalAccessException e) {
                    } catch (IllegalArgumentException e) {
                    } catch (InvocationTargetException e) {
                    }

                    this.init = true;
                }
                return this.dbAuthenticatorService;
            }
        }

        public String getURL(ServerConfig sc) {
            String s = this.JDBC_URL_FORMAT;
            s = s.replace("@HOST@", sc.getHost());
            s = s.replace("@PORT@", "" + sc.getPort());
            s = s.replace("@DB@", sc.getDatabase());

            return s;
        }

        public boolean isKDB() {
            return this.isKDB;
        }
    },

    MSSERVER("Microsoft SQL Server", "com.microsoft.sqlserver.jdbc.SQLServerDriver", 1433, true) {
        private static final String URL_PREFIX = "jdbc:sqlserver://";


        public String getURL(ServerConfig sc) {
            String s = "jdbc:sqlserver://" + sc.getHost() + ":" + sc.getPort();
            s = s + conv(";databaseName=", sc.getDatabase());
            return s;
        }
    },

    H2("H2", "org.h2.Driver", 8082, true) {
        private static final String URL_PREFIX = "jdbc:h2:tcp://";


        public String getURL(ServerConfig sc) {
            String s = "jdbc:h2:tcp://" + sc.getHost() + ":" + sc.getPort();
            s = s + conv("/", sc.getDatabase());
            s = s + ";DB_CLOSE_DELAY=-1";
            return s;
        }
    },

    MYSQL("MySQL", "com.mysql.jdbc.Driver", 3306, true) {
        private static final String URL_PREFIX = "jdbc:mysql://";

        public String getURL(ServerConfig sc) {
            return "jdbc:mysql://" + sc.getHost() + ":" + sc.getPort() + "/" + sc.getDatabase() + "?";
        }
    };
    private final String driver;
    private final boolean databaseRequired;
    private final int defaultPort;
    private final String niceName;

    JdbcTypes(String niceName, String driver, int defaultPort, boolean databaseRequired) {
        this.niceName = Preconditions.checkNotNull(niceName);
        this.driver = Preconditions.checkNotNull(driver);
        this.defaultPort = defaultPort;
        this.databaseRequired = databaseRequired;
    }

    private static String conv(String pre, String var) {
        if ((((var != null) ? 1 : 0) & ((var.length() > 0) ? 1 : 0)) != 0) {
            return pre + var;
        }
        return "";
    }

    private static String getProperty(String name, String defaultVal) {
        String a = System.getProperty(name);
        return (a == null) ? defaultVal : a;
    }

    private static int getProperty(String name, int defaultVal) {
        String a = System.getProperty(name);
        if (a != null) {
            try {
                return Integer.parseInt(a);
            } catch (NumberFormatException e) {
            }
        }
        return defaultVal;
    }

    private static boolean getProperty(String name, boolean defaultVal) {
        String a = System.getProperty(name);
        if (a != null) {
            try {
                return Boolean.parseBoolean(a);
            } catch (NumberFormatException e) {
            }
        }
        return defaultVal;
    }

    public String getComment(String singleLineCommentTxt) {
        if (singleLineCommentTxt.contains("\r") || singleLineCommentTxt.contains("\n")) {
            throw new IllegalArgumentException("single lines only permitted");
        }
        return "/* " + singleLineCommentTxt + " */";
    }

    public String getNiceName() {
        return this.niceName;
    }

    public boolean isDatabaseRequired() {
        return this.databaseRequired;
    }

    public String getDriver() {
        return this.driver;
    }

    public int getDefaultPort() {
        return this.defaultPort;
    }

    public boolean isKDB() {
        return false;
    }

    public DatabaseAuthenticationService getAuthenticator() {
        return null;
    }

    public abstract String getURL(ServerConfig paramServerConfig);
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\connections\JdbcTypes.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */