package com.example.jsockets.web;

import com.example.jsockets.messages.Messages;
import com.example.jsockets.security.JwtAuthentication;
import com.google.gson.Gson;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.socket.TextMessage;

@Controller
public class StompController {
    private final Gson gson = new Gson();
    private final SimpMessagingTemplate template;
    private Messages messages;

    public StompController(SimpMessagingTemplate template, Messages messages) {
        this.template = template;
        this.messages = messages;
    }

    // если сообщение пришло на /app/hello
    @MessageMapping("/hello")
    public void getMessage(Message<?> stompMessage) {
        // отправляем hello всем, кто подписан на /topic/chat
        LinkedMultiValueMap<String, String> nativeHeaders =
                (LinkedMultiValueMap<String, String>) stompMessage.getHeaders().get("nativeHeaders");
        String jwt = nativeHeaders.getFirst("jwt");
        JwtAuthentication jwtAuthentication = new JwtAuthentication(jwt);
        com.example.jsockets.messages.Message message = messages.create(
                jwtAuthentication.getAccount(),
                new String((byte[]) stompMessage.getPayload())
        );
        template.convertAndSend("/topic/chat", gson.toJson(message));
    }

    // если сообщение пришло на /app/bye
    @MessageMapping("/bye")
    // оно отправляется сразу всем, кто подписан на /topic/chat
    @SendTo("/topic/chat")
    public TextMessage byeMessage(Message<?> message) {
        return new TextMessage("Bye bye!");
    }
}
