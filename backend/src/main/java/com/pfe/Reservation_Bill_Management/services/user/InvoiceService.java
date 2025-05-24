package com.pfe.Reservation_Bill_Management.services.user;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.pfe.Reservation_Bill_Management.dao.BillingEntryRepository;
import com.pfe.Reservation_Bill_Management.dao.EcsUsageRepository;
import com.pfe.Reservation_Bill_Management.dao.InvoiceRepository;
import com.pfe.Reservation_Bill_Management.dao.UniversityRepository;
import com.pfe.Reservation_Bill_Management.dto.BillingEntryDTO;
import com.pfe.Reservation_Bill_Management.entities.BillingEntry;
import com.pfe.Reservation_Bill_Management.entities.EcsUsage;
import com.pfe.Reservation_Bill_Management.entities.Invoice;
import com.pfe.Reservation_Bill_Management.entities.Subscription;
import com.pfe.Reservation_Bill_Management.entities.University;

import jakarta.transaction.Transactional;

@Service
public class InvoiceService {
	
	
	 @Autowired
	    private BillingEntryRepository billingEntryRepository;

	    @Autowired
	    private InvoiceRepository invoiceRepository;

	    @Autowired
	    private UniversityRepository universityRepository;
	    
	    @Autowired
	    private EcsUsageRepository ecsUsageRepository;
	    
	    @Autowired
	    private BillingService billingService;
	    
	    @Transactional
	    @Scheduled(cron = "0 0 0 1 * *") // At 00:00 on 1st of each month
	    public void generateMonthlyInvoices() {
	        String lastMonth = YearMonth.now().minusMonths(1).toString();

	        List<University> universities = universityRepository.findAll();

	        for (University university : universities) {
	            List<BillingEntry> paygEntries = billingEntryRepository
	                .findByUniversityAndBillingMonth(university, lastMonth.toString());

	            BigDecimal paygTotal = paygEntries.stream()
	                .map(BillingEntry::getCost)
	                .reduce(BigDecimal.ZERO, BigDecimal::add);
	            
	            BigDecimal subscription = BigDecimal.valueOf(
	            	    university.getSubscriptions().get(0).getPrice()  // assuming this returns a float
	            	);

	            BigDecimal total = subscription.add(paygTotal);

	            Invoice invoice = new Invoice();
	            invoice.setUniversity(university);
	            invoice.setMonth(YearMonth.now().minusMonths(1));
	            invoice.setEntries(paygEntries);
	            Subscription activeSub = university.getSubscriptions().get(0);
	            BigDecimal monthlySubscription = BigDecimal.valueOf(activeSub.getPrice());

	            invoice.setFixedAmount(monthlySubscription);
	            invoice.setPaygTotal(paygTotal);
	            invoice.setTotalAmount(total);

	            invoiceRepository.save(invoice);
	        }
	    }
	    
	    @Transactional
	    @Scheduled(cron = "0 0 23 30 * *") // Run on 30th at 23:00 before invoice job
	    public void convertEcsUsageToBillingEntries() {
	        YearMonth lastMonth = YearMonth.now().minusMonths(1);
	        List<EcsUsage> ecsUsages = ecsUsageRepository.findAllByMonth(lastMonth.getMonthValue(),lastMonth.getYear());

	        for (EcsUsage usage : ecsUsages) {	            

	            // Create BillingEntryDTO
	            BillingEntryDTO dto = new BillingEntryDTO();
	            dto.professorId = usage.getProf().getId();
	            dto.startTime = usage.getStartTime();
	            dto.endTime = usage.getStopTime();
	            dto.cost =  BigDecimal.valueOf(usage.getCost());
	            dto.resourceId = usage.getCloudResource().getId();
	            dto.universityId = usage.getCloudResource().getReservation().getProfessor().getUniversity().get().getId();

	            billingService.createBillingEntryFromDto(dto);
	        }
	    }
	    
	    
	    public List<Invoice> getInvoicesForUniversity(String universityId) {
	        University university = universityRepository.findById(universityId)
	            .orElseThrow(() -> new IllegalArgumentException("University not found"));

	        return invoiceRepository.findByUniversityOrderByMonthDesc(university);
	    }
	    
	    public List<Invoice> getAllInvoices() {
	        return invoiceRepository.findAll();
	    }

	    public List<Invoice> getInvoicesByMonth(String month) {
	        return invoiceRepository.findByMonth(month);
	    }

	    public List<Invoice> getInvoicesByUniversityAndMonth(String universityId, YearMonth month) {
	        University university = universityRepository.findById(universityId)
	            .orElseThrow(() -> new IllegalArgumentException("University not found"));

	        return invoiceRepository.findByUniversityAndMonth(university, month.toString());
	    }
	    
	    public Invoice getInvoiceById(Long id) {
	    	Invoice invoice =  invoiceRepository.getById(id); 
	    	return invoice;
	    }
	
  }


