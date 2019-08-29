package com.olrox.chat.server;

import com.olrox.chat.server.message.*;
import com.olrox.chat.server.room.ChatRoom;
import com.olrox.chat.server.user.Agent;
import com.olrox.chat.server.user.Client;
import com.olrox.chat.server.user.User;
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

    // FIXME too many lines in "try"
    public void run() {
        try {

            reader = new MessageReader(socket);
            writer = new MessageWriter(socket);

            MessageFromUser message;

            while (true){
                message = reader.readMessage();

                CommandType command = message.getCommandType();

                if(command == CommandType.EXIT) {
                    break;
                }

                switch(command) {
                    case MESSAGE:
                        deliverMessage(message.getText(), room);
                        break;
                    case REGISTER:
                        login();
                        break;
                }
            }

            login();

            connectToRoom();

            //server.removeUser(userName, this);
            socket.close();
//
//            serverMessage = userName + " has quitted.";
//            server.broadcast(serverMessage, this);

        } catch (IOException ex) {
            logger.error("Error in UserThread: ", ex);
        }
    }

    private void login() throws IOException {
        writer.write("\nHello");
        writer.write("Print \"/register [agent|client] YourName\" to register");
        while (true) {

            String response = reader.readMessage().getText();
            if (response.startsWith("/register ")) {
                StringTokenizer tokenizer = new StringTokenizer(response, " ");

                if(tokenizer.countTokens()!=3) {
                    socketWriter.println("Incorrect command.");
                    continue;
                };

                tokenizer.nextToken();
                String userType =  tokenizer.nextToken();
                String username = tokenizer.nextToken();

                // FIXME factory?
                if(userType.equals("agent")){
                    user = new Agent(username);
                } else if(userType.equals("client")){
                    user = new Client(username);
                } else {
                    socketWriter.println("Sorry. You can't register as " + userType + ". Try again");
                    continue;
                }

                socketWriter.println("You are successfully registered as "
                        + user.getClass().getSimpleName().toLowerCase()
                        + " "
                        + user.getUsername());

                logger.info("User was registered as "
                        + user.getClass().getSimpleName()
                        + " "
                        + user.getUsername());

                break;

            } else {
                socketWriter.println("Sorry. You have a typo. Try again.");
            }
        }
    }

    private void connectToRoom(){

        room = server.connectToRoom(this);


    }

    private Message lastMessageToUser;

    /**
     * Sends a message to the client.
     */
    public void getMessage(String message) {
        socketWriter.println(message);
    }

    /**
     * Sends a message to the chat room.
     */
    public void deliverMessage(String text, ChatRoom room) {
        room.deliverMessage(text, this);
    }

}
