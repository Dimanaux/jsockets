package com.example.jsockets.web;

import com.example.jsockets.accounts.Account;
import com.example.jsockets.security.JwtAuthentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeFailureException;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.web.util.WebUtils;

import java.util.Map;

@Component
public class AuthHandshakeHandler implements HandshakeHandler {

    private DefaultHandshakeHandler defaultHandshakeHandler = new DefaultHandshakeHandler();

    @Override
    public boolean doHandshake(ServerHttpRequest serverRequest,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Map<String, Object> attributes) throws HandshakeFailureException {
        ServletServerHttpRequest request = (ServletServerHttpRequest) serverRequest;
        String jwt = WebUtils.getCookie(request.getServletRequest(), "X-Authorization").getValue();
        if (canAuthorize(jwt)) {
            return defaultHandshakeHandler.doHandshake(serverRequest, response, wsHandler, attributes);
        }
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return false;
    }

    private boolean canAuthorize(String jwt) {
        try {
            Account account = new JwtAuthentication(jwt).getAccount();
            return account != null;
        } catch (Exception e) {
            return false;
        }
    }
}