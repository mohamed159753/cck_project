package com.pfe.Reservation_Bill_Management.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pfe.Reservation_Bill_Management.entities.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

	// findAll() is already provided by JpaRepository
    // You don't need to define it yourself
	
	Optional<Invoice> findById(Long invoiceId);
}
