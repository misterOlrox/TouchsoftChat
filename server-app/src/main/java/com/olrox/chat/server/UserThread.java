package com.olrox.chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

public class UserThread extends Thread {
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



//            String userName = socketReader.readLine();
//            server.addUserName(userName);
//
//            String serverMessage = "New user connected: " + userName;
//            server.broadcast(serverMessage, this);
//
//            String clientMessage;
//
//            do {
//                clientMessage = socketReader.readLine();
//                serverMessage = "[" + userName + "]: " + clientMessage;
//                server.broadcast(serverMessage, this);
//
//            } while (!clientMessage.equals("bye"));
//
//            server.removeUser(userName, this);
//            socket.close();
//
//            serverMessage = userName + " has quitted.";
//            server.broadcast(serverMessage, this);

        } catch (IOException ex) {
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
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

                UserType userType;

                try {
                    userType = UserType.valueOf(typeStr.toUpperCase());
                } catch (IllegalArgumentException ex) {
                    socketWriter.println("Sorry. You can't register as " + typeStr + ". Try again");
                    continue;
                }

                user = new User(username, userType, true);

                socketWriter.println("You are successfully registered as "
                        + userType.toString().toLowerCase()
                        + " "
                        + username);

                System.out.println("User was registered as "
                        + userType.toString().toLowerCase()
                        + " "
                        + username);

                break;

            } else {
                socketWriter.println("Sorry. You have a typo. Try again.");
            }
        }
    }

    /**
     * Sends a message to the client.
     */
    private void sendMessage(String message) {
        socketWriter.println(message);
    }
}
