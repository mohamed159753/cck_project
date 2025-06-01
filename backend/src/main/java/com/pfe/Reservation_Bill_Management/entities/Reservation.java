package com.pfe.Reservation_Bill_Management.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reservation")
public class Reservation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "university_id")
    private University university;
    
    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor professor;
    
    @ManyToOne
    @JoinColumn(name = "resource_id")
    
    private CloudResource resource;
    
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;
    
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;
    
    @Enumerated(EnumType.STRING)
    private ApprovalStatus status;
    @Enumerated(EnumType.STRING)
    private ReservationType reservationType;
    
    @ManyToOne
    @JoinColumn(name = "approved_by_id")
    private UniversityAdmin approvedBy;

    @ManyToOne
    @JoinColumn(name = "rejected_by_id")
    private UniversityAdmin rejectedBy;

    @ManyToOne
    @JoinColumn(name = "cck_approved_by_id")
    private CckAdmin cckApprovedBy;

    @ManyToOne
    @JoinColumn(name = "cck_rejected_by_id")
    private CckAdmin cckRejectedBy;
    
   
    
    public enum ApprovalStatus {
        PENDING_UNIVERSITY,
        APPROVED_UNIVERSITY,
        PENDING_CCK,
        APPROVED_CCK,
        REJECTED, REJECTED_CCK, REJECTED_UNIVERSITY
    }
    
    public enum ReservationType {
        QUOTA,
        PAYG
    }
    
    private boolean isCreated = false;
    private boolean isDeleted = false;
    
    

    public ReservationType getReservationType() {
		return reservationType;
	}

	public void setReservationType(ReservationType reservationType) {
		this.reservationType = reservationType;
	}

	// Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public CloudResource getResource() {
        return resource;
    }

    public void setResource(CloudResource resource) {
        this.resource = resource;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public ApprovalStatus getStatus() {
        return status;
    }

    public void setStatus(ApprovalStatus status) {
        this.status = status;
    }

	public UniversityAdmin getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(UniversityAdmin approvedBy) {
		this.approvedBy = approvedBy;
	}

	public UniversityAdmin getRejectedBy() {
		return rejectedBy;
	}

	public void setRejectedBy(UniversityAdmin rejectedBy) {
		this.rejectedBy = rejectedBy;
	}

	public CckAdmin getCckApprovedBy() {
		return cckApprovedBy;
	}

	public void setCckApprovedBy(CckAdmin cckApprovedBy) {
		this.cckApprovedBy = cckApprovedBy;
	}

	public CckAdmin getCckRejectedBy() {
		return cckRejectedBy;
	}

	public void setCckRejectedBy(CckAdmin cckRejectedBy) {
		this.cckRejectedBy = cckRejectedBy;
	}

	public boolean isCreated() {
		return isCreated;
	}

	public void setCreated(boolean isCreated) {
		this.isCreated = isCreated;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	
    
    
    
}