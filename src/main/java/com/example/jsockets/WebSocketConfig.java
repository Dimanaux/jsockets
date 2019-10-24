package com.example.jsockets;

import com.example.jsockets.web.AuthHandshakeHandler;
import com.example.jsockets.web.MessagesWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final MessagesWebSocketHandler messagesWebSocketHandler;
    private final AuthHandshakeHandler authHandshakeHandler;

    public WebSocketConfig(MessagesWebSocketHandler messagesWebSocketHandler, AuthHandshakeHandler authHandshakeHandler) {
        this.messagesWebSocketHandler = messagesWebSocketHandler;
        this.authHandshakeHandler = authHandshakeHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(messagesWebSocketHandler, "/wschat")
                .setAllowedOrigins("*")
                .setHandshakeHandler(authHandshakeHandler);
    }
}
