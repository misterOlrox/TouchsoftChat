package com.olrox.chat.server;

public class ChatServerApplication {
    private final static int PORT = 50000;

    public static void main(String[] args) {
        ChatServer server = new ChatServer(PORT);
        server.execute();
    }
}
