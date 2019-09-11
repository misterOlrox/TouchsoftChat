package com.olrox.chat.server.web;

import com.olrox.chat.server.message.*;
import com.olrox.chat.server.user.User;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

import java.io.IOException;

/**
 * MessagingAdapter responsible to handle connection, receiving data, forward
 * the data to @see id.amrishodiq.jettywebsocket.MessagingLogic and receive
 * data from @see id.amrishodiq.jettywebsocket.MessagingLogic to be
 * delivered to recipient.
 * @author amrishodiq
 */
public class MessagingAdapter extends WebSocketAdapter implements UserSession {

    private Session session;
    private User currentUser;
    private MessageWriter messageWriter;
    private MessageReader messageReader;

    @Override
    public void onWebSocketConnect(Session session) {
        super.onWebSocketConnect(session);

        messageWriter = new WebSocketMessageWriter(this);

        currentUser = new User(messageWriter);

        this.session = session;
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {

        this.session = null;

        System.err.println("Close connection "+statusCode+", "+reason);

        super.onWebSocketClose(statusCode, reason);
    }

    @Override
    public void onWebSocketText(String text) {
        super.onWebSocketText(text);

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

    @Override
    public void receiveText(String text) throws Exception {
        if (session != null && session.isOpen()) {
            session.getRemote().sendString(text);
        }
    }

    @Override
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    @Override
    public void disconnect(int status, String reason) {

        session.close(status, reason);
        disconnect(status, reason);
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
