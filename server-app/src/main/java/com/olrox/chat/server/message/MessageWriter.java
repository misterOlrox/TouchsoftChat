package com.olrox.chat.server.message;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class MessageWriter {
    private PrintWriter writer;

    public MessageWriter(Socket socket) throws IOException {
        OutputStream output = socket.getOutputStream();
        writer = new PrintWriter(output, true);
    }
}
