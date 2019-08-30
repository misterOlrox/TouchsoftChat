package com.olrox.chat.server.user;

public interface FreeUserState implements UserState{
    boolean checkCompatibility(UserThread other);
}
