package com.pfe.Reservation_Bill_Management.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pfe.Reservation_Bill_Management.entities.CckAdmin;

public interface CckAdminRepository extends JpaRepository<CckAdmin, Long>{
	
	public Optional<CckAdmin> findByEmail(String Email);

}
