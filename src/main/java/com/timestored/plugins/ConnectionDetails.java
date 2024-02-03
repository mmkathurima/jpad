package com.timestored.plugins;

public class ConnectionDetails {
    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;

    public ConnectionDetails(String host, int port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
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
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\plugins\ConnectionDetails.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */