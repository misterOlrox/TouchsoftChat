package com.olrox.chat.server.message;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MessageReaderIoTest {

    @Test
    void closeTest() throws IOException {
        Socket socket = Mockito.mock(Socket.class);
        InputStream inputStream = Mockito.mock(InputStream.class);
        Mockito.when(socket.getInputStream()).thenReturn(inputStream);
        MessageReader messageReader = new MessageReaderIo(socket);

        messageReader.close();

        Mockito.verify(inputStream, Mockito.times(1)).close();
    }

    @Test
    void readMessage() throws IOException {
        Socket socket = mock(Socket.class);
        InputStream inputStream = mock(InputStream.class);
        when(socket.getInputStream()).thenReturn(inputStream);
        BufferedReader bufferedReader = mock(BufferedReader.class);
        String text = "Text from Message";
        when(bufferedReader.readLine()).thenReturn(text);
        MessageReaderIo messageReader = new MessageReaderIo(socket);
        messageReader.setReader(bufferedReader);

        Message message = messageReader.readMessage();

        assertEquals(text, message.getText());
    }
}