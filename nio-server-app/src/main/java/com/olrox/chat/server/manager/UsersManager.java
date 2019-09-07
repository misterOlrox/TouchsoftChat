package com.olrox.chat.server.manager;

import com.olrox.chat.server.user.User;

public interface UsersManager {
    boolean hasFreeClient();

    boolean hasFreeAgent();

    User pollFreeClient();

    User pollFreeAgent();

    void addFreeClient(User client);

    void addFreeAgent(User agent);

    void addOnlineUser(String username);

    void removeOnlineUser(String username);

    boolean checkIfOnline(String username);
}
