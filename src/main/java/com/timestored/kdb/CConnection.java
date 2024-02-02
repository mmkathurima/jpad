package com.timestored.kdb;

import com.timestored.connections.ServerConfig;
import kx.C;

import javax.activation.UnsupportedDataTypeException;
import java.io.IOException;
import java.util.logging.Logger;


public class CConnection
        implements KdbConnection {
    private static final Logger LOG = Logger.getLogger(CConnection.class.getName());

    private static final int RETRIES = 1;
    private final String host;
    private final int port;
    private final String username;
    private final String password;
    private C c;
    private boolean closed = false;

    CConnection(String host, int port, String username, String password) throws C.KException, IOException {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.c = new C(host, port, username + ":" + password);
        C.setEncoding("UTF-8");
    }


    CConnection(String host, int port) throws C.KException, IOException {
        this(host, port, null, null);
    }

    public CConnection(ServerConfig sconf) throws C.KException, IOException {
        this(sconf.getHost(), sconf.getPort(), sconf.getUsername(), sconf.getPassword());
    }


    public void close() throws IOException {
        LOG.info("close");
        this.closed = true;
        this.c.close();
    }


    public C.Flip queryFlip(String query) throws IOException, C.KException {
        Object k = query(query);
        if (k instanceof C.Flip) {
            return (C.Flip) k;
        }
        throw new UnsupportedDataTypeException("FlipExpected");
    }


    public C.Dict queryDict(String query) throws IOException, C.KException {
        Object k = query(query);
        if (k instanceof C.Dict) {
            return (C.Dict) k;
        }
        throw new UnsupportedDataTypeException("DictExpected");
    }


    public Object query(String query) throws IOException, C.KException {
        LOG.info("querying -> " + query);
        if (this.closed) {
            throw new IllegalStateException("we were closed");
        }

        Object ret = null;

        boolean sent = false;
        for (int r = 0; !sent; r++) {
            try {
                ret = this.c.k(query);
                sent = true;
                LOG.fine("query queried");
            } catch (IOException e) {
                try {
                    reconnect();
                } catch (IOException io) {
                }
                if (r >= 1) {
                    LOG.info("giving up reconnecting");
                    throw new IOException(e);
                }
            }
        }
        return ret;
    }


    public void send(String s) throws IOException {
        sendObject(s);
    }


    private void sendObject(Object obj) throws IOException {
        LOG.info("sending -> " + obj);
        if (this.closed) {
            throw new IllegalStateException("we were closed");
        }

        boolean sent = false;
        for (int r = 0; !sent; r++) {
            try {
                if (obj instanceof String) {
                    this.c.ks((String) obj);
                } else {
                    this.c.ks(obj);
                }
                sent = true;
                LOG.info("query sent");
            } catch (IOException e) {
                try {
                    LOG.info("query failed to send... reconnecting...");
                    reconnect();
                } catch (IOException io) {
                }
                if (r >= 1) {
                    LOG.info("giving up reconnecting");
                    throw new IOException(e);
                }
            }
        }
    }


    public void send(Object o) throws IOException {
        sendObject(o);
    }


    private void reconnect() throws IOException {
        if (this.closed) {
            throw new IllegalStateException("we were closed");
        }
        try {
            LOG.info("Trying reconnect host:" + this.host);
            this.c = new C(this.host, this.port, this.username + ":" + this.password);
        } catch (C.KException e) {
            throw new IOException(e);
        }
    }

    public String getName() {
        return this.host + ":" + this.port;
    }

    public boolean isConnected() {
        return (!this.closed && this.c.s.isConnected());
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\kdb\CConnection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */