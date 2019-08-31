package com.olrox.chat.server.manager;

import com.olrox.chat.server.user.FreeAgent;
import com.olrox.chat.server.user.FreeClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FreeUsersManager {

    private final static Logger logger = LogManager.getLogger(FreeUsersManager.class);

    private static Queue<FreeClient> freeClients = new ConcurrentLinkedQueue<>();

    private static Queue<FreeAgent> freeAgents = new ConcurrentLinkedQueue<>();

    public static boolean hasFreeClient(){
        logger.debug("Free clients: " + freeClients);
        logger.debug("Free agents: " + freeAgents);

        return !freeClients.isEmpty();
    }

    public static boolean hasFreeAgent(){
        logger.debug("Free clients: " + freeClients);
        logger.debug("Free agents: " + freeAgents);

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
}
