package com.pfe.Reservation_Bill_Management.services.user;


import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class LoginServiceUniversities {
	
	private final RestTemplate restTemplate;

    public LoginServiceUniversities(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public String fetchProjects() {
        String url = "https://iam-apigateway-proxy.mesrscloud.rnu.tn/v3/projects";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Auth-Token", "MIIFKQYJKoZIhvcNAQcCoIIFGjCCBRYCAQExD..."); // truncated token
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        return response.getBody();
    }

}
