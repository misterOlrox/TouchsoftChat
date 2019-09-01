package com.olrox.chat.server.user;

import com.olrox.chat.server.message.Message;

public interface User {
    void register(Message message);

    void sendMessage(Message message);

    void leave();

    void exit();

}
