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

    void read() throws IOException {
        message = messageReader.readMessage();
        state = PROCESSING;
        pool.execute(new Processor());
    }

    private synchronized void processAndHandOff() {
        send();
    }

    class Processor implements Runnable {
        public void run() {
            processAndHandOff();
        }
    }
}