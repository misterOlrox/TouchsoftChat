package com.olrox.chat.server.message;

import com.olrox.chat.server.user.User;

import java.time.LocalDateTime;

public class MessageFromServer implements Message{
    private String text;
    private LocalDateTime sendTime;
    private User recipient;

    public MessageFromServer(User recipient){
        this.recipient = recipient;
    }

    public MessageFromServer(String text, User recipient) {
        this.text = text;
        this.recipient = recipient;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getSendTime() {
        return sendTime;
    }

    public void setSendTime(LocalDateTime sendTime) {
        this.sendTime = sendTime;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }
}
