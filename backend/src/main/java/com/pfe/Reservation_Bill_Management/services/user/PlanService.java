package com.pfe.Reservation_Bill_Management.services.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pfe.Reservation_Bill_Management.dao.PlanRepository;
import com.pfe.Reservation_Bill_Management.entities.Plan;


@Service

public class PlanService {
	
    @Autowired
    private PlanRepository planRepository;
    
    public Optional<List<Plan>> getPlans() {
		return Optional.ofNullable(planRepository.findAll());
	}
    
    public Optional<Plan> getPlanByName(String name){
		return Optional.ofNullable(planRepository.findPlanByName(name));
    }
    
    


}
