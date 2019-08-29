package com.olrox.chat.server;

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

            while (true) {

                String userMessage;
                String serverMessage;

                userMessage = socketReader.readLine();

                if(userMessage.equals("/exit")){
                    break;
                }



                findChat();

//                serverMessage = "[" + userName + "]: " + userMessage;
//                server.broadcast(serverMessage, this);
            }

//            server.removeUser(userName, this);
//            socket.close();
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
                String typeStr =  tokenizer.nextToken();
                String username = tokenizer.nextToken();

                UserRole userRole;

                try {
                    userRole = UserRole.valueOf(typeStr.toUpperCase());
                } catch (IllegalArgumentException ex) {
                    socketWriter.println("Sorry. You can't register as " + typeStr + ". Try again");
                    continue;
                }

                user = new User(username, userRole, true);

                socketWriter.println("You are successfully registered as "
                        + userRole.toString().toLowerCase()
                        + " "
                        + username);

                logger.info("User was registered as "
                        + userRole.toString().toLowerCase()
                        + " "
                        + username);

                break;

            } else {
                socketWriter.println("Sorry. You have a typo. Try again.");
            }
        }
    }

    private void findChat(){
        switch(user.getType()){
            case AGENT:
                break;
            case CLIENT:

                break;
        }
    }

    /**
     * Sends a message to the client.
     */
    private void sendMessage(String message) {
        socketWriter.println(message);
    }
}
