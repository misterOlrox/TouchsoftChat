package com.olrox.chat.server.message;

public class MessageUtils {
    public static String createDataToSocket(Message message) {
        return  "[" + message.getAuthor().getName() + "] : " + message.getText();
    }
}
