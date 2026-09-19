package com.nilay.support.service;

import com.nilay.support.dto.request.LoginRequest;
import com.nilay.support.dto.request.RegisterRequest;
import com.nilay.support.dto.response.AuthResponse;
import com.nilay.support.model.Role;
import com.nilay.support.model.User;
import com.nilay.support.repository.UserRepository;
import com.nilay.support.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        // Step 1: Check if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        // Step 2: Build user with hashed password
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Step 3: Set role (default CUSTOMER if not provided)
        if (request.getRole() != null) {
            user.setRole(Role.valueOf(request.getRole().toUpperCase()));
        } else {
            user.setRole(Role.CUSTOMER);
        }

        // Step 4: Save to database
        userRepository.save(user);

        // Step 5: Generate token and return
        String token = jwtTokenProvider.generateToken(user.getEmail());
        return new AuthResponse(token, user.getRole().name());
    }

    public AuthResponse login(LoginRequest request) {
        // AuthenticationManager verifies email + password in one call
        // Throws exception automatically if wrong credentials
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // If we reach here, credentials are valid
        // Load user to get their role
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate and return token
        String token = jwtTokenProvider.generateToken(user.getEmail());
        return new AuthResponse(token, user.getRole().name());
    }
}
