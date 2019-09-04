package com.olrox.chat.server.message;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    @Test
    void getCommandType() {
        Message message1 = new Message("bvcbc");
        Message message2 = new Message("/register agent Mike");
        Message message3 = new Message("/leave");
        Message message4 = new Message("/exit");

        assertEquals(message1.getCommandType(), CommandType.MESSAGE);
        assertEquals(message2.getCommandType(), CommandType.REGISTER);
        assertEquals(message3.getCommandType(), CommandType.LEAVE);
        assertEquals(message4.getCommandType(), CommandType.EXIT);
    }
}