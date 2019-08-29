package com.olrox.chat.server.message;

import com.olrox.chat.server.user.User;

import java.time.LocalDateTime;

public class MessageFromUser implements Message{
    private User author;
    private String text;
    private boolean isViewed;
    private LocalDateTime sendTime;

    public MessageFromUser(User author, String text, User recipient) {
        this.author = author;
        this.text = text;
        sendTime = LocalDateTime.now();
        isViewed = false;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isViewed() {
        return isViewed;
    }

    public void setViewed(boolean viewed) {
        isViewed = viewed;
    }

    public LocalDateTime getSendTime() {
        return sendTime;
    }
}
