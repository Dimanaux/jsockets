package com.example.jsockets.accounts;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.LinkedHashMap;

@Entity
public class Account {
    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String passwordHash;

    public Account() {
    }

    public Account(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public Account(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public LinkedHashMap<String, String> toMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("id", id.toString());
        map.put("username", username);
        return map;
    }

    String passwordHash() {
        return passwordHash;
    }

    String username() {
        return username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
