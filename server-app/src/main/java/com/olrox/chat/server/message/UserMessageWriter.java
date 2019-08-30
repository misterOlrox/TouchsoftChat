package com.olrox.chat.server.message;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class UserMessageWriter implements MessageWriter{
    private PrintWriter writer;

    public UserMessageWriter(Socket socket) throws IOException {
        OutputStream output = socket.getOutputStream();
        writer = new PrintWriter(output, true);
    }

    public void write(String string) {
        Message message = new Message(string);
        write(message);
    }

    public void write(Message message) {
        writer.println(message.show());
    }

    public void close() {
        writer.close();
    }
}
