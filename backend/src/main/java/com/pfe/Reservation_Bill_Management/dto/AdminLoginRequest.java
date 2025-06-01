package com.pfe.Reservation_Bill_Management.dto;

public class AdminLoginRequest {
    private String email;
    private String universityId;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUniversityId() {
        return universityId;
    }

    public void setUniversityId(String universityId) {
        this.universityId = universityId;
    }
}

