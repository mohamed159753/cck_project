package com.pfe.Reservation_Bill_Management.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;


@Entity
public class University {
	
    @Id
    private String id;
    
    private String universityName;
    
    @Embedded
    private Quota quota;
    
    @OneToMany(mappedBy = "university")
    @JsonIgnore
    private List<Invoice> invoices;
    
    @OneToMany(mappedBy = "university", cascade = CascadeType.ALL)
    private List<Subscription> subscriptions;
    
  

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUniversityName() {
		return universityName;
	}

	public void setUniversityName(String universityName) {
		this.universityName = universityName;
	}


	public Quota getQuota() {
		return quota;
	}

	public void setQuota(Quota quota) {
		this.quota = quota;
	}

	public List<Invoice> getInvoices() {
		return invoices;
	}

	public void setInvoices(List<Invoice> invoices) {
		this.invoices = invoices;
	}

	public List<Subscription> getSubscriptions() {
		return subscriptions;
	}

	public void setSubscriptions(List<Subscription> subscriptions) {
		this.subscriptions = subscriptions;
	}
    
    

    
}