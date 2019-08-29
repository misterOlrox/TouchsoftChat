package com.olrox.chat.server;

import com.olrox.chat.server.user.Agent;
import com.olrox.chat.server.user.Client;
import com.olrox.chat.server.user.User;

public class ChatRoom {

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
        // FIXME NPE
        if(agentThread==from) {
            clientThread.getMessage(text);
        }
        else {
            agentThread.getMessage(text);
        }

    }
}
