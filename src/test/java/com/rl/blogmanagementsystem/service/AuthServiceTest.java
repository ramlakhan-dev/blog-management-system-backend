package com.rl.blogmanagementsystem.service;

import com.rl.blogmanagementsystem.dto.AuthResponse;
import com.rl.blogmanagementsystem.dto.SignupRequest;
import com.rl.blogmanagementsystem.entity.User;
import com.rl.blogmanagementsystem.repository.UserRepository;
import com.rl.blogmanagementsystem.util.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @Test
    void signupShouldCreateUser() {
        SignupRequest signupRequest = new SignupRequest(
                "Test",
                "test@gmail.com",
                "1234"
        );

        when(userRepository.existsByEmail(signupRequest.getEmail()))
                .thenReturn(false);

        when(passwordEncoder.encode(signupRequest.getPassword()))
                .thenReturn("encodedPassword");

        when(jwtUtil.generateToken(any()))
                .thenReturn("jwt-token");

        AuthResponse res = authService.signup(signupRequest);

        assertNotNull(res);
        Assertions.assertEquals("jwt-token", res.getToken());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void signupShouldThrowExceptionIfUserExists() {
        SignupRequest signupRequest = new SignupRequest(
                "Test",
                "test@gmail.com",
                "1234"
        );

        when(userRepository.existsByEmail(signupRequest.getEmail()))
                .thenReturn(true);

        Assertions.assertThrows(RuntimeException.class, () -> authService.signup(signupRequest));

        verify(userRepository, never()).save(any());
    }
}
