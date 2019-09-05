package com.olrox.chat.server.user.state;

import com.olrox.chat.server.manager.UsersManager;
import com.olrox.chat.server.message.MessageWriter;
import com.olrox.chat.server.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class BusyClientStateTest {

    @Mock
    UsersManager usersManagerMock;

    @Test
    void leave() {
        User agent = new User(mock(MessageWriter.class));
        User client = new User(mock(MessageWriter.class));
        BusyAgentState agentState = spy(new BusyAgentState(agent, mock(BusyClientState.class), usersManagerMock));
        BusyClientState clientState = spy(new BusyClientState(client, agentState, usersManagerMock));

        clientState.leave();

        verify(agentState, times(1)).setFree();
        verify(clientState, times(1)).setFree();
    }

    @Test
    void exit() {
        User client = new User(mock(MessageWriter.class));
        client.setUsername("testing");
        User agent = new User(mock(MessageWriter.class));
        BusyAgentState agentState = spy(new BusyAgentState(agent, mock(BusyClientState.class), usersManagerMock));
        BusyClientState clientState = spy(new BusyClientState(client, agentState, usersManagerMock));

        clientState.exit();

        verify(agentState, times(1)).setFree();
        verify(usersManagerMock, times(1)).removeOnlineUser(client.getUsername());
    }
}