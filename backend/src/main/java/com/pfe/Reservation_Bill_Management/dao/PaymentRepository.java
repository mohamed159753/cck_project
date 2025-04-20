package com.pfe.Reservation_Bill_Management.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pfe.Reservation_Bill_Management.entities.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
}
