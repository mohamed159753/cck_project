package com.pfe.Reservation_Bill_Management.dto;

public class UniversityStatsDTO {
    private int totalReservations;
    private int totalInstitutes;
    private int totalProfessors;
    
    // Getters and setters
    public int getTotalReservations() {
        return totalReservations;
    }
    
    public void setTotalReservations(int totalReservations) {
        this.totalReservations = totalReservations;
    }
    
    public int getTotalInstitutes() {
        return totalInstitutes;
    }
    
    public void setTotalInstitutes(int totalInstitutes) {
        this.totalInstitutes = totalInstitutes;
    }
    
    public int getTotalProfessors() {
        return totalProfessors;
    }
    
    public void setTotalProfessors(int totalProfessors) {
        this.totalProfessors = totalProfessors;
    }
}