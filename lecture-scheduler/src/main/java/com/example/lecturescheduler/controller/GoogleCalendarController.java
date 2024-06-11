package com.example.lecturescheduler.controller;

import com.example.lecturescheduler.dto.LectureSessionResponse;
import com.example.lecturescheduler.service.GoogleCalendarService;
import com.example.lecturescheduler.service.LectureSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/google")
public class GoogleCalendarController {

    @Autowired
    private GoogleCalendarService googleCalendarService;

    @Autowired
    LectureSessionService lectureSessionService;

    @Autowired
    public GoogleCalendarController(GoogleCalendarService googleCalendarService, LectureSessionService lectureSessionService){
        this.googleCalendarService = googleCalendarService;
        this.lectureSessionService = lectureSessionService;
    }

    @GetMapping("/calendar")
    @ResponseBody
    public String getCalendarEvents(@RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient,
                                    @AuthenticationPrincipal OAuth2User oauth2User) {
        String uri = "https://www.googleapis.com/calendar/v3/calendars/primary/events";

        String response = new RestTemplate().getForObject(uri + "?access_token=" + authorizedClient.getAccessToken().getTokenValue(), String.class);

        return response;
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
