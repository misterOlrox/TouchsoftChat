package com.olrox.chat.server.user.state;

import com.olrox.chat.server.exception.InvalidUserStateException;
import com.olrox.chat.server.manager.UsersManager;
import com.olrox.chat.server.manager.UsersManagerFactory;
import com.olrox.chat.server.message.Message;
import com.olrox.chat.server.message.MessageWriter;
import com.olrox.chat.server.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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
    void connectTest(){
        MessageWriter clientMessageWriter = spy(MessageWriter.class);
        MessageWriter agentMessageWriter = spy(MessageWriter.class);
        User client = new User(clientMessageWriter);
        FreeClientState freeClientState = new FreeClientState(client);
        client.setState(freeClientState);
        User agent = spy(new User(agentMessageWriter));
        FreeAgentState agentState = new FreeAgentState(agent);
        agent.setUsername("Mike");
        agent.setState(agentState);
        UsersManager usersManager = UsersManagerFactory.createUsersManager();
        usersManager.addFreeAgent(agent);
        usersManager.addOnlineUser(agent.getUsername());
        Message mockedMessage = spy(Message.class);

        freeClientState.sendMessage(mockedMessage);

        verify(agent).receiveFromUser(mockedMessage);
    }

    void exceptionTest(){
        MessageWriter clientMessageWriter = spy(MessageWriter.class);
        MessageWriter agentMessageWriter = spy(MessageWriter.class);
        User client = new User(clientMessageWriter);
        FreeClientState freeClientState = new FreeClientState(client);
        client.setState(freeClientState);
        User agent = spy(new User(agentMessageWriter));
        FreeAgentState agentState = new FreeAgentState(agent);
        agent.setUsername("Mike");
        UsersManager usersManager = UsersManagerFactory.createUsersManager();
        usersManager.addFreeAgent(agent);
        usersManager.addOnlineUser(agent.getUsername());
        Message mockedMessage = spy(Message.class);

        try {
            freeClientState.sendMessage(mockedMessage);
        } catch (InvalidUserStateException ex){
            assertEquals(ex.getMessage(), "Companion isn't in FreeAgentState.");
        }
    }
}