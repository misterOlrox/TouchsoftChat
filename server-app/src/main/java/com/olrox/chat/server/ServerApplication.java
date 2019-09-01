package com.olrox.chat.server;

import com.olrox.chat.server.manager.UsersManager;
import com.olrox.chat.server.message.ServerAsAuthor;
import com.olrox.chat.server.thread.ServerThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerApplication{

    public final static ServerAsAuthor SERVER_AS_AUTHOR = new ServerAsAuthor("Server");
    private final static int PORT = 50000;
    private final static Logger logger = LogManager.getLogger(ServerApplication.class);

    static {
        UsersManager.addOnlineUser(SERVER_AS_AUTHOR.getUsername());
    }

    public static void main(String[] args) {
        logger.info("Server Application is starting.");
        ServerThread server = new ServerThread(PORT);
        server.start();
    }
}
