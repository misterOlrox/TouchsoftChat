package com.olrox.chat.server.message;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class MessageReaderTest {

    @Test
    void closeTest() throws IOException {
        Socket socket = Mockito.mock(Socket.class);
        InputStream inputStream = Mockito.mock(InputStream.class);
        Mockito.when(socket.getInputStream()).thenReturn(inputStream);
        MessageReader messageReader = new MessageReader(socket);

        messageReader.close();

        Mockito.verify(inputStream, Mockito.times(1)).close();
    }
}