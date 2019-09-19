package com.olrox.chat.server.user.state;

import com.olrox.chat.server.manager.UsersManager;
import com.olrox.chat.server.manager.UsersManagerFactory;
import com.olrox.chat.server.message.Message;
import com.olrox.chat.server.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BusyClientState implements UserState {

    private final static Logger LOGGER = LogManager.getLogger(BusyClientState.class);

    private final User user;
    private BusyAgentState companion;
    private final UsersManager usersManager;

    public BusyClientState(User user, BusyAgentState companion, UsersManager usersManager) {
        this.user = user;
        this.companion = companion;
        this.usersManager = usersManager;
    }

    public BusyClientState(FreeClientState client) {
        this.user = client.getUser();
        usersManager = UsersManagerFactory.createUsersManager();
    }

    @Override
    public void register(Message message) {
        user.receiveFromServer("You are client " + this.user.getName() + " chatting with agent " +
                companion.getUser().getName() + ". You needn't to register.");
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
        LOGGER.info("Client " + this.user.getName() +
                " leave chat with agent " + companion.getUser().getName());

        this.user.receiveFromServer("You left agent " + companion.getUser().getName());
        companion.getUser().receiveFromServer("Client " + this.user.getName() + " left.");

        this.setFree();
        companion.setFree();
    }

    @Override
    public void exit() {
        LOGGER.info("Client " + this.user.getName() +
                " exit from chat with agent " + companion.getUser().getName());

        companion.getUser().receiveFromServer("Client " + this.user.getName() + " exited.");
        usersManager.removeOnlineUser(this.user.getName());
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
