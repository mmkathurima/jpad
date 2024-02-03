package com.timestored.connections;

import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.timestored.plugins.ConnectionDetails;
import net.jcip.annotations.Immutable;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

@Immutable
public class ServerConfig {
    private static final Color DEFAULT_COLOR = Color.WHITE;
    private final String name;
    private final String username;
    private final String password;
    private final String host;
    private final int port;
    private final String database;
    private final JdbcTypes jdbcType;
    private final Color color;

    public ServerConfig(String host, int port, String username, String password, String name, JdbcTypes jdbcType) {
        this(host, port, username, password, name, jdbcType, DEFAULT_COLOR, null, null);
    }

    public ServerConfig(String host, int port, String username, String password, String name, JdbcTypes jdbcType, Color color, String database) {
        this(host, port, username, password, name, jdbcType, DEFAULT_COLOR, database, null);
    }

    public ServerConfig(String host, int port, String username, String password, String name, JdbcTypes jdbcType, Color color, String database, String folder) {
        if (port < 0) {
            throw new IllegalArgumentException("Must specify positive port");
        }
        if (name.endsWith("/")) {
            throw new IllegalArgumentException("Name cannot end with a /");
        }

        this.database = database;
        this.color = (color == null) ? DEFAULT_COLOR : color;
        this.host = Preconditions.checkNotNull(host);
        this.port = port;
        this.username = username;
        this.password = password;

        String n = name;
        if (n == null || n.length() == 0) {
            n = host + ":" + port;
        }

        if (folder != null) {
            if (n.contains("/")) {
                throw new IllegalArgumentException("Cant specify name with path and separate folder");
            }
            String cf = cleanFolderName(folder);
            n = ((cf.length() > 0) ? (cf + "/") : "") + n;
        } else {

            n = Joiner.on("/").join(extractParts(n));
        }
        this.name = n;

        this.jdbcType = Preconditions.checkNotNull(jdbcType);
    }

    public ServerConfig(String host, int port, String username, String password, String name) {
        this(host, port, username, password, name, JdbcTypes.KDB, null, null, null);
    }

    public ServerConfig(String host, int port, String username, String password) {
        this(host, port, username, password, host + ":" + port, JdbcTypes.KDB, null, null, null);
    }

    public ServerConfig(String host, int port) {
        this(host, port, "", "", host + ":" + port, JdbcTypes.KDB, null, null, null);
    }

    public static List<String> extractParts(String name) {
        if (!name.contains("/")) {
            List<String> list = new ArrayList<String>(1);
            list.add(name);
            return list;
        }
        String[] a = name.split("/");
        List<String> r = new ArrayList<String>(a.length);
        for (String s : a) {
            if (s.length() > 0) {
                r.add(s);
            }
        }
        return r;
    }

    public static String cleanFolderName(String folder) {
        List<String> folds = extractParts(folder);
        return Joiner.on("/").join(folds);
    }

    public String getName() {
        return this.name;
    }

    public String getUsername() {
        return (this.username == null) ? "" : this.username;
    }

    public String getPassword() {
        return (this.password == null) ? "" : this.password;
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public String getDatabase() {
        return (this.database == null) ? "" : this.database;
    }

    ConnectionDetails getConnectionDetails() {
        return new ConnectionDetails(this.host, this.port, this.database, this.username, this.password);
    }

    public String getFolder() {
        int p = this.name.lastIndexOf("/");
        String s = (p > -1) ? this.name.substring(0, p) : "";
        return s;
    }

    public List<String> getFolders() {
        List<String> l = extractParts(this.name);
        return l.subList(0, l.size() - 1);
    }

    public String getShortName() {
        int p = this.name.lastIndexOf("/");
        return (p > -1) ? this.name.substring(p + 1) : this.name;
    }

    public JdbcTypes getJdbcType() {
        return this.jdbcType;
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("name", this.name).add("username", this.username).add("host", this.host).add("port", this.port).add("cstoreType", this.jdbcType).add("database", this.database).toString();
    }

    public String getUrl() {
        return this.jdbcType.getURL(this);
    }

    public Color getColor() {
        return (this.color == null) ? DEFAULT_COLOR : this.color;
    }

    public int hashCode() {
        return Objects.hashCode(this.name, this.username, this.password, this.host, Integer.valueOf(this.port), this.database, this.jdbcType, this.color);
    }

    public boolean equals(Object object) {
        if (object instanceof ServerConfig) {
            ServerConfig that = (ServerConfig) object;
            return (Objects.equal(this.name, that.name) && Objects.equal(this.username, that.username) && Objects.equal(this.password, that.password) && Objects.equal(this.host, that.host) && Objects.equal(Integer.valueOf(this.port), Integer.valueOf(that.port)) && Objects.equal(this.getDatabase(), that.getDatabase()) && Objects.equal(this.getFolder(), that.getFolder()) && Objects.equal(this.jdbcType, that.jdbcType) && Objects.equal(this.color, that.color));
        }

        return false;
    }

    public boolean isKDB() {
        return this.jdbcType.isKDB();
    }

    public boolean hasLogin() {
        return ((this.username != null && this.username.length() > 0) || (this.password != null && this.password.length() > 0));
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\connections\ServerConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */