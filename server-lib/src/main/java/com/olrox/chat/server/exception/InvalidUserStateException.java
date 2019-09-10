package com.olrox.chat.server.exception;

public class InvalidUserStateException extends RuntimeException{

    public InvalidUserStateException(String message) {
        super(message);
    }
}
