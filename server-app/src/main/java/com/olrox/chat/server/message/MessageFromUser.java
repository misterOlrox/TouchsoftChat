package com.olrox.chat.server.message;

import com.olrox.chat.server.user.User;

import java.time.LocalDateTime;

public class MessageFromUser implements Message{
    private User author;
    private String text;
    private boolean isViewed;
    private LocalDateTime sendTime;
    private CommandType commandType;

    public MessageFromUser(String text) {
        this.text = text;
        sendTime = LocalDateTime.now();
        isViewed = false;
        commandType = checkForCommand(text);
    }

    public MessageFromUser(User author, String text) {
        this(text);
        this.author = author;
    }

    private CommandType checkForCommand(String text) {
        if(!text.startsWith("/")) {
            return CommandType.MESSAGE;
        }

        for(CommandType command : CommandType.values()){
            if(text.startsWith(command.getCode())) {
                return command;
            }
        }

        return CommandType.MESSAGE;
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

    public CommandType getCommandType() {
        return commandType;
    }

    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }

    @Override
    public String show() {
        return toString();
    }

    @Override
    public String toString() {
        return "MessageFromUser{" +
                "author=" + author +
                ", text='" + text + '\'' +
                ", isViewed=" + isViewed +
                ", sendTime=" + sendTime +
                ", commandType=" + commandType +
                '}';
    }
}
