//what is this class needed
package com.olrox.chat.server.manager;

public class UsersManagerFactory {
    public static UsersManager createUsersManager(){
        return new UsersManagerImpl();
    }
}
