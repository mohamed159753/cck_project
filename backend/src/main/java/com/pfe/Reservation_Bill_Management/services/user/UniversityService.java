package com.pfe.Reservation_Bill_Management.services.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pfe.Reservation_Bill_Management.dao.ReservationRepository;
import com.pfe.Reservation_Bill_Management.dao.UniversityRepository;
import com.pfe.Reservation_Bill_Management.entities.Reservation;
import com.pfe.Reservation_Bill_Management.entities.Subscription;
import com.pfe.Reservation_Bill_Management.entities.University;

@Service
public class UniversityService {

    @Autowired
    private UniversityRepository universityRepository;

    public List<University> getAll() {
        return universityRepository.findAll();
    }

    public Optional<University> getById(String id) {
        return universityRepository.findById(id);
    }
    
    

    public University create(University university) {
        return universityRepository.save(university);
    }
/*
    public University updateQuota(int id, int quota) {
        University uni = universityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("University not found"));
        uni.setQuota(quota);
        return universityRepository.save(uni);
    }*/
}
