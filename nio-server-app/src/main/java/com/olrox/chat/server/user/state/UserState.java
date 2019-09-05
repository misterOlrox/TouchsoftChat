package com.olrox.chat.server.user.state;

import com.olrox.chat.server.message.Message;

public interface UserState {
    void register(Message message);

    void sendMessage(Message message);

    void leave();

    void exit();

}
