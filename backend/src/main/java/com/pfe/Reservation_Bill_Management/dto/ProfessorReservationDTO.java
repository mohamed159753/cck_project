package com.pfe.Reservation_Bill_Management.dto;

import java.time.LocalDateTime;

public class ProfessorReservationDTO {
    private String name;
    private String institute;
    private int numberOfReservations;
    private LocalDateTime lastUsed;
    
    // Getters and setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getInstitute() {
        return institute;
    }
    
    public void setInstitute(String institute) {
        this.institute = institute;
    }
    
    public int getNumberOfReservations() {
        return numberOfReservations;
    }
    
    public void setNumberOfReservations(int numberOfReservations) {
        this.numberOfReservations = numberOfReservations;
    }
    
    public LocalDateTime getLastUsed() {
        return lastUsed;
    }
    
    public void setLastUsed(LocalDateTime lastUsed) {
        this.lastUsed = lastUsed;
    }
}