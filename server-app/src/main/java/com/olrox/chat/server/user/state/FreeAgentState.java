package com.olrox.chat.server.user.state;

import com.olrox.chat.server.manager.UsersManager;
import com.olrox.chat.server.message.Message;
import com.olrox.chat.server.message.author.AuthorType;
import com.olrox.chat.server.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FreeAgentState implements UserState {

    private final static Logger logger = LogManager.getLogger(UnauthorizedState.class);

    private final User user;

    public FreeAgentState(UnauthorizedState user){
        this.user = user.getUser();
        this.user.setAuthorType(AuthorType.AGENT);
    }

    public FreeAgentState(BusyAgentState busyAgent) {
        this.user = busyAgent.getUser();
    }

    public synchronized void findCompanion(){
        logger.debug("Agent " + this.user.getUsername() + " trying to find client");
        if(UsersManager.hasFreeClient()){
            User companion = UsersManager.pollFreeClient();
            connect(companion);
        } else {
            user.receiveFromServer("You haven't companion. Your message will not be delivered.");
            UsersManager.addFreeAgent(this.user);
        }
    }

    private void connect(User companion){
        FreeClientState companionState;
        if(companion.getState() instanceof FreeClientState) {
            companionState = (FreeClientState) companion.getState();
        } else {
            throw new RuntimeException("Companion isn't in FreeClientState.");
        }

        BusyClientState busyClient = new BusyClientState(companionState);
        BusyAgentState busyAgent = new BusyAgentState(this);

        busyClient.setCompanion(busyAgent);
        busyAgent.setCompanion(busyClient);

        user.setState(busyAgent);
        companion.setState(busyClient);

        user.receiveFromServer("Now you chatting with client " + companion.getUsername());
        companion.receiveFromServer("Now you chatting with agent " + this.getUser().getUsername());

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
        UsersManager.removeOnlineUser(this.user.getUsername());
    }

    public User getUser() {
        return user;
    }
}
