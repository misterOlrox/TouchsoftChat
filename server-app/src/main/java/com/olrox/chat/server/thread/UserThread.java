package com.olrox.chat.server.thread;

import com.olrox.chat.server.ServerApplication;
import com.olrox.chat.server.message.CommandType;
import com.olrox.chat.server.message.Message;
import com.olrox.chat.server.message.MessageReader;
import com.olrox.chat.server.message.MessageWriter;
import com.olrox.chat.server.message.AuthoredMessageWriter;
import com.olrox.chat.server.message.UserMessageWriter;
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
                        leave(message);
                        break;
                    case EXIT:
                        return;
                }
            }
        } catch (IOException ex) {
            logger.error("Error in UserThread: ", ex);
        } finally {
            logger.info("End of run method.");
            closeConnections();
        }
    }

    private void register(Message message){
        this.user.register(message);
    }

    private void sendMessage(Message message){
        this.user.sendMessage(message);
    }

    private void leave(Message message) {
        this.user.leave(message);
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
