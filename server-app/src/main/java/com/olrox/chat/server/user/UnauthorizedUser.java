package com.olrox.chat.server.user;

import com.olrox.chat.server.UserThread;
import com.olrox.chat.server.message.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.StringTokenizer;

public class UnauthorizedUser implements User {

    private final static Logger logger = LogManager.getLogger(UnauthorizedUser.class);

    private UserThread thread;

    public UnauthorizedUser(UserThread thread) {
        this.thread = thread;
        thread.writeServerAnswer("Hello");
        writeOptions();
    }

    @Override
    public void register(Message message) {
        String response = message.getText();
        StringTokenizer tokenizer = new StringTokenizer(response, " ");

        if(tokenizer.countTokens()!=3) {
            thread.writeServerAnswer("Incorrect command.");
            return;
        }

        tokenizer.nextToken();
        String userType =  tokenizer.nextToken();
        String username = tokenizer.nextToken();

        String serverAnswer = "You are successfully registered as "
                + userType + " " + username;
        String loggerInfo = "User was registered as "
                + userType + " " + username;

        if(userType.equals("agent")){
            thread.writeServerAnswer(serverAnswer);
            logger.info(loggerInfo);
            FreeAgent agent = new FreeAgent(this, username);
            thread.setUser(agent);
            agent.findCompanion();
        } else if(userType.equals("client")){
            thread.writeServerAnswer(serverAnswer);
            logger.info(loggerInfo);
            thread.setUser(new FreeClient(this, username));
        } else {
            thread.writeServerAnswer("Sorry. You can't register as " + userType + ". Try again");
        }
    }

    @Override
    public void sendMessage(Message message) {
        writeOptions();
    }

    @Override
    public void leave(Message message) {
        writeOptions();
    }

    private void writeOptions() {
        thread.writeServerAnswer("Print \"/register [agent|client] YourName\" to register");
    }

    public UserThread getThread() {
        return thread;
    }
}