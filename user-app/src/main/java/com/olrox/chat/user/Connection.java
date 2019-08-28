package com.olrox.chat.user;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Connection {
    private String hostname;
    private int port;
    private User user;

    public Connection(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        this.user = new User();
    }

    public void start() {
        try {
            Socket socket = new Socket(hostname, port);

            System.out.println("Connected to the chat server");

            new ReadThread(socket, this).start();
            new WriteThread(socket, this).start();

        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        }
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
