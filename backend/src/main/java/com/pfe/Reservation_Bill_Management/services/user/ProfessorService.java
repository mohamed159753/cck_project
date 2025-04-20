package com.pfe.Reservation_Bill_Management.services.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pfe.Reservation_Bill_Management.dao.ProfessorRepository;
import com.pfe.Reservation_Bill_Management.entities.Professor;
import com.pfe.Reservation_Bill_Management.entities.Reservation;

@Service
public class ProfessorService {
    
    @Autowired
    private ProfessorRepository professorRepository;
    
    public int countInstitutes(int universityId) {
        return professorRepository.countInstitutes(universityId);
    }
    
    public int countProfessors(int universityId) {
        return professorRepository.countProfessors(universityId);
    }
    
    public int getUniqueInstitutes(int universityId) {
        return professorRepository.countInstitutes(universityId);
    }

	public Object findByUniversityId(int universityId) {
		// TODO Auto-generated method stub
		return null;
	}
}