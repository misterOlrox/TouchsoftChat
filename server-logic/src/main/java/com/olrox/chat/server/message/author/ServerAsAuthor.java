package com.olrox.chat.server.message.author;

import com.olrox.chat.server.manager.UsersManager;
import com.olrox.chat.server.manager.UsersManagerFactory;

public class ServerAsAuthor implements Author {
    private final static String NAME = "Server";
    private final static ServerAsAuthor INSTANCE = new ServerAsAuthor();

    private ServerAsAuthor(){
        UsersManager usersManager = UsersManagerFactory.createUsersManager();
        usersManager.addOnlineUser(this.getName());
    }

    public static ServerAsAuthor getInstance(){
        return INSTANCE;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public AuthorType getAuthorType() {
        return AuthorType.SERVER;
    }
}
