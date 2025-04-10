package com.pfe.Reservation_Bill_Management.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.pfe.Reservation_Bill_Management.entities.User;
import com.pfe.Reservation_Bill_Management.services.user.RegisterService;

import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class MyController {
	

	private final com.pfe.Reservation_Bill_Management.services.user.LoginService userService;
	private final com.pfe.Reservation_Bill_Management.services.user.RegisterService userServiceRegister;
	
	@Autowired
	public MyController(com.pfe.Reservation_Bill_Management.services.user.LoginService userService, RegisterService userServiceRegister ) {
		this.userService = userService;
		this.userServiceRegister = userServiceRegister;
	}

	

    @GetMapping("/")
    public ResponseEntity<Map<String, String>> hello() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "You're in the main page");
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        Map<String, String> response = new HashMap<>();
        
        String username = credentials.get("username");
        String password = credentials.get("password");
        
        if(userService.isValid(username, password)) {
        	response.put("message", "Logged In successfully");
        }
        else {
            response.put("message", "login failed");

        }
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody Map<String, String> credentials) {
        Map<String, String> response = new HashMap<>();
        
        String username = credentials.get("username"); 
        String email = credentials.get("email");
        String password = credentials.get("password");
        
        if(userServiceRegister.registerUser(username, email, password)) {
        	response.put("message", "User Registed Successfully from spring boot");
            return ResponseEntity.ok(response);
        }
        
        response.put("message", "User already exists");
        return ResponseEntity.ok(response);
    }
    

        @PostMapping("/chatbot")
        public ResponseEntity<String> chatbot(@RequestBody Map<String, String> request) {
            String userMessage = request.get("message");
            System.out.println("Received message: " + userMessage);
            
            String flaskApiUrl = "http://127.0.0.1:5000/chatbot";
            System.out.println("Flask API URL: " + flaskApiUrl);

            // Initialize RestTemplate and headers
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Create request payload
            Map<String, String> requestPayload = new HashMap<>();
            requestPayload.put("message", userMessage);

            // Log the request payload before sending it
            System.out.println("Sending request to Flask API: " + requestPayload);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestPayload, headers);

            // Try sending the request to Flask API
            try {
                // Send the message to the Flask API
                ResponseEntity<String> flaskResponse = restTemplate.postForEntity(flaskApiUrl, entity, String.class);

                // Log the Flask API response
                System.out.println("Received response from Flask: " + flaskResponse.getBody());

                // Return the response from Flask to the frontend
                return ResponseEntity.ok(flaskResponse.getBody());
            } catch (Exception e) {
                // Log the exception if there's an error with the API call
                System.err.println("Error while calling Flask API: " + e.getMessage());
                e.printStackTrace(); // Print the full stack trace for debugging

                // Return a generic error message
                return ResponseEntity.status(500).body("Error while processing the request. Please try again later.");
            }
        }
    }


    
 

