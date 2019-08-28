package com.olrox.chat.server;

public class ServerApplication {
    private final static int PORT = 50000;

    public static void main(String[] args) {
        Server server = new Server(PORT);
        server.start();
    }
}
