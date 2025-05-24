package com.pfe.Reservation_Bill_Management.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pfe.Reservation_Bill_Management.entities.User;
import java.util.List;


@Repository
public interface UserDAO extends JpaRepository<User, Long> {
	
	User findByUsername(String username);
	User findByEmail(String email);
}
