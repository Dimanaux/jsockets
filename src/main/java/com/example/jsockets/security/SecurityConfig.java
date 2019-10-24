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
    // подключем провайдер, который мы написали
    private final JwtAuthenticationProvider provider;

    public SecurityConfig(JwtAuthenticationProvider provider) {
        this.provider = provider;
    }


    // конфигурируем AuthenticationManager
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        // прикручиваем наш провайдер
        auth.authenticationProvider(provider);
    }

    // конфигурирем саму безопасность
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // отключаем csrf
        http.csrf().disable();

        http.cors();
        http.authorizeRequests().antMatchers("/wschat").permitAll();
        // отключаем сессии
        http.sessionManagement().disable();
        // добавляем наш фильтр
        http.addFilterBefore(new JwtAuthenticationFilter(), BasicAuthenticationFilter.class);
        // говорим, что разрешаем Swagger
        http.authorizeRequests().antMatchers("/swagger-ui.html#/**").permitAll();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}