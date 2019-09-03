package com.olrox.chat.server.user.state;

import com.olrox.chat.server.manager.UsersManager;
import com.olrox.chat.server.message.Message;
import com.olrox.chat.server.user.User;

public class BusyClientState implements UserState {
    private final User user;
    private BusyAgentState companion;

    public BusyClientState(FreeClientState client) {
        this.user = client.getUser();
    }

    @Override
    public void register(Message message) {
        user.receiveFromServer("You are client " + this.user.getUsername() + " chatting with agent " +
                companion.getUser().getUsername() + ". You needn't to register.");
    }

    @Override
    public void sendMessage(Message message) {
        companion.receiveFromClient(message);
    }

    public void receiveFromAgent(Message message) {
        user.receiveFromUser(message);
    }

    @Override
    public void leave() {
        this.user.receiveFromServer("You leaved agent " + companion.getUser().getUsername());
        companion.getUser().receiveFromServer("Client " + this.user.getUsername() + " leaved.");

        this.setFree();
        companion.setFree();
    }

    @Override
    public void exit() {
        companion.getUser().receiveFromServer("Client " + this.user.getUsername() + " exited.");
        UsersManager.removeOnlineUser(this.user.getUsername());
        companion.setFree();
    }

    public synchronized void setCompanion(BusyAgentState companion) {
        this.companion = companion;
    }

    public synchronized void setFree(){
        FreeClientState freeClient = new FreeClientState(this);
        this.user.setState(freeClient);
    }

    public User getUser() {
        return user;
    }
}