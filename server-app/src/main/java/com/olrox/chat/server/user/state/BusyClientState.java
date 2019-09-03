package com.olrox.chat.server.user.state;

import com.olrox.chat.server.manager.UsersManager;
import com.olrox.chat.server.manager.UsersManagerFactory;
import com.olrox.chat.server.message.Message;
import com.olrox.chat.server.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BusyClientState implements UserState {

    private final static Logger logger = LogManager.getLogger(BusyClientState.class);

    private final User user;
    private BusyAgentState companion;
    private final UsersManager usersManager;

    public BusyClientState(FreeClientState client) {
        this.user = client.getUser();
        usersManager = UsersManagerFactory.createUsersManager();
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
        this.user.receiveFromServer("You left agent " + companion.getUser().getUsername());
        companion.getUser().receiveFromServer("Client " + this.user.getUsername() + " left.");

        this.setFree();
        companion.setFree();

        logger.info("Client " + this.user.getUsername() +
                " leave chat with agent " + companion.getUser().getUsername());
    }

    @Override
    public void exit() {
        companion.getUser().receiveFromServer("Client " + this.user.getUsername() + " exited.");
        usersManager.removeOnlineUser(this.user.getUsername());
        companion.setFree();

        logger.info("Client " + this.user.getUsername() +
                " exit from chat with agent " + companion.getUser().getUsername());
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
