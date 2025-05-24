package com.pfe.Reservation_Bill_Management.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pfe.Reservation_Bill_Management.entities.Plan;
import com.pfe.Reservation_Bill_Management.entities.Professor;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long>{
	
	Plan findPlanByName(String name);
}
