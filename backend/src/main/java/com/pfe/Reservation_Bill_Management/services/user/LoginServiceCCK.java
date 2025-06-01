package com.pfe.Reservation_Bill_Management.services.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.pfe.Reservation_Bill_Management.dao.CckAdminRepository;
import com.pfe.Reservation_Bill_Management.dao.UniversityAdminRepository;
import com.pfe.Reservation_Bill_Management.entities.CckAdmin;
import com.pfe.Reservation_Bill_Management.entities.UniversityAdmin;

import org.springframework.http.MediaType;



@Service
public class LoginServiceCCK {
	
	@Autowired
	CckAdminRepository cckAdminRepository;


    public CckAdmin getOrCreateUniversityAdmin(String externalEmail) {
        Optional<CckAdmin> existing = cckAdminRepository.findByEmail(externalEmail);

        if (existing.isPresent()) {
            return existing.get();
        }

        CckAdmin admin = new CckAdmin();
        admin.setEmail(externalEmail);
        admin.setPassword(null); // Placeholder or null if field is optional

        return cckAdminRepository.save(admin);
    }
    
    public Optional<CckAdmin> getAdminById(Long id){
    	return cckAdminRepository.findById(id);
    }
}
