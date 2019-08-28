package com.olrox.chat.server;

import java.time.LocalDateTime;

public class Message {
    private String text;
    private boolean isViewed;
    private LocalDateTime sendTime;

    public Message(String text) {
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
