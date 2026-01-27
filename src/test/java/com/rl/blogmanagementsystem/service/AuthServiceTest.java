package com.rl.blogmanagementsystem.service;

import com.rl.blogmanagementsystem.dto.AuthResponse;
import com.rl.blogmanagementsystem.dto.LoginRequest;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

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

    @Test
    void loginShouldReturnToken() {
        LoginRequest loginRequest = new LoginRequest(
                "test@gmail.com",
                "1234"
        );

        User user = new User();
        user.setEmail("test@gmail.com");

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        when(jwtUtil.generateToken(any()))
                .thenReturn("jwt-token");

        AuthResponse res = authService.login(loginRequest);

        assertNotNull(res);
        assertEquals("jwt-token", res.getToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void loginShouldThrowExceptionWhenCredentialsWrong() {
        LoginRequest loginRequest = new LoginRequest("unknown@gmail.com", "1234");

        when(authenticationManager.authenticate(any()))
                .thenThrow(
                        new BadCredentialsException("Invalid credentials")
                );

        verify(userRepository, never()).findByEmail(any());

        BadCredentialsException exception = assertThrows(BadCredentialsException.class,
                () -> authService.login(loginRequest)
        );

        assertEquals("Invalid credentials", exception.getMessage());
    }
}
