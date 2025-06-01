package com.pfe.Reservation_Bill_Management.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.config.RepositoryNameSpaceHandler;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pfe.Reservation_Bill_Management.dao.EcsUsageRepository;
import com.pfe.Reservation_Bill_Management.dao.ReservationRepository;
import com.pfe.Reservation_Bill_Management.dao.ScheduleActionRepository;
import com.pfe.Reservation_Bill_Management.dto.UnavailableTimeSlot;
import com.pfe.Reservation_Bill_Management.entities.CckAdmin;
import com.pfe.Reservation_Bill_Management.entities.CloudResource;
import com.pfe.Reservation_Bill_Management.entities.EcsUsage;
import com.pfe.Reservation_Bill_Management.entities.Professor;
import com.pfe.Reservation_Bill_Management.entities.Quota;
import com.pfe.Reservation_Bill_Management.entities.Reservation;
import com.pfe.Reservation_Bill_Management.entities.Reservation.ApprovalStatus;
import com.pfe.Reservation_Bill_Management.entities.Reservation.ReservationType;
import com.pfe.Reservation_Bill_Management.entities.ScheduledAction;
import com.pfe.Reservation_Bill_Management.entities.ScheduledAction.ActionStatus;
import com.pfe.Reservation_Bill_Management.entities.ScheduledAction.ActionType;
import com.pfe.Reservation_Bill_Management.entities.University;
import com.pfe.Reservation_Bill_Management.entities.UniversityAdmin;
import com.pfe.Reservation_Bill_Management.security.JwtUtil;
import com.pfe.Reservation_Bill_Management.services.user.CloudResourceService;
import com.pfe.Reservation_Bill_Management.services.user.LoginServiceCCK;
import com.pfe.Reservation_Bill_Management.services.user.LoginServiceUniversities;
import com.pfe.Reservation_Bill_Management.services.user.ProfessorService;
import com.pfe.Reservation_Bill_Management.services.user.ReservationService;
import com.pfe.Reservation_Bill_Management.services.user.UniversityService;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "http://localhost:4200")
public class ReservationController {
    
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ProfessorService professorService;
    @Autowired
    private UniversityService uniService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CloudResourceService cldResourceService;
    @Autowired ReservationRepository reservationRepository;
    @Autowired EcsUsageRepository ecsUsageRepository;
    @Autowired LoginServiceUniversities universityService;
    @Autowired LoginServiceCCK cckService;
    @Autowired ScheduleActionRepository scheduleActionRepository;
    
    @GetMapping("/university/{universityId}")
    public ResponseEntity<Object> getReservationsByUniversity(
            @PathVariable String universityId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        if (startDate != null && endDate != null) {
            // Add this method to your service and repository
            return ResponseEntity.ok(reservationService.findByUniversityIdAndDateRange(universityId, startDate, endDate));
        }
        
        // Add this method to your service and repository
        return ResponseEntity.ok(reservationService.findByUniversityId(universityId));
    }
    
    @GetMapping("/professor/reservations")
    public ResponseEntity<List<Reservation>> getReservationsByProfessor(@RequestHeader("Authorization") String authHeader) {
        // Add this method to your service and repository
    	String token = authHeader.replace("Bearer ", "");
        Long professorId = jwtUtil.extractProfessorId(token);
        return ResponseEntity.ok(reservationService.findByProfessorId(professorId));
    }
    
