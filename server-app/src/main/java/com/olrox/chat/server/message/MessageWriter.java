package com.olrox.chat.server.message;

import com.olrox.chat.server.message.author.Author;

public interface MessageWriter {
    void write(String string, Author author);

    void write(Message message);

    void close();
}
