package com.timestored.connections;

import java.sql.SQLException;

public interface DBTestRunner {
    ConnectionManager start() throws SQLException;

    void stop();

    ServerConfig getServerConfig();
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\connections\DBTestRunner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */