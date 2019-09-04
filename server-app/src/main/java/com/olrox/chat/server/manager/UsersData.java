package com.olrox.chat.server.manager;

import com.olrox.chat.server.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

class UsersData {

    private final static Logger logger = LogManager.getLogger(UsersData.class);

    private final Queue<User> freeClients = new ArrayDeque<>();

    private final Queue<User> freeAgents = new ArrayDeque<>();

    private final Set<String> onlineUsers = new HashSet<>();

    private synchronized void removeOfflineClients(){
        User freeClient = freeClients.peek();
        while(freeClient != null && !onlineUsers.contains(freeClient.getUsername())){
            freeClients.poll();
            freeClient = freeClients.peek();
        }
    }

    private synchronized void removeOfflineAgents(){
        User freeAgent = freeAgents.peek();
        while(freeAgent != null && !onlineUsers.contains(freeAgent.getUsername())){
            freeAgents.poll();
            freeAgent = freeAgents.peek();
        }
    }

    public synchronized boolean hasFreeClient(){
        logger.debug("Free clients: " + freeClients);

        removeOfflineClients();
        return !freeClients.isEmpty();
    }

    public synchronized boolean hasFreeAgent(){
        logger.debug("Free agents: " + freeAgents);

        removeOfflineAgents();
        return !freeAgents.isEmpty();
    }

    public synchronized User pollFreeClient(){
        logger.debug("Free client was removed.");

        return freeClients.poll();
    }

    public synchronized User pollFreeAgent(){
        logger.debug("Free agent was removed");

        return freeAgents.poll();
    }

    public synchronized void addFreeClient(User client){
        freeClients.add(client);

        logger.debug("Free client " + client.getUsername() + " was added.");
        logger.debug("Free clients: " + freeClients);
    }

    public synchronized void addFreeAgent(User agent) {
        freeAgents.add(agent);

        logger.debug("Free agent " + agent.getUsername() + " was added.");
        logger.debug("Free agents: " + freeAgents);
    }

    public synchronized void addOnlineUser(String username) {
        onlineUsers.add(username);

        logger.debug("Online user was added: " + onlineUsers);
    }

    public synchronized void removeOnlineUser(String username) {
        onlineUsers.remove(username);

        logger.debug("Online user was removed: " + onlineUsers);
    }

    public synchronized boolean checkIfOnline(String username) {
        return onlineUsers.contains(username);
    }
}
