package com.pfe.Reservation_Bill_Management.services.user;

import java.util.Optional;
import java.util.UUID;

import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.pfe.Reservation_Bill_Management.dao.ProfessorRepository;
import com.pfe.Reservation_Bill_Management.dao.UniversityRepository;
import com.pfe.Reservation_Bill_Management.dao.UserDAO;

import com.pfe.Reservation_Bill_Management.entities.*;

@org.springframework.stereotype.Service
public class RegisterService {
	
	@Autowired
	private UserDAO userdao;
	
	@Autowired
	private UniversityRepository unidao;
	
	@Autowired
	private ProfessorRepository profdao;
	
	
	@Autowired
	private JavaMailSender mailSender;
	
	 @Autowired
	 private PasswordEncoder passwordEncoder;
	
	public boolean registerUser(String username, String email, String password, int cin, String uni_id, String institut) {
	    if (userdao.findByEmail(email) == null) {
	        Professor prof = new Professor();
	        prof.setUsername(username);
	        prof.setEmail(email);
	        prof.setCin(cin);
	        prof.setInstitute(institut);
	        prof.setPassword(passwordEncoder.encode(password));

	        Optional<University> universityOpt = unidao.findById(uni_id);
	        if (universityOpt.isPresent()) {
	            prof.setUniversity(universityOpt.get());
	        } else {
	            return false;
	        }

	        // Generate activation token
	        String token = UUID.randomUUID().toString();
	        prof.setActivationToken(token);
	        prof.setActivated(false);

	        userdao.save(prof);

	        // Send activation email
	        String activationLink = "https://1kjfg4hs-8080.uks1.devtunnels.ms/api/activate/" + token;
	        sendActivationEmail(email, activationLink);  // create this

	        return true;
	    }
	    return false;
	}
	
	
	public void sendActivationEmail(String toEmail, String activationLink) {
	    try {
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setFrom("RevBillingAdmin@gmail.com");
	        message.setTo(toEmail);
	        message.setSubject("Activate Your Account");
	        message.setText("Click the link to activate your account: " + activationLink);
	        mailSender.send(message);
	        System.out.println("Email sent to: " + toEmail);
	    } catch (Exception e) {
	        System.err.println("Failed to send email to " + toEmail);
	        e.printStackTrace();
	    }
	}
	
	public boolean activateUser(String token) {
        Professor user = profdao.findByActivationToken(token);
        if (user == null) {
            return false;
        }

        user.setActivated(true);
        user.setActivationToken(null);
        userdao.save(user);
        return true;
    }

	public void addUser(User user) {
		userdao.save(user);
	}
	

	

}
