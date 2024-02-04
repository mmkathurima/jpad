package com.timestored.connections;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.timestored.StringUtils;
import com.timestored.kdb.CConnection;
import com.timestored.kdb.KdbConnection;
import com.timestored.plugins.ConnectionDetails;
import com.timestored.plugins.DatabaseAuthenticationService;
import net.jcip.annotations.ThreadSafe;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;

import javax.sql.rowset.CachedRowSet;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

@ThreadSafe
public class ConnectionManager {
    private static final Logger LOG = Logger.getLogger(ConnectionManager.class.getName());
    private static final int MAX_STORAGE_SLOTS = 20;
    public static String XML_ROOT = "serverlist";
    private final List<ServerConfig> serverConns;
    private final Map<ServerConfig, ObjectPool> serverConnPool;
    private final Map<ServerConfig, Boolean> serverConnected = new ConcurrentHashMap<>();
    private final List<ServerConfig> readonlyServerConnections;
    private final CopyOnWriteArrayList<Listener> listeners;
    private String defaultLoginUsername = "";
    private String defaultLoginPassword = "";
    private Preferences preferences;
    private String prefKey;

    private ConnectionManager() {
        this.serverConnPool = new HashMap<>();
        this.serverConns = new CopyOnWriteArrayList<>();
        this.readonlyServerConnections = Collections.unmodifiableList(this.serverConns);
        this.listeners = new CopyOnWriteArrayList<>();
    }

    public static ConnectionManager newInstance() {
        return new ConnectionManager();
    }

    public static String getConnectionsXml(List<ServerConfig> serverConns) {
        XStream xstream = new XStream();
        xstream.processAnnotations(ServerConfigDTO.class);
        ArrayList<ServerConfigDTO> l = new ArrayList<>(serverConns.size());
        for (ServerConfig sc : serverConns) {
            l.add(new ServerConfigDTO(sc));
        }
        return xstream.toXML(l).replaceAll("list>", XML_ROOT + ">");
    }

    public static List<ServerConfig> getConnectionsFromXml(String serverListXml) throws IOException {
        try {
            if (serverListXml != null && serverListXml.length() > 0) {
                String s = serverListXml.replaceAll(XML_ROOT + ">", "list>");
                XStream xstream = new XStream(new StaxDriver());

                xstream.processAnnotations(ServerConfigDTO.class);
                ArrayList<ServerConfigDTO> a = (ArrayList<ServerConfigDTO>) xstream.fromXML(s);
                List<ServerConfig> r = new ArrayList<>();
                for (ServerConfigDTO scDTO : a)
                    r.add(scDTO.getInstance());
                return r;
            }
        } catch (Exception e) {
            String msg = "Could not convert serverListXml = " + serverListXml;
            LOG.log(Level.SEVERE, msg, e);
            throw new IOException(msg, e);
        }
        return Collections.emptyList();
    }

    public static boolean execute(String sql, Connection conn) {
        try (Statement st = conn.createStatement()) {
            st.execute(sql);
            return true;
        } catch (SQLException sqe) {
            LOG.log(Level.WARNING, "error running sql:\r\n" + sql, sqe);
        }
        return false;
    }

    public static CachedRowSet executeQuery(ServerConfig serverConfig, String sql, Connection conn) throws SQLException {
        try (Statement st = conn.createStatement()) {
            String pre = serverConfig.isKDB() ? "q)" : "";
            ResultSet rs = st.executeQuery(pre + sql);
            CachedRowSet crs = javax.sql.rowset.RowSetProvider.newFactory().createCachedRowSet();
            crs.populate(rs);
            return crs;
        } catch (SQLException sqe) {
            LOG.warning("Error running sql:\r\n" + sql);
            throw sqe;
        }
    }

    public void setPreferenceStore(Preferences preferences, String prefKeyPrefix) {
        this.preferences = preferences;
        this.prefKey = prefKeyPrefix;
        this.reloadFromPreferences();
    }

    public List<ServerConfig> getServerConnections() {
        List<ServerConfig> r = new ArrayList<>(this.readonlyServerConnections);
        Comparator<ServerConfig> alphabetOrder = new Comparator<>() {
            public int compare(ServerConfig sc1, ServerConfig sc2) {
                return sc1.getName().compareTo(sc2.getName());
            }
        };
        r.sort(alphabetOrder);
        return r;
    }

    public List<String> getServerNames() {
        List<String> s = Lists.newArrayList();
        for (ServerConfig sc : this.getServerConnections()) {
            s.add(sc.getName());
        }
        return Collections.unmodifiableList(s);
    }

