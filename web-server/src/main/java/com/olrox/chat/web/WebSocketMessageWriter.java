package com.olrox.chat.web;

import com.olrox.chat.server.message.Message;
import com.olrox.chat.server.message.MessageWriter;
import com.olrox.chat.server.message.author.Author;
import org.json.JSONObject;

import javax.websocket.Session;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

import static j2html.TagCreator.*;

public class WebSocketMessageWriter implements MessageWriter {
    private Session session;

    public WebSocketMessageWriter(Session session) {
        this.session = session;
    }

    @Override
    public void write(String string, Author author) {
        try {
            session.getBasicRemote().sendText(String.valueOf(new JSONObject()
                    .put("userMessage", createHtmlMessageFromSender(author.getUsername(), string))
                    .put("userlist", new HashSet())
            ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Builds a HTML element with a sender-name, a message, and a timestamp,
    private static String createHtmlMessageFromSender(String sender, String message) {
        return article(
                b(sender + " says:"),
                span(attrs(".timestamp"), new SimpleDateFormat("HH:mm:ss").format(new Date())),
                p(message)
        ).render();
    }

    @Override
    public void write(Message message) {
        write(message.getText(), message.getAuthor());
    }

    @Override
    public void close() {

    }
}
