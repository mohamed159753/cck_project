package com.pfe.Reservation_Bill_Management.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class BillingEntry {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private CloudResource resource;

    private long professorId;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private BigDecimal cost;

    private String billingMonth;

    @ManyToOne
    @JoinColumn(name = "university_id")
    private University university;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CloudResource getResource() {
        return resource;
    }

    public void setResource(CloudResource resource) {
        this.resource = resource;
    }

    public long getProfessorId() {
        return professorId;
    }

    public void setProfessorId(long professorId2) {
        this.professorId = professorId2;
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

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getBillingMonth() {
        return billingMonth;
    }

    public void setBillingMonth(String billingMonth) {
        this.billingMonth = billingMonth;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }
}
