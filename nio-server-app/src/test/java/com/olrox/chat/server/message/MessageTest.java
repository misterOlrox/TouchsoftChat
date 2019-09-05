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
        Message message5 = new Message(null);

        assertEquals(CommandType.MESSAGE, message1.getCommandType());
        assertEquals(CommandType.REGISTER, message2.getCommandType());
        assertEquals(CommandType.LEAVE, message3.getCommandType());
        assertEquals(CommandType.EXIT, message4.getCommandType());
        assertEquals(CommandType.EXIT, message5.getCommandType());
    }
}