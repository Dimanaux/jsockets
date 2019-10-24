package com.example.jsockets.web;

import com.example.jsockets.accounts.Account;
import com.example.jsockets.messages.Message;
import com.example.jsockets.messages.Messages;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MessagesWebSocketHandler extends TextWebSocketHandler {
    private final Messages messages;

    private final Gson jsonFactory = new Gson();

    private static Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public MessagesWebSocketHandler(Messages messages) {
        this.messages = messages;
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> wsMessage) throws Exception {
        Account account = getAccount(session);
        sessions.putIfAbsent(account.getUsername(), session);
        Message message = message(wsMessage, account);
        send(message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(getAccount(session).getUsername());
    }

    private void send(Message message) throws IOException {
        for (WebSocketSession currentSession : sessions.values()) {
            currentSession.sendMessage(new TextMessage(jsonFactory.toJson(message)));
        }
    }

    private Message message(WebSocketMessage<?> wsMessage, Account account) {
        String json = (String) wsMessage.getPayload();
        Message message = jsonFactory.fromJson(json, Message.class);
        return messages.create(account, message.getText());
    }

    private Account getAccount(WebSocketSession webSocketSession) {
        return (Account) webSocketSession.getAttributes().get("account");
    }
}
