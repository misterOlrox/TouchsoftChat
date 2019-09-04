package com.olrox.chat.server.message;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandTypeTest {

    @Test
    void getCode() {
        assertEquals(CommandType.REGISTER.getCode(), "/register");
        assertEquals(CommandType.MESSAGE.getCode(), "/message");
        assertEquals(CommandType.LEAVE.getCode(), "/leave");
        assertEquals(CommandType.EXIT.getCode(), "/exit");
    }
}