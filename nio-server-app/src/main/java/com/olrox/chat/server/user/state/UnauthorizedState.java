package com.olrox.chat.server.user.state;

import com.olrox.chat.server.manager.UsersManager;
import com.olrox.chat.server.manager.UsersManagerFactory;
import com.olrox.chat.server.message.Message;
import com.olrox.chat.server.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.StringTokenizer;

public class UnauthorizedState implements UserState {

    private final static Logger LOGGER = LogManager.getLogger(UnauthorizedState.class);

    private final User user;
    private final UsersManager usersManager;

    public UnauthorizedState(User user) {
        this.user = user;
        usersManager = UsersManagerFactory.createUsersManager();
        user.receiveFromServer("Hello");
        writeOptions();
    }

    @Override
    public void register(Message message) {
        String response = message.getText();
        StringTokenizer tokenizer = new StringTokenizer(response, " ");

        if(tokenizer.countTokens() != 3) {
            user.receiveFromServer("Incorrect command.");
            return;
        }

        tokenizer.nextToken();
        String userType =  tokenizer.nextToken();
        String username = tokenizer.nextToken();

        if(!userType.equals("agent") && !userType.equals("client")){
            user.receiveFromServer("Sorry. You can't register as " + userType + ". Try again");
            return;
        }

        boolean isNewUser = usersManager.addOnlineUser(username);

        if(!isNewUser){
            user.receiveFromServer("User with username " + username + " already exists.");
            return;
        }

        String serverAnswer = "You are successfully registered as "
                + userType + " " + username;
        String loggerInfo = "User was registered as "
                + userType + " " + username;

        user.setUsername(username);
        user.receiveFromServer(serverAnswer);
        LOGGER.info(loggerInfo);

        if(userType.equals("agent")){
            FreeAgentState state = new FreeAgentState(this);
            user.setState(state);
            state.findCompanion();
        }
        if(userType.equals("client")){
            user.setState(new FreeClientState(this));
        }
    }

    @Override
    public void sendMessage(Message message) {
        writeOptions();
    }

    @Override
    public void leave() {
        writeOptions();
    }

    @Override
    public void exit() {
        // do nothing
    }

    private void writeOptions() {
        user.receiveFromServer("Print \"/register [agent|client] YourName\" to register");
    }

    public User getUser() {
        return user;
    }
}