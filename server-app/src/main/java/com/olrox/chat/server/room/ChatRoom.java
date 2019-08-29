package com.olrox.chat.server.room;

import com.olrox.chat.server.UserThread;
import com.olrox.chat.server.message.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ChatRoom {

    private final static Logger logger = LogManager.getLogger(ChatRoom.class);

    private UserThread clientThread;
    private UserThread agentThread;

    // TODO check if this is normal
    private List<Message> messageList = new ArrayList<>();

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

    void saveMessage(Message message){
        messageList.add(message);
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
