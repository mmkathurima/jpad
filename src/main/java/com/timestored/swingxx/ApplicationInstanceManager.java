package com.timestored.swingxx;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApplicationInstanceManager {
    private static final Logger LOG = Logger.getLogger(ApplicationInstanceManager.class.getName());
    private static final int SINGLE_INSTANCE_NETWORK_SOCKET = 44331;
    private static final String SINGLE_INSTANCE_SHARED_KEY = "$$NewInstance$$\n";
    private static ApplicationInstanceListener subListener;

    public static boolean registerInstance(String[] args) {
        final boolean returnValueOnError = true;

        try {
            ServerSocket socket = new ServerSocket(44331, 10, InetAddress.getLocalHost());

            LOG.fine("Listening for application instances on socket 44331");
            Thread instanceListenerThread = new Thread(new Runnable() {
                public void run() {
                    boolean socketClosed = false;
                    while (!socketClosed) {
                        if (socket.isClosed()) {
                            socketClosed = true;
                            continue;
                        }
                        try {
                            Socket client = socket.accept();
                            ObjectInputStream oin = new ObjectInputStream(client.getInputStream());
                            String message = (String) oin.readObject();
                            if ("$$NewInstance$$\n".trim().equals(message.trim())) {
                                LOG.fine("Shared key matched - new application instance found");
                                String[] myArgs = (String[]) oin.readObject();
                                fireNewInstance(Arrays.asList(myArgs));
                            }
                            oin.close();
                            client.close();
                        } catch (ClassNotFoundException | IOException e) {
                            LOG.severe("Shared key matched - new application instance found");
                            socketClosed = true;
                        }
                    }
                }
            });

            instanceListenerThread.start();
        } catch (UnknownHostException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            return returnValueOnError;
        } catch (IOException e) {
            LOG.fine("Port is already taken.  Notifying first instance.");
            try {
                Socket clientSocket = new Socket(InetAddress.getLocalHost(), 44331);
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                out.writeObject("$$NewInstance$$\n");
                out.writeObject(args);
                out.close();
                clientSocket.close();
                LOG.fine("Successfully notified first instance.");
                return false;
            } catch (UnknownHostException e1) {
                LOG.log(Level.SEVERE, e.getMessage(), e);
                return returnValueOnError;
            } catch (IOException e1) {
                LOG.log(Level.SEVERE, "Error connecting to local port for single instance notification");
                LOG.log(Level.SEVERE, e1.getMessage(), e1);
                return returnValueOnError;
            }
        }

        return true;
    }

    public static void setApplicationInstanceListener(ApplicationInstanceListener listener) {
        subListener = listener;
    }

    private static void fireNewInstance(List<String> args) {
        if (subListener != null)
            subListener.newInstanceCreated(args);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\swingxx\ApplicationInstanceManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */