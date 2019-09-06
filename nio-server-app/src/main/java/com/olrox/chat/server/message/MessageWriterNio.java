package com.olrox.chat.server.message;

import com.olrox.chat.server.message.author.Author;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class MessageWriterNio implements MessageWriter{
    SocketChannel socketChannel;

    public MessageWriterNio(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public void write(String string, Author author) {
        Message message = new Message(string, author);
        write(message);
    }

    public void write(Message message) {
        ByteBuffer output = ByteBuffer.wrap((message.show() + "\n").getBytes());
        try {
            socketChannel.write(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
