package com.olrox.chat.server;

public class User {
    private String username;
    private UserType type;
    private boolean isOnline;

    public User(String username, UserType type, boolean isOnline) {
        this.username = username;
        this.type = type;
        this.isOnline = isOnline;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }
}
