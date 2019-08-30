package com.olrox.chat.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReadThread extends Thread {
    private BufferedReader reader;
    private Socket socket;
    private Connection connection;

    public ReadThread(Socket socket, Connection connection) {
        this.socket = socket;
        this.connection = connection;

        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException ex) {
            System.out.println("Error getting input stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                if(reader.ready()) {
                    String response = reader.readLine();
                    if(response == null){
                        System.out.println("Response is null. Exiting.");
                        break;
                    }
                    System.out.println(response);
                }
            } catch (IOException ex) {
                System.out.println("Error reading from server: " + ex.getMessage());
                ex.printStackTrace();
                break;
            }
        }
    }
}
