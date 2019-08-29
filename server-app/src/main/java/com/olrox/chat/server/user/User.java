package com.olrox.chat.server.user;

public class User {
    private String username;
    private UserType type;

    public User() {
        this.type = UserType.UNAUTHORIZED;
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
