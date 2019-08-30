package com.olrox.chat.server.user;

import com.olrox.chat.server.Server;
import com.olrox.chat.server.message.*;
import com.olrox.chat.server.room.ChatRoom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.Socket;

public class UserThread extends Thread {

    private final static Logger logger = LogManager.getLogger(UserThread.class);

    private User user;
    private ChatRoom room;
    private Socket socket;
    private Server server;
    private MessageReader reader;
    private MessageWriter serverWriter;
    private MessageWriter userWriter;
    private UserState userState = new UnauthorizedState();

    public UserThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ChatRoom getRoom() {
        return room;
    }

    public void setRoom(ChatRoom room) {
        this.room = room;
    }

    public UserState getUserState() {
        return userState;
    }

    public void setUserState(UserState userState) {
        this.userState = userState;
    }

    public void run() {

        user = new User();
        Message message;
        CommandType command = null;

        try {

            reader = new MessageReader(socket);
            serverWriter = new ServerMessageWriter(socket, server);
            userWriter = new UserMessageWriter(socket);

            serverWriter.write("Hello");

            while (true){
                message = reader.readMessage();

                logger.debug("Current message from user: " + message);

                processMessageWithState(message);
            }

        } catch (IOException ex) {
            logger.error("Error in UserThread: ", ex);
        } finally {
            exit();
            closeConnections();
        }
    }

    private void leave() {
        if(room != null) {
            server.disconnectFromRoom(this);
        }
    }

    private void exit(){
        if(room != null) {
            server.exitFromRooms(this);
        }
    }

    public void findCompanion(){
        server.findCompanion(this);
    }

    public boolean checkCompatibility(UserThread other) {
        if(userState instanceof FreeUserState) {
            return ((FreeUserState) userState).checkCompatibility(other);
        } else {
            return false;
        }
    }

    /**
     * Sends a message to the client.
     */
    public void writeMessageFromUser(Message message) {
        userWriter.write(message);
    }

    public void writeAsServer(String message) {
        serverWriter.write(message);
    }

    public void writeAsServer(Message message) {
        serverWriter.write(message);
    }

    /**
     * Sends a message to the chat room.
     */
    public void sendToChat(Message message) throws IOException{
        if(user.getType() != UserType.UNAUTHORIZED) {
            findCompanion();
            message.setAuthor(this.user);
            this.room.deliverMessage(message, this);
        }
//        else {
//            login(message);
//        }
    }

    public synchronized void closeConnections()  {
        logger.debug("closeConnections() method Enter");
        if (socket != null){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(userWriter != null) {
            userWriter.close();
        }
        if(serverWriter != null) {
            serverWriter.close();
        }


        logger.debug("closeConnections() method Exit");
    }

    public void processMessageWithState(Message message){
        userState.processMessage(this, message);
    }
}
