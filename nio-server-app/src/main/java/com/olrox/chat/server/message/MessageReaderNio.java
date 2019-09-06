package com.olrox.chat.server.message;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class MessageReaderNio implements MessageReader{
    SocketChannel socketChannel;
    ByteBuffer input = ByteBuffer.allocate(1024);
    String data;

    public MessageReaderNio(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public Message readMessage() throws IOException {
        int readCount = socketChannel.read(input);
        if (readCount > 0) {
            readProcess(readCount);
        }
        return new Message(data);
    }

    private synchronized void readProcess(int readCount) {
        StringBuilder sb = new StringBuilder();
        input.flip();
        byte[] subStringBytes = new byte[readCount];
        byte[] array = input.array();
        System.arraycopy(array, 0, subStringBytes, 0, readCount);
        // Assuming ASCII (bad assumption but simplifies the example)
        sb.append(new String(subStringBytes));
        input.clear();
        data = sb.toString().trim();
    }

    @Override
    public void close() throws IOException {

    }
}
