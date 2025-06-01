package com.pfe.Reservation_Bill_Management.web;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pfe.Reservation_Bill_Management.dao.CloudResourceRepository;
import com.pfe.Reservation_Bill_Management.dao.EcsUsageRepository;
import com.pfe.Reservation_Bill_Management.dao.ProfessorRepository;
import com.pfe.Reservation_Bill_Management.entities.CloudResource;
import com.pfe.Reservation_Bill_Management.entities.EcsUsage;
import com.pfe.Reservation_Bill_Management.entities.Professor;
import com.pfe.Reservation_Bill_Management.entities.Reservation.ReservationType;
import com.pfe.Reservation_Bill_Management.security.JwtUtil;

@RestController
@RequestMapping("/api/ecs-usage")
public class EcsUsageLogController {

    @Autowired
    private EcsUsageRepository ecsUsageRepository;

    @Autowired
    private CloudResourceRepository cloudResourceRepository;

    @Autowired
    private ProfessorRepository professorRepository;
    
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/start-log")
    public ResponseEntity<?> logStart(
            @RequestParam Long cloudResourceId,
            @RequestHeader("Authorization") String authHeader) {

        // 1. Extract user ID from token
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtil.extractProfessorId(token); // You must implement this method

        // 2. Fetch entities
        CloudResource resource = cloudResourceRepository.findById(cloudResourceId).orElse(null);
        Professor professor = professorRepository.findById(userId).orElse(null);
        
        System.out.println(resource);
        System.out.println(professor);


        // 3. Validation
        if (resource == null || professor == null) {
            return ResponseEntity.badRequest().body("Invalid resource or user.");
        }
        
        if(resource.getReservation().getReservationType() == ReservationType.PAYG) {
        	
        	// 4. Prevent duplicate sessions
            var existing = ecsUsageRepository.findByCloudResourceAndProfAndStopTimeIsNull(resource, professor);
            if (existing.isPresent()) {
                return ResponseEntity.badRequest().body("ECS is already running for this professor.");
            }

            // 5. Save usage log
            EcsUsage usage = new EcsUsage();
            usage.setCloudResource(resource);
            usage.setProf(professor);
            usage.setStartTime(LocalDateTime.now());

            ecsUsageRepository.save(usage);
            
            resource.setStatus("running");
        	Map<String, String> response = new HashMap<>();
        	cloudResourceRepository.save(resource);
        	response.put("message", "start time logged.");
            return ResponseEntity.ok(response);
        	
        }
        
        else {
        	Map<String, String> response = new HashMap<>();
        	response.put("message", "ECS start.");
        	resource.setStatus("running");
        	cloudResourceRepository.save(resource);
        	return ResponseEntity.ok(response);

        }

        
    }

    @PostMapping("/stop-log")
    public ResponseEntity<?> logStop(
            @RequestParam Long cloudResourceId,
            @RequestHeader("Authorization") String authHeader) {

        // 1. Extract professorId from the token
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtil.extractProfessorId(token); // same method as before

        // 2. Fetch entities
        CloudResource resource = cloudResourceRepository.findById(cloudResourceId).orElse(null);
        Professor professor = professorRepository.findById(userId).orElse(null);
        
        System.out.println(resource);
        System.out.println(professor);

        if (resource == null || professor == null) {
            return ResponseEntity.badRequest().body("Invalid resource or user.");
        }
        
        if(resource.getReservation().getReservationType() == ReservationType.PAYG) {


        // 3. Find active usage
        EcsUsage usage = ecsUsageRepository
            .findByCloudResourceAndProfAndStopTimeIsNull(resource, professor)
            .orElse(null);

        if (usage == null) {
            return ResponseEntity.badRequest().body("No running session found.");
        }

        // 4. Stop usage
        LocalDateTime stopTime = LocalDateTime.now();
        usage.setStopTime(stopTime);

        // 5. Cost calculation
        long minutes = Duration.between(usage.getStartTime(), stopTime).toMinutes();
        float hours = minutes / 60.0f;
        float cost = hours * resource.getPricePerHour();
        usage.setCost(cost);

        ecsUsageRepository.save(usage);
        
        resource.setStatus("shutdown");
    	cloudResourceRepository.save(resource);
    	
    	Map<String, String> response = new HashMap<>();
    	response.put("message", "Stop time logged. Usage: " + hours + " hours. Cost: $" + cost);

        return ResponseEntity.ok(response);
    }
        
        else {
        	resource.setStatus("shutdown");
        	cloudResourceRepository.save(resource);
        	Map<String, String> response = new HashMap<>();
        	response.put("message", "ECS shutdown.");
            return ResponseEntity.ok(response);

        }
        
  }
        

}
