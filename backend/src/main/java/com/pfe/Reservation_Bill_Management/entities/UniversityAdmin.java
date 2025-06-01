package com.pfe.Reservation_Bill_Management.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class UniversityAdmin extends User {
	
	@ManyToOne
	@JoinColumn(name = "university_id")
	private University university;
	
	@OneToMany(mappedBy = "approvedBy")
	@JsonIgnore
	private List<Reservation> approvedReservations;

	@OneToMany(mappedBy = "rejectedBy")
	@JsonIgnore
	private List<Reservation> rejectedReservations;
	
	
	public List<Reservation> getApprovedReservations() {
		return approvedReservations;
	}

	public void setApprovedReservations(List<Reservation> approvedReservations) {
		this.approvedReservations = approvedReservations;
	}

	public List<Reservation> getRejectedReservations() {
		return rejectedReservations;
	}

	public void setRejectedReservations(List<Reservation> rejectedReservations) {
		this.rejectedReservations = rejectedReservations;
	}

	public University getUniversity() {
		return university;
	}

	public void setUniversity(University university) {
		this.university = university;
	}

	
	
	
	
	

}
