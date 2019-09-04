package com.olrox.chat.server.manager;

import com.olrox.chat.server.user.User;

public class UsersManagerImpl implements UsersManager{
    private final static UsersData usersData = new UsersData();

    @Override
    public boolean hasFreeClient() {
        return usersData.hasFreeClient();
    }

    @Override
    public boolean hasFreeAgent() {
        return usersData.hasFreeAgent();
    }

    @Override
    public User pollFreeClient() {
        return usersData.pollFreeClient();
    }

    @Override
    public User pollFreeAgent() {
        return usersData.pollFreeAgent();
    }

    @Override
    public void addFreeClient(User client) {
        usersData.addFreeClient(client);
    }

    @Override
    public void addFreeAgent(User agent) {
        usersData.addFreeAgent(agent);
    }

    @Override
    public void addOnlineUser(String username) {
        usersData.addOnlineUser(username);
    }

    @Override
    public void removeOnlineUser(String username) {
        usersData.removeOnlineUser(username);
    }

    @Override
    public boolean checkIfOnline(String username) {
        return usersData.checkIfOnline(username);
    }
}
