package com.olrox.chat.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Server implements Author {

    private final static Logger logger = LogManager.getLogger(Server.class);

    private final int port;
    private final String name;
    private ChatManager chatManager = new ChatManager();
    private Set<UserThread> userThreads = new HashSet<>();

    public Server(int port, String serverName) {
        this.port = port;
        this.name = serverName;
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

    @Override
    public String getName() {
        return name;
    }

//    public void findCompanion(UserThread thread) {
//        for(UserThread otherThread : userThreads) {
//            if(otherThread.getRoom() == null){
//
//            }
//        }
//    }
//
//    public void disconnectFromRoom(UserThread thread) {
//        chatManager.disconnect(thread);
//    }
//
//    public void exitFromRooms(UserThread thread) {
//        chatManager.exit(thread);
//        userThreads.remove(thread);
//        logger.info("Removed thread " + thread);
//    }
}
