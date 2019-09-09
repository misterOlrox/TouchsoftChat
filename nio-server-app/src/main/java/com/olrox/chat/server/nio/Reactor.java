package com.olrox.chat.server.nio;

import com.olrox.chat.server.manager.UsersManager;
import com.olrox.chat.server.manager.UsersManagerFactory;
import com.olrox.chat.server.message.author.ServerAsAuthor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Reactor implements Runnable {

    private final static Logger LOGGER = LogManager.getLogger(Reactor.class);
    public final static ServerAsAuthor SERVER_AS_AUTHOR = new ServerAsAuthor("Server");

    static {
        UsersManager usersManager = UsersManagerFactory.createUsersManager();
        usersManager.addOnlineUser(SERVER_AS_AUTHOR.getUsername());
    }

    private final Selector selector;
    private final ServerSocketChannel serverSocketChannel;
    private final boolean isWithThreadPool;

    public Reactor(int port, boolean isWithThreadPool) throws IOException {

        this.isWithThreadPool = isWithThreadPool;
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);
        SelectionKey selectionKey0 = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        selectionKey0.attach(new Acceptor());
    }


    public void run() {
        LOGGER.info("Chat Server is listening on port " + serverSocketChannel.socket().getLocalPort());
        try {
            while (!Thread.interrupted()) {
                selector.select();
                Set selected = selector.selectedKeys();
                Iterator it = selected.iterator();
                while (it.hasNext()) {
                    dispatch((SelectionKey) (it.next()));
                }
                selected.clear();
            }
        } catch (IOException ex) {
            LOGGER.error("Error in the reactor: ", ex);
        }
    }

    private void dispatch(SelectionKey k) {
        Runnable r = (Runnable) (k.attachment());
        if (r != null) {
            r.run();
        }
    }

    private class Acceptor implements Runnable {
        public void run() {
            try {
                SocketChannel socketChannel = serverSocketChannel.accept();
                if (socketChannel != null) {
                    if (isWithThreadPool)
                        new UserHandlerWithThreadPool(selector, socketChannel);
                    else
                        new UserHandler(selector, socketChannel);
                }
                LOGGER.info("New connection accepted by Reactor");
            } catch (IOException ex) {
                LOGGER.error("Error in the reactor: ", ex);
            }
        }
    }
}