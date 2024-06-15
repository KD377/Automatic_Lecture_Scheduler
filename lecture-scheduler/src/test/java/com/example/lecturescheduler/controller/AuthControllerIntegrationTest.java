package com.example.lecturescheduler.controller;

import com.example.lecturescheduler.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class AuthControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHome() throws Exception {
        when(authService.home()).thenReturn(ResponseEntity.ok("Home"));

        mockMvc.perform(get("/windows/home"))
                .andExpect(status().isOk())
                .andExpect(content().string("Home"));
    }

    @Test
    @WithMockUser
    void testSecured() throws Exception {
        when(authService.secured(any())).thenReturn(ResponseEntity.ok("Secured"));

        mockMvc.perform(get("/windows/secured"))
                .andExpect(status().isOk())
                .andExpect(content().string("Secured"));
    }

    @Test
    void testSecuredUnauthorized() throws Exception {
        mockMvc.perform(get("/windows/secured"))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser
    void testLogout() throws Exception {
        when(authService.logout(any(), any())).thenReturn(ResponseEntity.ok("Logged out"));

        mockMvc.perform(get("/windows/logout"))
                .andExpect(status().isOk())
                .andExpect(content().string("Logged out"));
    }

    @Test
    void testLogoutUnauthorized() throws Exception {
        mockMvc.perform(get("/windows/logout"))
                .andExpect(status().isFound());
    }

}
