package com.pfe.Reservation_Bill_Management.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pfe.Reservation_Bill_Management.entities.Professor;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    
    @Query("SELECT COUNT(DISTINCT p.institute) FROM Professor p WHERE p.university.id = :universityId")
    int countInstitutes(@Param("universityId") int universityId);
    
    @Query("SELECT COUNT(p) FROM Professor p WHERE p.university.id = :universityId")
    int countProfessors(@Param("universityId") int universityId);
    
    @Query("SELECT DISTINCT p.institute FROM Professor p WHERE p.university.id = :universityId")
    List<String> findDistinctInstitutes(@Param("universityId") int universityId);
}