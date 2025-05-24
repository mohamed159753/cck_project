package com.pfe.Reservation_Bill_Management.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int storageInGb;
    private int vcpu;
    private int ramInMb;
    
    private float price;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getStorageInGb() { return storageInGb; }
    public void setStorageInGb(int storageInGb) { this.storageInGb = storageInGb; }

    public int getVcpu() { return vcpu; }
    public void setVcpu(int vcpu) { this.vcpu = vcpu; }

    public int getRamInMb() { return ramInMb; }
    public void setRamInMb(int ramInMb) { this.ramInMb = ramInMb; }

    public float getPrice() { return price; }
    
    public void setPrice(float price) { this.price = price; }
}
