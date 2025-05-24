package com.pfe.Reservation_Bill_Management.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invoiceId;
    
    private String month;
    private BigDecimal fixedAmount;
    private BigDecimal paygTotal;

    private BigDecimal totalAmount;
    private String status;
    private LocalDate IssueDate;
    private LocalDate DueDate;
    
    @ManyToOne
    @JoinColumn(name = "university_id")
    private University university;
    
    /* @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<Payment> payments; */
    
    @OneToMany
    private List<BillingEntry> entries;

	public Long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDate getIssueDate() {
		return IssueDate;
	}

	public void setIssueDate(LocalDate issueDate) {
		IssueDate = issueDate;
	}

	public LocalDate getDueDate() {
		return DueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		DueDate = dueDate;
	}

	public University getUniversity() {
		return university;
	}

	public void setUniversity(University university) {
		this.university = university;
	}

	public YearMonth getMonth() {
		return YearMonth.parse(month);
	}

	public void setMonth(YearMonth month) {
		this.month = month.toString();
	}

	public BigDecimal getFixedAmount() {
		return fixedAmount;
	}

	public void setFixedAmount(BigDecimal fixedAmount) {
		this.fixedAmount = fixedAmount;
	}

	public BigDecimal getPaygTotal() {
		return paygTotal;
	}

	public void setPaygTotal(BigDecimal paygTotal) {
		this.paygTotal = paygTotal;
	}

	public List<BillingEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<BillingEntry> entries) {
		this.entries = entries;
	}
	
	

	/*public List<Payment> getPayments() {
		return payments;
	}

	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	} */
    
    
    
    
    
    
}