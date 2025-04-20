package com.pfe.Reservation_Bill_Management.services.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pfe.Reservation_Bill_Management.dao.InvoiceRepository;
import com.pfe.Reservation_Bill_Management.entities.Invoice;

@Service
public class InvoiceService {
	    @Autowired
	    private InvoiceRepository invoiceRepository;

	    public List<Invoice> getAllInvoices() {
	        return invoiceRepository.findAll();
	    }
	    
	    public Optional<Invoice> getInvoiceById(Long invoiceId) {
	        return invoiceRepository.findById(invoiceId);
	    }
	}


