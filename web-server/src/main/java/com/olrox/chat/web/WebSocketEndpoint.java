package com.olrox.chat.web;

import com.olrox.chat.server.message.*;
import com.olrox.chat.server.user.User;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/chat")
public class WebSocketEndpoint {

    private Session session;
    private User currentUser;
    private MessageWriter messageWriter;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        messageWriter = new WebSocketMessageWriter(session);
        currentUser = new User(messageWriter);
    }

    @OnClose
    public void onClose() {
        this.session = null;
        exit();
    }

    @OnMessage
    public void onMessage(String text) {
        Message message = new Message(text);

        message.setCommandType(CommandParser.parse(text));

        message.setAuthor(currentUser);

        CommandType command = message.getCommandType();
        switch(command) {
            case REGISTER:
                register(message);
                break;
            case MESSAGE:
                sendMessage(message);
                break;
            case LEAVE:
                leave();
                break;
            case EXIT:
                exit();
        }
    }

    private void register(Message message){
        this.currentUser.register(message);
    }

    private void sendMessage(Message message){
        this.currentUser.sendMessage(message);
    }

    private void leave() {
        this.currentUser.leave();
    }

    private void exit() {
        this.currentUser.exit();
    }

}
