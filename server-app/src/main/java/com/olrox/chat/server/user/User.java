package com.olrox.chat.server.user;

import com.olrox.chat.server.Author;

public class User implements Author {
    private String name;
    private UserType type;

    public User() {
        this.type = UserType.UNAUTHORIZED;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
