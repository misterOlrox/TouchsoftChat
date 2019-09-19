package com.olrox.chat.server.user;

import com.olrox.chat.server.message.Message;
import com.olrox.chat.server.message.MessageWriter;
import com.olrox.chat.server.message.author.ServerAsAuthor;
import com.olrox.chat.server.user.state.UserState;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

class UserTest {

    @Test
    void register() {
        UserState userState = spy(UserState.class);
        User user = new User(userState);
        Message registerMessage = new Message();
        user.register(registerMessage);

        Mockito.verify(userState, times(1)).register(registerMessage);
    }

    @Test
    void sendMessage() {
        UserState userState = spy(UserState.class);
        User user = new User(userState);
        Message message = new Message();
        user.sendMessage(message);

        Mockito.verify(userState, times(1)).sendMessage(message);
    }

    @Test
    void leave() {
        UserState userState = spy(UserState.class);
        User user = new User(userState);
        user.leave();

        Mockito.verify(userState, times(1)).leave();
    }

    @Test
    void exit() {
        MessageWriter messageWriter = mock(MessageWriter.class);
        UserState userState = spy(UserState.class);
        User user = new User(messageWriter);
        user.setState(userState);
        user.exit();

        Mockito.verify(userState, times(1)).exit();
        Mockito.verify(messageWriter, times(1)).close();
    }

    @Test
    void receiveFromUser() {
        MessageWriter messageWriter = mock(MessageWriter.class);
        UserState userState = spy(UserState.class);
        User user = new User(messageWriter);
        user.setState(userState);
        Message message = spy(Message.class);
        user.receiveFromUser(message);

        Mockito.verify(messageWriter, times(1)).write(message);
    }

    @Test
    void receiveFromServer() {
        MessageWriter messageWriter = spy(MessageWriter.class);
        User user = new User(mock(UserState.class));
        user.setMessageWriter(messageWriter);
        user.receiveFromServer("text");

        Mockito.verify(messageWriter, times(1)).write(any(Message.class));
    }
}