    @GetMapping("/professor/count")
    public ResponseEntity<Object> getReservationsCountByProfessor(@RequestHeader("Authorization") String authHeader) {
        // Add this method to your service and repository
    	String token = authHeader.replace("Bearer ", "");
        Long professorId = jwtUtil.extractProfessorId(token);
        
        
        return ResponseEntity.ok(reservationService.getProfessorReservationsStast(professorId));
    }
    
    
    @PostMapping("/professor")
    public ResponseEntity<Reservation> saveReservation(@RequestHeader("Authorization") String authHeader,@RequestBody Map<String, String> details) {
        
    	
    	String token = authHeader.replace("Bearer ", "");
        Long professorId = jwtUtil.extractProfessorId(token);
        
    	String uni_id = details.get("university_id");
    	String status = details.get("status");
    	String start_time = details.get("start_time");
    	String end_time = details.get("end_time");
    	
    	
    	String resource_type = details.get("type");
    	String vcpu = details.get("vcpu");
    	String ram = details.get("ram");

    	String storage = details.get("storage");
    	String image = details.get("image");
    	
    	Quota quota = new Quota();
    	
	    Optional<University> uniOpt = uniService.getById(uni_id);

    	
    	quota = uniOpt.get().getQuota();
    	
    	DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

	    LocalDateTime startTime;
	    LocalDateTime endTime;
	    try {
	        startTime = LocalDateTime.parse(start_time, formatter);
	        endTime = LocalDateTime.parse(end_time, formatter);
	    } catch (DateTimeParseException e) {
	        return ResponseEntity.badRequest().body(null); // Invalid date format
	    }
	    
	    
    	
    	if (reservationService.canFitReservation(uniOpt.get(), startTime, endTime, Integer.parseInt(vcpu), Integer.parseInt(ram), Integer.parseInt(storage))) {

    		
    	
    	CloudResource cldResource = new CloudResource();
    	cldResource.setType("ECS");
    	cldResource.setVcpu(Integer.parseInt(vcpu));
    	cldResource.setRam(Integer.parseInt(ram));
    	cldResource.setStorage(Integer.parseInt(storage));
    	cldResource.setImage(image);
    	
    	cldResourceService.saveResource(cldResource);

    	
    	

    	    Optional<Professor> professorOpt = professorService.findById(professorId);
    	    if (!professorOpt.isPresent()) {
    	        return ResponseEntity.notFound().build(); // Professor not found
    	    }

    	    Reservation reservation = new Reservation();
    	    reservation.setProfessor(professorOpt.get());
    	    reservation.setStartTime(startTime);
    	    reservation.setEndTime(endTime);
    	    reservation.setStatus(ApprovalStatus.APPROVED_CCK);

    	    // Set resource and university (fetch them if needed, here they're null for simplicity)
    	    reservation.setResource(cldResource);
    	    if (!uniOpt.isPresent()) {
    	        return ResponseEntity.notFound().build(); // Professor not found
    	    }
    	    reservation.setUniversity(uniOpt.get());

    	    Reservation saved = reservationService.addReservation(reservation);
    	    
    	    //schedule creation and deletion if is not now 
    	    LocalDateTime now = LocalDateTime.now();
    	    boolean alreadyCreated = Boolean.parseBoolean(details.getOrDefault("startImmediately", "false"));

    	    if (startTime.isAfter(now) && !alreadyCreated) {
    	        // Schedule VM creation
    	        ScheduledAction createAction = new ScheduledAction();
    	        createAction.setReservationId(saved.getId());
    	        createAction.setScheduledTime(startTime);
    	        createAction.setActionType(ScheduledAction.ActionType.CREATE);
    	        createAction.setStatus(ScheduledAction.ActionStatus.PENDING);
    	        scheduleActionRepository.save(createAction);
    	    }
    	    
    	    ScheduledAction deleteAction = new ScheduledAction();
    	    deleteAction.setReservationId(reservation.getId());
    	    deleteAction.setScheduledTime(endTime);
    	    deleteAction.setActionType(ScheduledAction.ActionType.DELETE);
    	    deleteAction.setStatus(ScheduledAction.ActionStatus.PENDING);
    	    scheduleActionRepository.save(deleteAction);
    	    
    	    return ResponseEntity.ok(saved);
    	    
    	}
    	
    	else {
    	    return ResponseEntity
    	            .status(HttpStatus.FORBIDDEN)
    	            .build();
    	}// Or use .body(null) if you prefer
    }
    
    
    
