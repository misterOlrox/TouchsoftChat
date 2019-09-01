package com.olrox.chat.server.thread;

import com.olrox.chat.server.ServerApplication;
import com.olrox.chat.server.message.*;
import com.olrox.chat.server.user.UnauthorizedUser;
import com.olrox.chat.server.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.Socket;

public class UserThread extends Thread {

    private final static Logger logger = LogManager.getLogger(UserThread.class);

    private Socket socket;
    private MessageReader reader;
    private MessageWriter serverWriter;
    private MessageWriter userWriter;
    private User user;

    public UserThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        Message message;
        CommandType command;

        try {
            reader = new MessageReader(socket);
            serverWriter = new AuthoredMessageWriter(socket, ServerApplication.SERVER_AS_AUTHOR);
            userWriter = new UserMessageWriter(socket);
            user = new UnauthorizedUser(this);

            while (true){
                message = reader.readMessage();

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
            logger.error("Error in UserThread: ", ex);
        } finally {
            logger.info("User " + user + " exited");
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

    public void writeMessageFromUser(Message message) {
        userWriter.write(message);
    }

    public void writeServerAnswer(String message) {
        serverWriter.write(message);
    }

    public void writeServerAnswer(Message message) {
        serverWriter.write(message);
    }

    public void closeConnections()  {
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
        if(userWriter != null) {
            userWriter.close();
        }
        if(serverWriter != null) {
            serverWriter.close();
        }
    }

    public void setUserStatus(User user) {
        this.user = user;
    }
}
