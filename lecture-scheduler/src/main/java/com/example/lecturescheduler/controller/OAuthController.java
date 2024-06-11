package com.example.lecturescheduler.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
public class OAuthController {

    @PostMapping("/token")
    public ResponseEntity<Map<String, String>> getToken(@RequestBody Map<String, String> request) {
        String code = request.get("code");

        RestTemplate restTemplate = new RestTemplate();

        String url = "https://oauth2.googleapis.com/token";

        Map<String, String> body = new HashMap<>();
        body.put("code", code);
        body.put("client_id", "1075986554465-h89n6mls3cbdqsib3fa2h79ib4m0dg31.apps.googleusercontent.com");
        body.put("client_secret", "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");
        body.put("redirect_uri", "http://localhost:8080/login/oauth2/code/google");
        body.put("grant_type", "authorization_code");

        // Using RestTemplate to make the request to Google's OAuth2 token endpoint
        Map<String, String> response = restTemplate.postForObject(url, body, Map.class);

        // Extracting tokens from the response
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", response.get("access_token"));
        tokens.put("refreshToken", response.get("refresh_token"));

        // Returning the tokens as the response
        return ResponseEntity.ok(tokens);
    }
}
