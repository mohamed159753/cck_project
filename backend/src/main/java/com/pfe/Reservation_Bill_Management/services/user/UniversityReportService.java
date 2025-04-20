package com.pfe.Reservation_Bill_Management.services.user;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pfe.Reservation_Bill_Management.dao.ProfessorRepository;
import com.pfe.Reservation_Bill_Management.dao.ReservationRepository;
import com.pfe.Reservation_Bill_Management.dao.UniversityRepository;
import com.pfe.Reservation_Bill_Management.dto.InstituteConsumptionDTO;
import com.pfe.Reservation_Bill_Management.dto.ProfessorReservationDTO;
import com.pfe.Reservation_Bill_Management.dto.UniversityStatsDTO;
import com.pfe.Reservation_Bill_Management.entities.Professor;
import com.pfe.Reservation_Bill_Management.entities.Reservation;

@Service
public class UniversityReportService {

	@Autowired
    private ReservationRepository reservationRepository;
    
    @Autowired
    private ProfessorRepository professorRepository;
    
    public UniversityStatsDTO getDashboardStats(int universityId, int month, int year) {
        UniversityStatsDTO stats = new UniversityStatsDTO();
        
        stats.setTotalReservations(reservationRepository.countReservationsByMonthAndYear(universityId, month, year));
        stats.setTotalInstitutes(professorRepository.countInstitutes(universityId));
        stats.setTotalProfessors(professorRepository.countProfessors(universityId));
        
        return stats;
    }
    
    public List<InstituteConsumptionDTO> getTopInstitutesByConsumption(int universityId, int month, int year) {
        List<Object[]> results = reservationRepository.findTopInstitutesByConsumption(universityId, month, year);
        List<InstituteConsumptionDTO> institutes = new ArrayList<InstituteConsumptionDTO>();
        
        // Calculate total reservations for percentage calculation
        int totalReservations = reservationRepository.countReservationsByMonthAndYear(universityId, month, year);
        
        if (totalReservations > 0) {
            for (Object[] result : results) {
                String instituteName = (String) result[0];
                Long count = (Long) result[1];
                int percentage = (int) ((count * 100) / totalReservations);
                
                institutes.add(new InstituteConsumptionDTO(instituteName, percentage));
                
                if (institutes.size() >= 3) {
                    break; // Only return top 3
                }
            }
        }
        
        return institutes;
    }
    
    public List<ProfessorReservationDTO> getTopProfessorsByReservations(int universityId, int month, int year) {
        List<Object[]> results = reservationRepository.findTopProfessorsByReservations(universityId, month, year);
        List<ProfessorReservationDTO> professors = new ArrayList<>();
        
        for (Object[] result : results) {
            ProfessorReservationDTO dto = new ProfessorReservationDTO();
            dto.setName("Prof. " + (String) result[0]);
            dto.setInstitute((String) result[1]);
            dto.setNumberOfReservations(((Long) result[2]).intValue());
            dto.setLastUsed((LocalDateTime) result[3]);
            
            professors.add(dto);
            
            if (professors.size() >= 10) {
                break; // Limit to top 10
            }
        }
        
        return professors;
    }
}