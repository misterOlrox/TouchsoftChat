package com.olrox.chat.web;

import com.olrox.chat.server.io.IoServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class InitListener implements ServletContextListener {

    private final static Logger LOGGER = LogManager.getLogger(InitListener.class);
    //this port can be busy so app wil always crash
    private final static int TCP_PORT = 50000;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("IO tcp server is starting.");
        IoServer server = new IoServer(TCP_PORT);
        server.start();
        LOGGER.info("Continue working");
    }
}
