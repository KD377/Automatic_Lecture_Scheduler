package com.example.lecturescheduler.controller;

import com.example.lecturescheduler.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/windows")
public class AuthController {

    @Value("${google.calendar.api.url:https://www.googleapis.com/calendar/v3/calendars/primary/events}")
    private String googleCalendarApiUrl;

    @GetMapping("/fetch_calendar")
    public ResponseEntity<?> fetchCalendarEvents(@RequestHeader("Authorization") String accessToken) {


        return ResponseEntity.ok(accessToken);
    }
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/home")
    public ResponseEntity<String> home() {
        return authService.home();
    }


    @GetMapping("/secured")
    public ResponseEntity<String> secured() {
        return authService.secured();
    }

    @GetMapping("/secured2")
    public ResponseEntity<String> secured2(@AuthenticationPrincipal OAuth2User principal) {
        return authService.secured2(principal);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        return authService.logout(request, response);
    }
}
