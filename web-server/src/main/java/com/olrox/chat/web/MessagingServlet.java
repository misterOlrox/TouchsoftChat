package com.olrox.chat.web;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

/**
 * Well, it's just a servlet declaration.
 * @author amrishodiq
 */
public class MessagingServlet extends WebSocketServlet {
    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.register(MessagingAdapter.class);
    }

}
