package com.olrox.chat.server.io;

import com.olrox.chat.server.message.*;
import com.olrox.chat.server.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.Socket;

public class SocketConnectionHandler extends Thread {

    private final static Logger LOGGER = LogManager.getLogger(SocketConnectionHandler.class);

    private final Socket socket;
    private User user;
    private MessageReader reader;

    public SocketConnectionHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        Message message;
        CommandType command;

        try {
            reader = new MessageReaderIo(socket);
            MessageWriter messageWriter = new MessageWriterIo(socket);
            user = new User(messageWriter);

            while (true){
                message = reader.readMessage();
                message.setAuthor(user);

                command = message.getCommandType();
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
            }
        } catch (IOException ex) {
            LOGGER.error("Error in UserThread: ", ex);
        } finally {
            LOGGER.info("User " + user.getUsername() + " exited");
            closeConnections();
        }
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
    }

    private void closeConnections()  {
        if (socket != null){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