    @PostMapping("/professor/payg")
    public ResponseEntity<Reservation> saveReservationPyag(@RequestHeader("Authorization") String authHeader,@RequestBody Map<String, String> details) {
        
    	String token = authHeader.replace("Bearer ", "");
        Long professorId = jwtUtil.extractProfessorId(token);
        
    	String uni_id = details.get("university_id");
    	String status = details.get("status");
    	String start_time = details.get("start_time");
    	String end_time = details.get("end_time");
    	
    	
    	String resource_type = details.get("type");
    	String vcpu = details.get("vcpu");
    	String ram = details.get("ram");

    	String storage = details.get("storage");
    	String image = details.get("image");
    	
    	String image_id = details.get("imageId");
    	String flavor_id = details.get("flavorId");

    	Quota quota = new Quota();
    	
	    Optional<University> uniOpt = uniService.getById(uni_id);

    	
    	quota = uniOpt.get().getQuota();
    	
    	DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

	    LocalDateTime startTime;
	    LocalDateTime endTime;
	    try {
	        startTime = LocalDateTime.parse(start_time, formatter);
	        endTime = LocalDateTime.parse(end_time, formatter);
	    } catch (DateTimeParseException e) {
	        return ResponseEntity.badRequest().body(null); // Invalid date format
	    }
	    
	    
    	
    	

    		
    	
    	
    	CloudResource cldResource = new CloudResource();
    	cldResource.setType("ECS");
    	cldResource.setVcpu(Integer.parseInt(vcpu));
    	cldResource.setRam(Integer.parseInt(ram));
    	cldResource.setStorage(Integer.parseInt(storage));
    	cldResource.setImage(image);
    	cldResource.setImageId(image_id);
    	cldResource.setFlavorId(flavor_id);
    	cldResource.setPricePerHour(0.2f);
    	
    	cldResourceService.saveResource(cldResource);

    	
    	

    	    Optional<Professor> professorOpt = professorService.findById(professorId);
    	    if (!professorOpt.isPresent()) {
    	        return ResponseEntity.notFound().build(); // Professor not found
    	    }

    	    Reservation reservation = new Reservation();
    	    reservation.setProfessor(professorOpt.get());
    	    reservation.setStartTime(startTime);
    	    reservation.setEndTime(endTime);
    	    reservation.setStatus(ApprovalStatus.PENDING_UNIVERSITY);
    	    reservation.setReservationType(ReservationType.PAYG);

    	    // Set resource and university (fetch them if needed, here they're null for simplicity)
    	    reservation.setResource(cldResource);
    	    if (!uniOpt.isPresent()) {
    	        return ResponseEntity.notFound().build(); // Professor not found
    	    }
    	    reservation.setUniversity(uniOpt.get());

    	    Reservation saved = reservationService.addReservation(reservation);
    	    ScheduledAction deleteAction = new ScheduledAction();
    	    deleteAction.setReservationId(reservation.getId());
    	    deleteAction.setScheduledTime(endTime);
    	    deleteAction.setActionType(ScheduledAction.ActionType.DELETE);
    	    deleteAction.setStatus(ScheduledAction.ActionStatus.PENDING);
    	    scheduleActionRepository.save(deleteAction);
    	    return ResponseEntity.ok(saved);
    	    
    
    }
    
    @PutMapping("/{id}/vm")
    public ResponseEntity<?> updateReservationWithVmId(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(id);

        if (!reservationOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reservation not found");
        }

        String vmId = body.get("vm_id");
        Reservation reservation = reservationOpt.get();
        CloudResource cldResource = reservation.getResource();
        cldResource.setExternalId(vmId); // Make sure your Reservation entity has this field
        reservationService.addReservation(reservation); // Save the update

        return ResponseEntity.ok("Reservation updated with VM ID");
    }
    
