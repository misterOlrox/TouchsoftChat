package com.olrox.chat.server;

import com.olrox.chat.server.user.FreeAgent;
import com.olrox.chat.server.user.FreeClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ChatManager {

    private final static Logger logger = LogManager.getLogger(ChatManager.class);

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
        logger.debug("Free clients: " + freeClients);
        logger.debug("Free agents: " + freeAgents);

        return freeClients.poll();
    }

    public static FreeAgent pollFreeAgent(){
        logger.debug("Free agent was removed");
        logger.debug("Free clients: " + freeClients);
        logger.debug("Free agents: " + freeAgents);

        return freeAgents.poll();
    }

    public static void addFreeClient(FreeClient client){
        logger.debug("Free client " + client.getUsername() + " was added.");
        logger.debug("Free clients: " + freeClients);
        logger.debug("Free agents: " + freeAgents);

        freeClients.add(client);
    }

    public static void addFreeAgent(FreeAgent agent) {
        logger.debug("Free agent " + agent.getUsername() + " was added.");
        logger.debug("Free clients: " + freeClients);
        logger.debug("Free agents: " + freeAgents);

        freeAgents.add(agent);
    }

    @Override
    public String toString() {
        return "ChatRooms{" +
                "freeClients=" + freeClients +
                ", freeAgents=" + freeAgents +
                '}';
    }
}
