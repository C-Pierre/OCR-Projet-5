package com.openclassrooms.mddapi.auth.controller;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.openclassrooms.mddapi.auth.request.LoginRequest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import com.openclassrooms.mddapi.auth.request.RegisterRequest;
import com.openclassrooms.mddapi.auth.jwt.response.JwtResponse;
import com.openclassrooms.mddapi.auth.service.AuthLoginService;
import com.openclassrooms.mddapi.common.response.MessageResponse;
import com.openclassrooms.mddapi.auth.service.AuthRegisterService;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthLoginService authLoginService;

    @MockBean
    private AuthRegisterService authRegisterService;

    @Test
    void authenticateUser_shouldReturnJwtResponse() throws Exception {
        LoginRequest request = new LoginRequest("user@mail.com", "Test!1234");

        JwtResponse jwtResponse = new JwtResponse(
            "fake-jwt-token",
            1L,
            "username",
            "user@mail.com"
        );

        when(authLoginService.execute(request)).thenReturn(jwtResponse);

        mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("fake-jwt-token"));
    }

    @Test
    void authenticateUser_invalidRequest_shouldReturnBadRequest() throws Exception {
        LoginRequest request = new LoginRequest("test", "");

        mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void registerUser_shouldReturnSuccessMessage() throws Exception {
        RegisterRequest request =
            new RegisterRequest("user", "user@mail.com", "Test!1234");

        when(authRegisterService.execute(request))
            .thenReturn(new MessageResponse("User registered successfully!"));

        mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message")
            .value("User registered successfully!"));
    }

    @Test
    void registerUser_invalidRequest_shouldReturnBadRequest() throws Exception {
        RegisterRequest request = new RegisterRequest("test", "test", "");

        mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }
}
