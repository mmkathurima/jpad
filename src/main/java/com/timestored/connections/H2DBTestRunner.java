package com.timestored.connections;

import org.h2.tools.Server;

import java.sql.SQLException;


class H2DBTestRunner
        implements DBTestRunner {
    private static final int PORT = 8000;
    private static ConnectionManager csManager;
    private static Server server;
    private static ServerConfig serverConfig;

    public static DBTestRunner getInstance() {
        return new H2DBTestRunner();
    }

    public ConnectionManager start() throws SQLException {
        server = Server.createTcpServer(new String[]{"-tcpPort", "8000", "-tcpAllowOthers"}).start();

        csManager = ConnectionManager.newInstance();
        serverConfig = (new ServerConfigBuilder(new ServerConfig("localhost", 8000))).setName("h2Server").setJdbcType(JdbcTypes.H2).setDatabase("mem:db1").build();


        csManager.addServer(serverConfig);
        return csManager;
    }

    public void stop() {
        server.stop();
    }

    public ServerConfig getServerConfig() {
        return serverConfig;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\connections\H2DBTestRunner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */