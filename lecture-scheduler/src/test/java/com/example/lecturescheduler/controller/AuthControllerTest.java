package com.example.lecturescheduler.controller;

import com.example.lecturescheduler.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
public class AuthControllerTest {
    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHome() {
        when(authService.home()).thenReturn(ResponseEntity.ok("Home"));

        ResponseEntity<String> response = authController.home();
        assertNotNull(response);
        assertEquals("Home", response.getBody());
        verify(authService, times(1)).home();
    }

    @Test
    void testHomeFailure() {
        when(authService.home()).thenReturn(ResponseEntity.status(500).body("Error"));

        ResponseEntity<String> response = authController.home();
        assertNotNull(response);
        assertEquals("Error", response.getBody());
        assertEquals(500, response.getStatusCodeValue());
        verify(authService, times(1)).home();
    }

    @Test
    void testSecured() {
        OAuth2User principal = mock(OAuth2User.class);
        when(authService.secured(principal)).thenReturn(ResponseEntity.ok("Secured"));

        ResponseEntity<String> response = authController.secured(principal);
        assertNotNull(response);
        assertEquals("Secured", response.getBody());
        verify(authService, times(1)).secured(principal);
    }


    @Test
    void testSecuredUnauthorized() {
        OAuth2User principal = mock(OAuth2User.class);
        when(authService.secured(principal)).thenReturn(ResponseEntity.status(401).body("Unauthorized"));

        ResponseEntity<String> response = authController.secured(principal);
        assertNotNull(response);
        assertEquals("Unauthorized", response.getBody());
        assertEquals(401, response.getStatusCodeValue());
        verify(authService, times(1)).secured(principal);
    }


    @Test
    void testSecuredFailure() {
        OAuth2User principal = mock(OAuth2User.class);
        when(authService.secured(principal)).thenReturn(ResponseEntity.status(403).body("Forbidden"));

        ResponseEntity<String> response = authController.secured(principal);
        assertNotNull(response);
        assertEquals("Forbidden", response.getBody());
        assertEquals(403, response.getStatusCodeValue());
        verify(authService, times(1)).secured(principal);
    }

    @Test
    void testLogoutUnauthorized() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(authService.logout(request, response)).thenReturn(ResponseEntity.status(401).body("Unauthorized"));

        ResponseEntity<String> logoutResponse = authController.logout(request, response);
        assertNotNull(logoutResponse);
        assertEquals("Unauthorized", logoutResponse.getBody());
        assertEquals(401, logoutResponse.getStatusCodeValue());
        verify(authService, times(1)).logout(request, response);
    }

    @Test
    void testLogout() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(authService.logout(request, response)).thenReturn(ResponseEntity.ok("Logged out"));

        ResponseEntity<String> logoutResponse = authController.logout(request, response);
        assertNotNull(logoutResponse);
        assertEquals("Logged out", logoutResponse.getBody());
        verify(authService, times(1)).logout(request, response);
    }

    @Test
    void testLogoutFailure() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(authService.logout(request, response)).thenReturn(ResponseEntity.status(500).body("Logout failed"));

        ResponseEntity<String> logoutResponse = authController.logout(request, response);
        assertNotNull(logoutResponse);
        assertEquals("Logout failed", logoutResponse.getBody());
        assertEquals(500, logoutResponse.getStatusCodeValue());
        verify(authService, times(1)).logout(request, response);
    }
}
