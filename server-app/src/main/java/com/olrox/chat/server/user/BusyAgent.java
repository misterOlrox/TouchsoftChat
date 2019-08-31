package com.olrox.chat.server.user;

import com.olrox.chat.server.Author;
import com.olrox.chat.server.UserThread;
import com.olrox.chat.server.message.Message;

public class BusyAgent implements User, Author {
    private UserThread thread;
    private String username;
    private BusyClient companion;

    public BusyAgent(FreeAgent agent) {
        this.thread = agent.getThread();
        this.username = agent.getUsername();
    }

    @Override
    public void register(Message message) {
        thread.writeServerAnswer("You are agent " + getName() + " chatting with client " +
                companion.getName() + ". You needn't to register.");
    }

    @Override
    public void sendMessage(Message message) {
        message.setAuthor(this);
        companion.receiveFromAgent(message);
    }

    public void receiveFromClient(Message message) {
        thread.writeMessageFromUser(message);
    }

    @Override
    public void leave(Message message) {

    }

    public String getUsername() {
        return username;
    }

    @Override
    public String getName() {
        return getUsername();
    }

    public void setCompanion(BusyClient companion) {
        this.companion = companion;
    }

    public UserThread getThread() {
        return thread;
    }
}
