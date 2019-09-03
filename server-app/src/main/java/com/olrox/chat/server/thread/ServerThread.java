package com.olrox.chat.server.thread;

import com.olrox.chat.server.manager.UsersManager;
import com.olrox.chat.server.manager.UsersManagerFactory;
import com.olrox.chat.server.message.author.ServerAsAuthor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread {

    private final static Logger logger = LogManager.getLogger(ServerThread.class);

    public final static ServerAsAuthor SERVER_AS_AUTHOR = new ServerAsAuthor("Server");
    private final int port;

    static {
        UsersManager usersManager = UsersManagerFactory.createUsersManager();
        usersManager.addOnlineUser(SERVER_AS_AUTHOR.getUsername());
    }

    public ServerThread(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            logger.info("Chat Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();

                logger.info("New user connected");

                UserThread newUserThread = new UserThread(socket);
                newUserThread.start();
            }
        } catch (IOException ex) {
            logger.error("Error in the server: ", ex);
        }
    }
}
