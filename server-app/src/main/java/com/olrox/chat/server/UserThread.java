package com.olrox.chat.server;

import com.olrox.chat.server.user.Agent;
import com.olrox.chat.server.user.Client;
import com.olrox.chat.server.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

public class UserThread extends Thread {

    private final static Logger logger = LogManager.getLogger(UserThread.class);

    private User user;
    private Socket socket;
    private Server server;
    private BufferedReader socketReader;
    private PrintWriter socketWriter;
    private ChatRoom room;

    public UserThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    // FIXME too many lines in "try"
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            socketReader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            socketWriter = new PrintWriter(output, true);

            login();

            findChat();

            while (true) {

                String userMessage;
                String serverMessage;

                userMessage = socketReader.readLine();

                if(userMessage.equals("/exit")){
                    break;
                }

//                if(user.isFree()){
//                    findChat();
//                }



//                serverMessage = "[" + userName + "]: " + userMessage;
//                server.broadcast(serverMessage, this);
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

    private void login() throws IOException {
        socketWriter.println("\nHello");
        while (true) {
            socketWriter.println("Print \"/register [agent|client] YourName\" to register");
            String response = socketReader.readLine();
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

    private void findChat(){

        if(user instanceof Agent && !server.hasFreeClient()){
            room = new ChatRoom();
            room.setAgentThread(this);
            server.addRoomToFreeAgents(room);
            socketWriter.println("Wait your Client.");
        }
        if(user instanceof Agent && server.hasFreeClient()){
            room = server.pollClientRoom();
            room.setAgentThread(this);
            socketWriter.println("You joined client " + room.getClientThread().getUser().getUsername());
        }
        if(user instanceof Client && !server.hasFreeAgent()){
            room = new ChatRoom();
            room.setClientThread(this);
            server.addRoomToFreeClients(room);
            socketWriter.println("Wait your Agent.");
        }
        if(user instanceof Client && server.hasFreeAgent()){
            room = server.pollAgentRoom();
            room.setClientThread(this);
            socketWriter.println("You joined agent " + room.getAgentThread().getUser().getUsername());
        }
    }

    /**
     * Sends a message to the client.
     */
    public void sendMessage(String message) {
        socketWriter.println(message);
    }

    public void sendMessage(String text, ChatRoom room) {
        room.deliverMessage(text, this);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
