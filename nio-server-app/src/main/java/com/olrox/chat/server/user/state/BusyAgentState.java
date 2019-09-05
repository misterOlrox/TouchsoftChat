package com.olrox.chat.server.user.state;

import com.olrox.chat.server.manager.UsersManager;
import com.olrox.chat.server.manager.UsersManagerFactory;
import com.olrox.chat.server.message.Message;
import com.olrox.chat.server.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BusyAgentState implements UserState {

    private final static Logger LOGGER = LogManager.getLogger(BusyAgentState.class);

    private final User user;
    private BusyClientState companion;
    private final UsersManager usersManager;

    // for testing
    public BusyAgentState(User user, BusyClientState companion, UsersManager usersManager) {
        this.user = user;
        this.companion = companion;
        this.usersManager = usersManager;
    }

    public BusyAgentState(FreeAgentState agent) {
        this.user = agent.getUser();
        usersManager = UsersManagerFactory.createUsersManager();
    }

    @Override
    public void register(Message message) {
        user.receiveFromServer("You are agent " + this.user.getUsername() + " chatting with client " +
                companion.getUser().getUsername() + ". You needn't to register.");
    }

    @Override
    public void sendMessage(Message message) {
        companion.receiveFromAgent(message);
    }

    public void receiveFromClient(Message message) {
        user.receiveFromUser(message);
    }

    @Override
    public void leave() {
        this.user.receiveFromServer("You left client " + companion.getUser().getUsername());
        companion.getUser().receiveFromServer("Agent " + this.user.getUsername() + " left.");

        this.setFree();
        companion.setFree();

        LOGGER.info("Agent " + this.user.getUsername() +
                " leave chat with client " + companion.getUser().getUsername());
    }

    @Override
    public void exit() {
        companion.getUser().receiveFromServer("Agent " + this.user.getUsername() + " exited.");
        usersManager.removeOnlineUser(this.user.getUsername());
        companion.setFree();

        LOGGER.info("Agent " + this.user.getUsername() +
                " exit from chat with client " + companion.getUser().getUsername());
    }

    public synchronized void setCompanion(BusyClientState companion) {
        this.companion = companion;
    }

    public synchronized void setFree(){
        FreeAgentState freeAgent = new FreeAgentState(this);
        this.user.setState(freeAgent);
        freeAgent.findCompanion();
    }

    public User getUser() {
        return user;
    }
}
