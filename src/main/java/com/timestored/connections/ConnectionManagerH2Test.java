package com.timestored.connections;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionManagerH2Test {
    private final DBTestRunner tRunner = H2DBTestRunner.getInstance();
    protected volatile boolean failedFlag;
    private ConnectionManager csManager;

    @Before
    public void setUp() throws SQLException {
        this.csManager = this.tRunner.start();
    }

    @After
    public void shutDown() {
        this.tRunner.stop();
    }

    @Test
    public void test() throws IOException, SQLException {
        ServerConfig sc = this.tRunner.getServerConfig();

        final String createSQL = "CREATE TABLE STATION (ID INTEGER PRIMARY KEY,CITY CHAR(20),STATE CHAR(2),LAT_N REAL,LONG_W REAL); ";

        Assert.assertTrue(this.csManager.execute(sc, createSQL));

        final String insertSQL = "INSERT INTO STATION VALUES (13, 'Phoenix', 'AZ', 33, 112); INSERT INTO STATION VALUES (44, 'Denver', 'CO', 40, 105); ";

        Assert.assertTrue(this.csManager.execute(sc, insertSQL));

        try (ResultSet rs = this.csManager.executeQuery(sc, "select count(*) from STATION")) {
            rs.next();
            Assert.assertEquals(2L, rs.getInt(1));
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\connections\ConnectionManagerH2Test.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */