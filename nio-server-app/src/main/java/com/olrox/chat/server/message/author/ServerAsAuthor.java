package com.olrox.chat.server.message.author;

public class ServerAsAuthor implements Author {
    private final String username;

    public ServerAsAuthor(String name){
        this.username = name;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public AuthorType getAuthorType() {
        return AuthorType.SERVER;
    }
}
