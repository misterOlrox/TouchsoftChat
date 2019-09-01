package com.olrox.chat.server.user;

import com.olrox.chat.server.thread.UserThread;
import com.olrox.chat.server.message.Message;
import com.olrox.chat.server.manager.UsersManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FreeAgent implements User {

    private final static Logger logger = LogManager.getLogger(UnauthorizedUser.class);

    private final UserThread thread;
    private String username;

    public FreeAgent(UnauthorizedUser user, String username){
        this.thread = user.getThread();
        this.username = username;
    }

    public FreeAgent(BusyAgent busyAgent) {
        this.thread = busyAgent.getThread();
        this.username = busyAgent.getUsername();
    }

    public synchronized void findCompanion(){
        logger.debug("Agent " + username + " trying to find client");
        if(UsersManager.hasFreeClient()){
            FreeClient companion = UsersManager.pollFreeClient();
            connect(companion);
        } else {
            thread.writeServerAnswer("You haven't companion. Your message will not be delivered.");
            UsersManager.addFreeAgent(this);
        }
    }

    private void connect(FreeClient companion){
        BusyClient busyClient = new BusyClient(companion);
        BusyAgent busyAgent = new BusyAgent(this);

        busyClient.setCompanion(busyAgent);
        busyAgent.setCompanion(busyClient);

        thread.setUserStatus(busyAgent);
        companion.getThread().setUserStatus(busyClient);

        thread.writeServerAnswer("Now you chatting with client " + companion.getUsername());
        companion.getThread().writeServerAnswer("Now you chatting with agent " + this.getUsername());

        for(Message message : companion.getMessages()) {
            busyAgent.receiveFromClient(message);
        }
    }

    @Override
    public void register(Message message) {
        thread.writeServerAnswer("You are already registered as agent " + username);
    }

    @Override
    public void sendMessage(Message message) {
        thread.writeServerAnswer("You haven't companion. Your message will not be delivered.");
    }

    @Override
    public void leave() {
        thread.writeServerAnswer("You are agent. You can't leave.");
    }

    @Override
    public void exit() {
        UsersManager.removeOnlineUser(username);
    }

    public String getUsername() {
        return username;
    }

    public UserThread getThread() {
        return thread;
    }
}
