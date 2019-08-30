package com.olrox.chat.server.room;

import com.olrox.chat.server.user.FreeUserState;
import com.olrox.chat.server.user.UserState;
import com.olrox.chat.server.user.UserThread;
import com.olrox.chat.server.message.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ChatRoom {

    private final static Logger logger = LogManager.getLogger(ChatRoom.class);

    private UserThread clientThread;
    private UserThread agentThread;

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

    public void deliverMessage(Message message, UserThread from) {
        if(agentThread==null && clientThread!=null) {
            clientThread.writeAsServer("Wait for your companion");
            saveMessage(message);
            return;
        }

        if(agentThread!=null && clientThread==null) {
            agentThread.writeAsServer("Wait for your companion");
            return;
        }

        if(agentThread == from) {
            logger.debug("Agent " + agentThread.getUser().getName() +" writes message: " + message);
            logger.debug("Client " + clientThread.getUser().getName() + " receives message: " + message);
            clientThread.writeMessageFromUser(message);
        }
        if (clientThread == from) {
            logger.debug("Client " + clientThread.getUser().getName() +" writes message: " + message);
            logger.debug("Agent " + agentThread.getUser().getName() + " receives message: " + message);
            agentThread.writeMessageFromUser(message);
        }

    }

    public void deliverSavedMessages() {
        for(Message message : messageList) {
            agentThread.writeMessageFromUser(message);
        }
    }
}
