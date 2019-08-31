package com.olrox.chat.server.message;

public class ServerAsAuthor implements Author {
    private final String username;

    public ServerAsAuthor(String name){
        this.username = name;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
