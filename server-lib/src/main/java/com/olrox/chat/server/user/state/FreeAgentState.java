package com.olrox.chat.server.user.state;

import com.olrox.chat.server.exception.InvalidUserStateException;
import com.olrox.chat.server.manager.UsersManager;
import com.olrox.chat.server.manager.UsersManagerFactory;
import com.olrox.chat.server.message.Message;
import com.olrox.chat.server.message.author.AuthorType;
import com.olrox.chat.server.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FreeAgentState implements UserState {

    private final static Logger LOGGER = LogManager.getLogger(FreeAgentState.class);

    private final User user;
    private final UsersManager usersManager;

    public FreeAgentState(UnauthorizedState user){
        this.user = user.getUser();
        this.user.setAuthorType(AuthorType.AGENT);
        usersManager = UsersManagerFactory.createUsersManager();
    }

    public FreeAgentState(BusyAgentState busyAgent) {
        this.user = busyAgent.getUser();
        usersManager = UsersManagerFactory.createUsersManager();
    }

    public FreeAgentState(User user) {
        this.user = user;
        usersManager = UsersManagerFactory.createUsersManager();
    }

    public synchronized void findCompanion(){
        LOGGER.debug("Agent " + this.user.getUsername() + " trying to find client");
        User companion = usersManager.pollFreeClient();
        if(companion != null){
            connect(companion);
        } else {
            user.receiveFromServer("Wait for your companion.");
            usersManager.addFreeAgent(this.user);
        }
    }

    private void connect(User companion){
        FreeClientState companionState;
        if(companion.getState() instanceof FreeClientState) {
            companionState = (FreeClientState) companion.getState();
        } else {
            throw new InvalidUserStateException("Companion isn't in FreeClientState.");
        }

        BusyClientState busyClient = new BusyClientState(companionState);
        BusyAgentState busyAgent = new BusyAgentState(this);

        busyClient.setCompanion(busyAgent);
        busyAgent.setCompanion(busyClient);

        user.setState(busyAgent);
        companion.setState(busyClient);

        user.receiveFromServer("Now you chatting with client " + companion.getUsername());
        companion.receiveFromServer("Now you chatting with agent " + this.getUser().getUsername());

        LOGGER.info("Agent " + this.user.getUsername() + " start chat with client " + companion.getUsername());

        for (Message message : companionState.getMessages()) {
            busyAgent.receiveFromClient(message);
        }
    }

    @Override
    public void register(Message message) {
        user.receiveFromServer("You are already registered as agent " + this.user.getUsername());
    }

    @Override
    public void sendMessage(Message message) {
        user.receiveFromServer("You haven't companion. Your message will not be delivered.");
    }

    @Override
    public void leave() {
        user.receiveFromServer("You are agent. You can't leave.");
    }

    @Override
    public void exit() {
        usersManager.removeOnlineUser(this.user.getUsername());
    }

    public User getUser() {
        return user;
    }
}
