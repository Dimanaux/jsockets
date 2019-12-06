package com.example.jsockets.interceptors;

import com.example.jsockets.messages.Messages;
import com.google.gson.Gson;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
public class CustomInterceptor extends ChannelInterceptorAdapter {
    private Messages messages;
    private Gson gson = new Gson();

    public CustomInterceptor(Messages messages) {
        this.messages = messages;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) {
            return message;
        } else if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            System.out.println("К нам подключились!");
        } else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            System.out.println("+1 подписчик!");
        }
        return message;
    }
}
