package com.olrox.chat.web;

import com.olrox.chat.server.message.Message;
import com.olrox.chat.server.message.MessageWriter;
import com.olrox.chat.server.message.author.Author;
import org.json.JSONObject;

import javax.websocket.Session;

public class WebSocketMessageWriter implements MessageWriter {
    private Session session;

    public WebSocketMessageWriter(Session session) {
        this.session = session;
    }

    @Override
    public void write(String string, Author author) {

        Message message = new Message(string, author);

        try {
            session.getBasicRemote().sendText(String.valueOf(new JSONObject()
                    .put("author", message.getAuthor().getName())
                    .put("text", message.getText())
                    .put("time", message.getSendTime())
            ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(Message message) {
        write(message.getText(), message.getAuthor());
    }

    @Override
    public void close() {

    }
}
