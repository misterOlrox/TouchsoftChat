package com.olrox.chat.server.user.state;

import com.olrox.chat.server.manager.UsersManager;
import com.olrox.chat.server.manager.UsersManagerFactory;
import com.olrox.chat.server.message.Message;
import com.olrox.chat.server.message.MessageWriter;
import com.olrox.chat.server.message.author.AuthorType;
import com.olrox.chat.server.user.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;

import javax.jnlp.UnavailableServiceException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FreeClientStateTest {

    @Test
    void sendMessageTestWithPreviousUnauthorizedState() {
        User author = new User(mock(MessageWriter.class));
        UnauthorizedState prevState = new UnauthorizedState(author);
        FreeClientState state = new FreeClientState(prevState);
        author.setState(state);
        Message message1 = new Message("MESSAGE1", author);
        Message message2 = new Message("message 2", author);
        Message message3 = new Message("message 3", author);
        Message message4 = new Message("Message 4", author);
        List<Message> expected = new ArrayList<>();
        expected.add(message1);
        expected.add(message2);
        expected.add(message3);
        expected.add(message4);

        state.sendMessage(message1);
        state.sendMessage(message2);
        state.sendMessage(message3);
        state.sendMessage(message4);

        assertIterableEquals(expected, state.getMessages());
    }

    @Test
    void sendMessageToOnlineFreeAgent() {
//        User client = new User(mock(MessageWriter.class));
//        UnauthorizedState prevState = new UnauthorizedState(client);
//        FreeClientState state = new FreeClientState(prevState);
//        client.setState(state);
//        UsersManager usersManager = UsersManagerFactory.createUsersManager();
//        User agent = new User(mock(MessageWriter.class));
//        agent.setState(mock(FreeAgentState.class));
//        usersManager.addOnlineUser(agent.getUsername());
//        usersManager.addFreeAgent(agent);
//        Message message1 = new Message("MESSAGE1", client);
//        User mockedAgent = spy(agent);
//
//        client.sendMessage(message1);
//
//        verify(mockedAgent, times(1)).receiveFromUser(message1);
    }
}