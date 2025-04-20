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

import com.pfe.Reservation_Bill_Management.dao.UniversityRepository;
import com.pfe.Reservation_Bill_Management.entities.Subscription;
import com.pfe.Reservation_Bill_Management.entities.University;
import com.pfe.Reservation_Bill_Management.services.user.SubscriptionService;
import com.pfe.Reservation_Bill_Management.services.user.UniversityService;

@RestController
@RequestMapping("/api/universities")
@CrossOrigin(origins = "http://localhost:4200") // Adjust this to your Angular app URL

public class UniversityController {

    @Autowired
    private UniversityRepository universityRepository;

    @GetMapping
    public List<University> getAllUniversities() {
        return universityRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<University> getUniversityById(@PathVariable int id) {
        return universityRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public University createUniversity(@RequestBody University university) {
        return universityRepository.save(university);
    }

    @PutMapping("/{id}")
    public ResponseEntity<University> updateUniversity(@PathVariable int id, @RequestBody University universityDetails) {
        return universityRepository.findById(id).map(university -> {
            university.setQuota(universityDetails.getQuota());
            return ResponseEntity.ok(universityRepository.save(university));
        }).orElse(ResponseEntity.notFound().build());
    }
}
