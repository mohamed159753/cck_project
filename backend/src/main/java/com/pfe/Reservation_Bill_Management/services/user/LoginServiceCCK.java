package com.pfe.Reservation_Bill_Management.services.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.MediaType;



@Service
public class LoginServiceCCK {

    private static final String API_URL = "https://iam-apigateway-proxy.mesrscloud.rnu.tn/v3/auth/tokens";
    private static final String DOMAIN_NAME = "CCK";
    private static final String PROJECT_ID = "cd98a169ba7949cbb56ea6d05a06b0f4";

    public String authenticateWithCCK(String email, String password) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> requestBody = Map.of(
            "auth", Map.of(
                "identity", Map.of(
                    "methods", List.of("password"),
                    "password", Map.of(
                        "user", Map.of(
                            "name", email,
                            "password", password,
                            "domain", Map.of("name", DOMAIN_NAME)
                        )
                    )
                ),
                "scope", Map.of(
                    "project", Map.of("id", PROJECT_ID)
                )
            )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, requestEntity, String.class);

        String token = response.getHeaders().getFirst("X-Subject-Token");
        return token != null ? token : null;
    }
    
}
