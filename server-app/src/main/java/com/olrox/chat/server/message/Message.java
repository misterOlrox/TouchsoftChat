package com.olrox.chat.server.message;

import com.olrox.chat.server.message.author.Author;

import java.time.LocalDateTime;

public class Message {
    private Author author;
    private String text;
    private LocalDateTime sendTime;
    private CommandType commandType;

    public Message(String text) {
        this.text = text;
        sendTime = LocalDateTime.now();
        commandType = checkForCommand(text);
    }

    public Message(String text, Author author) {
        this.text = text;
        this.author = author;
        sendTime = LocalDateTime.now();
        commandType = checkForCommand(text);
    }

    private CommandType checkForCommand(String text) {
        if(text == null) {
            return CommandType.EXIT;
        }

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

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
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

    public CommandType getCommandType() {
        return commandType;
    }

    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }

    public String show() {
        return "[" + author.getUsername() + "] : " + text;
    }
}
