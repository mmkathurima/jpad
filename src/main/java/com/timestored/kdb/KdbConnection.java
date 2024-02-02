package com.timestored.kdb;

import kx.C;

import java.io.IOException;

public interface KdbConnection {
    String getName();

    void close() throws IOException;

    C.Flip queryFlip(String paramString) throws IOException, C.KException;

    C.Dict queryDict(String paramString) throws IOException, C.KException;

    Object query(String paramString) throws IOException, C.KException;

    void send(String paramString) throws IOException;

    void send(Object paramObject) throws IOException;

    boolean isConnected();
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\kdb\KdbConnection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */