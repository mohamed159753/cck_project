package com.pfe.Reservation_Bill_Management.entities;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable

public class Quota {
    private int storageInGb;
    private int vcpu;
    private int ramInMb;
    
    
	public int getStorageInGb() {
		return storageInGb;
	}
	public void setStorageInGb(int storageInGb) {
		this.storageInGb = storageInGb;
	}
	public int getVcpu() {
		return vcpu;
	}
	public void setVcpu(int vcpu) {
		this.vcpu = vcpu;
	}
	public int getRamInMb() {
		return ramInMb;
	}
	public void setRamInMb(int ramInMb) {
		this.ramInMb = ramInMb;
	}
    
    
}
