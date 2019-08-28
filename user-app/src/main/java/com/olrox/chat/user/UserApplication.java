package com.olrox.chat.user;

public class UserApplication {
    private final static String HOSTNAME = "localhost";
    private final static int PORT = 50000;

    // TODO LOGGING

    public static void main(String[] args) {
        Connection connection = new Connection(HOSTNAME, PORT);
        connection.start();
    }
}
