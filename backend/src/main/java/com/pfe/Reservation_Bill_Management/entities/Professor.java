package com.pfe.Reservation_Bill_Management.entities;

import java.util.Optional;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
    
public class Professor extends User {
	
	    @Column(name = "cin")
	    private int cin;
	    
	    @Column(name = "institute")
	    private String institute;
	    
	    @ManyToOne
	    @JoinColumn(name = "university_id")
	    private University university;
	    
	    @Column(name = "is_activated")
	    private boolean isActivated = false;

	    @Column(name = "activation_token")
	    private String activationToken;
	    
	    
	    	
		public boolean isActivated() {
			return isActivated;
		}

		public void setActivated(boolean isActivated) {
			this.isActivated = isActivated;
		}

		public String getActivationToken() {
			return activationToken;
		}

		public void setActivationToken(String activationToken) {
			this.activationToken = activationToken;
		}

		public int getCin() {
			return cin;
		}

		public void setCin(int cin) {
			this.cin = cin;
		}

		public String getInstitute() {
			return institute;
		}

		public void setInstitute(String institute) {
			this.institute = institute;
		}

		public Optional<University> getUniversity() {
			return Optional.ofNullable(university);
		}

		public void setUniversity(University university2) {
			this.university = university2;
		}
	    
	    
	    
	    
	}




