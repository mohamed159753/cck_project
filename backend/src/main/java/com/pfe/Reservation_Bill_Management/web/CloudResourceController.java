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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pfe.Reservation_Bill_Management.entities.CloudResource;
import com.pfe.Reservation_Bill_Management.security.JwtUtil;
import com.pfe.Reservation_Bill_Management.services.user.CloudResourceService;

@RestController
@RequestMapping("/api/resources")
@CrossOrigin(origins = "http://localhost:4200")
public class CloudResourceController {
    
    private final CloudResourceService cloudResourceService;
    
    private final JwtUtil jwtUtil;
    
    @Autowired
    public CloudResourceController(CloudResourceService cloudResourceService,JwtUtil jwtUtil ) {
        this.cloudResourceService = cloudResourceService;
        this.jwtUtil = jwtUtil;
    }
    
    @GetMapping
    public ResponseEntity<List<CloudResource>> getAllResources() {
        return new ResponseEntity<>(cloudResourceService.getAllResources(), HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CloudResource> getResourceById(@PathVariable Long id) {
        return cloudResourceService.getResourceById(id)
                .map(resource -> new ResponseEntity<>(resource, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/type/{type}")
    public ResponseEntity<List<CloudResource>> getResourcesByType(@PathVariable String type) {
        List<CloudResource> resources = cloudResourceService.getResourcesByType(type);
        if (resources.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }
    
    @PostMapping
    public ResponseEntity<CloudResource> createResource(@RequestBody CloudResource resource) {
        return new ResponseEntity<>(cloudResourceService.saveResource(resource), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CloudResource> updateResource(@PathVariable Long id, @RequestBody CloudResource resource) {
        return cloudResourceService.getResourceById(id)
                .map(existingResource -> {
                    resource.setId(id);
                    return new ResponseEntity<>(cloudResourceService.saveResource(resource), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteResource(@PathVariable Long id) {
        try {
            cloudResourceService.deleteResource(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<CloudResource>> getActiveEcs(@RequestHeader("Authorization") String authHeader
) {
    	String token = authHeader.replace("Bearer ", "");
        Long professorId = jwtUtil.extractProfessorId(token);
        return ResponseEntity.ok(cloudResourceService.getActiveEcs(professorId));
    }
    
    @GetMapping("/active-payg")
    public ResponseEntity<List<CloudResource>> getActivePaygEcs() {
        return ResponseEntity.ok(cloudResourceService.getActivePaygEcs());
    }
    /*
    @GetMapping("/{id}/check-availability")
    public ResponseEntity<Boolean> checkAvailability(@PathVariable Long id) {
        return new ResponseEntity<>(cloudResourceService.checkAvailability(id), HttpStatus.OK);
    }
    
    @PutMapping("/{id}/update-price")
    public ResponseEntity<HttpStatus> updatePrice(@PathVariable Long id, @RequestParam float newPrice) {
        try {
            cloudResourceService.updatePrice(id, newPrice);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/
}