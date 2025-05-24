package com.pfe.Reservation_Bill_Management.web;


import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pfe.Reservation_Bill_Management.dto.InstituteConsumptionDTO;
import com.pfe.Reservation_Bill_Management.dto.ProfessorReservationDTO;
import com.pfe.Reservation_Bill_Management.dto.UniversityStatsDTO;
import com.pfe.Reservation_Bill_Management.services.user.UniversityReportService;
@CrossOrigin(origins = "http://localhost:4200") // Adjust this to your Angular app URL
@RestController
@RequestMapping("/api/dashboard")
public class UniversityReportController {
	@Autowired
    private UniversityReportService dashboardService;
    
    @GetMapping("/stats/{universityId}/{month}/{year}")
    public ResponseEntity<UniversityStatsDTO> getDashboardStats(
            @PathVariable String universityId,
            @PathVariable int month,
            @PathVariable int year) {
        
        return ResponseEntity.ok(dashboardService.getDashboardStats(universityId, month, year));
    }
    
    @GetMapping("/institutes/{universityId}/{month}/{year}")
    public ResponseEntity<List<InstituteConsumptionDTO>> getTopInstitutes(
            @PathVariable String universityId,
            @PathVariable int month,
            @PathVariable int year) {
        
        return ResponseEntity.ok(dashboardService.getTopInstitutesByConsumption(universityId, month, year));
    }
    
    @GetMapping("/professors/{universityId}/{month}/{year}")
    public ResponseEntity<List<ProfessorReservationDTO>> getTopProfessors(
            @PathVariable String universityId,
            @PathVariable int month,
            @PathVariable int year) {
        
        return ResponseEntity.ok(dashboardService.getTopProfessorsByReservations(universityId, month, year));
    }
}

