package com.pfe.Reservation_Bill_Management.services.user;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pfe.Reservation_Bill_Management.dao.InvoiceRepository;
import com.pfe.Reservation_Bill_Management.dao.PaymentRepository;
import com.pfe.Reservation_Bill_Management.entities.Invoice;
import com.pfe.Reservation_Bill_Management.entities.Payment;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public Payment make(Payment payment) {
        return paymentRepository.save(payment);
    }

    public List<Payment> getByInvoiceId(Long invoiceId) {
        return paymentRepository.findAll().stream()
            .filter(p -> p.getInvoice().getInvoiceId() == invoiceId)
            .collect(Collectors.toList());
    }
}
