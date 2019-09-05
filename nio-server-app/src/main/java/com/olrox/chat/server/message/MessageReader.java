package com.olrox.chat.server.message;

import java.io.IOException;

public interface MessageReader {
    Message readMessage() throws IOException;

    void close() throws IOException;
}
