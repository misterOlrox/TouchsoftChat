package com.olrox.chat.server.nio;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HandlerWithThreadPool extends Handler {

    private final static Logger LOGGER = LogManager.getLogger(HandlerWithThreadPool.class);

    static ExecutorService pool = Executors.newFixedThreadPool(4);
    static final int PROCESSING = 2;

    public HandlerWithThreadPool(Selector sel, SocketChannel c) throws IOException {
        super(sel, c);
    }

    void read() {
        state = PROCESSING;
        pool.execute(new Processor());

        selectionKey.interestOps(SelectionKey.OP_WRITE);
    }

    //Start processing in a new Processer Thread and Hand off to the reactor thread.
    private synchronized void processAndHandOff() throws IOException {
        message = messageReader.readMessage();

        //Read processing done. Now the server is ready to send a message to the client.
        state = SENDING;
    }

    class Processor implements Runnable {
        public void run() {
            try {
                LOGGER.info("Process runs in processor.");

                processAndHandOff();
            } catch (IOException ex) {
                LOGGER.error("Error in Processer: ", ex);
            }
        }
    }
}