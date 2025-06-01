package com.pfe.Reservation_Bill_Management.services.user;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.pfe.Reservation_Bill_Management.dao.ProfessorRepository;
import com.pfe.Reservation_Bill_Management.dao.ReservationRepository;
import com.pfe.Reservation_Bill_Management.dto.UnavailableTimeSlot;
import com.pfe.Reservation_Bill_Management.entities.CckAdmin;
import com.pfe.Reservation_Bill_Management.entities.CloudResource;
import com.pfe.Reservation_Bill_Management.entities.Professor;
import com.pfe.Reservation_Bill_Management.entities.Quota;
import com.pfe.Reservation_Bill_Management.entities.Reservation;
import com.pfe.Reservation_Bill_Management.entities.Reservation.ApprovalStatus;
import com.pfe.Reservation_Bill_Management.entities.University;
import com.pfe.Reservation_Bill_Management.entities.UniversityAdmin;

@Service
public class ReservationService {
    
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ProfessorRepository professorRepository;
    
    public int countReservationsByMonthAndYear(String universityId, int month, int year) {
        return reservationRepository.countReservationsByMonthAndYear(universityId, month, year);
    }
    
    public List<Object[]> findTopInstitutesByConsumption(String universityId, int month, int year) {
        return reservationRepository.findTopInstitutesByConsumption(universityId, month, year);
    }
    
    public List<Object[]> findTopProfessorsByReservations(String universityId, int month, int year) {
        return reservationRepository.findTopProfessorsByReservations(universityId, month, year);
    }

    public List<Reservation> findByUniversityId(String universityId) {
        return reservationRepository.findByUniversityId(universityId);
    }

    public List<Reservation> findByUniversityIdAndDateRange(String universityId, LocalDateTime startDate, LocalDateTime endDate) {
        return reservationRepository.findByUniversityIdAndStartTimeBetween(universityId, startDate, endDate);
    }

    public List<Reservation> findByProfessorId(long professorId) {
        return reservationRepository.findByProfessorId(professorId);
    }
    
    public  Reservation addReservation(Reservation reservation) {
    	return reservationRepository.save(reservation);
    }
    
    public Map<String, Object> getProfessorReport(Long professorId, int month, int year) {
        Map<String, Object> report = new HashMap<>();
        
        // Get totals from repository
        List<Object[]> totalsList = reservationRepository.getTotalsByProfessor(professorId, month, year);
        
        // Default values
        int totalVcpu = 0;
        int totalVram = 0;
        int totalDisk = 0;
        
        // Process results if they exist
        if (totalsList != null && !totalsList.isEmpty()) {
            Object[] row = totalsList.get(0); // Get the first row
            
            if (row.length >= 4) {
                // professorId is at index 0, we already have it
                totalVcpu = convertToInt(row[1]); // vcpu is index 1
                totalVram = convertToInt(row[2]); // ram is index 2
                totalDisk = convertToInt(row[3]); // disk is index 3
            }
        }
        
        // Get image usage
        List<Object[]> usageList = reservationRepository.getImageUsageByProfessor(professorId, month, year);
        Map<String, Integer> usageMap = new HashMap<>();
        
        if (usageList != null && !usageList.isEmpty()) {
            for (Object[] row : usageList) {
                if (row.length >= 2) {
                    String image = (String) row[0];
                    int count = convertToInt(row[1]);
                    usageMap.put(image, count);
                }
            }
        }
        
        // Calculate percentages
        int sumUsage = usageMap.values().stream().mapToInt(i -> i).sum();
        Map<String, Integer> usagePercentages = new HashMap<>();
        
        if (sumUsage > 0) {
            for (Map.Entry<String, Integer> entry : usageMap.entrySet()) {
                int percent = (int) Math.round((entry.getValue() * 100.0) / sumUsage);
                usagePercentages.put(entry.getKey(), percent);
            }
        }
        
        // Build the report
        report.put("totalDisk", totalDisk);
        report.put("totalVcpu", totalVcpu);
        report.put("totalVram", totalVram);
        report.put("imageUsage", usagePercentages);
        
        return report;
    }

    // Helper method for safer conversion to int
    private int convertToInt(Object value) {
        if (value == null) {
            return 0;
        }
        
        if (value instanceof Number) {
            return ((Number) value).intValue();
        } else if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        
        return 0;
    }
    
    
    public Map<String, Object> getProfessorReservationsStast(Long professorId) {
        Map<String, Object> stats = new HashMap<>();

        // Get the professor entity
        Professor professor = professorRepository.findById(professorId).orElse(null);
        if (professor == null) {
            return stats; // Return empty stats if professor not found
        }

        // Get all reservations for this professor
        List<Reservation> professorReservations = reservationRepository.findByProfessorId(professorId);

        int totalReservations = professorReservations.size();

        // Count based on enum values
        int pendingReservations = (int) professorReservations.stream()
            .filter(r -> r.getStatus() == ApprovalStatus.PENDING_UNIVERSITY || 
                         r.getStatus() == ApprovalStatus.PENDING_CCK)
            .count();

        int approvedReservations = (int) professorReservations.stream()
            .filter(r -> r.getStatus() == ApprovalStatus.APPROVED_UNIVERSITY ||
                         r.getStatus() == ApprovalStatus.APPROVED_CCK)
            .count();

        int canceledReservations = (int) professorReservations.stream()
            .filter(r -> r.getStatus() == ApprovalStatus.REJECTED_UNIVERSITY ||
                         r.getStatus() == ApprovalStatus.REJECTED_CCK)
            .count();

        stats.put("reservations", totalReservations);
        stats.put("pending", pendingReservations);
        stats.put("approved", approvedReservations);
        stats.put("canceled", canceledReservations);

        return stats;
    }

    
   
