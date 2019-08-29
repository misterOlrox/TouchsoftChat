package com.olrox.chat.server;

import com.olrox.chat.server.user.Agent;
import com.olrox.chat.server.user.Client;
import com.olrox.chat.server.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {

    private final static Logger logger = LogManager.getLogger(Server.class);

    private int port;

    // TODO check if this is normal
    private Queue<ChatRoom> freeClients = new ArrayDeque<>();

    // TODO check if this is normal
    private Queue<ChatRoom> freeAgents = new ArrayDeque<>();

    private Set<UserThread> userThreads = new HashSet<>();

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            logger.info("Chat Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();

                logger.info("New user connected");

                UserThread newUserThread = new UserThread(socket, this);
                userThreads.add(newUserThread);
                newUserThread.start();

            }

        } catch (IOException ex) {
            logger.error("Error in the server: ", ex);
        }
    }

    // TODO new class ChatRooms

    public boolean hasFreeClient(){
        return !freeClients.isEmpty();
    }

    public boolean hasFreeAgent(){
        return !freeAgents.isEmpty();
    }

    public ChatRoom pollClientRoom(){
        return freeClients.poll();
    }

    public ChatRoom pollAgentRoom(){
        return freeAgents.poll();
    }

    public void addRoomToFreeClients(ChatRoom room){
        freeClients.add(room);
    }

    public void addRoomToFreeAgents(ChatRoom room) {
        freeAgents.add(room);
    }

//    public ChatRoom findChatRoom(User user){
//        if(user.getChatRoom()!=null){
//            return user.getChatRoom();
//        }
//
//        switch(user.getRole()){
//            case AGENT:
//                if(!this.hasFreeClient()){
//                    freeAgents.add(user);
//                    user.setChatRoom(new ChatRoom(user));
//                    return user.getChatRoom();
//                }
//                else{
//                    User mate = freeClients.poll();
//
//                }
//                break;
//            case CLIENT:
//                if(!this.hasFreeAgent()) {
//                    freeClients.add(user);
//                    return null;
//                }
//                else{
//                    return freeAgents.poll();
//                }
//                break;
//        }
//    }
//
//    public void getFreeAgent(User client){
//        while(!freeAgents.isEmpty()){
//            User freeAgent = freeAgents.poll();
//            if(freeAgent.isOnline()){
//                // TODO finish this
//                return;
//            }
//        }
//        freeClients.add(client);
//    }
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
