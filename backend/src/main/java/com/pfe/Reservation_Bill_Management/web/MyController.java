package com.pfe.Reservation_Bill_Management.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pfe.Reservation_Bill_Management.dao.UniversityRepository;
import com.pfe.Reservation_Bill_Management.dto.AdminLoginRequest;
import com.pfe.Reservation_Bill_Management.entities.CckAdmin;
import com.pfe.Reservation_Bill_Management.entities.Professor;
import com.pfe.Reservation_Bill_Management.entities.University;
import com.pfe.Reservation_Bill_Management.entities.UniversityAdmin;
import com.pfe.Reservation_Bill_Management.entities.User;
import com.pfe.Reservation_Bill_Management.security.JwtUtil;
import com.pfe.Reservation_Bill_Management.services.user.LoginServiceCCK;
import com.pfe.Reservation_Bill_Management.services.user.LoginServiceUniversities;
import com.pfe.Reservation_Bill_Management.services.user.ProfessorService;
import com.pfe.Reservation_Bill_Management.services.user.RegisterService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class MyController {
	

	private final com.pfe.Reservation_Bill_Management.services.user.LoginService userService;
	private final com.pfe.Reservation_Bill_Management.services.user.RegisterService userServiceRegister;
	private final LoginServiceUniversities loginServiceUniversities;
	private final ProfessorService professorService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	 @Autowired
	 private LoginServiceUniversities universityAdminService;
	 
	 @Autowired
	 private LoginServiceCCK cckAdminService;
	 
	 @Autowired
	 private UniversityRepository universityRepository;
	
	@Autowired
	public MyController(com.pfe.Reservation_Bill_Management.services.user.LoginService userService, RegisterService userServiceRegister, LoginServiceUniversities loginServiceUni,ProfessorService professorService ) {
		this.userService = userService;
		this.userServiceRegister = userServiceRegister;
		this.loginServiceUniversities = loginServiceUni;
		this.professorService = professorService;
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
        	
        	Optional<Professor> prof = professorService.findProfByEmail(email);
        	
        	if (!prof.get().isActivated()) {
            	return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        	}
        	
        	 String token = jwtUtil.generateToken(email,prof.get().getId());
        	 response.put("token", token);
        	 response.put("status", "success");
        	 return ResponseEntity.ok(response);
        }
        else {
        	response.put("status", "fail");
        	response.put("errorMessage", "Invalid username or password");
        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody Map<String, String> credentials) {
        Map<String, String> response = new HashMap<>();
        
        String username = credentials.get("username"); 
        String email = credentials.get("email");
        String password = credentials.get("password");
        String cinString = credentials.get("cin");
        int cin = Integer.parseInt(cinString);
        String uni_id = credentials.get("projectId");
        String institut = credentials.get("institut");
        


        
        if(userServiceRegister.registerUser(username,email,password,cin,uni_id,institut)) {
        	response.put("message", "User Registed Successfully from spring boot");
            return ResponseEntity.ok(response);
        }
        
        response.put("message", "User already exists");
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/uni-login")
    public UniversityAdmin uniLogin(@RequestBody AdminLoginRequest request) {
        University uni = universityRepository.findById(request.getUniversityId())
            .orElseThrow(() -> new RuntimeException("University not found"));

        return universityAdminService.getOrCreateUniversityAdmin(request.getEmail(), uni);
    }
    
    @PostMapping("/cck-login")
    public CckAdmin uniLogin(@RequestBody String email) {
       

        return cckAdminService.getOrCreateUniversityAdmin(email);
    }
    /* @PostMapping("/cck-login")
    public CckAdmin Ccklogin(@RequestBody AdminLoginRequest request) {
        return cckAdminService.getOrCreateUniversityAdmin(request.getEmail(), ); 
    }*/
    
    @GetMapping("/activate/{token}")
    public ResponseEntity<String> activateAccount(@PathVariable String token) {
        boolean activated = userServiceRegister.activateUser(token);

        if (!activated) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid activation link.");
        }

        return ResponseEntity.ok("Your account has been activated. You can now log in.");
    }
    
    
    
    

        
    }


    
 

