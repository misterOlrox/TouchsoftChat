package com.olrox.chat.server;

import com.olrox.chat.server.room.ChatRoom;
import com.olrox.chat.server.room.ChatRooms;
import com.olrox.chat.server.user.UserThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Server {

    private final static Logger logger = LogManager.getLogger(Server.class);

    private int port;
    private ChatRooms chatRooms = new ChatRooms();
    private Set<UserThread> userThreads = new HashSet<>();

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            logger.info("Chat Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();

                logger.info("New user connected");

                UserThread newUserThread = new UserThread(socket, this);
                userThreads.add(newUserThread);
                newUserThread.start();

            }

        } catch (IOException ex) {
            logger.error("Error in the server: ", ex);
        }
    }

    public void connectToRoom(UserThread thread) {
        chatRooms.connectToRoom(thread);
    }
}
