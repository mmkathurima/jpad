package com.timestored.connections;

import java.awt.Color;

public class ServerConfigBuilder {
    private String name;
    private String username;
    private String password;
    private String host;
    private int port;
    private String database;
    private JdbcTypes jdbcType;

    private Color color;

    public ServerConfigBuilder(ServerConfig sc) {
        this.name = sc.getName();
        this.username = sc.getUsername();
        this.password = sc.getPassword();
        this.host = sc.getHost();
        this.port = sc.getPort();
        this.database = sc.getDatabase();
        this.jdbcType = sc.getJdbcType();
        this.color = sc.getColor();
    }

    public ServerConfigBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ServerConfigBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public ServerConfigBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public ServerConfigBuilder setDatabase(String database) {
        this.database = database;
        return this;
    }

    public ServerConfigBuilder setJdbcType(JdbcTypes jdbcType) {
        this.jdbcType = jdbcType;
        return this;
    }

    public ServerConfigBuilder setFolder(String folder) {
        int p = this.name.lastIndexOf("/");
        String n = (p > -1) ? this.name.substring(p + 1) : this.name;
        String f = folder;
        if (!f.equals("") && !f.endsWith("/")) {
            f = f + "/";
        }
        this.name = f + n;
        return this;
    }

    public ServerConfigBuilder setColor(Color color) {
        this.color = color;
        return this;
    }

    public ServerConfigBuilder setHost(String host) {
        this.host = host;
        return this;
    }

    public ServerConfigBuilder setPort(int port) {
        if (port < 0) {
            throw new IllegalArgumentException("Must specify positive port");
        }
        this.port = port;
        return this;
    }

    public ServerConfig build() {
        if (this.port == -1) {
            this.port = this.jdbcType.getDefaultPort();
        }
        return new ServerConfig(this.host, this.port, this.username, this.password, this.name, this.jdbcType, this.color, this.database);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\connections\ServerConfigBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */