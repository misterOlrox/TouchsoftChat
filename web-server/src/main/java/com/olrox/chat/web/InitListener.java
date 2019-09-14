package com.olrox.chat.web;

import com.olrox.chat.server.nio.Reactor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;

@WebListener
public class InitListener implements ServletContextListener {

    private final static Logger LOGGER = LogManager.getLogger(InitListener.class);
    private final static int REACTOR_PORT = 50000;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("Reactor is starting.");
        Reactor reactor = null;
        try {
            reactor = new Reactor(REACTOR_PORT, true);
        } catch (IOException e) {
            LOGGER.error("Error while starting Reactor" + e);
        }
        new Thread(reactor).start();
    }
}