    @GetMapping("/universities/{universityId}/unavailable-times")
    public ResponseEntity<List<UnavailableTimeSlot>> getUnavailableTimeSlots(
            @PathVariable String universityId,
            @RequestParam int vcpu,
            @RequestParam int ram,
            @RequestParam int storage,
            @RequestParam String from,
            @RequestParam String to) {

        Optional<University> uniOpt = uniService.getById(universityId);
        if (uniOpt.isEmpty()) return ResponseEntity.notFound().build();

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime fromTime = LocalDateTime.parse(from, formatter);
        LocalDateTime toTime = LocalDateTime.parse(to, formatter);

        // Get all existing reservations for this university
        List<Reservation> allReservations = reservationService.findByUniversityId(universityId);
        
        // Convert existing reservations to unavailable slots
        List<UnavailableTimeSlot> existingReservationSlots = allReservations.stream()
            .filter(r -> r.getStatus() != ApprovalStatus.REJECTED_CCK && 
                         r.getStatus() != ApprovalStatus.REJECTED_UNIVERSITY)
            .map(r -> new UnavailableTimeSlot(r.getStartTime(), r.getEndTime()))
            .collect(Collectors.toList());

        // Get resource conflict slots
        List<UnavailableTimeSlot> resourceConflicts = reservationService.getUnavailableSlots(
            uniOpt.get(), fromTime, toTime, vcpu, ram, storage);

        // Combine both lists
        List<UnavailableTimeSlot> allUnavailable = new ArrayList<>();
        allUnavailable.addAll(existingReservationSlots);
        allUnavailable.addAll(resourceConflicts);

        return ResponseEntity.ok(allUnavailable);
    }
    
