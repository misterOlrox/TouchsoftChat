package com.olrox.chat.server;

import com.olrox.chat.server.io.IoServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerApplication{

    private final static int PORT = 50000;
    private final static Logger LOGGER = LogManager.getLogger(ServerApplication.class);

    public static void main(String[] args) {
        LOGGER.info("Server Application is starting.");
        IoServer server = new IoServer(PORT);
        server.start();
    }
}
