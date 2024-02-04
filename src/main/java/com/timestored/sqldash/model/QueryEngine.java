package com.timestored.sqldash.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.timestored.connections.ConnectionManager;
import com.timestored.connections.ServerConfig;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QueryEngine {
    private static final Logger LOG = Logger.getLogger(QueryEngine.class.getName());
    private final List<QueryEngineListener> listeners = new CopyOnWriteArrayList<>();
    private final int milliseconds = 50;
    private final ConcurrentLinkedQueue<Queryable> priorityQueue = new ConcurrentLinkedQueue<>();
    private final Map<Queryable, ResultSet> queryablesResultCache = Maps.newConcurrentMap();
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private int counter;
    private ConnectionManager connMan;
    private Collection<Queryable> queryables = new CopyOnWriteArrayList<>();
    private QueryTranslator queryTranslator;

    private QueryEngine(ConnectionManager connMan) {
        this.connMan = connMan;
    }

    public static QueryEngine newQueryEngine(ConnectionManager connMan) {
        return new QueryEngine(connMan);
    }

    public void addToPriorityQueue(Collection<Queryable> qs) {
        this.priorityQueue.addAll(qs);
    }

    public void startUp() {
        LOG.info("startUp");
        this.scheduler = Executors.newSingleThreadScheduledExecutor();

        this.scheduler.scheduleWithFixedDelay(new Runnable() {
            public void run() {
                try {
                    ConnectionManager cm = QueryEngine.this.connMan;

                    Queryable w;
                    while ((w = QueryEngine.this.priorityQueue.poll()) != null) {
                        LOG.info("priorityQueueing");
                        QueryEngine.this.requery(w, cm);
                    }

                    QueryEngine.this.counter++;
                    if (QueryEngine.this.counter % 2 == 0 &&
                            cm != null && !cm.isEmpty()) {
                        for (Queryable app : QueryEngine.this.queryables) {

                            int refRate = app.getRefreshPeriod();
                            int m = refRate / 100;
                            if (refRate != -1 && (m == 0 || QueryEngine.this.counter % m == 0)) {
                                QueryEngine.this.requery(app, cm);
                            }
                        }
                    }
                } catch (Exception e) {
                    LOG.log(Level.SEVERE, "big loopy scheduled problems", e);
                }
            }
        }, 50L, 50L, TimeUnit.MILLISECONDS);
    }

    private void requery(Queryable w, ConnectionManager connMan) {
        String qry = w.getQuery();
        LOG.info("requery -> " + w.getServerName() + " : " + qry);

        if (qry == null || qry.length() < 1 || w.getServerName() == null || connMan.isEmpty()) {
            return;
        }

        ResultSet crs = null;
        Exception e = null;

        ServerConfig sc;
        try {
            String srv = w.getServerName();
            if (srv == null) {
                throw new IllegalStateException("No Server Selected.");
            }

            sc = connMan.getServer(srv);
            if (sc == null) {
                throw new IllegalStateException("Could not find server:" + srv);
            }

            String query = qry;
            if (this.queryTranslator != null) {
                query = this.queryTranslator.translate(qry, sc.getJdbcType());
            }
            if (query == null) {
                throw new IllegalStateException("Missing Required Argument.");
            }

            crs = connMan.executeQuery(sc, query);
        } catch (Exception ee) {
            e = ee;
            LOG.log(Level.WARNING, "app update error for query:" + qry);
        }

        if (crs != null) {

            ResultSet prevRS = this.queryablesResultCache.get(w);
            if (!DBHelper.isEqual(prevRS, crs)) {
                this.queryablesResultCache.put(w, crs);
                for (QueryEngineListener l : this.listeners) {
                    l.tabChanged(w, crs);
                }
            }
        } else {
            if (e == null) {
                e = new IOException("unknown querying problem");
            }
            for (QueryEngineListener l : this.listeners) {
                l.queryError(w, e);
            }
        }
    }

    public void setConnectionManager(ConnectionManager connMan) {
        this.connMan = Preconditions.checkNotNull(connMan);
    }

    public void shutDown() {
        LOG.info("shutDown");
        this.scheduler.shutdown();
        this.scheduler = null;
    }

    public void setQueryables(Collection<Queryable> queryables) {
        this.queryables = Preconditions.checkNotNull(queryables);
        this.queryablesResultCache.clear();
    }

    public void setQueryTranslator(QueryTranslator queryTranslator) {
        this.queryTranslator = Preconditions.checkNotNull(queryTranslator);
    }

    public void addListener(QueryEngineListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(QueryEngineListener listener) {
        this.listeners.remove(listener);
    }

    public interface QueryEngineListener {
        void tabChanged(Queryable param1Queryable, ResultSet param1ResultSet);

        void queryError(Queryable param1Queryable, Exception param1Exception);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\model\QueryEngine.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */