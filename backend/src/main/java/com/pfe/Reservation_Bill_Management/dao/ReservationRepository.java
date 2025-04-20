package com.pfe.Reservation_Bill_Management.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pfe.Reservation_Bill_Management.entities.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.university.id = :universityId AND MONTH(r.startTime) = :month AND YEAR(r.startTime) = :year")
    int countReservationsByMonthAndYear(@Param("universityId") int universityId, @Param("month") int month, @Param("year") int year);
    
    @Query("SELECT r.professor.institute, COUNT(r) FROM Reservation r WHERE r.university.id = :universityId AND MONTH(r.startTime) = :month AND YEAR(r.startTime) = :year GROUP BY r.professor.institute ORDER BY COUNT(r) DESC")
    List<Object[]> findTopInstitutesByConsumption(@Param("universityId") int universityId, @Param("month") int month, @Param("year") int year);
    
    @Query("SELECT r.professor.username, r.professor.institute, COUNT(r), MAX(r.endTime) FROM Reservation r WHERE r.university.id = :universityId AND MONTH(r.startTime) = :month AND YEAR(r.startTime) = :year GROUP BY r.professor.username, r.professor.institute ORDER BY COUNT(r) DESC")
    List<Object[]> findTopProfessorsByReservations(@Param("universityId") int universityId, @Param("month") int month, @Param("year") int year);
    
    @Query("SELECT COUNT(DISTINCT r.professor.institute) FROM Reservation r WHERE r.university.id = :universityId")
    int countInstitutes(@Param("universityId") int universityId);
    
    @Query("SELECT COUNT(DISTINCT r.professor.id) FROM Reservation r WHERE r.university.id = :universityId")
    int countProfessors(@Param("universityId") int universityId);
    List<Reservation> findByUniversityId(int universityId);

    @Query("SELECT r FROM Reservation r WHERE r.university.id = :universityId AND r.startTime >= :startDate AND r.endTime <= :endDate")
    List<Reservation> findByUniversityIdAndStartTimeBetween(
        @Param("universityId") int universityId, 
        @Param("startDate") LocalDateTime startDate, 
        @Param("endDate") LocalDateTime endDate);

    List<Reservation> findByProfessorId(long professorId);
}