package com.example.Appointment.security;

import com.example.Appointment.domain.User;
import com.example.Appointment.repositories.UserRepository;
import com.example.Appointment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author ginger1998
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       User user=userRepository.findByEmail(email);
       if(user==null){
           throw new UsernameNotFoundException("User with email "+email+" not found");
       }
        return user;
    }
}
