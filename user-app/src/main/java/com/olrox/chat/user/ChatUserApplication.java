package com.olrox.chat.user;

public class ChatUserApplication {
    public static void main(String[] args) {
        if (args.length < 2) return;

        String hostname = args[0];
        int port = Integer.parseInt(args[1]);

        ChatUser client = new ChatUser(hostname, port);
        client.execute();
    }
}
