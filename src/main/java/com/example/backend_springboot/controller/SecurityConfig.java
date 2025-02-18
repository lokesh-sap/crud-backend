package com.example.backend_springboot.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity in development
                .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers("/api/v1/**").permitAll() // Allow access to all your API endpoints
                    .anyRequest().authenticated() // Secure other endpoints if necessary
                );
        return http.build();
    }
}