package com.pfe.Reservation_Bill_Management.dao;

import java.time.YearMonth;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pfe.Reservation_Bill_Management.entities.BillingEntry;
import com.pfe.Reservation_Bill_Management.entities.University;

@Repository
public interface BillingEntryRepository extends JpaRepository<BillingEntry, Long>  {

	List<BillingEntry> findByUniversityAndBillingMonth(University university, String billingMonth);




}
