package com.olrox.chat.web;

import com.olrox.chat.server.message.Message;
import com.olrox.chat.server.message.MessageWriter;
import com.olrox.chat.server.message.author.Author;

public class WebSocketMessageWriter implements MessageWriter {
    private MessagingAdapter messagingAdapter;

    public WebSocketMessageWriter(MessagingAdapter messagingAdapter) {
        this.messagingAdapter = messagingAdapter;
    }

    @Override
    public void write(String string, Author author) {
        try {
            messagingAdapter.receiveText(string);
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
