package com.olrox.chat.server;

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
                        exit();
                        break;
                    case NULL:
                        nullMessageHandle();
                        break;
                }
            }
        } catch (IOException ex) {
            logger.error("Error in UserThread: ", ex);
        } finally {
            exit();
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

    private void exit() {

    }

    // FIXME another exception
    private void nullMessageHandle() throws IOException{
        logger.error("Null message from user.");
        throw new IOException("Null message from user.");
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

    public synchronized void closeConnections()  {
        logger.debug("closeConnections() method Enter");
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


        logger.debug("closeConnections() method Exit");
    }

    public void setUserStatus(User user) {
        this.user = user;
    }
}
