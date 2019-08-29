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
    private MessageWriter writer;

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

    // FIXME too many lines in "try"
    public void run() {

        user = new User();
        MessageFromUser message;

        try {

            reader = new MessageReader(socket);
            writer = new MessageWriter(socket);

            writer.write("\nHello");

            while (true){
                if(user.getType() == UserType.UNAUTHORIZED) {
                    writer.write("Print \"/register [agent|client] YourName\" to register");
                    message = reader.readMessage();
                    CommandType command = message.getCommandType();

                    if(command == CommandType.EXIT) {
                        break;
                    }

                    login(message);
                    continue;
                }

                message = reader.readMessage();

                CommandType command = message.getCommandType();

                if(command == CommandType.EXIT) {
                    break;
                }

                switch(command) {
                    case MESSAGE:
                        deliverMessage(message);
                        break;
                }
            }

            //server.removeUser(userName, this);
            socket.close();
//
//            serverMessage = userName + " has quitted.";
//            server.broadcast(serverMessage, this);

        } catch (IOException ex) {
            logger.error("Error in UserThread: ", ex);
        }
    }

    private void login(MessageFromUser message) {
        String response = message.getText();
        CommandType command = message.getCommandType();

        if (command == CommandType.REGISTER) {
            StringTokenizer tokenizer = new StringTokenizer(response, " ");

            if(tokenizer.countTokens()!=3) {
                writer.write("Incorrect command.");
                return;
            };

            tokenizer.nextToken();
            String userType =  tokenizer.nextToken();
            String username = tokenizer.nextToken();

            user.setUsername(username);

            if(userType.equals("agent")){
                user.setType(UserType.AGENT);
            } else if(userType.equals("client")){
                user.setType(UserType.CLIENT);
            } else {
                writer.write("Sorry. You can't register as " + userType + ". Try again");
                return;
            }

            writer.write("You are successfully registered as "
                    + user.getClass().getSimpleName().toLowerCase()
                    + " "
                    + user.getUsername());

            logger.info("User was registered as "
                    + user.getClass().getSimpleName()
                    + " "
                    + user.getUsername());
        } else {
            writer.write("Sorry. You have a typo. Try again.");
        }
    }

    private void connectToRoom(){
        server.connectToRoom(this);
    }

    /**
     * Sends a message to the client.
     */
    public void getMessage(Message message) {
        writer.write(message);
    }

    public void getMessage(String message) {
        writer.write(message);
    }

    /**
     * Sends a message to the chat room.
     */
    public void deliverMessage(MessageFromUser message) throws IOException{
        if(user.getType() != UserType.UNAUTHORIZED) {
            connectToRoom();
            this.room.deliverMessage(message, this);
        } else {
            login(message);
        }
    }
}
