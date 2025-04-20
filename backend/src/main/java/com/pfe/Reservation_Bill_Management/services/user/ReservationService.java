package com.pfe.Reservation_Bill_Management.services.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pfe.Reservation_Bill_Management.dao.ReservationRepository;
import com.pfe.Reservation_Bill_Management.entities.Reservation;

@Service
public class ReservationService {
    
    @Autowired
    private ReservationRepository reservationRepository;
    
    public int countReservationsByMonthAndYear(int universityId, int month, int year) {
        return reservationRepository.countReservationsByMonthAndYear(universityId, month, year);
    }
    
    public List<Object[]> findTopInstitutesByConsumption(int universityId, int month, int year) {
        return reservationRepository.findTopInstitutesByConsumption(universityId, month, year);
    }
    
    public List<Object[]> findTopProfessorsByReservations(int universityId, int month, int year) {
        return reservationRepository.findTopProfessorsByReservations(universityId, month, year);
    }

    public List<Reservation> findByUniversityId(int universityId) {
        return reservationRepository.findByUniversityId(universityId);
    }

    public List<Reservation> findByUniversityIdAndDateRange(int universityId, LocalDateTime startDate, LocalDateTime endDate) {
        return reservationRepository.findByUniversityIdAndStartTimeBetween(universityId, startDate, endDate);
    }

    public List<Reservation> findByProfessorId(long professorId) {
        return reservationRepository.findByProfessorId(professorId);
    }
}