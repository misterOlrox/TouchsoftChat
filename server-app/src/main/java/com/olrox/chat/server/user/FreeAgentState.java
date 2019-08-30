package com.olrox.chat.server.user;

import com.olrox.chat.server.message.Message;

public class FreeAgentState implements UserState {
    @Override
    public void processMessage(UserThread userThread, Message message){
        switch (message.getCommandType()){
            case REGISTER:
                userThread.writeAsServer("You are already registered");
                break;
            case MESSAGE:
                break;
            case LEAVE:
                userThread.writeAsServer("You are agent. You can't leave.");
                break;
            case EXIT:
            case NULL:
                userThread.closeConnections();
                break;
        }
    }
}
