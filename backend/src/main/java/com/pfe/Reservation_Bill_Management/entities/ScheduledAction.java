package com.pfe.Reservation_Bill_Management.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class ScheduledAction {
    @Id @GeneratedValue
    private Long id;

    private Long reservationId;
    @Enumerated(EnumType.STRING)
    private ActionType actionType; // CREATE or DELETE

    private LocalDateTime scheduledTime;

    @Enumerated(EnumType.STRING)
    private ActionStatus status;
    
    
    public enum ActionType{
    	CREATE,
    	DELETE
    }
    public enum ActionStatus{
    	PENDING,
    	FAILED,
    	DONE// PENDING, DONE, FAILED
    }
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getReservationId() {
		return reservationId;
	}
	public void setReservationId(Long reservationId) {
		this.reservationId = reservationId;
	}
	public ActionType getActionType() {
		return actionType;
	}
	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}
	public LocalDateTime getScheduledTime() {
		return scheduledTime;
	}
	public void setScheduledTime(LocalDateTime scheduledTime) {
		this.scheduledTime = scheduledTime;
	}
	public ActionStatus getStatus() {
		return status;
	}
	public void setStatus(ActionStatus status) {
		this.status = status;
	}
    
    
}
