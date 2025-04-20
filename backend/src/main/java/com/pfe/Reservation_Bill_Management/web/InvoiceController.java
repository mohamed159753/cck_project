package com.pfe.Reservation_Bill_Management.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pfe.Reservation_Bill_Management.entities.Invoice;
import com.pfe.Reservation_Bill_Management.services.user.InvoiceService;
import com.pfe.Reservation_Bill_Management.services.user.PaymentService;

@RestController
@RequestMapping("/api/invoices")
@CrossOrigin(origins = "http://localhost:4200") // Adjust this to your Angular app URL
//@CrossOrigin(origins = "*") // Allow Angular requests
public class InvoiceController {
	    @Autowired
	    private InvoiceService invoiceService;

	    @GetMapping()
	    public List<Invoice> getAllInvoices() {
	        return invoiceService.getAllInvoices();
	    }
	    
	    @GetMapping("{invoiceId}")
	    public Optional<Invoice> getInvoiceById(@PathVariable("invoiceId") Long invoiceId) {
	        return invoiceService.getInvoiceById(invoiceId);
	    }
	}
