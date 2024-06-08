package com.example.lecturescheduler.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/google")
public class GoogleCalendarController {

    @GetMapping("/calendar")
    @ResponseBody
    public String getCalendarEvents(@RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient,
                                    @AuthenticationPrincipal OAuth2User oauth2User) {
        String uri = "https://www.googleapis.com/calendar/v3/calendars/primary/events";

        String response = new RestTemplate().getForObject(uri + "?access_token=" + authorizedClient.getAccessToken().getTokenValue(), String.class);

        return response;
    }




}
