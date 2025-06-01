package com.pfe.Reservation_Bill_Management.services.user;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.pfe.Reservation_Bill_Management.dao.ScheduleActionRepository;
import com.pfe.Reservation_Bill_Management.entities.ScheduledAction;
import com.pfe.Reservation_Bill_Management.entities.ScheduledAction.ActionStatus;
import com.pfe.Reservation_Bill_Management.entities.ScheduledAction.ActionType;

@Service
public class EcsSchedulerService {
	
	@Autowired
	ScheduleActionRepository actionRepo;
	
	@Autowired
	CloudResourceService cloudResourceService;
	
	@Scheduled(fixedRate = 60000)
	public void processScheduledActions() {
	    List<ScheduledAction> dueActions = actionRepo.findDueActions(LocalDateTime.now());

	    for (ScheduledAction action : dueActions) {
	        try {
	            if (action.getActionType() == ActionType.CREATE) {
	                cloudResourceService.createEcsQuotaForReservation(action.getReservationId());
	            } else {
	                cloudResourceService.deleteVMForReservation(action.getReservationId());
	            }
	            action.setStatus(ActionStatus.DONE);
	        } catch (Exception e) {
	            action.setStatus(ActionStatus.FAILED);
	            e.printStackTrace(); // Optional: log the error or send an alert
	        }
	        actionRepo.save(action);
	    }
	}

}
