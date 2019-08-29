package com.olrox.chat.server.user;

import com.olrox.chat.server.ChatRoom;
import com.olrox.chat.server.Message;

public abstract class User {
    String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void sendMessage(Message message, ChatRoom chatRoom){

    }
}
