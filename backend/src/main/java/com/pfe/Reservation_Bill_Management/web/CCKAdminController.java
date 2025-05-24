package com.pfe.Reservation_Bill_Management.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pfe.Reservation_Bill_Management.services.user.CCKService;

import jakarta.ws.rs.PathParam;

import com.pfe.Reservation_Bill_Management.entities.University;
import com.pfe.Reservation_Bill_Management.entities.Professor;
import com.pfe.Reservation_Bill_Management.entities.Reservation;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*") // Adjust as needed for security
public class CCKAdminController {

    @Autowired
    private CCKService cckAdminService;

    /**
     * Get dashboard statistics
     */
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardStatistics() {
        return ResponseEntity.ok(cckAdminService.getDashboardStatistics());
    }
    
    @GetMapping("/dashboard/university/{id}")
    public ResponseEntity<Map<String, Object>> getUniversityDashboardStatistics(@PathVariable String id) {
        return ResponseEntity.ok(cckAdminService.getUniversityDashboardStatistics(id));
    }

    /**
     * University endpoints
     */
    @GetMapping("/universities")
    public ResponseEntity<List<University>> getAllUniversities() {
        return ResponseEntity.ok(cckAdminService.getAllUniversities());
    }

    @GetMapping("/universities/{id}")
    public ResponseEntity<University> getUniversityById(@PathVariable String id) {
        University university = cckAdminService.getUniversityById(id);
        if (university != null) {
            return ResponseEntity.ok(university);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/universities")
    public ResponseEntity<University> createUniversity(@RequestBody University university) {
        return new ResponseEntity<>(cckAdminService.createUniversity(university), HttpStatus.CREATED);
    }

    @PutMapping("/universities/{id}")
    public ResponseEntity<University> updateUniversity(@PathVariable String id, @RequestBody University university) {
        University updatedUniversity = cckAdminService.updateUniversity(id, university);
        if (updatedUniversity != null) {
            return ResponseEntity.ok(updatedUniversity);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/universities/{id}")
    public ResponseEntity<Void> deleteUniversity(@PathVariable String id) {
        if (cckAdminService.deleteUniversity(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Professor endpoints
     */
    @GetMapping("/universities/{universityId}/professors")
    public ResponseEntity<List<Professor>> getProfessorsByUniversity(@PathVariable String universityId) {
        return ResponseEntity.ok(cckAdminService.getProfessorsByUniversity(universityId));
    }

    @GetMapping("/professors/{id}")
    public ResponseEntity<Professor> getProfessorById(@PathVariable Long id) {
        Professor professor = cckAdminService.getProfessorById(id);
        if (professor != null) {
            return ResponseEntity.ok(professor);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/universities/{universityId}/professors")
    public ResponseEntity<Professor> createProfessor(@RequestBody Professor professor, 
                                                   @PathVariable String universityId) {
        Professor newProfessor = cckAdminService.createProfessor(professor, universityId);
        if (newProfessor != null) {
            return new ResponseEntity<>(newProfessor, HttpStatus.CREATED);
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/professors/{id}")
    public ResponseEntity<Professor> updateProfessor(@PathVariable Long id, @RequestBody Professor professor) {
        Professor updatedProfessor = cckAdminService.updateProfessor(id, professor);
        if (updatedProfessor != null) {
            return ResponseEntity.ok(updatedProfessor);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/professors/{id}")
    public ResponseEntity<Void> deleteProfessor(@PathVariable Long id) {
        if (cckAdminService.deleteProfessor(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> getReservations() {
        List<Reservation> reservations = cckAdminService.getReservations();
        if (reservations != null) {
            return ResponseEntity.ok(reservations);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/reservations/status")
    public ResponseEntity<List<Reservation>> getReservations2() {
        List<Reservation> reservations = cckAdminService.getReservations();
        if (reservations != null) {
            return ResponseEntity.ok(reservations);
        }
        return ResponseEntity.notFound().build();
    }
}