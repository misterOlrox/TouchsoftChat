package com.olrox.chat.server.user.state;

import com.olrox.chat.server.message.Message;
import com.olrox.chat.server.message.MessageWriter;
import com.olrox.chat.server.message.author.AuthorType;
import com.olrox.chat.server.user.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class UnauthorizedStateTest {

    @Test
    void registerAsAgent() {
        Message message = new Message("/register agent Mike1");
        User user = new User(Mockito.mock(MessageWriter.class));
        UnauthorizedState state = new UnauthorizedState(user);

        state.register(message);

        assertEquals("Mike1", user.getUsername());
        assertEquals(AuthorType.AGENT, user.getAuthorType());
        assertTrue(user.getState() instanceof FreeAgentState || user.getState() instanceof BusyAgentState);
    }

    @Test
    void registerAsClient() {
        Message message = new Message("/register client Alice");
        User user = new User(Mockito.mock(MessageWriter.class));
        UnauthorizedState state = new UnauthorizedState(user);

        state.register(message);

        assertEquals("Alice", user.getUsername());
        assertEquals(AuthorType.CLIENT, user.getAuthorType());
        assertTrue(user.getState() instanceof FreeClientState);
    }

    @Test
    void registerFail1() {
        Message message = new Message("/register cvbcbcv Alice");
        User user = new User(Mockito.mock(MessageWriter.class));
        UnauthorizedState state = new UnauthorizedState(user);

        state.register(message);

        assertNull(user.getUsername());
        assertNull(user.getAuthorType());
        assertTrue(user.getState() instanceof UnauthorizedState);
    }

    @Test
    void registerFail2() {
        Message message = new Message("/register agent");
        User user = new User(Mockito.mock(MessageWriter.class));
        UnauthorizedState state = new UnauthorizedState(user);

        state.register(message);

        assertNull(user.getUsername());
        assertNull(user.getAuthorType());
        assertTrue(user.getState() instanceof UnauthorizedState);
    }

    @Test
    void registerFail3() {
        Message message = new Message("/register wrongtype Alex");
        User user = new User(Mockito.mock(MessageWriter.class));
        UnauthorizedState state = new UnauthorizedState(user);

        state.register(message);

        assertNull(user.getUsername());
        assertNull(user.getAuthorType());
        assertTrue(user.getState() instanceof UnauthorizedState);
    }

    @Test
    void registerFail4() {
        Message message1 = new Message("/register agent Alex");
        Message message2 = new Message("/register client Alex");
        User user1 = new User(Mockito.mock(MessageWriter.class));
        User user2 = new User(Mockito.mock(MessageWriter.class));
        User mockedUser = Mockito.spy(user2);
        UnauthorizedState state1 = new UnauthorizedState(user1);
        UnauthorizedState state2 = new UnauthorizedState(mockedUser);

        state1.register(message1);
        state2.register(message2);

        assertEquals("Alex", user1.getUsername());
        assertEquals(AuthorType.AGENT, user1.getAuthorType());
        assertTrue(user1.getState() instanceof FreeAgentState);
        Mockito.verify(mockedUser).receiveFromServer("User with username " + "Alex" + " already exists.");
    }
}