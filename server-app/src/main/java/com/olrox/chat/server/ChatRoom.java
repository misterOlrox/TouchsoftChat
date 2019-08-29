package com.olrox.chat.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChatRoom {

    private final static Logger logger = LogManager.getLogger(ChatRoom.class);

    private UserThread clientThread;
    private UserThread agentThread;

    public UserThread getClientThread() {
        return clientThread;
    }

    public void setClientThread(UserThread clientThread) {
        this.clientThread = clientThread;
    }

    public UserThread getAgentThread() {
        return agentThread;
    }

    public void setAgentThread(UserThread agentThread) {
        this.agentThread = agentThread;
    }

    public void deliverMessage(String text, UserThread from) {
        // TODO saving
        if(agentThread==null || clientThread==null) {
            noCompanionMessage(from);
            return;
        }

        if(agentThread==from) {
            logger.debug("Agent " + agentThread.getUser().getUsername() +" writes message: " + text);
            logger.debug("Client " + clientThread.getUser().getUsername() + " receives message: " + text);
            clientThread.getMessage(text);
        }
        else {
            logger.debug("Client " + clientThread.getUser().getUsername() +" writes message: " + text);
            logger.debug("Agent " + agentThread.getUser().getUsername() + " receives message: " + text);
            agentThread.getMessage(text);
        }

    }

    public void noCompanionMessage(UserThread userThread) {
        userThread.getMessage("Wait for your companion");
    }
}
