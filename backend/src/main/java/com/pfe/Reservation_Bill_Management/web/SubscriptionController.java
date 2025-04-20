package com.pfe.Reservation_Bill_Management.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pfe.Reservation_Bill_Management.dao.SubscriptionRepository;
import com.pfe.Reservation_Bill_Management.entities.Subscription;
import com.pfe.Reservation_Bill_Management.services.user.SubscriptionService;

@RestController
@RequestMapping("/api/subscriptions")
@CrossOrigin(origins = "http://localhost:4200") // Adjust this to your Angular app URL

public class SubscriptionController {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @GetMapping
    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    @PostMapping
    public Subscription createSubscription(@RequestBody Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelSubscription(@PathVariable int id) {
        return subscriptionRepository.findById(id).map(sub -> {
            sub.setStatus("Cancelled");
            return ResponseEntity.ok(subscriptionRepository.save(sub));
        }).orElse(ResponseEntity.notFound().build());
    }
}
