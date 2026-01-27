package com.rl.blogmanagementsystem.controller;

import com.rl.blogmanagementsystem.dto.AuthResponse;
import com.rl.blogmanagementsystem.dto.SignupRequest;
import com.rl.blogmanagementsystem.service.AuthService;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Test
    void signupShouldReturn201() throws Exception {

        AuthResponse res = new AuthResponse("jwt-token");
        when(authService.signup(any(SignupRequest.class)))
                .thenReturn(res);

        mockMvc.perform(
                post("/api/auth/signup")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content("""
                                    {
                                        "name": "Test",
                                        "email": "test@gmail.com",
                                        "password": "1234"
                                    }
                                """
                        )
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("jwt-token"));

    }
}
