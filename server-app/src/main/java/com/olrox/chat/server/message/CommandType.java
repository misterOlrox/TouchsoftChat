package com.olrox.chat.server.message;

public enum CommandType {
    MESSAGE("/message"),
    REGISTER("/register"),
    LEAVE("/leave"),
    EXIT("/exit");

    private String code;

    CommandType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
