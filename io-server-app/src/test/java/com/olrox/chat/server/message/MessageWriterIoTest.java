package com.olrox.chat.server.message;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

class MessageWriterIoTest {

    @Test
    void close() throws IOException {
        Socket socket = mock(Socket.class);
        OutputStream out = mock(OutputStream.class);
        Mockito.when(socket.getOutputStream()).thenReturn(out);
        MessageWriter messageWriter = new MessageWriterIo(socket);

        messageWriter.close();

        Mockito.verify(out, Mockito.times(1)).close();
    }

    @Test
    void write() throws IOException {
        Socket socket = mock(Socket.class);
        OutputStream out = mock(OutputStream.class);
        Mockito.when(socket.getOutputStream()).thenReturn(out);
        MessageWriterIo messageWriter = new MessageWriterIo(socket);
        Message message = mock(Message.class);
        PrintWriter printWriter = mock(PrintWriter.class);
        messageWriter.setWriter(printWriter);

        messageWriter.write(message);

        Mockito.verify(printWriter, times(1)).println(message.show());
    }
}