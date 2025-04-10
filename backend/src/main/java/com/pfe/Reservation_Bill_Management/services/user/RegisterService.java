package com.pfe.Reservation_Bill_Management.services.user;

import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.pfe.Reservation_Bill_Management.dao.UserDAO;

import com.pfe.Reservation_Bill_Management.entities.*;

@org.springframework.stereotype.Service
public class RegisterService {
	
	@Autowired
	UserDAO userdao;
	
	public void addUser(User user) {
		userdao.save(user);
	}

}
