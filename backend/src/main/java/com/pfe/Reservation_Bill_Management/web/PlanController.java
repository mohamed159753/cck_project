package com.pfe.Reservation_Bill_Management.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pfe.Reservation_Bill_Management.entities.Plan;
import com.pfe.Reservation_Bill_Management.entities.Professor;
import com.pfe.Reservation_Bill_Management.entities.University;
import com.pfe.Reservation_Bill_Management.services.user.PlanService;

@RestController
@RequestMapping("/api/plans")
@CrossOrigin(origins = "http://localhost:4200")
public class PlanController {
	
	@Autowired
	PlanService planService;
	
	@GetMapping
    public ResponseEntity<List<Plan>> getPlans() {
        List<Plan> plans = planService.getPlans().orElseThrow(() -> new RuntimeException("No plans found"));
        return ResponseEntity.ok(plans);
    }

}
