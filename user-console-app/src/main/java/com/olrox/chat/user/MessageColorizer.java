package com.olrox.chat.user;

public class MessageColorizer {
    public MessageColorizer() {
    }

    public String coloredMessage(String source) {
        if(source.startsWith("[Server]")){
            return "\033[33m" + source +"\033[m";
        } else {
            return "\033[32m" + source +"\033[m";
        }
    }
}
