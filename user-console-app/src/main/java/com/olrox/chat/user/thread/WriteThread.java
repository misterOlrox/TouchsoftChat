package com.olrox.chat.user.thread;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class WriteThread extends Thread{
    private PrintWriter writer;
    private Socket socket;

    public WriteThread(Socket socket) {
        this.socket = socket;

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
            if(socket.isClosed()) {
                System.out.println("Server is not available. Your last message wasn't delivered.");
                break;
            }

            text = in.nextLine();
            writer.println(text);

            if(text.equals("/exit")) {
                try {
                    socket.shutdownInput();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("You are exited from user-app.");
                break;
            }
        }
    }
}
