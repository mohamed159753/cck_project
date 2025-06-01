package com.pfe.Reservation_Bill_Management.services.user;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pfe.Reservation_Bill_Management.dao.CloudResourceRepository;
import com.pfe.Reservation_Bill_Management.dao.EcsUsageRepository;
import com.pfe.Reservation_Bill_Management.dao.ReservationRepository;
import com.pfe.Reservation_Bill_Management.entities.CloudResource;
import com.pfe.Reservation_Bill_Management.entities.EcsUsage;
import com.pfe.Reservation_Bill_Management.entities.Reservation;

@Service
public class CloudResourceService {
	
	private final ReservationRepository reservationRepository;
    
    private final CloudResourceRepository cloudResourceRepository;
    
    @Autowired
    private EcsUsageRepository ecsUsageRepository;
    
    
    @Autowired
    public CloudResourceService(CloudResourceRepository cloudResourceRepository, ReservationRepository reservationRepository) {
        this.cloudResourceRepository = cloudResourceRepository;
        this.reservationRepository = reservationRepository;
    }
    
    public List<CloudResource> getAllResources() {
        return cloudResourceRepository.findAll();
    }
    
    public Optional<CloudResource> getResourceById(Long id) {
        return cloudResourceRepository.findById(id);
    }
    
    public List<CloudResource> getResourcesByType(String type) {
        return cloudResourceRepository.findByType(type);
    }
    
    public CloudResource saveResource(CloudResource resource) {
        return cloudResourceRepository.save(resource);
    }
    
    public void deleteResource(Long id) {
        cloudResourceRepository.deleteById(id);
    }
    
    public List<CloudResource> getActiveEcs(Long ProfessorId) {
        return reservationRepository.findActiveResources(ProfessorId, LocalDateTime.now());
    }
    
    public List<CloudResource> getActivePaygEcs() {
        return reservationRepository.findActivePaygResources(LocalDateTime.now());
    }
    
    

   

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void createEcsPaygForReservation(Long reservationId) throws Exception {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        Map<String, String> payload = new HashMap<>();
        payload.put("storage", String.valueOf(reservation.getResource().getStorage()));
        payload.put("imageId", reservation.getResource().getImageId());
        payload.put("flavorId", reservation.getResource().getFlavorId());

        String requestBody = objectMapper.writeValueAsString(payload);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:5002/admin/createVM"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> responseVM = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (responseVM.statusCode() == 200) {
            JsonNode responseJson = objectMapper.readTree(responseVM.body());
            String vmId = responseJson.get("vm_id").asText();
            CloudResource cldResource = reservation.getResource();

            cldResource.setExternalId(vmId);
            reservationRepository.save(reservation);

            // Optional: Register usage
            EcsUsage usage = new EcsUsage();
            usage.setCloudResource(reservation.getResource());
            usage.setStartTime(LocalDateTime.now());
            ecsUsageRepository.save(usage);
        } else {
            throw new RuntimeException("Flask VM creation failed: " + responseVM.body());
        }
    }
    
    public void createEcsQuotaForReservation(Long reservationId) throws Exception {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        Map<String, String> payload = new HashMap<>();
        payload.put("storage", String.valueOf(reservation.getResource().getStorage()));
        payload.put("imageId", reservation.getResource().getImageId());
        payload.put("flavorId", reservation.getResource().getFlavorId());

        String requestBody = objectMapper.writeValueAsString(payload);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:5002/admin/createVM"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> responseVM = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (responseVM.statusCode() == 200) {
            JsonNode responseJson = objectMapper.readTree(responseVM.body());
            String vmId = responseJson.get("vm_id").asText();
            CloudResource cldResource = reservation.getResource();

            cldResource.setExternalId(vmId);
            reservationRepository.save(reservation);

            
        } else {
            throw new RuntimeException("Flask VM creation failed: " + responseVM.body());
        }
    }

    public void deleteVMForReservation(Long reservationId) throws Exception {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        
        CloudResource cldResource = reservation.getResource();

        String vmId = cldResource.getExternalId();
        if (vmId == null || vmId.isEmpty()) {
            throw new RuntimeException("VM ID is missing");
        }

        Map<String, String> payload = new HashMap<>();
        payload.put("vm_id", vmId);

        String requestBody = objectMapper.writeValueAsString(payload);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:5002/admin/deleteVM"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
        	//cldResource.setExternalId(null);// clear after deletion
            reservationRepository.save(reservation);
        } else {
            throw new RuntimeException("Flask VM deletion failed: " + response.body());
        }
    }
    
    
    /*
    public boolean checkAvailability(Long resourceId) {
        Optional<CloudResource> resource = cloudResourceRepository.findById(resourceId);
        return resource.map(CloudResource::check_availability).orElse(false);
    }
    
    public void updatePrice(Long resourceId, float newPrice) {
        Optional<CloudResource> resourceOpt = cloudResourceRepository.findById(resourceId);
        if (resourceOpt.isPresent()) {
            CloudResource resource = resourceOpt.get();
            resource.update_price(newPrice);
            cloudResourceRepository.save(resource);
        }
    }*/
}
