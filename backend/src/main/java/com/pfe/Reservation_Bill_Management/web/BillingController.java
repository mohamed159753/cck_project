package com.pfe.Reservation_Bill_Management.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pfe.Reservation_Bill_Management.dto.BillingEntryDTO;
import com.pfe.Reservation_Bill_Management.services.user.BillingService;

@RestController
@RequestMapping("/api/billing")
@CrossOrigin(origins = "*") 
public class BillingController {
	
	@Autowired
	BillingService billingService;
	
	
	@PostMapping("/api/billing/payg-entry")
	public ResponseEntity<?> createBillingEntry(@RequestBody BillingEntryDTO dto) {
	    billingService.createBillingEntryFromDto(dto);
	    return ResponseEntity.ok("Billing entry created");
	}

}
