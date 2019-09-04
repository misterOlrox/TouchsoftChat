package com.olrox.chat.server.message;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class MessageWriterImplTest {

    @Test
    void closeTest() throws IOException {
        Socket socket = Mockito.mock(Socket.class);
        OutputStream out = Mockito.mock(OutputStream.class);
        Mockito.when(socket.getOutputStream()).thenReturn(out);
        MessageWriter messageWriter = new MessageWriterImpl(socket);

        messageWriter.close();

        Mockito.verify(out, Mockito.times(1)).close();
    }
}