package com.example.lecturescheduler.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private OAuth2User principal;



    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testHome() {
        ResponseEntity<String> response = authService.home();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Hello, welcome to the home page!", response.getBody());
    }

    @Test
    public void testSecured() {
        when(principal.getAttribute("email")).thenReturn("test@example.com");
        ResponseEntity<String> response = authService.secured(principal);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Hello, test@example.com!", response.getBody());
    }


    @Test
    void testLogoutWithAuthentication()  {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        ResponseEntity<String> responseEntity = authService.logout(request, response);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals("User logged out successfully.", responseEntity.getBody());
        verify(securityContext, times(1)).getAuthentication();
        verify(request, times(2)).getSession(false);
    }

    @Test
    void testLogoutWithoutAuthentication()  {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(null);

        ResponseEntity<String> responseEntity = authService.logout(request, response);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals("User logged out successfully.", responseEntity.getBody());
        verify(securityContext, times(1)).getAuthentication();
        verify(request, never()).getSession(false);
    }
}
