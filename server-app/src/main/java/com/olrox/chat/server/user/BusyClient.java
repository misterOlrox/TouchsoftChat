package com.olrox.chat.server.user;

import com.olrox.chat.server.Author;
import com.olrox.chat.server.UserThread;
import com.olrox.chat.server.message.Message;

public class BusyClient implements User, Author {
    private UserThread thread;
    private String username;
    private BusyAgent companion;

    public BusyClient(FreeClient client) {
        this.thread = client.getThread();
        this.username = client.getUsername();
    }

    @Override
    public void register(Message message) {
        thread.writeServerAnswer("You are client " + getName() + " chatting with agent " +
                companion.getName() + ". You needn't to register.");
    }

    @Override
    public void sendMessage(Message message) {
        message.setAuthor(this);
        companion.receiveFromClient(message);
    }

    public void receiveFromAgent(Message message) {
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

    public void setCompanion(BusyAgent companion) {
        this.companion = companion;
    }

    public UserThread getThread() {
        return thread;
    }
}
