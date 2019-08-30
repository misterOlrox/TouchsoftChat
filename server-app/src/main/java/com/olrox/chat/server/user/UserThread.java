package com.olrox.chat.server.user;

import com.olrox.chat.server.Server;
import com.olrox.chat.server.message.*;
import com.olrox.chat.server.room.ChatRoom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

public class UserThread extends Thread {

    private final static Logger logger = LogManager.getLogger(UserThread.class);

    private User user;
    private ChatRoom room;
    private Socket socket;
    private Server server;
    private MessageReader reader;
    private MessageWriter serverWriter;
    private MessageWriter userWriter;

    public UserThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ChatRoom getRoom() {
        return room;
    }

    public void setRoom(ChatRoom room) {
        this.room = room;
    }

    public void run() {

        user = new User();
        Message message;
        CommandType command = null;

        try {

            reader = new MessageReader(socket);
            serverWriter = new ServerMessageWriter(socket, server);
            userWriter = new UserMessageWriter(socket);

            serverWriter.write("Hello");

            while (command != CommandType.EXIT){
                logger.debug("User's state: " + user.toString());

                if(command == CommandType.NULL) {
                    logger.error("Server received null message from user");
                    break;
                }

                if(user.getType() == UserType.UNAUTHORIZED) {
                    serverWriter.write("Print \"/register [agent|client] YourName\" to register");
                    message = reader.readMessage();
                    command = message.getCommandType();

                    logger.debug("Current message from user: " + message);

                    if(command == CommandType.REGISTER) {
                        login(message);
                    }

                    if(user.getType() == UserType.AGENT) {
                        connectToRoom();
                    }
                    continue;
                }

                message = reader.readMessage();

                logger.debug("Current message from user: " + message);

                command = message.getCommandType();

                switch(command) {
                    case MESSAGE:
                        sendToChat(message);
                        break;
                    case LEAVE:
                        leave();
                }
            }

        } catch (IOException ex) {
            logger.error("Error in UserThread: ", ex);
        } finally {
            exit();
            closeConnections();
        }
    }

    private void login(Message message) {
        String response = message.getText();
        CommandType command = message.getCommandType();

        if (command == CommandType.REGISTER) {
            StringTokenizer tokenizer = new StringTokenizer(response, " ");

            if(tokenizer.countTokens()!=3) {
                serverWriter.write("Incorrect command.");
                return;
            };

            tokenizer.nextToken();
            String userType =  tokenizer.nextToken();
            String username = tokenizer.nextToken();

            user.setName(username);

            if(userType.equals("agent")){
                user.setType(UserType.AGENT);
            } else if(userType.equals("client")){
                user.setType(UserType.CLIENT);
            } else {
                serverWriter.write("Sorry. You can't register as " + userType + ". Try again");
                return;
            }

            serverWriter.write("You are successfully registered as "
                    + userType
                    + " "
                    + user.getName());

            logger.info("User was registered as "
                    + userType
                    + " "
                    + user.getName());
        } else {
            serverWriter.write("Sorry. You have a typo. Try again.");
        }
    }

    private void leave() {
        if(room != null) {
            server.disconnectFromRoom(this);
        }
    }

    private void exit(){
        if(room != null) {
            server.exitFromRooms(this);
        }
    }

    private void connectToRoom(){
        server.connectToRoom(this);
    }

    /**
     * Sends a message to the client.
     */
    public void writeMessageFromUser(Message message) {
        userWriter.write(message);
    }

    public void writeAsServer(String message) {
        serverWriter.write(message);
    }

    public void writeAsServer(Message message) {
        serverWriter.write(message);
    }

    /**
     * Sends a message to the chat room.
     */
    public void sendToChat(Message message) throws IOException{
        if(user.getType() != UserType.UNAUTHORIZED) {
            connectToRoom();
            message.setAuthor(this.user);
            this.room.deliverMessage(message, this);
        } else {
            login(message);
        }
    }

    private synchronized void closeConnections()  {
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
}
