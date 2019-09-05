package com.olrox.chat.server.message.author;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ServerAsAuthorTest {

    @Test
    void getUsernameTest1() {
        ServerAsAuthor serverAsAuthor = new ServerAsAuthor("SERVER");

        assertEquals("SERVER", serverAsAuthor.getUsername());
    }

    @Test
    void getUsernameTest2() {
        ServerAsAuthor serverAsAuthor = new ServerAsAuthor("server");

        assertNotEquals("SERVER", serverAsAuthor.getUsername());
    }

    @Test
    void getAuthorType() {
        ServerAsAuthor serverAsAuthor = new ServerAsAuthor("SERVER");

        assertEquals(AuthorType.SERVER, serverAsAuthor.getAuthorType());
        assertNotEquals(AuthorType.AGENT, serverAsAuthor.getAuthorType());
    }
}