    /* public void updateReservationStatus(Long reservationId, String status) {
    	
    	Reservation reservation = reservationRepository.getById(reservationId);
    	reservation.setStatus(status);
    	reservationRepository.save(reservation);
    } */
    
    public void approveByUniversity(Long reservationId, UniversityAdmin admin) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow();
        reservation.setStatus(ApprovalStatus.APPROVED_UNIVERSITY);
        

        reservation.setStatus(ApprovalStatus.PENDING_CCK);
        reservation.setApprovedBy(admin);
        reservationRepository.save(reservation);
    }
    
    public void rejectByUniversity(Long reservationId, String reason, UniversityAdmin admin) {
        Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (reservation.getStatus() != ApprovalStatus.PENDING_UNIVERSITY) {
            throw new IllegalStateException("Reservation must be in PENDING_UNIVERSITY state to be rejected by the university.");
        }

        reservation.setStatus(ApprovalStatus.REJECTED_UNIVERSITY);
        reservation.setRejectedBy(admin);
        reservationRepository.save(reservation);
    }
    
    public void approveByCCK(Long reservationId, CckAdmin admin) {
        Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (reservation.getStatus() != ApprovalStatus.PENDING_CCK) {
            throw new IllegalStateException("Reservation must be in PENDING_CCK state to be approved by CCK.");
        }

        reservation.setStatus(ApprovalStatus.APPROVED_CCK);
        reservation.setCckApprovedBy(admin);
        reservationRepository.save(reservation);
    }
    
    public void rejectByCCK(Long reservationId, String reason, CckAdmin admin) {
        Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (reservation.getStatus() != ApprovalStatus.PENDING_CCK) {
            throw new IllegalStateException("Reservation must be in PENDING_CCK state to be rejected by CCK.");
        }

        reservation.setStatus(ApprovalStatus.REJECTED_CCK);
        reservation.setCckRejectedBy(admin);
        reservationRepository.save(reservation);
    }
    
    public boolean canFitReservation(University university, LocalDateTime start, LocalDateTime end,
            int requestedVcpu, int requestedRam, int requestedStorage) {
    	
    	Quota quota = university.getQuota();

    	if (requestedVcpu > quota.getVcpu() ||
    	    requestedRam > quota.getRamInMb() ||
    	    requestedStorage > quota.getStorageInGb()) {
    	     return false; // Don't return any "unavailable" slots
    	}

			List<Reservation> overlappingReservations = reservationRepository.findByUniversityId(university.getId())
			.stream()
			.filter(r -> {
			ApprovalStatus status = r.getStatus();
			return status != ApprovalStatus.REJECTED_CCK &&
			status != ApprovalStatus.REJECTED_UNIVERSITY &&
			r.getEndTime().isAfter(start) && r.getStartTime().isBefore(end);
			})
			.collect(Collectors.toList());
			
			int usedVcpu = 0, usedRam = 0, usedStorage = 0;
			for (Reservation r : overlappingReservations) {
			CloudResource res = r.getResource();
			if (res != null) {
			usedVcpu += res.getVcpu();
			usedRam += res.getRam();
			usedStorage += res.getStorage();
			}
			}
			
			
			return (usedVcpu + requestedVcpu <= quota.getVcpu()) &&
			(usedRam + requestedRam <= quota.getRamInMb()) &&
			(usedStorage + requestedStorage <= quota.getStorageInGb());
		}
    
    public List<UnavailableTimeSlot> getUnavailableSlots(University university, LocalDateTime from, LocalDateTime to,
            int requestedVcpu, int requestedRam, int requestedStorage) {
		List<UnavailableTimeSlot> unavailable = new ArrayList<>();
		
		LocalDateTime current = from;
		
		while (current.plusHours(1).isBefore(to)) {
		LocalDateTime slotStart = current;
		LocalDateTime slotEnd = current.plusHours(1);
		
		boolean fits = canFitReservation(university, slotStart, slotEnd, requestedVcpu, requestedRam, requestedStorage);
		
		if (!fits) {
		unavailable.add(new UnavailableTimeSlot(slotStart, slotEnd));
		}
		
		current = current.plusMinutes(60); // configurable step
		}
		
		return unavailable;
		}
    
    
    
    
    
}