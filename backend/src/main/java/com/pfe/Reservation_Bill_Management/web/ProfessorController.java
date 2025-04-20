package com.pfe.Reservation_Bill_Management.web;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pfe.Reservation_Bill_Management.entities.Professor;
import com.pfe.Reservation_Bill_Management.entities.Reservation;
import com.pfe.Reservation_Bill_Management.services.user.ProfessorService;

@RestController
@RequestMapping("/api/professors")
@CrossOrigin(origins = "http://localhost:4200")
public class ProfessorController {
    
    @Autowired
    private ProfessorService professorService;
    
    @GetMapping("/university/{universityId}")
    public ResponseEntity<Object> getProfessorsByUniversity(@PathVariable int universityId) {
        return ResponseEntity.ok(professorService.findByUniversityId(universityId));
    }
    
    @GetMapping("/institutes/{universityId}")
    public ResponseEntity<Integer> getUniqueInstitutes(@PathVariable int universityId) {
        return ResponseEntity.ok(professorService.getUniqueInstitutes(universityId));
    }
    
    @GetMapping("/count/institutes/{universityId}")
    public ResponseEntity<Integer> countInstitutes(@PathVariable int universityId) {
        return ResponseEntity.ok(professorService.countInstitutes(universityId));
    }
    
    @GetMapping("/count/professors/{universityId}")
    public ResponseEntity<Integer> countProfessors(@PathVariable int universityId) {
        return ResponseEntity.ok(professorService.countProfessors(universityId));
    }
}