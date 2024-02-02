package com.timestored.connections;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.awt.Color;


@XStreamAlias("ServerConfig")
class ServerConfigDTO {
    @XStreamAsAttribute
    private final String name;
    @XStreamAsAttribute
    private final String username;
    @XStreamAsAttribute
    private final String password;
    @XStreamAsAttribute
    private final String host;
    @XStreamAsAttribute
    private final int port;
    @XStreamAsAttribute
    private final String database;
    @XStreamAsAttribute
    private final JdbcTypes jdbcType;
    private final transient Color color = null;
    @XStreamAsAttribute
    private final Integer clr;
    @XStreamAsAttribute
    private String folder;

    ServerConfigDTO(ServerConfig sc) {
        this.name = sc.getName();
        this.username = sc.getUsername();
        this.password = sc.getPassword();
        this.host = sc.getHost();
        this.port = sc.getPort();
        this.database = sc.getDatabase();
        this.jdbcType = sc.getJdbcType();

        this.clr = Integer.valueOf(sc.getColor().getRGB());
    }

    ServerConfig getInstance() {
        Color c = null;
        if (this.clr != null) {
            c = new Color(this.clr.intValue());
        }

        String n = this.name;
        if (this.folder != null && this.folder.length() > 0) {
            n = this.folder + "/" + this.name;
        }
        return new ServerConfig(this.host, this.port, this.username, this.password, n, this.jdbcType, c, this.database);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\connections\ServerConfigDTO.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */