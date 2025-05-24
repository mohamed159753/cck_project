package com.pfe.Reservation_Bill_Management.services.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pfe.Reservation_Bill_Management.dao.SubscriptionRepository;
import com.pfe.Reservation_Bill_Management.entities.Subscription;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public List<Subscription> getAll() {
        return subscriptionRepository.findAll();
    }

    public Subscription create(Subscription sub) {
        return subscriptionRepository.save(sub);
    }
/*
    public Subscription cancel(int id) {
        Subscription sub = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));
        sub.setStatus("Cancelled");
        return subscriptionRepository.save(sub);
    }*/
}