    @GetMapping("/universities/{universityId}/availability-check")
    public ResponseEntity<Map<String, Object>> checkAvailability(
            @PathVariable String universityId,
            @RequestParam int vcpu,
            @RequestParam int ram,
            @RequestParam int storage,
            @RequestParam String from,
            @RequestParam String to) {

        Optional<University> uniOpt = uniService.getById(universityId);
        if (uniOpt.isEmpty()) return ResponseEntity.notFound().build();

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime fromTime = LocalDateTime.parse(from, formatter);
        LocalDateTime toTime = LocalDateTime.parse(to, formatter);

        University university = uniOpt.get();
        Quota quota = university.getQuota();
        
        Map<String, Object> response = new HashMap<>();
        
        // First check if requested resources exceed quota limits
        boolean quotaExceeded = vcpu > quota.getVcpu() || 
                               ram > quota.getRamInMb() || 
                               storage > quota.getStorageInGb();
        
        if (quotaExceeded) {
            response.put("quotaExceeded", true);
            response.put("timeConflict", false);
            response.put("message", "Requested resources exceed university quota limits");
            response.put("unavailableSlots", new ArrayList<>());
            return ResponseEntity.ok(response);
        }
        
        // If quota is fine, check for time conflicts
        List<UnavailableTimeSlot> unavailableSlots = reservationService.getUnavailableSlots(
            university, fromTime, toTime, vcpu, ram, storage);
        
        response.put("quotaExceeded", false);
        response.put("timeConflict", !unavailableSlots.isEmpty());
        response.put("unavailableSlots", unavailableSlots);
        response.put("message", unavailableSlots.isEmpty() ? "Available" : "Time conflicts found");
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/report/professor")
    public Map<String, Object> getProfessorReport(
    	@RequestHeader("Authorization") String authHeader,
        @RequestParam int month,
        @RequestParam int year) {
        
    	
    	String token = authHeader.replace("Bearer ", "");
        Long professorId = jwtUtil.extractProfessorId(token);
        
        return reservationService.getProfessorReport(professorId, month, year);
    }
    
   
    
    
    @GetMapping("/count/{universityId}/{month}/{year}")
    public ResponseEntity<Integer> countReservationsByMonthAndYear(
            @PathVariable String universityId,
            @PathVariable int month,
            @PathVariable int year) {
        
        return ResponseEntity.ok(reservationService.countReservationsByMonthAndYear(universityId, month, year));
    }
    
   /* @PutMapping("/{reservationId}/status")
    public ResponseEntity<Map<String, String>> updateReservationStatus(
            @PathVariable Long reservationId,
            @RequestBody String status) {

        Map<String, String> response = new HashMap<>();
        
        try {
            reservationService.updateReservationStatus(reservationId, status);
            response.put("message", "Reservation status updated successfully");
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
        } catch (Exception e) {
            response.put("message", "Failed to update reservation status: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    } */
    
    @PutMapping("/{reservationId}/status")
    public ResponseEntity<Map<String, String>> updateReservationStatus(
            @PathVariable Long reservationId,
            @RequestBody Map<String, String> body) {

        Map<String, String> response = new HashMap<>();

        String statusStr = body.get("status");
        if (statusStr == null) {
            response.put("message", "Status is required");
            return ResponseEntity.badRequest().body(response);
        }

        ApprovalStatus newStatus;
        try {
            newStatus = ApprovalStatus.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            response.put("message", "Invalid status value");
            return ResponseEntity.badRequest().body(response);
        }
        
        Long adminId = body.get("adminId") != null ? Long.parseLong(body.get("adminId")) : null;
        


        try {
            switch (newStatus) {
                case APPROVED_UNIVERSITY:
                	Optional<UniversityAdmin> admin = universityService.getAdminById(adminId);
                    reservationService.approveByUniversity(reservationId,admin.get());
                    break;
                case REJECTED_UNIVERSITY:
                    // You might want to pass a rejection reason from the request
                	Optional<UniversityAdmin> admin2 = universityService.getAdminById(adminId);
                    reservationService.rejectByUniversity(reservationId, "No reason provided",admin2.get());
                    break;
                case APPROVED_CCK:
                	Optional<CckAdmin> admin3 = cckService.getAdminById(adminId);
                    reservationService.approveByCCK(reservationId,admin3.get());
                    Optional<Reservation> reservation = reservationRepository.findById(reservationId);
                    
                    CloudResource cldResource = reservation.get().getResource();

                    // Build payload
                    Map<String, Object> payload = new HashMap<>();
                    payload.put("flavorId", cldResource.getFlavorId());
                    payload.put("imageId", cldResource.getImageId());
                    payload.put("storage", cldResource.getStorage());
                    payload.put("id", reservation.get().getProfessor().getId());
                    payload.put("email", reservation.get().getProfessor().getEmail());

                    // Optional: include user details if needed in the Flask route
                    // payload.put("email", reservation.getUser().getEmail());
            	    LocalDateTime now = LocalDateTime.now();
                    if (reservation.get().getStartTime().isAfter(now)) {
            	        // Schedule VM creation
            	        ScheduledAction createAction = new ScheduledAction();
            	        createAction.setReservationId(reservation.get().getId());
            	        createAction.setScheduledTime(reservation.get().getStartTime());
            	        createAction.setActionType(ScheduledAction.ActionType.CREATE);
            	        createAction.setStatus(ScheduledAction.ActionStatus.PENDING);
            	        scheduleActionRepository.save(createAction);
            	    }
                    
                    else {

                    try {
                        HttpClient client = HttpClient.newHttpClient();
                        ObjectMapper objectMapper = new ObjectMapper();
                        String requestBody = objectMapper.writeValueAsString(payload);

                        HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create("http://localhost:5002/admin/createVM"))  // change to Flask server URL
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                            .build();

                        HttpResponse<String> responseVM = client.send(request, HttpResponse.BodyHandlers.ofString());

                        System.out.println("VM creation response: " + responseVM.body());
                        if (responseVM.statusCode() == 200) {
                            // Parse the JSON response to extract VM ID
                            JsonNode responseJson = objectMapper.readTree(responseVM.body());
                            String vmId = responseJson.get("vm_id").asText();
                            cldResource.setExternalId(vmId);
                        }
                        
                        EcsUsage usage = new EcsUsage();
                        usage.setCloudResource(cldResource); // assuming a @ManyToOne link
                        usage.setStartTime(LocalDateTime.now());
						ecsUsageRepository.save(usage);

                    } catch (Exception e) {
                        System.err.println("Failed to trigger VM creation: " + e.getMessage());
                    }
                   }
                    
                    break;
                case REJECTED_CCK:
                	Optional<CckAdmin> admin4 = cckService.getAdminById(adminId);
                    reservationService.rejectByCCK(reservationId, "No reason provided",admin4.get());
                    break;
                default:
                    response.put("message", "Status update not supported for: " + newStatus);
                    return ResponseEntity.badRequest().body(response);
            }

            response.put("message", "Reservation status updated successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Failed to update reservation status: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    
}