package com.pfe.Reservation_Bill_Management.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pfe.Reservation_Bill_Management.entities.CloudResource;
import com.pfe.Reservation_Bill_Management.entities.Reservation;
import com.pfe.Reservation_Bill_Management.entities.Reservation.ApprovalStatus;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	
	
    List<Reservation> findByStatus(ApprovalStatus status);
    
	@Query("SELECT COUNT(r) FROM Reservation r " +
		       "WHERE r.university.id = :universityId " +
		       "AND MONTH(r.startTime) = :month AND YEAR(r.startTime) = :year")
		int countReservationsByMonthAndYear(@Param("universityId") String universityId,
		                                     @Param("month") int month,
		                                     @Param("year") int year);
	
	@Query("SELECT r.professor.institute, COUNT(r), " +
		       "SUM(r.resource.vcpu), SUM(r.resource.ram), SUM(r.resource.storage) " +
		       "FROM Reservation r " +
		       "WHERE r.university.id = :universityId " +
		       "AND MONTH(r.startTime) = :month AND YEAR(r.startTime) = :year " +
		       "GROUP BY r.professor.institute " +
		       "ORDER BY COUNT(r) DESC")
		List<Object[]> findTopInstitutesByConsumption(@Param("universityId") String universityId,
		                                              @Param("month") int month,
		                                              @Param("year") int year);
     
     
 
    
    @Query("SELECT r.professor.username, r.professor.institute, COUNT(r), MAX(r.endTime) FROM Reservation r WHERE r.university.id = :universityId AND MONTH(r.startTime) = :month AND YEAR(r.startTime) = :year GROUP BY r.professor.username, r.professor.institute ORDER BY COUNT(r) DESC")
    List<Object[]> findTopProfessorsByReservations(@Param("universityId") String universityId, @Param("month") int month, @Param("year") int year);
    
    @Query("SELECT COUNT(DISTINCT r.professor.institute) FROM Reservation r WHERE r.university.id = :universityId")
    int countInstitutes(@Param("universityId") String universityId);
    
    @Query("SELECT COUNT(DISTINCT r.professor.id) FROM Reservation r WHERE r.university.id = :universityId")
    int countProfessors(@Param("universityId") String universityId);
    List<Reservation> findByUniversityId(String universityId);

    @Query("SELECT r FROM Reservation r WHERE r.university.id = :universityId AND r.startTime >= :startDate AND r.endTime <= :endDate")
    List<Reservation> findByUniversityIdAndStartTimeBetween(
        @Param("universityId") String universityId, 
        @Param("startDate") LocalDateTime startDate, 
        @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT r.resource.image, SUM(r.resource.storage) " +
    	       "FROM Reservation r " +
    	       "WHERE r.professor.id = :professorId AND MONTH(r.startTime) = :month AND YEAR(r.startTime) = :year " +
    	       "GROUP BY r.resource.image")
    	List<Object[]> getImageUsageByProfessor(@Param("professorId") Long professorId,
    	                                        @Param("month") int month,
    	                                        @Param("year") int year);
    	
    	
    	
    	
    	@Query(value = """
    		    SELECT 
    		        COUNT(*) 
    		    FROM 
    		        rev_billing.reservation r
    		    WHERE 
    		        r.professor_id = :professorId
    		        AND MONTH(r.start_time) = :month
    		        AND YEAR(r.start_time) = :year
    		    """, nativeQuery = true)
    		int countReservations(@Param("professorId") Long professorId,
    		                      @Param("month") int month,
    		                      @Param("year") int year);

    		// Add this method to test join condition
    		@Query(value = """
    		    SELECT 
    		        COUNT(*) 
    		    FROM 
    		        rev_billing.reservation r
    		    JOIN 
    		        rev_billing.cloud_resources cr ON r.resource_id = cr.id
    		    WHERE 
    		        r.professor_id = :professorId
    		        AND MONTH(r.start_time) = :month
    		        AND YEAR(r.start_time) = :year
    		    """, nativeQuery = true)
    		int countReservationsWithResources(@Param("professorId") Long professorId,
    		                                  @Param("month") int month,
    		                                  @Param("year") int year);
    		
    		
    	
    	

    		@Query(value = """
    				SELECT
    				    r.professor_id AS professorId,
    				    SUM(COALESCE(cr.vcpu, 0)) AS totalVcpu,
    				    SUM(COALESCE(cr.ram, 0)) AS totalVram,
    				    SUM(COALESCE(cr.storage, 0)) AS totalDisk
    				FROM
    				    rev_billing.reservation r
    				JOIN
    				    rev_billing.cloud_resources cr ON r.resource_id = cr.id
    				WHERE
    				    r.professor_id = :professorId
    				    AND MONTH(r.start_time) = :month
    				    AND YEAR(r.start_time) = :year
    				GROUP BY
    				    r.professor_id
    				""", nativeQuery = true)
    				List<Object[]> getTotalsByProfessor(@Param("professorId") Long professorId,
    				                             @Param("month") int month,
    				                             @Param("year") int year);
    				
    				
    				@Query("SELECT r.resource FROM Reservation r " +
    					       "WHERE r.status = 'APPROVED_CCK' " +
    					       "AND r.professor.id = :professorId " +
    					       "AND r.startTime <= :now AND r.endTime >= :now")
    					List<CloudResource> findActiveResources(@Param("professorId") Long professorId, @Param("now") LocalDateTime now);
    				
    				
    				@Query("SELECT r.resource FROM Reservation r " +
    				           "WHERE r.reservationType = 'PAYG' " +
    				           "AND r.status = 'APPROVED_CCK' " +
    				           "AND r.startTime <= :now AND r.endTime >= :now")
    				    List<CloudResource> findActivePaygResources(@Param("now") LocalDateTime now);
    				
    List<Reservation> findByProfessorId(long professorId);
}