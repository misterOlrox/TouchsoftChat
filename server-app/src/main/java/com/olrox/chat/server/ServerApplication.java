package com.olrox.chat.server;

import com.olrox.chat.server.thread.ServerThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerApplication{

    private final static int PORT = 50000;
    private final static Logger logger = LogManager.getLogger(ServerApplication.class);

    public static void main(String[] args) {
        logger.info("Server Application is starting.");
        ServerThread server = new ServerThread(PORT);
        server.start();
    }
}
