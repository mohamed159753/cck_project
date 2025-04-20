package com.pfe.Reservation_Bill_Management.web;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

import com.pfe.Reservation_Bill_Management.dao.ReservationRepository;
import com.pfe.Reservation_Bill_Management.entities.Reservation;
import com.pfe.Reservation_Bill_Management.services.user.ReservationService;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "http://localhost:4200")
public class ReservationController {
    
    @Autowired
    private ReservationService reservationService;
    
    @GetMapping("/university/{universityId}")
    public ResponseEntity<Object> getReservationsByUniversity(
            @PathVariable int universityId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        if (startDate != null && endDate != null) {
            // Add this method to your service and repository
            return ResponseEntity.ok(reservationService.findByUniversityIdAndDateRange(universityId, startDate, endDate));
        }
        
        // Add this method to your service and repository
        return ResponseEntity.ok(reservationService.findByUniversityId(universityId));
    }
    
    @GetMapping("/professor/{professorId}")
    public ResponseEntity<List<Reservation>> getReservationsByProfessor(@PathVariable long professorId) {
        // Add this method to your service and repository
        return ResponseEntity.ok(reservationService.findByProfessorId(professorId));
    }
    
    @GetMapping("/count/{universityId}/{month}/{year}")
    public ResponseEntity<Integer> countReservationsByMonthAndYear(
            @PathVariable int universityId,
            @PathVariable int month,
            @PathVariable int year) {
        
        return ResponseEntity.ok(reservationService.countReservationsByMonthAndYear(universityId, month, year));
    }
}