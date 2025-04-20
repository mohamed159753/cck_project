package com.pfe.Reservation_Bill_Management.services.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pfe.Reservation_Bill_Management.dao.CloudResourceRepository;
import com.pfe.Reservation_Bill_Management.entities.CloudResource;

@Service
public class CloudResourceService {
    
    private final CloudResourceRepository cloudResourceRepository;
    
    @Autowired
    public CloudResourceService(CloudResourceRepository cloudResourceRepository) {
        this.cloudResourceRepository = cloudResourceRepository;
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