    private void addServerSilently(ServerConfig serverConnection) {
        Preconditions.checkNotNull(serverConnection);
        String name = serverConnection.getName();
        ServerConfig existingSC = this.getServer(name);
        if (existingSC != null) {
            if (existingSC.equals(serverConnection)) {
                return;
            }
            throw new IllegalArgumentException("Server name must be unique. Cant use this call to update settings.");
        }

        this.serverConns.add(serverConnection);
        this.serverConnected.put(serverConnection, Boolean.FALSE);
        LOG.info("added server: " + serverConnection);
    }

    public void addServer(ServerConfig serverConnection) {
        this.reloadFromPreferences();
        this.addServerSilently(serverConnection);
        this.save();
        this.notifyListeners();
    }

    public void addServer(List<ServerConfig> connections) {
        this.reloadFromPreferences();
        Preconditions.checkNotNull(connections);
        List<ServerConfig> failedConfigs = new ArrayList<>();
        for (ServerConfig sc : connections) {
            try {
                this.addServerSilently(sc);
            } catch (IllegalArgumentException iae) {
                LOG.log(Level.WARNING, "Could not add sc: " + sc.toString(), iae);
                failedConfigs.add(sc);
            }
        }

        this.save();
        this.notifyListeners();
    }

    public void updateServer(String oldServerName, ServerConfig serverConnection) {
        String newName = serverConnection.getName();
        if (!newName.equals(oldServerName) && this.getServer(newName) != null) {
            throw new IllegalArgumentException("That server name is already taken.");
        }
        LOG.info("updateServer(" + oldServerName + " -> " + serverConnection + ")");

        ServerConfig existingSC;
        synchronized (this.serverConns) {
            this.reloadFromPreferences();
            existingSC = this.getServer(oldServerName);
            if (existingSC != null) {
                this.serverConns.remove(existingSC);
                this.statusUpdate(existingSC, false);
                this.serverConns.add(serverConnection);
                this.statusUpdate(serverConnection, false);
            }
            this.save();
        }
        LOG.info("updated server: " + serverConnection);
        this.notifyListeners();
        if (existingSC == null)
            throw new IllegalArgumentException("server does not exist already, so can't remove");
    }

    public void moveServer(ServerConfig serverConfig, String folderName) {
        Preconditions.checkNotNull(serverConfig);
        LOG.info("moveServer(" + serverConfig.getName() + " to " + folderName + ")");
        String f = (folderName == null) ? "" : folderName;
        if (!serverConfig.getFolder().equals(f)) {
            ServerConfig sc = (new ServerConfigBuilder(serverConfig)).setFolder(folderName).build();
            this.updateServer(serverConfig.getName(), sc);
        }
    }

    public boolean removeServer(String name) {
        ServerConfig sc = this.getServer(name);
        return sc != null && this.removeServer(sc);
    }

    private boolean[] removeServers(List<ServerConfig> serverConfigs) {
        if (serverConfigs.size() > 0) {
            this.reloadFromPreferences();
            boolean[] goners = new boolean[serverConfigs.size()];
            for (int i = 0; i < serverConfigs.size(); i++) {
                goners[i] = this.serverConns.remove(serverConfigs.get(i));
                if (goners[i]) {
                    LOG.info("removed server: " + serverConfigs);
                }
            }
            this.save();
            this.notifyListeners();
            return goners;
        }
        return new boolean[0];
    }

    public boolean removeServer(ServerConfig serverConfig) {
        return this.removeServers(Lists.newArrayList(serverConfig))[0];
    }

    public void removeServers() {
        this.serverConns.clear();
        this.save();
        LOG.info("removed all servers");
        this.notifyListeners();
    }

    public Connection getConnection(String serverName) throws IOException {
        ServerConfig sc = this.getServer(serverName);
        if (sc != null) {
            return this.getConnection(this.getServer(serverName));
        }
        return null;
    }

    public boolean isConnected(String serverName) {
        return this.isConnected(this.getServer(serverName));
    }

    public Connection getConnection(ServerConfig serverConfig) throws IOException {
        if (this.serverConns.contains(serverConfig)) {
            return this.getConn(serverConfig);
        }
        return null;
    }

