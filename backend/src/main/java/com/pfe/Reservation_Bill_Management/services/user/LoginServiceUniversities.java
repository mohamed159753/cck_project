package com.pfe.Reservation_Bill_Management.services.user;


import java.util.Optional;

import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.pfe.Reservation_Bill_Management.dao.UniversityAdminRepository;
import com.pfe.Reservation_Bill_Management.entities.University;
import com.pfe.Reservation_Bill_Management.entities.UniversityAdmin;

@org.springframework.stereotype.Service
public class LoginServiceUniversities {
	
	@Autowired
	UniversityAdminRepository universityAdminRepository;


	public UniversityAdmin getOrCreateUniversityAdmin(String email, University university) {
	    return universityAdminRepository.findByEmail(email).orElseGet(() -> {
	        UniversityAdmin admin = new UniversityAdmin();
	        admin.setEmail(email);
	        admin.setUniversity(university);
	        return universityAdminRepository.save(admin);
	    });
	}
    
    public Optional<UniversityAdmin> getAdminById(Long id) {
		return universityAdminRepository.findById(id);
    	
    }

}
