package com.olrox.chat.server.user.state;

import com.olrox.chat.server.manager.UsersManager;
import com.olrox.chat.server.message.MessageWriter;
import com.olrox.chat.server.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BusyAgentStateTest {

    @Mock
    UsersManager usersManagerMock;

    @Test
    void setFree() {
        User agent = new User(mock(MessageWriter.class));
        BusyClientState clientState = mock(BusyClientState.class);
        BusyAgentState agentState = new BusyAgentState(agent, clientState, usersManagerMock);

        agentState.setFree();

        assertTrue(agentState.getUser().getState() instanceof FreeAgentState);
    }

    @Test
    void leave() throws IOException {
        User agent = new User(mock(MessageWriter.class));
        User client = new User(mock(MessageWriter.class));
        BusyClientState clientState = spy(new BusyClientState(client, mock(BusyAgentState.class), usersManagerMock));
        BusyAgentState agentState = spy(new BusyAgentState(agent, clientState, usersManagerMock));

        agentState.leave();

        verify(agentState, times(1)).setFree();
        verify(clientState, times(1)).setFree();
    }

    @Test
    void exit() {
        User agent = new User(mock(MessageWriter.class));
        agent.setUsername("testing");
        User client = new User(mock(MessageWriter.class));
        BusyClientState clientState = spy(new BusyClientState(client, mock(BusyAgentState.class), usersManagerMock));
        BusyAgentState agentState = spy(new BusyAgentState(agent, clientState, usersManagerMock));

        agentState.exit();

        verify(clientState, times(1)).setFree();
        verify(usersManagerMock, times(1)).removeOnlineUser(agent.getUsername());
    }
}