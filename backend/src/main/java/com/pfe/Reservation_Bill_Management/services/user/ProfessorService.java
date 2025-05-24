package com.pfe.Reservation_Bill_Management.services.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pfe.Reservation_Bill_Management.dao.ProfessorRepository;
import com.pfe.Reservation_Bill_Management.entities.Professor;
import com.pfe.Reservation_Bill_Management.entities.Reservation;

@Service
public class ProfessorService {
    
    @Autowired
    private ProfessorRepository professorRepository;
    
    @Autowired
    private EmailChangeService emailChangeService;
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private RegisterService registerService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    
    public int countInstitutes(String universityId) {
        return professorRepository.countInstitutes(universityId);
    }
    
    public int countProfessors(String universityId) {
        return professorRepository.countProfessors(universityId);
    }
    
    public int getUniqueInstitutes(String universityId) {
        return professorRepository.countInstitutes(universityId);
    }

	public Object findByUniversityId(String universityId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Optional<Professor> findProfByEmail(String email) {
		return Optional.of(professorRepository.findByEmail(email));
	}

	public Optional<Professor> findById(Long professorId) {
		// TODO Auto-generated method stub
		return professorRepository.findById(professorId);
	}
	
	public Professor updateOwnInformation(Long professorId, Professor updatedData) {
	    return professorRepository.findById(professorId).map(professor -> {
	        // Check if email changed
	        if (updatedData.getEmail() != null && !updatedData.getEmail().equals(professor.getEmail())) {
	            // Generate activation token
	            String token = UUID.randomUUID().toString();
	            professor.setActivationToken(token);

	            // Store new email in memory
	            emailChangeService.storeNewEmail(professorId, updatedData.getEmail());

	            // Send confirmation email to new email address
	            String link = "https://1kjfg4hs-8080.uks1.devtunnels.ms/api/professors/activate/" + token;
	            registerService.sendActivationEmail(updatedData.getEmail(), link);

	            // Do NOT update professor.setEmail yet
	        }

	        if (updatedData.getPassword() != null && !updatedData.getPassword().isEmpty()) {
	        	professor.setPassword(passwordEncoder.encode(updatedData.getPassword()));
	        }

	        return professorRepository.save(professor);
	    }).orElseThrow(() -> new RuntimeException("Professor not found"));
	}
	
	public boolean changePassword(Long professorId, String currentPassword, String newPassword) {
	    Optional<Professor> optionalProfessor = professorRepository.findById(professorId);

	    if (optionalProfessor.isPresent()) {
	        Professor professor = optionalProfessor.get();

	        if (passwordEncoder.matches(currentPassword, professor.getPassword())) {
	            professor.setPassword(passwordEncoder.encode(newPassword));
	            professorRepository.save(professor);
	            return true;
	        }
	    }

	    return false;
	}
	
	
}