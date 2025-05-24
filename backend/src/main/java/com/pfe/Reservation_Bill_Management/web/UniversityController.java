package com.pfe.Reservation_Bill_Management.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pfe.Reservation_Bill_Management.dao.UniversityRepository;
import com.pfe.Reservation_Bill_Management.dto.UniversityRequest;
import com.pfe.Reservation_Bill_Management.entities.Plan;
import com.pfe.Reservation_Bill_Management.entities.Quota;
import com.pfe.Reservation_Bill_Management.entities.Subscription;
import com.pfe.Reservation_Bill_Management.entities.University;
import com.pfe.Reservation_Bill_Management.services.user.PlanService;
import com.pfe.Reservation_Bill_Management.services.user.SubscriptionService;
import com.pfe.Reservation_Bill_Management.services.user.UniversityService;

@RestController
@RequestMapping("/api/universities")
@CrossOrigin(origins = "http://localhost:4200") // Adjust this to your Angular app URL

public class UniversityController {

    @Autowired
    private UniversityRepository universityRepository;
    @Autowired
    private PlanService planService;

    @GetMapping
    public List<University> getAllUniversities() {
        return universityRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<University> getUniversityById(@PathVariable String id) {
        return universityRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}")
    public University createUniversity(@PathVariable String id, @RequestBody UniversityRequest request) {
    	University university = new University();
    	university.setId(id);
    	university.setUniversityName(request.getName());
    	
    	Optional<Plan> plan = planService.getPlanByName(request.getPlanName());
    	Quota quata = new Quota();
    	quata.setVcpu(plan.get().getVcpu());
    	quata.setStorageInGb(plan.get().getStorageInGb());
    	quata.setRamInMb(plan.get().getRamInMb());

    	university.setQuota(quata);
    	
    	Subscription subscription = new Subscription();
    	
    	subscription.setPlan(plan.get());
    	subscription.setPrice(plan.get().getPrice());
    	subscription.setStatus("Active");
    	subscription.setUniversity(university);
    	
    	Date startDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.YEAR, 1); // add 1 year
        Date endDate = calendar.getTime();

        subscription.setStartDate(startDate);
        subscription.setEndDate(endDate);

        // Initialize subscriptions list if null
        if (university.getSubscriptions() == null) {
            university.setSubscriptions(new ArrayList<>());
        }
        university.getSubscriptions().add(subscription);
        
      
    	
        return universityRepository.save(university);
    }

    @PutMapping("/{id}")
    public ResponseEntity<University> updateUniversity(@PathVariable String id, @RequestBody University universityDetails) {
        return universityRepository.findById(id).map(university -> {
            university.setQuota(universityDetails.getQuota());
            return ResponseEntity.ok(universityRepository.save(university));
        }).orElse(ResponseEntity.notFound().build());
    }
    
    
}
