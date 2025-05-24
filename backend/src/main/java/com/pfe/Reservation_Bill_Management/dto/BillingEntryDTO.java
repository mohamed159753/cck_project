package com.pfe.Reservation_Bill_Management.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BillingEntryDTO {
	
	public long professorId;
    public Long resourceId;
    public LocalDateTime startTime;
    public LocalDateTime endTime;
    public BigDecimal cost;
    public String universityId;

}
