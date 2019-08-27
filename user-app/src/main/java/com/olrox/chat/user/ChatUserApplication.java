package com.olrox.chat.user;

public class ChatUserApplication {
    private final static String HOSTNAME = "localhost";
    private final static int PORT = 50000;

    public static void main(String[] args) {
        ChatUser client = new ChatUser(HOSTNAME, PORT);
        client.execute();
    }
}
