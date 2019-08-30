package com.olrox.chat.user;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class WriteThread extends Thread{
    private PrintWriter writer;
    private Socket socket;
    private Connection connection;

    public WriteThread(Socket socket, Connection connection) {
        this.socket = socket;
        this.connection = connection;

        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run() {

        Scanner in = new Scanner(System.in);

        String text;

        while (true) {
            text = in.nextLine();
            writer.println(text);

            if(text.equals("/exit")) {
                break;
            }
        }

        try {
            socket.close();
        } catch (IOException ex) {
            System.out.println("Error writing to server: " + ex.getMessage());
        }

        writer.println("You are exited from user-app.");
    }
}
