package com.olrox.chat.server.user;

import com.olrox.chat.server.message.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.StringTokenizer;

public class UnauthorizedState implements UserState {

    private final static Logger logger = LogManager.getLogger(UnauthorizedState.class);

    @Override
    public void processMessage(UserThread userThread, Message message) {
        userThread.writeAsServer("Print \"/register [agent|client] YourName\" to register");

        switch (message.getCommandType()){
            case REGISTER:
                UserType result = register(userThread, message);
                if(result == UserType.AGENT){
                    userThread.setUserState(new FreeAgentState());
                }
                if(result == UserType.CLIENT){
                    userThread.setUserState(new FreeClientState());
                }
                break;
            case MESSAGE:
            case LEAVE:
                userThread.writeAsServer("You aren't authorized.");
                break;
            case EXIT:
            case NULL:
                userThread.closeConnections();
                break;
        }
    }

    private UserType register(UserThread userThread, Message message) {
        String response = message.getText();
        StringTokenizer tokenizer = new StringTokenizer(response, " ");

        if(tokenizer.countTokens()!=3) {
            userThread.writeAsServer("Incorrect command.");
            return UserType.UNAUTHORIZED;
        }

        tokenizer.nextToken();
        String userType =  tokenizer.nextToken();
        String username = tokenizer.nextToken();

        User user = userThread.getUser();

        user.setName(username);

        if(userType.equals("agent")){
            user.setType(UserType.AGENT);
        } else if(userType.equals("client")){
            user.setType(UserType.CLIENT);
        } else {
            userThread.writeAsServer("Sorry. You can't register as " + userType + ". Try again");
            return UserType.UNAUTHORIZED;
        }

        userThread.writeAsServer("You are successfully registered as "
                + userType
                + " "
                + user.getName());

        logger.info("User was registered as "
                + userType
                + " "
                + user.getName());

        return user.getType();
    }
}