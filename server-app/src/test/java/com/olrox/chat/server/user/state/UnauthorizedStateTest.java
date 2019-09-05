package com.olrox.chat.server.user.state;

import com.olrox.chat.server.manager.UsersManager;
import com.olrox.chat.server.manager.UsersManagerFactory;
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
        Message message = new Message("/register agent Mike");
        User user = new User(Mockito.mock(MessageWriter.class));
        UnauthorizedState state = new UnauthorizedState(user);
        UsersManager usersManager = UsersManagerFactory.createUsersManager();

        state.register(message);

        assertEquals("Mike", user.getUsername());
        assertEquals(AuthorType.AGENT, user.getAuthorType());
        assertTrue(user.getState() instanceof FreeAgentState || user.getState() instanceof BusyAgentState);
        assertTrue(usersManager.checkIfOnline("Mike"));
    }

    @Test
    void registerAsClient() {
        Message message = new Message("/register client Alice");
        User user = new User(Mockito.mock(MessageWriter.class));
        UnauthorizedState state = new UnauthorizedState(user);
        UsersManager usersManager = UsersManagerFactory.createUsersManager();

        state.register(message);

        assertEquals("Alice", user.getUsername());
        assertEquals(AuthorType.CLIENT, user.getAuthorType());
        assertTrue(user.getState() instanceof FreeClientState);
        assertTrue(usersManager.checkIfOnline("Alice"));
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
}