    public void returnConn(ServerConfig serverConfig, Connection conn) {
        ObjectPool sp = this.serverConnPool.get(serverConfig);
        if (sp != null && conn != null) {
            try {
                if (conn.isClosed()) {
                    sp.invalidateObject(conn);
                } else {
                    sp.returnObject(conn);
                }
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "error returning object to pool", e);
            }
        }
    }

    private Connection getConn(ServerConfig serverConfig) throws IOException {
        try {
            GenericObjectPool genericObjectPool;
            ObjectPool connPool = this.serverConnPool.get(serverConfig);

            genericObjectPool = new GenericObjectPool();
            Class.forName(serverConfig.getJdbcType().getDriver());
            ServerConfig sc = this.overrideServerConfig(serverConfig);

            DriverManagerConnectionFactory driverManagerConnectionFactory = new DriverManagerConnectionFactory(sc.getUrl(), sc.getUsername(), sc.getPassword());

            PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(driverManagerConnectionFactory, genericObjectPool, null, null, false, true);

            this.serverConnPool.put(serverConfig, genericObjectPool);
            this.statusUpdate(serverConfig, true);

            Connection c = (Connection) genericObjectPool.borrowObject();
            if (c.isClosed()) {
                genericObjectPool.invalidateObject(c);
                c = null;
            }

            return c;
        } catch (Exception e) {
            if (this.serverConnected.containsKey(serverConfig)) {
                this.statusUpdate(serverConfig, false);
            }
            LOG.info("getConn Exception server: " + serverConfig.toString());
            throw new IOException(e);
        }
    }

    private ServerConfig overrideServerConfig(ServerConfig serverConfig) {
        ServerConfig sc = serverConfig;

        if (!serverConfig.hasLogin() && (this.defaultLoginPassword != null || this.defaultLoginUsername != null)) {
            sc = (new ServerConfigBuilder(serverConfig)).setUsername(this.defaultLoginUsername).setPassword(this.defaultLoginPassword).build();
        }

        DatabaseAuthenticationService dps = serverConfig.getJdbcType().getAuthenticator();
        if (dps != null) {
            ConnectionDetails connDetails = dps.getonConnectionDetails(sc.getConnectionDetails());
            sc = (new ServerConfigBuilder(serverConfig)).setHost(connDetails.getHost()).setPort(connDetails.getPort()).setDatabase(connDetails.getDatabase()).setUsername(connDetails.getUsername()).setPassword(connDetails.getPassword()).build();
        }

        return sc;
    }

    public ServerConfig getServer(String serverName) {
        Preconditions.checkNotNull(serverName);

        synchronized (this.serverConns) {
            for (ServerConfig sc : this.serverConns) {
                if (sc.getName().equals(serverName)) {
                    return sc;
                }
            }
        }
        return null;
    }

    private void notifyListeners() {
        for (Listener l : this.listeners) {
            try {
                l.prefChange();
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "problem notifying listener.", e);
            }
        }
    }

    public void addListener(Listener prefListener) {
        this.listeners.add(prefListener);
    }

    public boolean removeListener(Listener prefListener) {
        return this.listeners.remove(prefListener);
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("serverConns", this.serverConns).add("listeners", this.listeners).toString();
    }

    public void testConnection(ServerConfig serverConfig) throws IOException {
        boolean connected;

        Connection conn = this.getConn(serverConfig);
        try {
            connected = !conn.isClosed();
        } catch (SQLException e) {
            connected = false;
        } finally {
            this.returnConn(serverConfig, conn);
        }

        if (this.serverConns.contains(serverConfig)) {
            this.statusUpdate(serverConfig, connected);
        }

        if (!connected) {
            throw new IOException();
        }
    }

    public KdbConnection getKdbConnection(ServerConfig serverConfig) {
        CConnection kdbConn = null;
        if (serverConfig.isKDB()) {
            try {
                kdbConn = new CConnection(this.overrideServerConfig(serverConfig));
                this.statusUpdate(serverConfig, true);
            } catch (Exception e) {
                this.statusUpdate(serverConfig, false);
            }
        }
        return kdbConn;
    }

    private void statusUpdate(ServerConfig serverConfig, boolean connected) {
        Boolean prevVal = this.serverConnected.put(serverConfig, connected);
        boolean change = (prevVal == null || !prevVal.equals(connected));
        if (change) {
            LOG.info(serverConfig.getName() + " Connected = " + connected);
            for (Listener l : this.listeners) {
                l.statusChange(serverConfig, connected);
            }
        }
    }

    public KdbConnection getKdbConnection(String serverName) {
        ServerConfig sc = this.getServer(serverName);
        return (sc != null) ? this.getKdbConnection(sc) : null;
    }

    public boolean isConnected(ServerConfig sc) {
        if (sc != null) {
            Boolean b = this.serverConnected.get(sc);
            return b != null && b;
        }
        return false;
    }

    public boolean contains(ServerConfig serverConfig) {
        return this.serverConns.contains(serverConfig);
    }

    public boolean isEmpty() {
        return (this.serverConns.size() == 0);
    }

    public void refreshFromPreferences() {
        if (this.reloadFromPreferences()) {
            this.notifyListeners();
        }
    }

    public void setDefaultLogin(String username, String password) {
        if (!StringUtils.equals(this.defaultLoginUsername, username) || !StringUtils.equals(this.defaultLoginPassword, password)) {

            this.defaultLoginUsername = username;
            this.defaultLoginPassword = password;
            this.serverConnPool.clear();
            this.notifyListeners();
        }
    }

    private boolean reloadFromPreferences() {
        if (this.preferences != null) {

            StringBuilder sb = new StringBuilder(this.preferences.get(this.prefKey, ""));
            for (int i = 0; i < 20; i++) {
                sb.append(this.preferences.get(this.prefKey + i, ""));
            }
            String txt = sb.toString();

            try {
                txt = PreferenceHelper.decode(txt);

                List<ServerConfig> sConns = getConnectionsFromXml(txt);
                if (!sConns.equals(this.serverConns)) {
                    this.serverConns.clear();
                    this.serverConns.addAll(sConns);
                    return true;
                }
            } catch (IOException e) {
                LOG.log(Level.SEVERE, "Could not decrypt connection details txt = " + txt, e);
            }
        }
        return false;
    }

    private void save() {
        if (this.preferences != null) {
            String txt = getConnectionsXml(this.serverConns);
            txt = PreferenceHelper.encode(txt);
            if (txt.length() > 155648) {
                LOG.info("txt.length = " + txt.length() + " maxLength = " + 73728);
                throw new IllegalArgumentException("Too many connections to save");
            }

            if (txt.length() <= 8192) {
                this.preferences.put(this.prefKey, txt);
            } else {
                this.preferences.put(this.prefKey, txt.substring(0, 8192));
                txt.substring(8192);

                for (int i = 0; i < 20; i++) {
                    int stPos = (i + 1) * 8192;
                    int endPos = Math.min((i + 2) * 8192, txt.length());
                    if (stPos < txt.length()) {
                        this.preferences.put(this.prefKey + i, txt.substring(stPos, endPos));
                    } else {
                        this.preferences.put(this.prefKey + i, "");
                    }
                }
            }
        }
    }

    public String getConnectionsXml() {
        return getConnectionsXml(this.serverConns);
    }

    public Set<String> getFolders() {
        Set<String> r = Collections.emptySet();
        if (!this.serverConns.isEmpty()) {
            r = Sets.newHashSet();
            for (ServerConfig sc : this.serverConns) {
                r.add(sc.getFolder());
            }
        }
        return r;
    }

    public boolean execute(ServerConfig serverConfig, String sql) {
        boolean b = false;
        try {
            Connection conn = this.getConnection(serverConfig);
            if (conn == null) {
                throw new IOException("cant find server");
            }
            b = execute(sql, conn);
            this.returnConn(serverConfig, conn);
        } catch (IOException e) {
            LOG.log(Level.WARNING, "error getting connection:\r\n", e);
        }

        return b;
    }

    public boolean execute(ServerConfig sc, List<String> sqlQueries) {
        boolean success = true;
        for (String sql : sqlQueries) {
            success = (success && this.execute(sc, sql));
        }
        return success;
    }

    public CachedRowSet executeQuery(ServerConfig serverConfig, String sql) throws SQLException, IOException {
        Connection conn = this.getConnection(serverConfig);
        if (conn == null) {
            throw new IOException("Could not find server");
        }
        try {
            return executeQuery(serverConfig, sql, conn);
        } finally {
            this.returnConn(serverConfig, conn);
        }
    }

    public int removeFolder(String folder) {
        List<ServerConfig> removedServers = this.getServersInFolder(folder);
        boolean[] r = this.removeServers(removedServers);
        int c = 0;
        for (boolean b : r) {
            if (b) c++;
        }
        return c;
    }

    public int renameFolder(String from, String to) {
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(to);
        to = ServerConfig.cleanFolderName(to);
        from = ServerConfig.cleanFolderName(from);

        LOG.info("renameFolder(" + from + " -> " + to + ")");

        synchronized (this.serverConns) {
            this.reloadFromPreferences();
            Collection<ServerConfig> fromSCs = this.getServersInFolder(from);
            if (fromSCs.isEmpty()) {
                return 0;
            }
            for (ServerConfig existingSC : fromSCs) {
                this.serverConns.remove(existingSC);
                this.statusUpdate(existingSC, false);
                String newFolder = to + existingSC.getFolder().substring(from.length());
                ServerConfig sc = (new ServerConfigBuilder(existingSC)).setFolder(newFolder).build();
                this.serverConns.add(sc);
                this.statusUpdate(sc, false);
            }
            this.save();
            this.notifyListeners();
            return fromSCs.size();
        }
    }

    public List<ServerConfig> getServersInFolder(String folder) {
        Preconditions.checkNotNull(folder);
        String fn = ServerConfig.cleanFolderName(folder);
        List<ServerConfig> r = null;
        for (ServerConfig sc : this.serverConns) {
            if (sc.getFolder().startsWith(fn)) {
                if (r == null) {
                    r = new ArrayList<>();
                }
                r.add(sc);
            }
        }
        return (r == null) ? Collections.emptyList() : r;
    }

    public interface Listener {
        void prefChange();

        void statusChange(ServerConfig param1ServerConfig, boolean param1Boolean);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\connections\ConnectionManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */