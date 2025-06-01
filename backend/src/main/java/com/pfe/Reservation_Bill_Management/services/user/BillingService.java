package com.pfe.Reservation_Bill_Management.services.user;

import java.time.YearMonth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pfe.Reservation_Bill_Management.dao.BillingEntryRepository;
import com.pfe.Reservation_Bill_Management.dao.CloudResourceRepository;
import com.pfe.Reservation_Bill_Management.dao.UniversityRepository;
import com.pfe.Reservation_Bill_Management.dto.BillingEntryDTO;
import com.pfe.Reservation_Bill_Management.entities.BillingEntry;

@Service
public class BillingService {
	
	@Autowired
    CloudResourceRepository cloudResourceRepository;
	@Autowired
    UniversityRepository universityRepository;
	@Autowired
    BillingEntryRepository billingEntryRepository;
	public void createBillingEntryFromDto(BillingEntryDTO dto) {
	    BillingEntry entry = new BillingEntry();
	    entry.setProfessorId(dto.professorId);
	    entry.setStartTime(dto.startTime);
	    entry.setEndTime(dto.endTime);
	    entry.setCost(dto.cost);
	    entry.setBillingMonth(YearMonth.from(dto.startTime).toString());
		entry.setResource(cloudResourceRepository.findById(dto.resourceId).orElse(null));
	    entry.setUniversity(universityRepository.findById(dto.universityId).orElse(null));
	    billingEntryRepository.save(entry);
	}

}
