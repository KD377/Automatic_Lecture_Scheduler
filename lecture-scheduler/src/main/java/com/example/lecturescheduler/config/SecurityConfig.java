package com.example.lecturescheduler.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/classrooms/**", "/api/groups/**", "/api/instructors/**", "/api/subjects/**", "/api/algorithm/**","/token").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(withDefaults())
                .httpBasic(withDefaults());
        return http.build();
    }
}
