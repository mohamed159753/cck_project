package com.pfe.Reservation_Bill_Management.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.pfe.Reservation_Bill_Management.entities.User;
import com.pfe.Reservation_Bill_Management.services.user.RegisterService;

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
   



    public ResponseEntity<Map<String, String>> register(User user) {
        Map<String, String> response = new HashMap<>();
        userServiceRegister.addUser(user);
        response.put("message", "You're in the main page");
        return ResponseEntity.ok(response);
    }
    
    //For CCK login 
    
    @Autowired
    private com.pfe.Reservation_Bill_Management.services.user.LoginServiceCCK loginServiceCCK;

    @PostMapping("/login/cck")
    public ResponseEntity<Map<String, String>> loginCCK(@RequestBody Map<String, String> credentials) {
        Map<String, String> response = new HashMap<>();
        
        String username = credentials.get("username");
        String password = credentials.get("password");

        String token = loginServiceCCK.authenticateWithCCK(username, password);
        
        if (token != null) {
            response.put("message", "Login successful via CCK");
            response.put("token", token);
        } else {
            response.put("message", "Login failed via CCK");
        }

        return ResponseEntity.ok(response);
    }

    
}


