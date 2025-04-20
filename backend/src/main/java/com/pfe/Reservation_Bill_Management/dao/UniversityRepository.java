package com.pfe.Reservation_Bill_Management.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pfe.Reservation_Bill_Management.entities.University;

@Repository
public interface UniversityRepository extends JpaRepository<University, Integer>{

}

