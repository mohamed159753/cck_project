package com.pfe.Reservation_Bill_Management.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.pfe.Reservation_Bill_Management.dao.UserDAO;
import com.pfe.Reservation_Bill_Management.entities.Professor;
import com.pfe.Reservation_Bill_Management.entities.User;

@Service
public class LoginService {

    private final UserDAO userdao;

    @Autowired
	private PasswordEncoder passwordEncoder;
    
    public LoginService(UserDAO userdao) {
        this.userdao = userdao;
    }

    public Boolean isValid(String email, String password) {

        User user = userdao.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return true;
        }

        return false;
    }
    
    
}
