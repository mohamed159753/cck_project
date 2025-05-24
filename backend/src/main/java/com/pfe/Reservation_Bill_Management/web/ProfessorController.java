package com.pfe.Reservation_Bill_Management.web;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pfe.Reservation_Bill_Management.dao.ProfessorRepository;
import com.pfe.Reservation_Bill_Management.dto.ChangePasswordRequest;
import com.pfe.Reservation_Bill_Management.entities.Professor;
import com.pfe.Reservation_Bill_Management.entities.Reservation;
import com.pfe.Reservation_Bill_Management.entities.University;
import com.pfe.Reservation_Bill_Management.security.JwtUtil;
import com.pfe.Reservation_Bill_Management.services.user.EmailChangeService;
import com.pfe.Reservation_Bill_Management.services.user.ProfessorService;

@RestController
@RequestMapping("/api/professors")
@CrossOrigin(origins = "http://localhost:4200")
public class ProfessorController {
    
    @Autowired
    private ProfessorService professorService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private EmailChangeService emailChangeService;
    
    @Autowired
    private ProfessorRepository professorRepository;

    
    @GetMapping("/university")
    public ResponseEntity<Map<String, String>> getUniversityName(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Long professorId = jwtUtil.extractProfessorId(token);

        Optional<Professor> professor = professorService.findById(professorId);
        
        Optional<University> uni = professor.get().getUniversity();
        String uni_name = uni.get().getUniversityName();
        
        Map<String, String> response = new HashMap<>();
        response.put("universityName", uni.get().getUniversityName());
        return ResponseEntity.ok(response);    }
    
    @GetMapping("/university_id")
    public ResponseEntity<Map<String, String>> getUniversityId(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Long professorId = jwtUtil.extractProfessorId(token);

        Optional<Professor> professor = professorService.findById(professorId);
        
        Optional<University> uni = professor.get().getUniversity();
        String uni_id = uni.get().getId();
        
        Map<String, String> response = new HashMap<>();
        response.put("universityId", uni_id);
        return ResponseEntity.ok(response);    }
    
    
    
    @GetMapping("/university/{universityId}")
    public ResponseEntity<Object> getProfessorsByUniversity(@PathVariable String universityId) {
        return ResponseEntity.ok(professorService.findByUniversityId(universityId));
    }
    
    @GetMapping("/institutes/{universityId}")
    public ResponseEntity<Integer> getUniqueInstitutes(@PathVariable String universityId) {
        return ResponseEntity.ok(professorService.getUniqueInstitutes(universityId));
    }
    
    @GetMapping("/count/institutes/{universityId}")
    public ResponseEntity<Integer> countInstitutes(@PathVariable String universityId) {
        return ResponseEntity.ok(professorService.countInstitutes(universityId));
    }
    
    @GetMapping("/count/professors/{universityId}")
    public ResponseEntity<Integer> countProfessors(@PathVariable String universityId) {
        return ResponseEntity.ok(professorService.countProfessors(universityId));
    }
    
    @PutMapping("/me")
    public ResponseEntity<Professor> updateOwnProfile(@RequestBody Professor updatedProfessor, 
                                                      @RequestHeader("Authorization") String authHeader) {
        // For now, just fake user ID. Later replace this with ID from token or session
        String token = authHeader.replace("Bearer ", "");
        Long currentProfessorId = jwtUtil.extractProfessorId(token); // implement this
        Professor updated = professorService.updateOwnInformation(currentProfessorId, updatedProfessor);
        return ResponseEntity.ok(updated);
    }
    
    @GetMapping("/me")
    public ResponseEntity<Optional<Professor>> getOwnProfile(@RequestHeader("Authorization") String authHeader) {
    	String token = authHeader.replace("Bearer ", "");
        Long currentProfessorId = jwtUtil.extractProfessorId(token);
        Optional<Professor> prof = professorService.findById(currentProfessorId);
        return ResponseEntity.ok(prof);
    }
    
    @GetMapping("/activate/{token}")
    public ResponseEntity<String> activateOrConfirmEmail(@PathVariable String token) {
		Professor prof = professorRepository.findByActivationToken(token);
        if (prof == null) {
            return ResponseEntity.badRequest().body("Invalid or expired token.");
        }

        if (!prof.isActivated()) {
            // Account activation
            prof.setActivated(true);
            prof.setActivationToken(null);
            professorRepository.save(prof);
            return ResponseEntity.ok("Account activated successfully.");
        } else {
            // Email change confirmation
            String newEmail = emailChangeService.getNewEmail(prof.getId());
            if (newEmail == null) {
                return ResponseEntity.badRequest().body("No pending email change found.");
            }

            prof.setEmail(newEmail);
            prof.setActivationToken(null);
            professorRepository.save(prof);

            emailChangeService.clearNewEmail(prof.getId());
            return ResponseEntity.ok("Email updated successfully.");
        }
    }
    
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request,
                                            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Long professorId = jwtUtil.extractProfessorId(token);

        boolean success = professorService.changePassword(professorId, request.getCurrentPassword(), request.getNewPassword());

        if (success) {
            return ResponseEntity.ok(Collections.singletonMap("message", "Password changed successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Current password is incorrect.");
        }
    }
}