package com.hadiyasa.springRestfulAPI.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final ApiTokenFilter apiTokenFilter;

    public SecurityConfig(ApiTokenFilter apiTokenFilter) {
        this.apiTokenFilter = apiTokenFilter;
    }

    /**
     * HttpSecurity: Objek untuk mengonfigurasi keamanan web berdasarkan HTTP.
     * */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        /**
         * csrf         : Serangan yang memaksa pengguna melakukan
         *                tindakan tidak sah di situs tempat mereka telah diautentikasi.
         * disable()    : Menonaktifkan perlindungan CSRF
         * */
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/current").authenticated()
                        .anyRequest().permitAll())
                .addFilterBefore(apiTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
