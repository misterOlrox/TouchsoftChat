package com.olrox.chat.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerApplication {

    private final static int PORT = 50000;
    private final static Logger logger = LogManager.getLogger(ServerApplication.class);

    public static void main(String[] args) {
        logger.info("Server Application is starting.");
        Server server = new Server(PORT);
        server.start();
    }
}
