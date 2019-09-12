package com.olrox.chat.web;

import com.olrox.chat.server.user.User;

public interface UserSession {
    void receiveText(String text) throws Exception;
    void setCurrentUser(User user);
    void disconnect(int status, String reason);
}
