package com.olrox.chat.server.user.state;

import com.olrox.chat.server.manager.UsersManager;
import com.olrox.chat.server.manager.UsersManagerFactory;
import com.olrox.chat.server.message.Message;
import com.olrox.chat.server.message.author.AuthorType;
import com.olrox.chat.server.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class FreeClientState implements UserState {

    private final static Logger LOGGER = LogManager.getLogger(FreeClientState.class);

    private final User user;
    private List<Message> messages = new ArrayList<>();
    private boolean isWaiting = false;
    private final UsersManager usersManager;

    public FreeClientState(UnauthorizedState previousState) {
        this.user = previousState.getUser();
        this.user.setAuthorType(AuthorType.CLIENT);
        this.user.receiveFromServer("Type your messages and we will find you an agent.");
        usersManager = UsersManagerFactory.createUsersManager();
    }

    public FreeClientState(BusyClientState busyClientState) {
        this.user = busyClientState.getUser();
        usersManager = UsersManagerFactory.createUsersManager();
    }

    public FreeClientState(User user) {
        this.user = user;
        usersManager = UsersManagerFactory.createUsersManager();
    }

    private void findCompanion() {
        User companion = usersManager.pollFreeAgent();
        if (companion != null) {
            connect(companion);
        } else {
            user.receiveFromServer("We haven't free agents. You can write messages and they will be saved.");
            usersManager.addFreeClient(this.user);
            isWaiting = true;
        }
    }

    private void connect(User companion) {
        UserState companionState = companion.getState();
        BusyClientState busyClient = new BusyClientState(this);
        BusyAgentState busyAgent = new BusyAgentState((FreeAgentState) companionState);

        busyClient.setCompanion(busyAgent);
        busyAgent.setCompanion(busyClient);

        this.user.setState(busyClient);
        companion.setState(busyAgent);

        user.receiveFromServer("Now you chatting with agent " + companion.getName());
        companion.receiveFromServer("Now you chatting with client " + this.user.getName());

        LOGGER.info("Client " + this.user.getName() + " start chat with agent " + companion.getName());

        for (Message message : messages) {
            busyAgent.receiveFromClient(message);
        }
    }

    @Override
    public void register(Message message) {
        user.receiveFromServer("You are already registered as client " + user.getName());
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
        usersManager.removeOnlineUser(user.getName());
    }

    public User getUser() {
        return user;
    }

    public List<Message> getMessages() {
        return messages;
    }
}