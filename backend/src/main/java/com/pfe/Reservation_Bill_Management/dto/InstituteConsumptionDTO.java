package com.pfe.Reservation_Bill_Management.dto;

public class InstituteConsumptionDTO {
    private String instituteName;
    private int percentage;
    
    public InstituteConsumptionDTO(String instituteName, int percentage) {
        this.instituteName = instituteName;
        this.percentage = percentage;
    }
    
    public String getInstituteName() {
        return instituteName;
    }
    
    public void setInstituteName(String instituteName) {
        this.instituteName = instituteName;
    }
    
    public int getPercentage() {
        return percentage;
    }
    
    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }
}