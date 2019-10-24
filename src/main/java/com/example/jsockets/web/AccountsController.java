package com.example.jsockets.web;

import com.example.jsockets.accounts.Account;
import com.example.jsockets.accounts.Accounts;
import com.example.jsockets.security.JwtAuthentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AccountsController {
    private final Accounts accounts;

    public AccountsController(Accounts accounts) {
        this.accounts = accounts;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/welcome")
    public ResponseEntity<String> welcome(@AuthenticationPrincipal Authentication authentication) {
        JwtAuthentication jwtAuthentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        Account currentAccount = jwtAuthentication.getAccount();
        return ResponseEntity.ok("welcome, " + currentAccount.toMap());
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/login")
    public ResponseEntity<LinkedHashMap<String, String>> login(@RequestBody LinkedHashMap<String, String> credentials) {
        return ResponseEntity.ok(accounts.jwtLogin(credentials));
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody LinkedHashMap<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        return ResponseEntity.ok(accounts.create(username, password).toMap());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/hello")
    public ResponseEntity<Object> hello() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthentication) {
            JwtAuthentication jwtAuthentication = (JwtAuthentication) authentication;
            Account account = jwtAuthentication.getAccount();
            LinkedHashMap<String, String> response = account.toMap();
            response.put("text", "Hello, " + response.get("username"));
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.ok("Cannot authenticate via JWT.");
        }
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<?> accountInfo(@PathVariable("id") Long id) {
        Optional<Account> account = accounts.findById(id);
        return account
                .map(Account::toMap)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
