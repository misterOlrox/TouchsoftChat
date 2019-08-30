package com.olrox.chat.server.message;

public interface MessageWriter {
    void write(String string);

    void write(Message message);

    void close();
}
