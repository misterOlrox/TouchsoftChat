package com.olrox.chat.server.user;

import com.olrox.chat.server.message.Message;
import com.olrox.chat.server.message.MessageWriter;
import com.olrox.chat.server.message.author.Author;
import com.olrox.chat.server.message.author.AuthorType;
import com.olrox.chat.server.message.author.ServerAsAuthor;
import com.olrox.chat.server.user.state.UnauthorizedState;
import com.olrox.chat.server.user.state.UserState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class User implements Author {

    private final static Logger LOGGER = LogManager.getLogger(User.class);

    private UserState state;
    private AuthorType authorType;
    private String name;
    private MessageWriter messageWriter;

    public User(MessageWriter messageWriter) {
        this.messageWriter = messageWriter;
        this.state = new UnauthorizedState(this);
    }

    public User(UserState state) {
        this.state = state;
    }

    public User(UserState state, AuthorType authorType, String name, MessageWriter messageWriter) {
        this.state = state;
        this.authorType = authorType;
        this.name = name;
        this.messageWriter = messageWriter;
    }

    public UserState getState() {
        return state;
    }

    public void setState(UserState state) {
        this.state = state;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public AuthorType getAuthorType() {
        return authorType;
    }

    public void setAuthorType(AuthorType authorType) {
        this.authorType = authorType;
    }

    public MessageWriter getMessageWriter() {
        return messageWriter;
    }

    public void setMessageWriter(MessageWriter messageWriter) {
        this.messageWriter = messageWriter;
    }

    public void register(Message message){
        this.state.register(message);
    }

    public void sendMessage(Message message){
        this.state.sendMessage(message);
    }

    public void leave() {
        this.state.leave();
    }

    public void exit() {
        this.state.exit();
        LOGGER.info("User " + name + " exited");
        messageWriter.close();
    }

    public void receiveFromUser(Message message) {
        messageWriter.write(message);
    }

    public void receiveFromServer(String text) {
        messageWriter.write(new Message(text, ServerAsAuthor.getInstance()));
    }
}
