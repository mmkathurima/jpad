package com.timestored.kdb;

import com.google.common.io.Files;
import com.timestored.connections.ConnectionManager;
import com.timestored.connections.ServerConfig;
import com.timestored.misc.CmdRunner;
import kx.C;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;


public class KdbTestHelper {
    public static final int QPORT = 15000;
    public static final ServerConfig SERVER_CONFIG = new ServerConfig("localhost", 15000, "", "", "testServer");
    public static final ServerConfig SERVER_CONFIG_B = new ServerConfig("localhost", 15001, "", "");
    public static final ServerConfig DISCONNECTED_SERVER_CONFIG = new ServerConfig("localhost", 10201, "", "", "DISCONNECTED_SERVER");
    private static final Logger LOG = Logger.getLogger(KdbTestHelper.class.getName());
    private static final String QHOST = "localhost";
    private static final String TEST_SERVER_NAME = "testServer";
    private static Process proc;

    private static Process proc_b;
    private static ConnectionManager connMan = ConnectionManager.newInstance();


    private static KdbConnection kdbConn;


    private static Connection conn;


    private static File latestDir;


    public static KdbConnection getNewKdbConnection() throws C.KException, IOException, InterruptedException {
        killAnyOpenProcesses();
        startQ();
        kdbConn = new CConnection("localhost", 15000);
        return kdbConn;
    }


    private static void startQ() throws IOException, InterruptedException {
        if (proc != null || proc_b != null) {
            throw new IllegalStateException("Q Proc already started");
        }
        latestDir = Files.createTempDir();
        proc = CmdRunner.startProc("q -p 15000", latestDir);
        proc_b = CmdRunner.startProc("q -p 15001", latestDir);
        Thread.sleep(200L);
    }


    private static void l(Exception e) {
        LOG.log(Level.WARNING, "error", e);
    }


    public static void killAnyOpenProcesses() throws IOException {
        if (kdbConn != null) {
            closeConn(kdbConn);
        } else if (conn != null) {
            try {
                conn.createStatement().execute("q)exit 0");
            } catch (Exception e) {
                l(e);
            }
            try {
                conn.close();
            } catch (Exception e) {
                l(e);
            }
            connMan.returnConn(SERVER_CONFIG, conn);
        } else {
            try {
                closeConn(new CConnection("localhost", 15000));
            } catch (IOException e1) {

            } catch (C.KException e) {
                throw new IOException("q proc already open and passworded");
            }
        }


        if (proc != null)
            try {
                proc.destroy();
            } catch (Exception e) {
                l(e);
            }

        if (proc_b != null) {
            try {
                proc_b.destroy();
            } catch (Exception e) {
                l(e);
            }

        }
        proc = null;
        proc_b = null;
        conn = null;
        kdbConn = null;
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
    }

    private static void closeConn(KdbConnection kc) {
        LOG.info("kdbConn.close()");
        try {
            kc.send("exit 0");
        } catch (Exception e) {
            l(e);
        }
        try {
            kc.close();
        } catch (Exception e) {
            l(e);
        }

    }

    public static String getServerName() {
        return SERVER_CONFIG.getName();
    }

    public static String getServerNameB() {
        return SERVER_CONFIG_B.getName();
    }


    public static Connection getNewConn() throws IOException {
        killAnyOpenProcesses();

        try {
            startQ();
            connMan = ConnectionManager.newInstance();
            try {
                connMan.addServer(SERVER_CONFIG);
            } catch (IllegalArgumentException e) {
            }
            try {
                connMan.addServer(SERVER_CONFIG_B);
            } catch (IllegalArgumentException e) {
            }

            conn = connMan.getConnection(SERVER_CONFIG);


            Statement st = conn.createStatement();
            st.execute("q)static:([] sym:1000?`4; price:1000?100.0)");
        } catch (SQLException se) {
            try {
                conn = connMan.getConnection(SERVER_CONFIG);
            } catch (IOException e) {
                throw new IOException(se);
            }
        } catch (InterruptedException ie) {
            throw new IOException(ie);
        }

        return conn;
    }


    public static ConnectionManager getNewConnectedMangager() throws IOException, InterruptedException {
        ConnectionManager cMan = ConnectionManager.newInstance();
        killAnyOpenProcesses();
        startQ();
        try {
            cMan.addServer(SERVER_CONFIG);
            cMan.addServer(SERVER_CONFIG_B);
            cMan.addServer(DISCONNECTED_SERVER_CONFIG);
        } catch (IllegalArgumentException e) {
        }


        return cMan;
    }


    public static File getLatestDir() {
        return latestDir;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\kdb\KdbTestHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */