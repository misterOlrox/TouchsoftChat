package com.olrox.chat.server.nio;

import com.olrox.chat.server.message.*;
import com.olrox.chat.server.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class Handler implements Runnable {

    private final static Logger LOGGER = LogManager.getLogger(Handler.class);

    private final SocketChannel socketChannel;
    private final SelectionKey selectionKey;
    private static final int READING = 0, SENDING = 1;
    private int state = READING;
    private final MessageReader messageReader;
    private User user;

    Handler(Selector selector, SocketChannel c) throws IOException {

        LOGGER.info("Handler was created.");

        socketChannel = c;
        c.configureBlocking(false);
        selectionKey = socketChannel.register(selector, 0);
        selectionKey.attach(this);
        selectionKey.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
        messageReader = new MessageReaderNio(socketChannel);
        MessageWriter messageWriter = new MessageWriterNio(socketChannel);
        user = new User(messageWriter);
    }


    public void run() {
        try {
            if (state == READING) {
                read();
            } else if (state == SENDING) {
                send();
            }
        } catch (IOException ex) {
            LOGGER.error("Error in Handler: ", ex);
        }
    }

    private Message message;

    private void read() throws IOException {
        message = messageReader.readMessage();

        state = SENDING;
        // Interested in writing
        selectionKey.interestOps(SelectionKey.OP_WRITE);
    }

    private void send(){
        message.setAuthor(user);

        CommandType command = message.getCommandType();
        switch(command) {
            case REGISTER:
                register(message);
                break;
            case MESSAGE:
                sendMessage(message);
                break;
            case LEAVE:
                leave();
                break;
            case EXIT:
                exit();
                return;
        }

        selectionKey.interestOps(SelectionKey.OP_READ);
        state = READING;
    }


    private void register(Message message){
        this.user.register(message);
    }

    private void sendMessage(Message message){
        this.user.sendMessage(message);
    }

    private void leave() {
        this.user.leave();
    }

    private void exit() {
        this.user.exit();
        try {
            socketChannel.close();
            messageReader.close();
        } catch (IOException ex) {
            LOGGER.error("Error in Handler: ", ex);
        }
    }
}