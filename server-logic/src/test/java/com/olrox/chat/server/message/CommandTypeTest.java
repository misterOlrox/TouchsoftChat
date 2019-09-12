package com.olrox.chat.server.message;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandTypeTest {

    @Test
    void getCode() {
        assertEquals("/register", CommandType.REGISTER.getCode());
        assertEquals("/message", CommandType.MESSAGE.getCode());
        assertEquals("/leave", CommandType.LEAVE.getCode());
        assertEquals("/exit", CommandType.EXIT.getCode());
    }
}