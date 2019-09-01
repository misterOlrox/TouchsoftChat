package com.olrox.chat.server.user;

import com.olrox.chat.server.message.Author;
import com.olrox.chat.server.thread.UserThread;
import com.olrox.chat.server.message.Message;
import com.olrox.chat.server.manager.UsersManager;

import java.util.ArrayList;
import java.util.List;

public class FreeClient implements User, Author {
    private final UserThread thread;
    private String username;
    private List<Message> messages = new ArrayList<>();
    private boolean isWaiting = false;

    public FreeClient(UnauthorizedUser user, String username){
        this.thread = user.getThread();
        this.username = username;
        thread.writeServerAnswer("Type your messages and we will find you an agent.");
    }

    public FreeClient(BusyClient busyClient) {
        this.thread = busyClient.getThread();
        this.username = busyClient.getUsername();
    }

    private void findCompanion(){
        if(UsersManager.hasFreeAgent()){
            FreeAgent companion = UsersManager.pollFreeAgent();
            connect(companion);
        } else {
            thread.writeServerAnswer("We haven't free agents. You can write messages and they will be saved.");
            UsersManager.addFreeClient(this);
            isWaiting = true;
        }
    }

    private void connect(FreeAgent companion){
        BusyClient busyClient = new BusyClient(this);
        BusyAgent busyAgent = new BusyAgent(companion);

        busyClient.setCompanion(busyAgent);
        busyAgent.setCompanion(busyClient);

        this.thread.setUserStatus(busyClient);
        companion.getThread().setUserStatus(busyAgent);

        thread.writeServerAnswer("Now you chatting with agent " + companion.getUsername());
        companion.getThread().writeServerAnswer("Now you chatting with client " + this.getUsername());

        for(Message message : messages) {
            busyAgent.receiveFromClient(message);
        }
    }

    @Override
    public void register(Message message) {
        thread.writeServerAnswer("You are already registered as client " + username);
    }

    @Override
    public void sendMessage(Message message) {
        message.setAuthor(this);
        messages.add(message);
        if (!isWaiting) {
            findCompanion();
        }
    }

    @Override
    public void leave() {
        thread.writeServerAnswer("You aren't chatting.");
    }

    @Override
    public void exit() {
        UsersManager.removeOnlineUser(username);
    }

    @Override
    public String getUsername() {
        return username;
    }

    public UserThread getThread() {
        return thread;
    }

    public List<Message> getMessages() {
        return messages;
    }
}