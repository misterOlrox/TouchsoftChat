package com.olrox.chat.server.user;

import com.olrox.chat.server.message.Author;
import com.olrox.chat.server.thread.UserThread;
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
        thread.writeServerAnswer("You are agent " + this.getUsername() + " chatting with client " +
                companion.getUsername() + ". You needn't to register.");
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
        this.thread.writeServerAnswer("You leaved client " + companion.getUsername());
        companion.getThread().writeServerAnswer("Agent " + username + " leaved.");

        this.setFree();
        companion.setFree();
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setCompanion(BusyClient companion) {
        this.companion = companion;
    }

    public void setFree(){
        FreeAgent freeAgent = new FreeAgent(this);
        this.thread.setUserStatus(freeAgent);
        freeAgent.findCompanion();
    }

    public UserThread getThread() {
        return thread;
    }
}
