package com.olrox.chat.web;

import com.olrox.chat.server.NioServerApplication;
import com.olrox.chat.server.nio.Reactor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;

/**
 * The executable main class.
 * @author amrishodiq
 */
public class MessagingServer {

    private final static int PORT = 50000;
    private final static Logger LOGGER = LogManager.getLogger(NioServerApplication.class);

    private Server server;

    public void setup() {
        server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8080);
        server.addConnector(connector);

        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.setContextPath("/");
        server.setHandler(handler);

        handler.addServlet(MessagingServlet.class, "/messaging");
    }

    public void start() throws Exception {
        server.start();
        server.dump(System.err);
        server.join();
    }

    public static void main(String args[]) throws Exception {
        LOGGER.info("Server Application is starting.");
        Reactor reactor = new Reactor(PORT, true);
        new Thread(reactor).start();
        MessagingServer theServer = new MessagingServer();
        theServer.setup();
        theServer.start();

    }
}
