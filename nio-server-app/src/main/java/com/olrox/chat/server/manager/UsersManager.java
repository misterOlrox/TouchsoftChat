package com.olrox.chat.server.manager;

import com.olrox.chat.server.user.User;

public interface UsersManager {
    boolean hasFreeClient();

    boolean hasFreeAgent();

    User pollFreeClient();

    User pollFreeAgent();

    void addFreeClient(User client);

    void addFreeAgent(User agent);

    boolean addOnlineUser(String username);

    void removeOnlineUser(String username);
}
