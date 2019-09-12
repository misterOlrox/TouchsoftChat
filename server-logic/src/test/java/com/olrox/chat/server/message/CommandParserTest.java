package com.olrox.chat.server.message;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandParserTest {

    @Test
    void parse() {
        assertEquals(CommandType.MESSAGE, CommandParser.parse("bvcbc"));
        assertEquals(CommandType.REGISTER, CommandParser.parse("/register agent Mike"));
        assertEquals(CommandType.LEAVE, CommandParser.parse("/leave"));
        assertEquals(CommandType.EXIT, CommandParser.parse("/exit"));
        assertEquals(CommandType.EXIT, CommandParser.parse(null));
        assertEquals(CommandType.MESSAGE, CommandParser.parse("/not_a_command"));
    }
}