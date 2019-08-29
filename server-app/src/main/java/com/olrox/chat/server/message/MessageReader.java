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

    // TODO
    public String readLine() throws IOException{
        String data = reader.readLine();

        CommandType command = checkForCommand(data);

        return data;

    }

    private CommandType checkForCommand(String string) {
        if(!string.startsWith("/")) {
            return null;
        }

        for(CommandType command : CommandType.values()){
            if(string.startsWith(command.getCode())) {
                return command;
            }
        }

        return null;
    }
}
