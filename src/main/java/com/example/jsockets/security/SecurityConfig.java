package com.example.jsockets.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration // говорим, что у нас конфигурационный класс
@EnableGlobalMethodSecurity(prePostEnabled = true) // включаем проверку безопасности через аннотации
@EnableWebSecurity // включаем безопасность
@ComponentScan("com.example.jsockets") // говорим, чтобы искал все компоненты в наших пакетах
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtAuthenticationProvider provider;

    public SecurityConfig(JwtAuthenticationProvider provider) {
        this.provider = provider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(provider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors();
        http.authorizeRequests().antMatchers("/wschat").permitAll();
        http.sessionManagement().disable();
        http.addFilterBefore(new JwtAuthenticationFilter(), BasicAuthenticationFilter.class);
        http.authorizeRequests().antMatchers("/swagger-ui.html#/**").permitAll();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
