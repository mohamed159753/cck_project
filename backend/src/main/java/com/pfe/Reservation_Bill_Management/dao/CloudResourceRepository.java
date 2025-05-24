package com.pfe.Reservation_Bill_Management.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pfe.Reservation_Bill_Management.entities.CloudResource;

@Repository
public interface CloudResourceRepository extends JpaRepository<CloudResource, Long> {
    List<CloudResource> findByType(String type);
    List<CloudResource> findByPricePerHourLessThan(float price);
}
