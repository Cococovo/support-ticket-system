package com.nilay.support.security;

import com.nilay.support.model.User;
import com.nilay.support.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
     private UserRepository userRepository;

     @Override
    public UserDetails loadUserByUsername (String username){

         User user = userRepository.findByEmail(username)
                 .orElseThrow(() -> new UsernameNotFoundException(
                         "User not found with email: " + username));

         UserDetails userDetails = org.springframework.security.core.userdetails.User
                 .withUsername(user.getEmail())
                 .password(user.getPassword())
                 .roles(user.getRole().name())
                 .build();

        return userDetails;
     }
}
