package com.example.jsockets.web;

import com.example.jsockets.accounts.Account;
import com.example.jsockets.messages.Message;
import com.example.jsockets.messages.Messages;
import com.example.jsockets.security.JwtAuthentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/messages")
@CrossOrigin(origins = "http://localhost:3000")
public class MessagesController {
    private final Messages messages;

    public MessagesController(Messages messages) {
        this.messages = messages;
    }

    @GetMapping("/notification")
    public ResponseEntity<Message> notifyAboutNewMessages() {
        Message message = messages.waitForNewMessage();
        return ResponseEntity.ok(message);
    }

    @GetMapping
    public ResponseEntity<List<Message>> messageHistory() {
        return ResponseEntity.ok(messages.history());
    }

    @PostMapping
    public ResponseEntity<Message> createNewMessage(@RequestBody LinkedHashMap<String, Object> body) {
        Account author = ((JwtAuthentication) SecurityContextHolder.getContext().getAuthentication()).getAccount();
        Message message = messages.create(author, (String) body.get("text"));
        return ResponseEntity.ok(message);
    }
}
