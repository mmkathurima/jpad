package com.timestored.sqldash.model;

import com.google.common.base.Preconditions;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;


public class Queryable {
    private static final Logger LOG = Logger.getLogger(Queryable.class.getName());

    private final transient List<Listener> listeners = new CopyOnWriteArrayList<Listener>();

    private String serverName = "localhost:5000";
    private String query = "";
    private int refreshPeriod;

    public Queryable() {
    }

    public Queryable(String serverName, String query, int refreshPeriod) {
        this.serverName = serverName;
        this.query = query;
        this.refreshPeriod = refreshPeriod;
    }

    public Queryable(String serverName, String query) {
        this(serverName, query, 0);
    }

    Queryable(Queryable app) {
        this.serverName = app.getServerName();
        this.query = app.getQuery();
        this.refreshPeriod = app.getRefreshPeriod();
    }

    public String getServerName() {
        return this.serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = Preconditions.checkNotNull(serverName);
        cc();
    }

    public String getQuery() {
        return this.query;
    }

    public void setQuery(String query) {
        this.query = Preconditions.checkNotNull(query);
        cc();
    }

    public int getRefreshPeriod() {
        return this.refreshPeriod;
    }

    public void setRefreshPeriod(int milliseconds) {
        if (milliseconds < -1) {
            throw new IllegalArgumentException("refresh period must be >=-1");
        }
        this.refreshPeriod = milliseconds;
        cc();
    }

    private void cc() {
        LOG.info("Queryable configChanged");
        for (Listener l : this.listeners) {
            l.configChanged(this);
        }
    }

    public boolean addListener(Listener queryableListener) {
        return this.listeners.add(queryableListener);
    }

    public boolean removeListener(Listener queryableListener) {
        return this.listeners.remove(queryableListener);
    }

    public interface Listener {
        void configChanged(Queryable param1Queryable);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\model\Queryable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */