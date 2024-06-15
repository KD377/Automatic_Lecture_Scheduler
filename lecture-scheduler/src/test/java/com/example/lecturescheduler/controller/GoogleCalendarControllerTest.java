package com.example.lecturescheduler.controller;


import com.example.lecturescheduler.service.GoogleCalendarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class GoogleCalendarControllerTest {
    @InjectMocks
    private GoogleCalendarController googleCalendarController;

    @Mock
    private GoogleCalendarService googleCalendarService;

    @Mock
    private OAuth2AuthorizedClient authorizedClient;

    @Mock
    private OAuth2User oauth2User;

    @Mock
    private OAuth2AccessToken accessToken;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(authorizedClient.getAccessToken()).thenReturn(accessToken);
        when(accessToken.getTokenValue()).thenReturn("test-token");
    }

    @Test
    void testAddEventsToCalendarSuccess() throws GeneralSecurityException, IOException {
        when(googleCalendarService.getUserEmail(oauth2User)).thenReturn("test@example.com");
        doNothing().when(googleCalendarService).addEventsToCalendar(anyString(), any(OAuth2AccessToken.class));

        ResponseEntity<String> response = googleCalendarController.addEventsToCalendar(authorizedClient, oauth2User);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Lecture sessions added to Google Calendar.", response.getBody());
        verify(googleCalendarService, times(1)).addEventsToCalendar(anyString(), any(OAuth2AccessToken.class));
    }

    @Test
    void testAddEventsToCalendarFailure() throws GeneralSecurityException, IOException {
        when(googleCalendarService.getUserEmail(oauth2User)).thenReturn("test@example.com");
        doThrow(new GeneralSecurityException()).when(googleCalendarService).addEventsToCalendar(anyString(), any(OAuth2AccessToken.class));

        ResponseEntity<String> response = googleCalendarController.addEventsToCalendar(authorizedClient, oauth2User);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Error occurred while adding lecture sessions to Google Calendar.", response.getBody());
        verify(googleCalendarService, times(1)).addEventsToCalendar(anyString(), any(OAuth2AccessToken.class));
    }
}
