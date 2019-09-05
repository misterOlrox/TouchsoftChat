package com.olrox.chat.server.message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class MessageReaderImpl implements MessageReader{
    private BufferedReader reader;

    public MessageReaderImpl(Socket socket) throws IOException {
        InputStream input = socket.getInputStream();
        reader = new BufferedReader(new InputStreamReader(input));
    }

    public Message readMessage() throws IOException{
        String text = reader.readLine();

        Message message = new Message(text);

        return message;
    }

    public void close() throws IOException {
        reader.close();
    }

    BufferedReader getReader() {
        return reader;
    }

    void setReader(BufferedReader reader) {
        this.reader = reader;
    }
}
