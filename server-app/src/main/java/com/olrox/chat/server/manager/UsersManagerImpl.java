package com.olrox.chat.server.manager;

import com.olrox.chat.server.user.User;

public class UsersManagerImpl implements UsersManager{
    @Override
    public boolean hasFreeClient() {
        return UsersData.hasFreeClient();
    }

    @Override
    public boolean hasFreeAgent() {
        return UsersData.hasFreeAgent();
    }

    @Override
    public User pollFreeClient() {
        return UsersData.pollFreeClient();
    }

    @Override
    public User pollFreeAgent() {
        return UsersData.pollFreeAgent();
    }

    @Override
    public void addFreeClient(User client) {
        UsersData.addFreeClient(client);
    }

    @Override
    public void addFreeAgent(User agent) {
        UsersData.addFreeAgent(agent);
    }

    @Override
    public void addOnlineUser(String username) {
        UsersData.addOnlineUser(username);
    }

    @Override
    public void removeOnlineUser(String username) {
        UsersData.removeOnlineUser(username);
    }

    @Override
    public boolean checkIfOnline(String username) {
        return UsersData.checkIfOnline(username);
    }
}
