package com.olrox.chat.server.room;

import com.olrox.chat.server.user.User;
import com.olrox.chat.server.user.UserThread;
import com.olrox.chat.server.user.UserType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashSet;
import java.util.Queue;

public class ChatRooms {

    private final static Logger logger = LogManager.getLogger(ChatRooms.class);

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

    // FIXME please
    public void connectToRoom(UserThread userThread){
        if(userThread.getRoom() != null) {
            logger.debug("User " + userThread.getUser().getName() + " already in room.");
            return;
        }

        ChatRoom room = null;

        User user = userThread.getUser();

        if(user.getType() == UserType.AGENT && !hasFreeClient()){
            room = new ChatRoom();
            room.setAgentThread(userThread);
            addRoomToFreeAgents(room);
            userThread.writeAsServer("Wait your client.");
        }
        if(user.getType() == UserType.AGENT && hasFreeClient()){
            room = pollClientRoom();
            room.setAgentThread(userThread);
            userThread.writeAsServer("You joined client " + room.getClientThread().getUser().getName());
            room.getClientThread().writeAsServer("Agent joined you");
            room.deliverSavedMessages();
        }
        if(user.getType() == UserType.CLIENT && !hasFreeAgent()){
            room = new ChatRoom();
            room.setClientThread(userThread);
            addRoomToFreeClients(room);
            userThread.writeAsServer("Wait your agent.");
        }
        if(user.getType() == UserType.CLIENT && hasFreeAgent()){
            room = pollAgentRoom();
            room.setClientThread(userThread);
            room.getAgentThread().writeAsServer("Client joined you.");
            userThread.writeAsServer("You joined agent " + room.getAgentThread().getUser().getName());
        }

        userThread.setRoom(room);

        this.allRooms.add(room);
    }
}
