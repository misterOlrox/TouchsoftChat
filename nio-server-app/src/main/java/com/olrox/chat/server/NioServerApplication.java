package com.olrox.chat.server;

import com.olrox.chat.server.nio.Reactor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class NioServerApplication {

    private final static int PORT = 50000;
    private final static Logger LOGGER = LogManager.getLogger(NioServerApplication.class);

    public static void main(String[] args) throws IOException {
        LOGGER.info("Server Application is starting.");
        Reactor reactor = new Reactor(PORT, true);
        new Thread(reactor).start();
    }
}
