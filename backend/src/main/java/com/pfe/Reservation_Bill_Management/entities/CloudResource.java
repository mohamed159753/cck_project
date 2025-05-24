package com.pfe.Reservation_Bill_Management.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cloud_resources")

public class CloudResource {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String type;
    
    private int vcpu;
    
    private int ram;
    
    private int storage;
    
    private String image;
    
    private String flavorId;
    
    private String imageId;
    
    private String externalId;
    
    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;
    
    
    
    public String getFlavorId() {
		return flavorId;
	}

	public void setFlavorId(String flavorId) {
		this.flavorId = flavorId;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	@Column(name = "price_per_hour", nullable = false)
    private float pricePerHour;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public float getPricePerHour() {
		return pricePerHour;
	}

	public void setPricePerHour(float pricePerHour) {
		this.pricePerHour = pricePerHour;
	}

	public int getVcpu() {
		return vcpu;
	}

	public void setVcpu(int vcpu) {
		this.vcpu = vcpu;
	}

	public int getRam() {
		return ram;
	}

	public void setRam(int ram) {
		this.ram = ram;
	}

	public int getStorage() {
		return storage;
	}

	public void setStorage(int storage) {
		this.storage = storage;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Reservation getReservation() {
		return reservation;
	}

	public void setReservation(Reservation reservation) {
		this.reservation = reservation;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
	
	
	
	
	
	
    
    
    
    
    
    
}