package com.olrox.chat.server;

public class ClientAndAgentRoom {
    private User client;
    private User agent;

    public ClientAndAgentRoom(User client, User agent) {
        this.client = client;
        this.agent = agent;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public User getAgent() {
        return agent;
    }

    public void setAgent(User agent) {
        this.agent = agent;
    }
}
