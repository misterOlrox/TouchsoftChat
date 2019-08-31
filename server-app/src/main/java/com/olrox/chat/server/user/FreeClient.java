package com.olrox.chat.server.user;

import com.olrox.chat.server.Author;
import com.olrox.chat.server.UserThread;
import com.olrox.chat.server.message.Message;
import com.olrox.chat.server.ChatManager;

import java.util.ArrayList;
import java.util.List;

public class FreeClient implements User, Author {
    private UserThread thread;
    private String username;
    private List<Message> messages = new ArrayList<>();
    private boolean isWaiting = false;

    public FreeClient(UnauthorizedUser user, String username){
        this.thread = user.getThread();
        this.username = username;
        thread.writeServerAnswer("Type your messages and we will find you an agent.");
    }

    private void findCompanion(){
        if(ChatManager.hasFreeAgent()){
            FreeAgent companion = ChatManager.pollFreeAgent();
            connect(companion);
        } else {
            thread.writeServerAnswer("We haven't free agents. You can write messages and they will be saved.");
            ChatManager.addFreeClient(this);
            isWaiting = true;
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
    public void leave(Message message) {
        thread.writeServerAnswer("You aren't chatting.");
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String getName() {
        return username;
    }

    public void connect(FreeAgent companion){
        BusyClient busyClient = new BusyClient(this);
        BusyAgent busyAgent = new BusyAgent(companion);

        busyClient.setCompanion(busyAgent);
        busyAgent.setCompanion(busyClient);

        this.thread.setUser(busyClient);
        companion.getThread().setUser(busyAgent);

        thread.writeServerAnswer("Now you chatting with agent " + companion.getUsername());
        companion.getThread().writeServerAnswer("Now you chatting with client " + this.getUsername());

        for(Message message : messages) {
            busyAgent.receiveFromClient(message);
        }
    }

    public UserThread getThread() {
        return thread;
    }

    public List<Message> getMessages() {
        return messages;
    }
}