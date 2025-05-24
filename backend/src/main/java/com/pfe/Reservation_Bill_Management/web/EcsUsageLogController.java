package com.pfe.Reservation_Bill_Management.web;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pfe.Reservation_Bill_Management.dao.CloudResourceRepository;
import com.pfe.Reservation_Bill_Management.dao.EcsUsageRepository;
import com.pfe.Reservation_Bill_Management.dao.ProfessorRepository;
import com.pfe.Reservation_Bill_Management.entities.CloudResource;
import com.pfe.Reservation_Bill_Management.entities.EcsUsage;
import com.pfe.Reservation_Bill_Management.entities.Professor;

@RestController
@RequestMapping("/ecs-usage")
public class EcsUsageLogController {

    @Autowired
    private EcsUsageRepository ecsUsageRepository;

    @Autowired
    private CloudResourceRepository cloudResourceRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @PostMapping("/start-log")
    public ResponseEntity<?> logStart(@RequestParam Long cloudResourceId, @RequestParam Long userId) {
        CloudResource resource = cloudResourceRepository.findById(cloudResourceId).orElse(null);
        Professor professor = professorRepository.findById(userId).orElse(null);

        if (resource == null || professor == null) {
            return ResponseEntity.badRequest().body("Invalid resource or user.");
        }

        // Prevent duplicate sessions
        var existing = ecsUsageRepository.findByCloudResourceAndProfAndStopTimeIsNull(resource, professor);
        if (existing.isPresent()) {
            return ResponseEntity.badRequest().body("ECS is already running for this professor.");
        }

        EcsUsage usage = new EcsUsage();
        usage.setCloudResource(resource);
        usage.setProf(professor);
        usage.setStartTime(LocalDateTime.now());

        ecsUsageRepository.save(usage);
        return ResponseEntity.ok("Start time logged.");
    }

    @PostMapping("/stop-log")
    public ResponseEntity<?> logStop(@RequestParam Long cloudResourceId, @RequestParam Long userId) {
        CloudResource resource = cloudResourceRepository.findById(cloudResourceId).orElse(null);
        Professor professor = professorRepository.findById(userId).orElse(null);

        if (resource == null || professor == null) {
            return ResponseEntity.badRequest().body("Invalid resource or user.");
        }

        EcsUsage usage = ecsUsageRepository.findByCloudResourceAndProfAndStopTimeIsNull(resource, professor)
                .orElse(null);

        if (usage == null) {
            return ResponseEntity.badRequest().body("No running session found.");
        }

        LocalDateTime stopTime = LocalDateTime.now();
        usage.setStopTime(stopTime);

        // Cost calculation
        long minutes = Duration.between(usage.getStartTime(), stopTime).toMinutes();
        float hours = minutes / 60.0f;
        float cost = hours * resource.getPricePerHour();
        usage.setCost(cost);

        ecsUsageRepository.save(usage);

        return ResponseEntity.ok("Stop time logged. Usage: " + hours + " hours. Cost: $" + cost);
    }
}
