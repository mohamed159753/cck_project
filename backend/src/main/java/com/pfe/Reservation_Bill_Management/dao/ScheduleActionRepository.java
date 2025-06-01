package com.pfe.Reservation_Bill_Management.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pfe.Reservation_Bill_Management.entities.ScheduledAction;

public interface ScheduleActionRepository extends JpaRepository<ScheduledAction, Long> {
	
	@Query("SELECT a FROM ScheduledAction a WHERE a.scheduledTime <= :now AND a.status = 'PENDING'")
    List<ScheduledAction> findDueActions(LocalDateTime now);

}
