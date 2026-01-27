package com.rl.blogmanagementsystem.service;

import com.rl.blogmanagementsystem.dto.AuthResponse;
import com.rl.blogmanagementsystem.dto.SignupRequest;
import com.rl.blogmanagementsystem.entity.User;
import com.rl.blogmanagementsystem.enums.Role;
import com.rl.blogmanagementsystem.repository.UserRepository;
import com.rl.blogmanagementsystem.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthResponse signup(SignupRequest signupRequest) {
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new RuntimeException("User already exists");
        }

        User user = new User();
        user.setName(signupRequest.getName());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);
        String token = jwtUtil.generateToken(savedUser);
        return new AuthResponse(token);
    }
}
