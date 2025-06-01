package com.pfe.Reservation_Bill_Management.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pfe.Reservation_Bill_Management.entities.UniversityAdmin;

public interface UniversityAdminRepository extends JpaRepository<UniversityAdmin,Long>{
	
	public Optional<UniversityAdmin> findByEmail(String email);

}
