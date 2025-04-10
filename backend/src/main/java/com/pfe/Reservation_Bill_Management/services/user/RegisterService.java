package com.pfe.Reservation_Bill_Management.services.user;

import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.pfe.Reservation_Bill_Management.dao.UserDAO;

import com.pfe.Reservation_Bill_Management.entities.*;

@org.springframework.stereotype.Service
public class RegisterService {
	
	@Autowired
	UserDAO userdao;
	
<<<<<<< HEAD
	public boolean registerUser(String username, String email, String password) {
		if(userdao.findByEmail(email) == null) {
			User user = new User();
			user.setUsername(username);
	        user.setEmail(email);
	        user.setPassword(password);
			userdao.save(user);
			return true;
		}
		return false;
=======
	public void addUser(User user) {
		userdao.save(user);
>>>>>>> db9f408 (Latest Changes)
	}

}
