package com.olrox.chat.server;

public class User {
    private String username;
    private UserRole type;
    private boolean isOnline;

    public User(String username, UserRole type, boolean isOnline) {
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

    public UserRole getType() {
        return type;
    }

    public void setType(UserRole type) {
        this.type = type;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public void sendMessage(Message message, ClientAndAgentRoom chatRoom){

    }

}
