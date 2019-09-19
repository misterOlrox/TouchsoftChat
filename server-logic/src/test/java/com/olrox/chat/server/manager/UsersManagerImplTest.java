package com.olrox.chat.server.manager;

import com.olrox.chat.server.exception.InvalidUserStateException;
import com.olrox.chat.server.message.MessageWriter;
import com.olrox.chat.server.user.User;
import com.olrox.chat.server.user.state.FreeAgentState;
import com.olrox.chat.server.user.state.FreeClientState;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.*;

class UsersManagerImplTest {

    @Test
    void test() {
        UsersManagerImpl usersManager = new UsersManagerImpl();
        User user1 = new User(mock(MessageWriter.class));
        User user2 = new User(mock(MessageWriter.class));
        User user3 = new User(mock(MessageWriter.class));
        User user4 = new User(mock(MessageWriter.class));
        user1.setName("1");
        user2.setName("2");
        user3.setName("3");
        user4.setName("4");
        user1.setState(mock(FreeClientState.class));
        user2.setState(mock(FreeAgentState.class));
        user3.setState(mock(FreeAgentState.class));
        user4.setState(mock(FreeClientState.class));
        usersManager.addOnlineUser(user1.getName());
        usersManager.addOnlineUser(user2.getName());
        usersManager.addOnlineUser(user3.getName());
        usersManager.addOnlineUser(user4.getName());

        usersManager.addFreeClient(user1);
        usersManager.addFreeAgent(user2);
        usersManager.addFreeAgent(user3);
        usersManager.addFreeClient(user4);
        usersManager.removeOnlineUser("4");


        assertEquals(user1, usersManager.pollFreeClient());
        assertNull(usersManager.pollFreeClient());
        assertEquals(user2, usersManager.pollFreeAgent());
        assertEquals(user3, usersManager.pollFreeAgent());
    }

    @Test
    public void exception1(){
        UsersManagerImpl usersManager = new UsersManagerImpl();
        User user1 = new User(mock(MessageWriter.class));

        try{
            usersManager.addFreeClient(user1);
        } catch (InvalidUserStateException ex){
            assertEquals(ex.getMessage(), "User isn't in FreeClientState.");
        }
    }

    @Test
    public void exception2(){
        UsersManagerImpl usersManager = new UsersManagerImpl();
        User user1 = new User(mock(MessageWriter.class));

        try{
            usersManager.addFreeAgent(user1);
        } catch (InvalidUserStateException ex){
            assertEquals(ex.getMessage(), "User isn't in FreeAgentState.");
        }
    }
}