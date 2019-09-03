package com.olrox.chat.server.user.state;

import com.olrox.chat.server.manager.UsersManager;
import com.olrox.chat.server.message.Message;
import com.olrox.chat.server.message.author.AuthorType;
import com.olrox.chat.server.user.User;

import java.util.ArrayList;
import java.util.List;

public class FreeClientState implements UserState {
    private final User user;
    private List<Message> messages = new ArrayList<>();
    private boolean isWaiting = false;

    public FreeClientState(UnauthorizedState previousState){
        this.user = previousState.getUser();
        this.user.setAuthorType(AuthorType.CLIENT);
        this.user.receiveFromServer("Type your messages and we will find you an agent.");
    }

    public FreeClientState(BusyClientState busyClientState) {
        this.user = busyClientState.getUser();
    }

    private void findCompanion(){
        if(UsersManager.hasFreeAgent()){
            User companion = UsersManager.pollFreeAgent();
            connect(companion);
        } else {
            user.receiveFromServer("We haven't free agents. You can write messages and they will be saved.");
            UsersManager.addFreeClient(this.user);
            isWaiting = true;
        }
    }

    private void connect(User companion){
        UserState companionState = companion.getState();
        if(companionState instanceof FreeAgentState) {
            BusyClientState busyClient = new BusyClientState(this);
            BusyAgentState busyAgent = new BusyAgentState((FreeAgentState)companionState);

            busyClient.setCompanion(busyAgent);
            busyAgent.setCompanion(busyClient);

            this.user.setState(busyClient);
            companion.setState(busyAgent);

            user.receiveFromServer("Now you chatting with agent " + companion.getUsername());
            companion.receiveFromServer("Now you chatting with client " + this.user.getUsername());

            for (Message message : messages) {
                busyAgent.receiveFromClient(message);
            }
        } else {
            throw new RuntimeException("Companion isn't in FreeAgentState.");
        }
    }

    @Override
    public void register(Message message) {
        user.receiveFromServer("You are already registered as client " + user.getUsername());
    }

    @Override
    public void sendMessage(Message message) {
        messages.add(message);
        if (!isWaiting) {
            findCompanion();
        }
    }

    @Override
    public void leave() {
        user.receiveFromServer("You aren't chatting.");
    }

    @Override
    public void exit() {
        UsersManager.removeOnlineUser(user.getUsername());
    }

    public User getUser() {
        return user;
    }

    public List<Message> getMessages() {
        return messages;
    }
}