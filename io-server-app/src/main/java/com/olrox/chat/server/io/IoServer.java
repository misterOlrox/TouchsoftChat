package com.olrox.chat.server.io;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class IoServer {

    private final static Logger LOGGER = LogManager.getLogger(IoServer.class);

    private final int port;

    public IoServer(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            LOGGER.info("Chat Server is listening on port " + port);

            while (!Thread.interrupted()) {
                Socket socket = serverSocket.accept();

                LOGGER.info("New user connected");

                SocketConnectionHandler handler = new SocketConnectionHandler(socket);
                handler.start();
            }
        } catch (IOException ex) {
            LOGGER.error("Error in the server: ", ex);
        }
    }
}
