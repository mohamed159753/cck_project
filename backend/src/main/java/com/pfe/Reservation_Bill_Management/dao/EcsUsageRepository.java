package com.pfe.Reservation_Bill_Management.dao;

import com.pfe.Reservation_Bill_Management.entities.EcsUsage;
import com.pfe.Reservation_Bill_Management.entities.Professor;
import com.pfe.Reservation_Bill_Management.entities.CloudResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EcsUsageRepository extends JpaRepository<EcsUsage, Long> {
    
    // Find all usage sessions by user
    List<EcsUsage> findByProf(Professor prof);
    
    @Query("SELECT u FROM EcsUsage u WHERE FUNCTION('MONTH', u.startTime) = :month AND FUNCTION('YEAR', u.startTime) = :year")
    List<EcsUsage> findAllByMonth(@Param("month") int month, @Param("year") int year);

    // Find unfinished session for a given resource and user (start clicked, stop not yet clicked)
    Optional<EcsUsage> findByCloudResourceAndProfAndStopTimeIsNull(CloudResource cloudResource, Professor professor);
}