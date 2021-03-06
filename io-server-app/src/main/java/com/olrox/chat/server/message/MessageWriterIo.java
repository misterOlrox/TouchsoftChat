package com.olrox.chat.server.message;

import com.olrox.chat.server.message.author.Author;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class MessageWriterIo implements MessageWriter{
    private PrintWriter writer;

    public MessageWriterIo(Socket socket) throws IOException {
        OutputStream output = socket.getOutputStream();
        writer = new PrintWriter(output, true);
    }

    public void write(String string, Author author) {
        Message message = new Message(string, author);
        write(message);
    }

    public void write(Message message) {
        String data = MessageUtils.createDataToSocket(message);
        writer.println(data);
    }

    public void close() {
        writer.close();
    }

    void setWriter(PrintWriter writer) {
        this.writer = writer;
    }
}
