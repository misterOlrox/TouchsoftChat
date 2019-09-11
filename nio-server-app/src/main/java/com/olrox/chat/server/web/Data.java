package com.olrox.chat.server.web;

import com.olrox.chat.server.message.Message;
import com.olrox.chat.server.user.User;

/**
 * Data is a wrapper for transmittable object between client and server.
 * @author amrishodiq
 */
public class Data {
    public static final int AUTHENTICATION_LOGIN = 1;

    public static final int MESSAGING_SEND = 101;

    public int operation;
    public User user;
    public Message message;
    public String session;
}
