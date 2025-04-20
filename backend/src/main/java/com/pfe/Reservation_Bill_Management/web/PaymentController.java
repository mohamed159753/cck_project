package com.pfe.Reservation_Bill_Management.web;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pfe.Reservation_Bill_Management.dao.PaymentRepository;
import com.pfe.Reservation_Bill_Management.entities.Payment;
import com.pfe.Reservation_Bill_Management.services.user.PaymentService;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:4200") // Adjust this to your Angular app URL

public class PaymentController {

    @Autowired
    private PaymentRepository paymentRepository;

    @PostMapping
    public Payment makePayment(@RequestBody Payment payment) {
        return paymentRepository.save(payment);
    }

    @GetMapping("/invoice/{invoiceId}")
    public List<Payment> getPaymentsByInvoice(@PathVariable int invoiceId) {
        return paymentRepository.findAll().stream()
            .filter(p -> p.getInvoice().getInvoiceId() == invoiceId)
            .collect(Collectors.toList());
    }
}
