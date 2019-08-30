package com.olrox.chat.server.message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class MessageReader {
    private BufferedReader reader;

    public MessageReader(Socket socket) throws IOException {
        InputStream input = socket.getInputStream();
        reader = new BufferedReader(new InputStreamReader(input));
    }

    public Message readMessage() throws IOException{
        String data = reader.readLine();

        Message message = new Message(data);

        return message;
    }


}
