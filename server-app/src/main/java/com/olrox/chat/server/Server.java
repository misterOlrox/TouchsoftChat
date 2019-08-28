package com.olrox.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class Server {

    private int port;

    // TODO check if this is normal
    private Queue<User> freeClients = new ArrayDeque<>();

    // TODO check if this is normal
    private Queue<User> freeAgents = new ArrayDeque<>();

    private Set<UserThread> userThreads = new HashSet<>();

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Chat Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();

                System.out.println("New user connected");

                UserThread newUserThread = new UserThread(socket, this);
                userThreads.add(newUserThread);
                newUserThread.start();

            }

        } catch (IOException ex) {
            System.out.println("Error in the server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
//
//    /**
//     * Delivers a message from one user to others (broadcasting)
//     */
//    void broadcast(String message, UserThread excludeUser) {
//        for (UserThread aUser : userThreads) {
//            if (aUser != excludeUser) {
//                aUser.sendMessage(message);
//            }
//        }
//    }
//
//    /**
//     * Stores username of the newly connected client.
//     */
//    void addUserName(String userName) {
//        usernames.add(userName);
//    }
//
//    /**
//     * When a client is disconneted, removes the associated username and UserThread
//     */
//    void removeUser(String userName, UserThread aUser) {
//        boolean removed = usernames.remove(userName);
//        if (removed) {
//            userThreads.remove(aUser);
//            System.out.println("The user " + userName + " quitted");
//        }
//    }
//
//    Set<String> getUsernames() {
//        return this.usernames;
//    }
//
//    /**
//     * Returns true if there are other users connected (not count the currently connected user)
//     */
//    boolean hasUsers() {
//        return !this.usernames.isEmpty();
//    }
}