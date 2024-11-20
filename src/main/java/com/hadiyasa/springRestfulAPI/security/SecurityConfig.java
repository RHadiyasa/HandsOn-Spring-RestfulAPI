package com.hadiyasa.springRestfulAPI.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/api/users","/auth/login"))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users","/auth/login").permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }
}
