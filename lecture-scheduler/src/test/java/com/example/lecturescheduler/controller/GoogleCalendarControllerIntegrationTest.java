package com.example.lecturescheduler.controller;

import com.example.lecturescheduler.service.GoogleCalendarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.security.GeneralSecurityException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class GoogleCalendarControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GoogleCalendarService googleCalendarService;

    @Mock
    private OAuth2AuthorizedClient authorizedClient;

    @Mock
    private OAuth2AccessToken accessToken;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(authorizedClient.getAccessToken()).thenReturn(accessToken);
        when(accessToken.getTokenValue()).thenReturn("test-token");
    }

    @Test
    @WithMockUser
    void testAddEventsToCalendarSuccess() throws Exception {
        when(googleCalendarService.getUserEmail(any())).thenReturn("test@example.com");
        doNothing().when(googleCalendarService).addEventsToCalendar(anyString(), any(OAuth2AccessToken.class));

        mockMvc.perform(post("/google/add")
                        .with(oauth2Login().attributes(attrs -> {
                            attrs.put("sub", "1234567890");
                            attrs.put("email", "test@example.com");
                        }))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Lecture sessions added to Google Calendar."));
    }

    @Test
    @WithMockUser
    void testAddEventsToCalendarFailure() throws Exception {
        when(googleCalendarService.getUserEmail(any())).thenReturn("test@example.com");
        doThrow(new GeneralSecurityException()).when(googleCalendarService).addEventsToCalendar(anyString(), any(OAuth2AccessToken.class));

        mockMvc.perform(post("/google/add")
                        .with(oauth2Login().attributes(attrs -> {
                            attrs.put("sub", "1234567890");
                            attrs.put("email", "test@example.com");
                        }))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error occurred while adding lecture sessions to Google Calendar."));
    }
}
