package com.olrox.chat.server.message;

import com.olrox.chat.server.message.author.Author;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;

public class MessageWriterNio implements MessageWriter{

    private final static Logger LOGGER = LogManager.getLogger(MessageWriterNio.class);

    private SocketChannel socketChannel;

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
        } catch (ClosedChannelException e) {
            LOGGER.warn("Channel already closed.");
        } catch (IOException ex) {
            LOGGER.error("Exception while writing a message: " + ex);
        }
    }

    public void close() {
        try {
            socketChannel.close();
        } catch (IOException ex) {
            LOGGER.error("Exception while closing: " + ex);
        }
    }
}
