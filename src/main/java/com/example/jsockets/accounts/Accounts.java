package com.example.jsockets.accounts;

import com.example.jsockets.security.JwtAuthentication;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.function.Supplier;

@Service
public class Accounts implements UserDetailsService {
    private final AccountsRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordEncoder encoder;

    public Accounts(AccountsRepository repository, PasswordEncoder passwordEncoder, PasswordEncoder encoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.encoder = encoder;
    }

    public Optional<Account> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return byUsername(username)
                .map(AccountDetails::new)
                .orElseThrow(notFound(username));
    }

    public LinkedHashMap<String, String> jwtLogin(LinkedHashMap<String, String> credentials) {
        return byUsername(credentials.get("username"))
                .filter(a -> encoder.matches(credentials.get("password"), a.getPasswordHash()))
                .map(this::createJwtForAccount)
                .orElseThrow(incorrectCredentials());
    }

    private LinkedHashMap<String, String> createJwtForAccount(Account account) {
        return new JwtAuthentication(account).toMap();
    }

    public Account create(String username, String password) {
        String passwordHash = passwordEncoder.encode(password);
        Account account = new Account(username, passwordHash);
        return repository.save(account);
    }

    private Optional<Account> byUsername(String username) {
        return repository.findFirstByUsernameIgnoreCase(username);
    }

    private Supplier<BadCredentialsException> incorrectCredentials() {
        return () -> new BadCredentialsException("Incorrect login or password");
    }

    private Supplier<UsernameNotFoundException> notFound(String username) {
        return () -> new UsernameNotFoundException("No account with username " + username);
    }
}
