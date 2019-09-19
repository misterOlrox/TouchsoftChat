package com.olrox.chat.server.user.state;

import com.olrox.chat.server.manager.UsersManager;
import com.olrox.chat.server.manager.UsersManagerFactory;
import com.olrox.chat.server.message.Message;
import com.olrox.chat.server.message.MessageWriter;
import com.olrox.chat.server.user.User;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class FreeAgentStateTest {
    @Test
    void connectTest(){
        MessageWriter clientMessageWriter = spy(MessageWriter.class);
        MessageWriter agentMessageWriter = spy(MessageWriter.class);
        User client = new User(clientMessageWriter);
        FreeClientState freeClientState = new FreeClientState(client);
        client.setState(freeClientState);
        User agent = spy(new User(agentMessageWriter));
        client.setName("ClientTest");
        UsersManager usersManager = UsersManagerFactory.createUsersManager();
        usersManager.addFreeClient(client);
        usersManager.addOnlineUser(client.getName());
        Message mockedMessage = spy(Message.class);
        client.sendMessage(mockedMessage);
        client.sendMessage(mockedMessage);
        client.sendMessage(mockedMessage);
        FreeAgentState agentState = new FreeAgentState(agent);
        agent.setState(agentState);

        agentState.findCompanion();


        verify(agent, times(3)).receiveFromUser(mockedMessage);
    }
}