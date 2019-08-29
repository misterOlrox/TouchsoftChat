package com.olrox.chat.server.room;

import com.olrox.chat.server.UserThread;
import com.olrox.chat.server.message.MessageFromServer;
import com.olrox.chat.server.user.Agent;
import com.olrox.chat.server.user.Client;
import com.olrox.chat.server.user.User;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashSet;
import java.util.Queue;

public class ChatRooms {

    private Collection<ChatRoom> allRooms = new HashSet<>();

    // TODO check if this is normal
    private Queue<ChatRoom> freeClients = new ArrayDeque<>();

    // TODO check if this is normal
    private Queue<ChatRoom> freeAgents = new ArrayDeque<>();

    public boolean hasFreeClient(){
        return !freeClients.isEmpty();
    }

    public boolean hasFreeAgent(){
        return !freeAgents.isEmpty();
    }

    public ChatRoom pollClientRoom(){
        return freeClients.poll();
    }

    public ChatRoom pollAgentRoom(){
        return freeAgents.poll();
    }

    public void addRoomToFreeClients(ChatRoom room){
        freeClients.add(room);
    }

    public void addRoomToFreeAgents(ChatRoom room) {
        freeAgents.add(room);
    }

    // FIXME
    public ChatRoom connectToRoom(UserThread userThread){
        ChatRoom room = null;

        User user = userThread.getUser();

        MessageFromServer message = new MessageFromServer(user);

        if(user instanceof Agent && !hasFreeClient()){
            room = new ChatRoom();
            room.setAgentThread(userThread);
            addRoomToFreeAgents(room);
            message.setText("Wait your client.");
        }
        if(user instanceof Agent && hasFreeClient()){
            room = pollClientRoom();
            room.setAgentThread(userThread);
            message.setText("You joined client " + room.getClientThread().getUser().getUsername());
        }
        if(user instanceof Client && !hasFreeAgent()){
            room = new ChatRoom();
            room.setClientThread(userThread);
            addRoomToFreeClients(room);
            message.setText("Wait your agent.");
        }
        if(user instanceof Client && hasFreeAgent()){
            room = pollAgentRoom();
            room.setClientThread(userThread);
            message.setText("You joined agent " + room.getAgentThread().getUser().getUsername());
        }

        message.setSendTime(LocalDateTime.now());
        room.saveMessage(message);

        return room;
    }
}
