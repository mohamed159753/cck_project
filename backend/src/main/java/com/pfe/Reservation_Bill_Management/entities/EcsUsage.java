package com.pfe.Reservation_Bill_Management.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ecs_usage")
public class EcsUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

  

    @ManyToOne
    @JoinColumn(name = "prof_id", nullable = false)
    private Professor prof;
    
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "stop_time")
    private LocalDateTime stopTime;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "cost", precision = 10)
    private Float cost;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @ManyToOne
    @JoinColumn(name = "cloud_resource_id", nullable = false)
    private CloudResource cloudResource;
    
    

    // Constructors
    public EcsUsage() {}

    public EcsUsage(LocalDateTime startTime) {
     
        this.startTime = startTime;
        this.createdAt = LocalDateTime.now();
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}



	public Professor getProf() {
		return prof;
	}

	public void setProf(Professor prof) {
		this.prof = prof;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getStopTime() {
		return stopTime;
	}

	public void setStopTime(LocalDateTime stopTime) {
		this.stopTime = stopTime;
	}

	public Integer getDurationMinutes() {
		return durationMinutes;
	}

	public void setDurationMinutes(Integer durationMinutes) {
		this.durationMinutes = durationMinutes;
	}

	public Float getCost() {
		return cost;
	}

	public void setCost(float cost2) {
		this.cost = cost2;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public CloudResource getCloudResource() {
		return cloudResource;
	}

	public void setCloudResource(CloudResource cloudResource) {
		this.cloudResource = cloudResource;
	}
	
	
    

    
}
