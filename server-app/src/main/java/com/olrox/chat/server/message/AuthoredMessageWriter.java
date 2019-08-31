package com.olrox.chat.server.message;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class AuthoredMessageWriter implements MessageWriter{
    private PrintWriter writer;
    private Author author;

    public AuthoredMessageWriter(Socket socket, Author author) throws IOException {
        OutputStream output = socket.getOutputStream();
        writer = new PrintWriter(output, true);
        this.author = author;
    }

    public void write(String string) {
        Message message = new Message(string);
        write(message);
    }

    public void write(Message message) {
        message.setAuthor(author);
        writer.println(message.show());
    }

    public void close() {
        writer.close();
    }
}
