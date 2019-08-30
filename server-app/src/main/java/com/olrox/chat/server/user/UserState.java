package com.olrox.chat.server.user;

import com.olrox.chat.server.message.Message;

public interface UserState {
    public void processMessage(UserThread userThread, Message message);
}
