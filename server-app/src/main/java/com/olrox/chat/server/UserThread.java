package com.olrox.chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class UserThread extends Thread {
    private Socket socket;
    private Server server;
    private PrintWriter socketWriter;

    public UserThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    // FIXME too many lines in "try"
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader socketReader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            socketWriter = new PrintWriter(output, true);

            printGreeting();

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

    void printGreeting(){
        socketWriter.println("Hello. Print \"/register [agent|client] YourName\" to register");
    }

    /**
     * Sends a message to the client.
     */
    void sendMessage(String message) {
        socketWriter.println(message);
    }
}
