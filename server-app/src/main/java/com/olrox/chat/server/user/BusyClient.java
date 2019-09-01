package com.olrox.chat.server.user;

import com.olrox.chat.server.manager.UsersManager;
import com.olrox.chat.server.thread.UserThread;
import com.olrox.chat.server.message.Author;
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
        thread.writeServerAnswer("You are client " + this.getUsername() + " chatting with agent " +
                companion.getUsername() + ". You needn't to register.");
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
    public void leave() {
        this.thread.writeServerAnswer("You leaved agent " + companion.getUsername());
        companion.getThread().writeServerAnswer("Client " + username + " leaved.");

        this.setFree();
        companion.setFree();
    }

    @Override
    public void exit() {
        companion.getThread().writeServerAnswer("Client " + username + " exited.");
        UsersManager.removeOnlineUser(username);
        companion.setFree();
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setCompanion(BusyAgent companion) {
        this.companion = companion;
    }

    public void setFree(){
        FreeClient freeClient = new FreeClient(this);
        this.thread.setUserStatus(freeClient);
    }

    public UserThread getThread() {
        return thread;
    }
}
