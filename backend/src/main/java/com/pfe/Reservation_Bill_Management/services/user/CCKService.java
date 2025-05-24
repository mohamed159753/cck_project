package com.pfe.Reservation_Bill_Management.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pfe.Reservation_Bill_Management.dao.UniversityRepository;
import com.pfe.Reservation_Bill_Management.dao.ProfessorRepository;
import com.pfe.Reservation_Bill_Management.dao.ReservationRepository;
import com.pfe.Reservation_Bill_Management.dao.CloudResourceRepository;
import com.pfe.Reservation_Bill_Management.entities.University;
import com.pfe.Reservation_Bill_Management.entities.Professor;
import com.pfe.Reservation_Bill_Management.entities.Reservation;
import com.pfe.Reservation_Bill_Management.entities.Reservation.ApprovalStatus;
import com.pfe.Reservation_Bill_Management.entities.Reservation.ReservationType;
import com.pfe.Reservation_Bill_Management.entities.CloudResource;
import com.pfe.Reservation_Bill_Management.entities.Quota;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class CCKService {

    @Autowired
    private UniversityRepository universityRepository;
    
    @Autowired
    private ProfessorRepository professorRepository;
    
    @Autowired
    private ReservationRepository reservationRepository;
    
    @Autowired
    private CloudResourceRepository cloudResourceRepository;
        
    /**
     * Get complete dashboard statistics
     */
    
    public List<Reservation> getReservations(){
		List<Reservation> reservations = reservationRepository.findByStatus(ApprovalStatus.PENDING_CCK);
		return reservations;
    	
    }
    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> stats = new HashMap<>();

        // Count reservations
        List<Reservation> allReservations = reservationRepository.findAll();
        int totalReservations = allReservations.size();

        // Count pending reservations
        int pendingReservations = (int) allReservations.stream()
            .filter(r -> r.getStatus() == ApprovalStatus.PENDING_UNIVERSITY ||
                         r.getStatus() == ApprovalStatus.PENDING_CCK)
            .count();

        // Count approved reservations
        int approvedReservations = (int) allReservations.stream()
            .filter(r -> r.getStatus() == ApprovalStatus.APPROVED_UNIVERSITY ||
                         r.getStatus() == ApprovalStatus.APPROVED_CCK)
            .count();

        stats.put("reservations", totalReservations);
        stats.put("pending", pendingReservations);
        stats.put("approved", approvedReservations);

        // Quota breakdown
        Map<String, Integer> quotas = calculateTotalQuotas();
        stats.put("TotalVcpuQuotaReserved", quotas.get("vcpu"));
        stats.put("TotalRamQuotaReserved", quotas.get("ram"));
        stats.put("TotalStorageQuotaReserved", quotas.get("storage"));

        Map<String, Integer> used = calculateCurrentUsagePerResource(allReservations);

        int vcpuQuota = quotas.get("vcpu");
        int ramQuota = quotas.get("ram");
        int storageQuota = quotas.get("storage");

        int vcpuUsed = used.get("vcpu");
        int ramUsed = used.get("ram");
        int storageUsed = used.get("storage");

        double vcpuUsePercent = vcpuQuota > 0 ? (vcpuUsed * 100.0) / vcpuQuota : 0;
        double ramUsePercent = ramQuota > 0 ? (ramUsed * 100.0) / ramQuota : 0;
        double storageUsePercent = storageQuota > 0 ? (storageUsed * 100.0) / storageQuota : 0;

        stats.put("vcpuUsed", vcpuUsed);
        stats.put("ramUsed", ramUsed);
        stats.put("storageUsed", storageUsed);

        stats.put("vcpuUsePercent", vcpuUsePercent);
        stats.put("ramUsePercent", ramUsePercent);
        stats.put("storageUsePercent", storageUsePercent);

        // Usage and ranking
        stats.put("usageStatistics", getDetailedMonthlyUsageStatistics());
        stats.put("topUniversities", getTopUniversitiesByResourceUsage(3, "vcpu"));

        return stats;
    }
    
    private Map<String, Integer> calculateCurrentUsagePerResource(List<Reservation> reservations) {
        LocalDateTime now = LocalDateTime.now();
        int vcpu = 0, ram = 0, storage = 0;

        for (Reservation r : reservations) {
            ApprovalStatus status = r.getStatus();
            if (status == null) continue;

            boolean isActiveStatus = status == ApprovalStatus.PENDING_UNIVERSITY ||
                                     status == ApprovalStatus.PENDING_CCK ||
                                     status == ApprovalStatus.APPROVED_UNIVERSITY ||
                                     status == ApprovalStatus.APPROVED_CCK;

            boolean isWithinActivePeriod = now.isAfter(r.getStartTime()) && now.isBefore(r.getEndTime());
            boolean isStillPending = status == ApprovalStatus.PENDING_UNIVERSITY || status == ApprovalStatus.PENDING_CCK;

            if (isActiveStatus && (isWithinActivePeriod || isStillPending)) {
                CloudResource res = r.getResource();
                if (res != null) {
                    vcpu += res.getVcpu();
                    ram += res.getRam();
                    storage += res.getStorage();
                }
            }
        }

        Map<String, Integer> result = new HashMap<>();
        result.put("vcpu", vcpu);
        result.put("ram", ram);
        result.put("storage", storage);
        return result;
    }
    
    /**
     * Calculate total quota across all universities
     * Returns a map with vcpu, ram, and storage total quotas
     */
    private Map<String, Integer> calculateTotalQuotas() {
        // Get all universities and sum their quotas
        List<University> universities = universityRepository.findAll();
        int totalVcpuQuota = 0;
        int totalRamQuota = 0;
        int totalStorageQuota = 0;
        
        for (University university : universities) {
            Quota quota = university.getQuota();
            if (quota != null) {
                totalVcpuQuota += quota.getVcpu();
                totalRamQuota += quota.getRamInMb();
                totalStorageQuota += quota.getStorageInGb();
            }
        }
        
        Map<String, Integer> quotas = new HashMap<>();
        quotas.put("vcpu", totalVcpuQuota > 0 ? totalVcpuQuota : 1); // Avoid division by zero
        quotas.put("ram", totalRamQuota > 0 ? totalRamQuota : 1);
        quotas.put("storage", totalStorageQuota > 0 ? totalStorageQuota : 1);
        
        return quotas;
    }
    
    /**
     * Calculate total quota (combined value for overall percentage)
     */
    private int calculateTotalQuota() {
        Map<String, Integer> quotas = calculateTotalQuotas();
        // You can decide how to weight these values - here we just sum them
        return quotas.get("vcpu") + quotas.get("ram") + quotas.get("storage");
    }
    
    /**
     * Calculate current resource usage based on active and pending reservations
     */
    private double calculateCurrentUsage(List<Reservation> reservations) {
        LocalDateTime now = LocalDateTime.now();
        double totalUsage = 0;

        // Active approved/in-progress/completed reservations
        totalUsage += reservations.stream()
            .filter(r -> {
                ApprovalStatus status = r.getStatus();
                return status != null &&
                       (status == ApprovalStatus.APPROVED_CCK || 
                        status == ApprovalStatus.APPROVED_UNIVERSITY) &&
                       now.isAfter(r.getStartTime()) && now.isBefore(r.getEndTime());
            })
            .mapToDouble(r -> {
                CloudResource resource = r.getResource();
                return (resource != null)
                    ? resource.getVcpu() + resource.getRam() + resource.getStorage()
                    : 0;
            })
            .sum();

        // Pending reservations (unstarted but reserved)
        totalUsage += reservations.stream()
            .filter(r -> {
                ApprovalStatus status = r.getStatus();
                return status == ApprovalStatus.PENDING_CCK || status == ApprovalStatus.PENDING_UNIVERSITY;
            })
            .mapToDouble(r -> {
                CloudResource resource = r.getResource();
                return (resource != null)
                    ? resource.getVcpu() + resource.getRam() + resource.getStorage()
                    : 0;
            })
            .sum();

        return totalUsage;
    }

    
    /**
     * Get monthly usage statistics
     */
    private Map<String, Integer> getMonthlyUsageStatistics() {
        Map<String, Integer> monthlyUsage = new LinkedHashMap<>();

        int currentYear = Year.now().getValue();
        String[] months = {"jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec"};

        List<Reservation> allReservations = reservationRepository.findAll();

        for (int i = 0; i < months.length; i++) {
            int monthValue = i + 1;

            LocalDateTime startOfMonth = LocalDateTime.of(currentYear, monthValue, 1, 0, 0);
            LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusSeconds(1);

            int monthlyResourceUsage = allReservations.stream()
                .filter(r -> {
                    LocalDateTime start = r.getStartTime();
                    LocalDateTime end = r.getEndTime();
                    ApprovalStatus status = r.getStatus();

                    return start != null && end != null &&
                           status != null &&
                           (
                               status == ApprovalStatus.APPROVED_CCK ||
                               status == ApprovalStatus.APPROVED_UNIVERSITY ||
                               status == ApprovalStatus.PENDING_CCK ||
                               status == ApprovalStatus.PENDING_UNIVERSITY) &&
                           start.isBefore(endOfMonth) && end.isAfter(startOfMonth);
                })
                .mapToInt(r -> {
                    CloudResource resource = r.getResource();
                    if (resource != null) {
                        return resource.getVcpu() + (resource.getRam() / 100) + (resource.getStorage() / 10);
                    }
                    return 0;
                })
                .sum();

            monthlyUsage.put(months[i], monthlyResourceUsage);
        }

        return monthlyUsage;
    }

    
    /**
     * Get top universities by resource usage
     */
    private List<Map<String, Object>> getTopUniversitiesByResourceUsage(int limit, String resourceType) {
        List<University> allUniversities = universityRepository.findAll();
        List<Map<String, Object>> topUniversities = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (University university : allUniversities) {
            List<Reservation> universityReservations = reservationRepository.findByUniversityId(university.getId());

            int cpuUsage = 0;
            int ramUsage = 0;
            int storageUsage = 0;

            for (Reservation reservation : universityReservations) {
                if (isCountableReservation(reservation, now)) {
                    CloudResource resource = reservation.getResource();
                    if (resource != null) {
                        cpuUsage += resource.getVcpu();
                        ramUsage += resource.getRam();
                        storageUsage += resource.getStorage();
                    }
                }
            }

            Map<String, Object> uniData = new HashMap<>();
            uniData.put("name", university.getUniversityName());
            uniData.put("cpuUsage", cpuUsage);
            uniData.put("ramUsage", ramUsage);
            uniData.put("storageUsage", storageUsage);

            topUniversities.add(uniData);
        }

        // Sort and return the top N
        final String sortField = getSortFieldByResourceType(resourceType);
        return topUniversities.stream()
            .sorted((a, b) -> ((Integer) b.get(sortField)).compareTo((Integer) a.get(sortField)))
            .limit(limit)
            .collect(Collectors.toList());
    }

    // Reusable status check logic
    private boolean isCountableReservation(Reservation reservation, LocalDateTime now) {
        ApprovalStatus status = reservation.getStatus();
        if (status == null) return false;

        boolean validStatus = status == ApprovalStatus.APPROVED_CCK ||
                              status == ApprovalStatus.APPROVED_UNIVERSITY ||
                              status == ApprovalStatus.PENDING_CCK ||
                              status == ApprovalStatus.PENDING_UNIVERSITY;
        if (!validStatus) return false;

        // Count either currently active or pending
        return reservation.getStartTime() != null &&
               reservation.getEndTime() != null &&
               (now.isAfter(reservation.getStartTime()) && now.isBefore(reservation.getEndTime())
                || status == ApprovalStatus.PENDING_CCK || status == ApprovalStatus.PENDING_UNIVERSITY);
    }

    // Helper method to determine which field to sort by based on resource type
    private String getSortFieldByResourceType(String resourceType) {
        if (resourceType == null) {
            return "cpuUsage"; // Default
        }
        
        switch (resourceType.toUpperCase()) {
            case "RAM":
                return "ramUsage";
            case "STORAGE":
                return "storageUsage";
            case "CPU":
            default:
                return "cpuUsage";
        }
    }
    
    /**
     * Get all universities
     */
    public List<University> getAllUniversities() {
        return universityRepository.findAll();
    }
    
    /**
     * Get university by ID
     */
    public University getUniversityById(String id) {
        return universityRepository.findById(id).orElse(null);
    }
    
    /**
     * Create new university
     */
    public University createUniversity(University university) {
        return universityRepository.save(university);
    }
    
    /**
     * Update existing university
     */
    public University updateUniversity(String id, University universityDetails) {
        University university = universityRepository.findById(id).orElse(null);
        if (university != null) {
            university.setUniversityName(universityDetails.getUniversityName());
            if (universityDetails.getQuota() != null) {
                university.setQuota(universityDetails.getQuota());
            }
            return universityRepository.save(university);
        }
        return null;
    }
    
    /**
     * Delete university
     */
    public boolean deleteUniversity(String id) {
        if (universityRepository.existsById(id)) {
            universityRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * Get professors by university ID
     */
    public List<Professor> getProfessorsByUniversity(String universityId) {
        // More efficient implementation using direct filtering
        return professorRepository.findAll().stream()
            .filter(p -> p.getUniversity().isPresent() && 
                   universityId.equals(p.getUniversity().get().getId()))
            .collect(Collectors.toList());
    }
    
    /**
     * Get professor by ID
     */
    public Professor getProfessorById(Long id) {
        return professorRepository.findById(id).orElse(null);
    }
    
    /**
     * Create professor for university
     */
    public Professor createProfessor(Professor professor, String universityId) {
        University university = universityRepository.findById(universityId).orElse(null);
        if (university != null) {
            professor.setUniversity(university);
            return professorRepository.save(professor);
        }
        return null;
    }
    
    /**
     * Update professor
     */
    public Professor updateProfessor(Long id, Professor professorDetails) {
        Professor professor = professorRepository.findById(id).orElse(null);
        if (professor != null) {
            professor.setEmail(professorDetails.getEmail());
            professor.setUsername(professorDetails.getUsername());
            professor.setInstitute(professorDetails.getInstitute());
            if (professorDetails.getUniversity().isPresent()) {
                professor.setUniversity(professorDetails.getUniversity().get());
            }
            // Don't update password unless explicitly provided with a new one
            if (professorDetails.getPassword() != null && !professorDetails.getPassword().isEmpty()) {
                professor.setPassword(professorDetails.getPassword());
            }
            return professorRepository.save(professor);
        }
        return null;
    }
    
    /**
     * Delete professor
     */
    public boolean deleteProfessor(Long id) {
        if (professorRepository.existsById(id)) {
            professorRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    
    /**
     * Get monthly usage statistics separated by resource type
     */
    private Map<String, Map<String, Integer>> getDetailedMonthlyUsageStatistics() {
        Map<String, Map<String, Integer>> monthlyUsage = new LinkedHashMap<>();
        
        // Current year
        int currentYear = Year.now().getValue();
        
        // Months (abbreviated to 3 letters as shown in your UI)
        String[] months = {"jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec"};
        
        // Get all reservations
        List<Reservation> allReservations = reservationRepository.findAll();
        
        // Calculate usage for each month
        for (int i = 0; i < months.length; i++) {
            int monthIndex = i + 1; // 1-based month index
            final int monthValue = monthIndex;
            
            // Initialize resource counters for the month
            int totalVcpu = 0;
            int totalRam = 0;
            int totalStorage = 0;
            
            // Find reservations for this month
            List<Reservation> monthReservations = allReservations.stream()
            	    .filter(r -> {
            	        // Check if the reservation overlaps with this month in the current year
            	        LocalDateTime startOfMonth = LocalDateTime.of(currentYear, monthValue, 1, 0, 0);
            	        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusSeconds(1);

            	        ApprovalStatus status = r.getStatus();
            	        boolean validStatus = status == ApprovalStatus.APPROVED_CCK ||
            	                              status == ApprovalStatus.APPROVED_UNIVERSITY ||
            	                              status == ApprovalStatus.PENDING_CCK ||
            	                              status == ApprovalStatus.PENDING_UNIVERSITY;

            	        return r.getStartTime().isBefore(endOfMonth)
            	            && r.getEndTime().isAfter(startOfMonth)
            	            && status != null
            	            && validStatus;
            	    })
            	    .collect(Collectors.toList());

            // Calculate total resources for each month
            for (Reservation reservation : monthReservations) {
                CloudResource resource = reservation.getResource();
                if (resource != null) {
                    totalVcpu += resource.getVcpu();
                    totalRam += resource.getRam();
                    totalStorage += resource.getStorage();
                }
            }
            
            // Store the resource usage for this month
            Map<String, Integer> monthData = new HashMap<>();
            monthData.put("vcpu", totalVcpu);
            monthData.put("ram", totalRam);
            monthData.put("storage", totalStorage);
            
            monthlyUsage.put(months[i], monthData);
        }
        
        return monthlyUsage;
    }
    
    /**
     * Get dashboard statistics for a specific university
     * @param universityId The ID of the university to get statistics for
     * @return Map of statistics for the specified university
     */
    public Map<String, Object> getUniversityDashboardStatistics(String universityId) {
        Map<String, Object> stats = new HashMap<>();

        // Find the university
        University university = universityRepository.findById(universityId).orElse(null);
        if (university == null) {
            return stats; // Return empty stats if university not found
        }

        // Get university name
        stats.put("universityName", university.getUniversityName());

        // Get all reservations for this university
        List<Reservation> universityReservations = reservationRepository.findByUniversityId(universityId);

        int totalReservations = universityReservations.size();

        // Count pending and approved reservations
        int pendingReservations = (int) universityReservations.stream()
        	    .filter(r -> {
        	    	ReservationType type = r.getReservationType();
        	        ApprovalStatus status = r.getStatus();
        	        return status != null &&
        	               (status == ApprovalStatus.PENDING_CCK ||
        	                status == ApprovalStatus.PENDING_UNIVERSITY );
        	        })
        	    .count();

        	int approvedReservations = (int) universityReservations.stream()
        	    .filter(r -> {
        	        ApprovalStatus status = r.getStatus();
        	    	ReservationType type = r.getReservationType();

        	        return status != null &&
        	               (status == ApprovalStatus.APPROVED_CCK ||
        	                status == ApprovalStatus.APPROVED_UNIVERSITY);
        	    })
        	    .count();

        stats.put("reservations", totalReservations);
        stats.put("pending", pendingReservations);
        stats.put("approved", approvedReservations);

        // Quota info
        Quota universityQuota = university.getQuota();
        if (universityQuota != null) {
            int totalVcpuQuota = universityQuota.getVcpu();
            int totalRamQuota = universityQuota.getRamInMb();
            int totalStorageQuota = universityQuota.getStorageInGb();

            // Calculate usage (only from approved or completed)
            int usedVcpu = universityReservations.stream()
            	    .filter(r -> r.getStatus() != null &&
            	                (r.getStatus() == ApprovalStatus.APPROVED_CCK ||
            	                 r.getStatus() == ApprovalStatus.APPROVED_UNIVERSITY ||
            	                 r.getStatus() == ApprovalStatus.PENDING_CCK ||
            	                 r.getStatus() == ApprovalStatus.PENDING_UNIVERSITY) &&
            	                 r.getResource() != null && r.getReservationType() != ReservationType.PAYG )
            	    .mapToInt(r -> r.getResource().getVcpu())
            	    .sum();

            	int usedRam = universityReservations.stream()
            	    .filter(r -> r.getStatus() != null &&
            	                (r.getStatus() == ApprovalStatus.APPROVED_CCK ||
            	                 r.getStatus() == ApprovalStatus.APPROVED_UNIVERSITY ||
            	                 r.getStatus() == ApprovalStatus.PENDING_CCK ||
            	                 r.getStatus() == ApprovalStatus.PENDING_UNIVERSITY) &&
            	                 r.getResource() != null && r.getReservationType() != ReservationType.PAYG)
            	    .mapToInt(r -> r.getResource().getRam()) // or getRam() if correct
            	    .sum();

            	int usedStorage = universityReservations.stream()
            	    .filter(r -> r.getStatus() != null &&
            	                (r.getStatus() == ApprovalStatus.APPROVED_CCK ||
            	                 r.getStatus() == ApprovalStatus.APPROVED_UNIVERSITY ||
            	                 r.getStatus() == ApprovalStatus.PENDING_CCK ||
            	                 r.getStatus() == ApprovalStatus.PENDING_UNIVERSITY) &&
            	                 r.getResource() != null && r.getReservationType() != ReservationType.PAYG)
            	    .mapToInt(r -> r.getResource().getStorage())
            	    .sum();

            stats.put("TotalVcpuQuotaReserved", totalVcpuQuota);
            stats.put("TotalRamQuotaReserved", totalRamQuota);
            stats.put("TotalStorageQuotaReserved", totalStorageQuota);

            stats.put("UsedVcpu", usedVcpu);
            stats.put("UsedRam", usedRam);
            stats.put("UsedStorage", usedStorage);

            // Percentage calculations
            stats.put("vcpuUsePercent", totalVcpuQuota == 0 ? 0 : (usedVcpu * 100) / totalVcpuQuota);
            stats.put("ramUsePercent", totalRamQuota == 0 ? 0 : (usedRam * 100) / totalRamQuota);
            stats.put("storageUsePercent", totalStorageQuota == 0 ? 0 : (usedStorage * 100) / totalStorageQuota);
        }

        // Add detailed stats
        stats.put("usageStatistics", getDetailedUniversityMonthlyUsageStatistics(universityId));

        return stats;
    }



    /**
     * Calculate resource usage for a specific university based on its reservations
     */
    private double calculateUniversityUsage(List<Reservation> universityReservations) {
        LocalDateTime now = LocalDateTime.now();
        
        double totalUsage = 0;
        
        // For active approved reservations
        totalUsage += universityReservations.stream()
            .filter(r -> now.isAfter(r.getStartTime()) && now.isBefore(r.getEndTime())
                    && "APPROVED".equals(r.getStatus()))
            .mapToDouble(r -> {
                CloudResource resource = r.getResource();
                if (resource != null) {
                    // Calculate weighted sum of resources (adjust weights as needed)
                    return resource.getVcpu() + resource.getRam() + resource.getStorage();
                }
                return 0;
            })
            .sum();
        
        // Also consider pending reservations
        totalUsage += universityReservations.stream()
            .filter(r -> "PENDING".equals(r.getStatus()))
            .mapToDouble(r -> {
                CloudResource resource = r.getResource();
                if (resource != null) {
                    // Calculate weighted sum of resources (adjust weights as needed)
                    return resource.getVcpu() + resource.getRam() + resource.getStorage();
                }
                return 0;
            })
            .sum();
            
        return totalUsage;
    }

    /**
     * Get monthly usage statistics for a specific university
     */
    private Map<String, Integer> getUniversityMonthlyUsageStatistics(String universityId) {
        Map<String, Integer> monthlyUsage = new LinkedHashMap<>();
        
        // Current year
        int currentYear = Year.now().getValue();
        
        // Months (abbreviated to 3 letters as shown in your UI)
        String[] months = {"jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec"};
        
        // Get university reservations
        List<Reservation> universityReservations = reservationRepository.findByUniversityId(universityId);
        
        // Calculate usage for each month
        for (int i = 0; i < months.length; i++) {
            int monthIndex = i + 1; // 1-based month index
            final int monthValue = monthIndex;
            
            // Count resources used in each month (combine vCPU, RAM, and storage)
            int monthlyResourceUsage = universityReservations.stream()
            	    .filter(r -> {
            	        // Check if the reservation overlaps with this month in the current year
            	        LocalDateTime startOfMonth = LocalDateTime.of(currentYear, monthValue, 1, 0, 0);
            	        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusSeconds(1);

            	        ApprovalStatus status = r.getStatus();
            	        return (r.getStartTime().isBefore(endOfMonth) && r.getEndTime().isAfter(startOfMonth)) &&
            	               (status != null &&
            	                (status == ApprovalStatus.APPROVED_CCK ||
            	                 status == ApprovalStatus.APPROVED_UNIVERSITY ||
            	                 status == ApprovalStatus.PENDING_CCK ||
            	                 status == ApprovalStatus.PENDING_UNIVERSITY) && r.getReservationType() != ReservationType.PAYG);
            	    })
                .mapToInt(r -> {
                    CloudResource resource = r.getResource();
                    if (resource != null) {
                        // Compute a weighted sum of resources
                        return resource.getVcpu() ;
                    }
                    return 0;
                })
                .sum();
            
            monthlyUsage.put(months[i], monthlyResourceUsage);
        }
        
        return monthlyUsage;
    }

    /**
     * Get top professors by resource usage for a specific university
     */
    

    /**
     * Get detailed monthly usage statistics by resource type for a specific university
     */
    private Map<String, Map<String, Integer>> getDetailedUniversityMonthlyUsageStatistics(String universityId) {
        Map<String, Map<String, Integer>> monthlyUsage = new LinkedHashMap<>();
        
        // Current year
        int currentYear = Year.now().getValue();
        
        // Months (abbreviated to 3 letters as shown in your UI)
        String[] months = {"jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec"};
        
        // Get university reservations
        List<Reservation> universityReservations = reservationRepository.findByUniversityId(universityId);
        
        // Calculate usage for each month
        for (int i = 0; i < months.length; i++) {
            int monthIndex = i + 1; // 1-based month index
            final int monthValue = monthIndex;
            
            // Initialize resource counters for the month
            int totalVcpu = 0;
            int totalRam = 0;
            int totalStorage = 0;
            
            // Find reservations for this month
            List<Reservation> monthReservations = universityReservations.stream()
            		   .filter(r -> {
            		        LocalDateTime startOfMonth = LocalDateTime.of(currentYear, monthValue, 1, 0, 0);
            		        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusSeconds(1);

            		        return (r.getStartTime().isBefore(endOfMonth) && r.getEndTime().isAfter(startOfMonth)) &&
            		               (r.getStatus() != null && (
            		                   r.getStatus() == ApprovalStatus.APPROVED_CCK ||
            		                   r.getStatus() == ApprovalStatus.APPROVED_UNIVERSITY ||
            		                   r.getStatus() == ApprovalStatus.PENDING_CCK ||
            		                   r.getStatus() == ApprovalStatus.PENDING_UNIVERSITY
            		               ) && r.getReservationType() != ReservationType.PAYG);
            		    })
            		    .collect(Collectors.toList());
            
            // Calculate total resources for each month
            for (Reservation reservation : monthReservations) {
                CloudResource resource = reservation.getResource();
                if (resource != null) {
                    totalVcpu += resource.getVcpu();
                    totalRam += resource.getRam();
                    totalStorage += resource.getStorage();
                }
            }
            
            // Store the resource usage for this month
            Map<String, Integer> monthData = new HashMap<>();
            monthData.put("vcpu", totalVcpu);
            monthData.put("ram", totalRam);
            monthData.put("storage", totalStorage);
            
            monthlyUsage.put(months[i], monthData);
        }
        
        return monthlyUsage;
    }
    
    
    
}