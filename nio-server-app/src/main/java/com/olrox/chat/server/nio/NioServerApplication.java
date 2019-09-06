package com.olrox.chat.server.nio;

import java.io.IOException;

public class NioServerApplication {
    public static void main(String[] args) throws IOException {

        Reactor reactor  = new Reactor(50000, false);
        new Thread(reactor).start();
    }
}
