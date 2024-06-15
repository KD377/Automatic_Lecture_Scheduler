package com.example.lecturescheduler.controller;

import com.example.lecturescheduler.service.GoogleCalendarService;
import com.example.lecturescheduler.service.LectureSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;


@RestController
@RequestMapping("/google")
public class GoogleCalendarController {

    private final GoogleCalendarService googleCalendarService;

    LectureSessionService lectureSessionService;

    @Autowired
    public GoogleCalendarController(GoogleCalendarService googleCalendarService, LectureSessionService lectureSessionService){
        this.googleCalendarService = googleCalendarService;
        this.lectureSessionService = lectureSessionService;
    }


    @PostMapping("/add")
    public ResponseEntity<String> addEventsToCalendar(@RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient,@AuthenticationPrincipal OAuth2User oauth2User ){
        String email = String.valueOf(googleCalendarService.getUserEmail(oauth2User));
        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
        try {
            googleCalendarService.addEventsToCalendar(email, accessToken);
            return ResponseEntity.ok("Lecture sessions added to Google Calendar.");
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error occurred while adding lecture sessions to Google Calendar.");
        }
    }



}
