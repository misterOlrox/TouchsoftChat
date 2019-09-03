package com.olrox.chat.server.manager;

import com.olrox.chat.server.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

// TODO replace logic in another place?
public class UsersManager {

    private final static Logger logger = LogManager.getLogger(UsersManager.class);

    private final static Queue<User> freeClients = new ArrayDeque<>();

    private final static Queue<User> freeAgents = new ArrayDeque<>();

    private final static Set<String> onlineUsers = new HashSet<>();

    private synchronized static void removeOfflineClients(){
        User freeClient = freeClients.peek();
        while(freeClient != null && !onlineUsers.contains(freeClient.getUsername())){
            freeClient = freeClients.poll();
        }
    }

    private synchronized static void removeOfflineAgents(){
        User freeAgent = freeAgents.peek();
        while(freeAgent != null && !onlineUsers.contains(freeAgent.getUsername())){
            freeAgent = freeAgents.poll();
        }
    }

    public synchronized static boolean hasFreeClient(){
        logger.debug("Free clients: " + freeClients);

        removeOfflineClients();
        return !freeClients.isEmpty();
    }

    public synchronized static boolean hasFreeAgent(){
        logger.debug("Free agents: " + freeAgents);

        removeOfflineAgents();
        return !freeAgents.isEmpty();
    }

    public synchronized static User pollFreeClient(){
        logger.debug("Free client was removed.");

        return freeClients.poll();
    }

    public synchronized static User pollFreeAgent(){
        logger.debug("Free agent was removed");

        return freeAgents.poll();
    }

    public synchronized static void addFreeClient(User client){
        freeClients.add(client);

        logger.debug("Free client " + client.getUsername() + " was added.");
        logger.debug("Free clients: " + freeClients);
    }

    public synchronized static void addFreeAgent(User agent) {
        freeAgents.add(agent);

        logger.debug("Free agent " + agent.getUsername() + " was added.");
        logger.debug("Free agents: " + freeAgents);
    }

    public synchronized static void addOnlineUser(String username) {
        onlineUsers.add(username);

        logger.debug("Online user was added: " + onlineUsers);
    }

    public synchronized static void removeOnlineUser(String username) {
        onlineUsers.remove(username);

        logger.debug("Online user was removed: " + onlineUsers);
    }

    public synchronized static boolean checkOnline(String username) {
        return onlineUsers.contains(username);
    }
}
