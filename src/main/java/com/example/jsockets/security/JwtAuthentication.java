package com.example.jsockets.security;

import com.example.jsockets.accounts.Account;
import com.example.jsockets.accounts.AccountDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.security.Key;
import java.util.Collection;
import java.util.LinkedHashMap;

public class JwtAuthentication implements Authentication {
    private static Key key = Keys.hmacShaKeyFor("This must have 256+ bits! (32+ chars)".getBytes());

    private Account account;
    private String token;

    public JwtAuthentication(String jwt) {
        this.token = jwt;
        Claims body = Jwts.parser().setSigningKey(key).parseClaimsJws(jwt).getBody();
        Long id = Long.parseLong(body.get("id", String.class));
        String username = body.get("username", String.class);
        this.account = new Account(id, username);
    }

    public JwtAuthentication(Account account) {
        this.account = account;
        LinkedHashMap<String, Object> claims = new LinkedHashMap<>(account.toMap());
        token = Jwts.builder()
                .addClaims(claims)
                .signWith(key)
                .compact();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return account.getPasswordHash();
    }

    @Override
    public Object getDetails() {
        return new AccountDetails(account);
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return account.getUsername();
    }

    public Account getAccount() {
        return account;
    }

    public LinkedHashMap<String, String> toMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("jwt", token);
        return map;
    }
}
