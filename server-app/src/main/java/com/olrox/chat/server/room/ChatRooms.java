package com.olrox.chat.server.room;

import com.olrox.chat.server.user.User;
import com.olrox.chat.server.user.UserThread;
import com.olrox.chat.server.user.UserType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

// TODO remake in Observer??? or remove
public class ChatRooms {

    private final static Logger logger = LogManager.getLogger(ChatRooms.class);

    // TODO check if this is normal
    private Queue<ChatRoom> freeClients = new ConcurrentLinkedQueue<>();

    // TODO check if this is normal
    private Queue<ChatRoom> freeAgents = new ConcurrentLinkedQueue<>();

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
    public void connect(UserThread userThread){
        if(userThread.getRoom() != null) {
            logger.debug("User " + userThread.getUser().getName() + " already in room " + userThread.getRoom());
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
    }

    public void disconnect(UserThread userThread) {
        ChatRoom currentRoom = userThread.getRoom();

        if(userThread == currentRoom.getClientThread() && currentRoom.getAgentThread() != null) {
            currentRoom.getAgentThread().writeAsServer(userThread.getUser().getName() + " leaved.");
        }
        if(userThread == currentRoom.getAgentThread() && currentRoom.getClientThread() != null){
            currentRoom.getClientThread().writeAsServer(userThread.getUser().getName() + " leaved.");
        }

        disconnectClient(currentRoom.getClientThread());
    }

    public void exit(UserThread userThread) {
        ChatRoom currentRoom = userThread.getRoom();

        if(userThread == currentRoom.getClientThread() && currentRoom.getAgentThread() != null) {
            currentRoom.getAgentThread().writeAsServer(userThread.getUser().getName() + " exited.");
            exitClient(currentRoom.getClientThread());
        }
        if(userThread == currentRoom.getAgentThread() && currentRoom.getClientThread() != null){
            currentRoom.getClientThread().writeAsServer(userThread.getUser().getName() + " exited.");
            exitAgent(currentRoom.getAgentThread());
        }

        logger.debug("EXITING: " + this);

    }

    private void disconnectClient(UserThread userThread){
        if(userThread == null) {
            return;
        }

        ChatRoom currentRoom = userThread.getRoom();
        currentRoom.setClientThread(null);
        freeAgents.add(currentRoom);

        userThread.setRoom(null);
    }

    private void disconnectAgent(UserThread userThread) {
        if(userThread == null) {
            return;
        }

        ChatRoom currentRoom = userThread.getRoom();
        currentRoom.setAgentThread(null);
        freeClients.add(currentRoom);
        connect(currentRoom.getClientThread());

        userThread.setRoom(null);
    }

    private void exitClient(UserThread userThread){
        if(userThread == null) {
            return;
        }

        ChatRoom currentRoom = userThread.getRoom();
        currentRoom.setClientThread(null);

        userThread.setRoom(null);


    }

    private void exitAgent(UserThread userThread) {
        if(userThread == null) {
            return;
        }

        ChatRoom currentRoom = userThread.getRoom();
        currentRoom.setAgentThread(null);
        connect(currentRoom.getClientThread());

        userThread.setRoom(null);
    }

    @Override
    public String toString() {
        return "ChatRooms{" +
                "freeClients=" + freeClients +
                ", freeAgents=" + freeAgents +
                '}';
    }
}
