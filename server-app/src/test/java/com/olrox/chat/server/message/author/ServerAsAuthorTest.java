package com.olrox.chat.server.message.author;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ServerAsAuthorTest {

    @Test
    void getUsernameTest1() {
        ServerAsAuthor serverAsAuthor = new ServerAsAuthor("SERVER");

        assertEquals(serverAsAuthor.getUsername(), "SERVER");
    }

    @Test
    void getUsernameTest2() {
        ServerAsAuthor serverAsAuthor = new ServerAsAuthor("server");

        assertNotEquals(serverAsAuthor.getUsername(), "SERVER");
    }

    @Test
    void getAuthorType() {
        ServerAsAuthor serverAsAuthor = new ServerAsAuthor("SERVER");

        assertEquals(serverAsAuthor.getAuthorType(), AuthorType.SERVER);
        assertNotEquals(serverAsAuthor.getAuthorType(), AuthorType.AGENT);
    }
}