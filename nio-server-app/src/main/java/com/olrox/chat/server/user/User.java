package com.olrox.chat.server.user;

import com.olrox.chat.server.message.Message;
import com.olrox.chat.server.message.MessageWriter;
import com.olrox.chat.server.message.author.Author;
import com.olrox.chat.server.message.author.AuthorType;
import com.olrox.chat.server.nio.Reactor;
import com.olrox.chat.server.user.state.UnauthorizedState;
import com.olrox.chat.server.user.state.UserState;

public class User implements Author {
    private UserState state;
    private AuthorType authorType;
    private String username;
    private MessageWriter messageWriter;

    public User(MessageWriter messageWriter) {
        this.messageWriter = messageWriter;
        this.state = new UnauthorizedState(this);
    }

    public User(UserState state) {
        this.state = state;
    }

    public User(UserState state, AuthorType authorType, String username, MessageWriter messageWriter) {
        this.state = state;
        this.authorType = authorType;
        this.username = username;
        this.messageWriter = messageWriter;
    }

    public UserState getState() {
        return state;
    }

    public void setState(UserState state) {
        this.state = state;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public AuthorType getAuthorType() {
        return authorType;
    }

    public void setAuthorType(AuthorType authorType) {
        this.authorType = authorType;
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
        messageWriter.close();
    }

    public void receiveFromUser(Message message) {
        messageWriter.write(message);
    }

    public void receiveFromServer(String text) {
        messageWriter.write(new Message(text, Reactor.SERVER_AS_AUTHOR));
    }
}
