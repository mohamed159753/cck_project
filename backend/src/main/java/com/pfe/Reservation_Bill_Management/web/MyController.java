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
import com.pfe.Reservation_Bill_Management.services.user.LoginServiceUniversities;
import com.pfe.Reservation_Bill_Management.services.user.RegisterService;

import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class MyController {
	

	private final com.pfe.Reservation_Bill_Management.services.user.LoginService userService;
	private final com.pfe.Reservation_Bill_Management.services.user.RegisterService userServiceRegister;
	private final LoginServiceUniversities loginServiceUniversities;
	
	@Autowired
	public MyController(com.pfe.Reservation_Bill_Management.services.user.LoginService userService, RegisterService userServiceRegister, LoginServiceUniversities loginServiceUni ) {
		this.userService = userService;
		this.userServiceRegister = userServiceRegister;
		this.loginServiceUniversities = loginServiceUni;
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
        
        String email = credentials.get("username");
        String password = credentials.get("password");
        
        if(userService.isValid(email, password)) {
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
    
    
    
    

        
    }


    
 

