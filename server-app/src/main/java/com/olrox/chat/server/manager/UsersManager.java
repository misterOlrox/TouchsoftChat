package com.olrox.chat.server.manager;

import com.olrox.chat.server.user.FreeAgent;
import com.olrox.chat.server.user.FreeClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

// TODO synchronized?
// TODO replace logic in another place?
public class UsersManager {

    private final static Logger logger = LogManager.getLogger(UsersManager.class);

    private static Queue<FreeClient> freeClients = new ArrayDeque<>();

    private static Queue<FreeAgent> freeAgents = new ArrayDeque<>();

    private static Set<String> onlineUsers = new HashSet<>();

    private static void removeOfflineClients(){
        FreeClient freeClient = freeClients.peek();
        while(freeClient != null && !onlineUsers.contains(freeClient.getUsername())){
            freeClient = freeClients.poll();
        }
    }

    private static void removeOfflineAgents(){
        FreeAgent freeAgent = freeAgents.peek();
        while(freeAgent != null && !onlineUsers.contains(freeAgent.getUsername())){
            freeAgent = freeAgents.poll();
        }
    }

    public static boolean hasFreeClient(){
        logger.debug("Free clients: " + freeClients);
        logger.debug("Free agents: " + freeAgents);

        removeOfflineClients();
        return !freeClients.isEmpty();
    }

    public static boolean hasFreeAgent(){
        logger.debug("Free clients: " + freeClients);
        logger.debug("Free agents: " + freeAgents);

        removeOfflineAgents();
        return !freeAgents.isEmpty();
    }

    public static FreeClient pollFreeClient(){
        logger.debug("Free client was removed.");

        return freeClients.poll();
    }

    public static FreeAgent pollFreeAgent(){
        logger.debug("Free agent was removed");

        return freeAgents.poll();
    }

    public static void addFreeClient(FreeClient client){
        freeClients.add(client);

        logger.debug("Free client " + client.getUsername() + " was added.");
        logger.debug("Free clients: " + freeClients);
        logger.debug("Free agents: " + freeAgents);
    }

    public static void addFreeAgent(FreeAgent agent) {
        freeAgents.add(agent);

        logger.debug("Free agent " + agent.getUsername() + " was added.");
        logger.debug("Free clients: " + freeClients);
        logger.debug("Free agents: " + freeAgents);
    }

    public static void addOnlineUser(String username) {
        onlineUsers.add(username);
    }

    public static void removeOnlineUser(String username) {
        onlineUsers.remove(username);
    }

    public static boolean checkOnline(String username) {
        return onlineUsers.contains(username);
    }
}
