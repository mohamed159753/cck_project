package com.pfe.Reservation_Bill_Management.dto;

public class InstituteConsumptionDTO {
    private String instituteName;
    private double vcpu;
    private double ram;
    private double storage;
    private int percentage;

    public InstituteConsumptionDTO(String instituteName, double vcpu, double ram, double storage, int percentage) {
        this.instituteName = instituteName;
        this.vcpu = vcpu;
        this.ram = ram;
        this.storage = storage;
        this.percentage = percentage;
    }

    // Getters and setters
    public String getInstituteName() {
        return instituteName;
    }

    public double getVcpu() {
        return vcpu;
    }

    public double getRam() {
        return ram;
    }

    public double getStorage() {
        return storage;
    }

    public int getPercentage() {
        return percentage;
    }
}
