package com.example.springsecurity.Service;

import com.example.springsecurity.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    UserDetailsService userDetailsService();

    long saveUser(User user);

    User findByEmail(String email);
}
