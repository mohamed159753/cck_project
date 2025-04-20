package com.pfe.Reservation_Bill_Management.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "professors")

    
public class Professor extends User {
	
	    @Column(name = "cin")
	    private int cin;
	    
	    @Column(name = "institute")
	    private String institute;
	    
	    @ManyToOne
	    @JoinColumn(name = "university_id")
	    private University university;

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

		public University getUniversity() {
			return university;
		}

		public void setUniversity(University university) {
			this.university = university;
		}
	    
	    
	    
	    
	}




