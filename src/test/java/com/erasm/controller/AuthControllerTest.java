package com.erasm.controller;

import com.erasm.dto.request.LoginRequest;
import com.erasm.dto.request.RegisterRequest;
import com.erasm.dto.response.AuthResponse;
import com.erasm.exception.DuplicateResourceException;
import com.erasm.exception.GlobalExceptionHandler;
import com.erasm.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private AuthService authService;
    @MockBean private com.erasm.security.JwtTokenProvider jwtTokenProvider;
    @MockBean private com.erasm.security.CustomUserDetailsService customUserDetailsService;
    @MockBean private com.erasm.repository.BlacklistedTokenRepository blacklistedTokenRepository;

    @Test @DisplayName("POST /auth/register - success 201")
    void register_Success() throws Exception {
        RegisterRequest req = RegisterRequest.builder()
                .email("new@erasm.com").password("Pass@1234").role("EMPLOYEE").build();
        AuthResponse resp = AuthResponse.builder()
                .accessToken("token").refreshToken("refresh").tokenType("Bearer")
                .expiresIn(900000).email("new@erasm.com").role("EMPLOYEE").build();
        when(authService.register(any())).thenReturn(resp);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").value("token"))
                .andExpect(jsonPath("$.data.email").value("new@erasm.com"))
                .andExpect(jsonPath("$.data.role").value("EMPLOYEE"));
    }

    @Test @DisplayName("POST /auth/register - duplicate email 409")
    void register_DuplicateEmail() throws Exception {
        RegisterRequest req = RegisterRequest.builder()
                .email("exists@erasm.com").password("Pass@1234").role("EMPLOYEE").build();
        when(authService.register(any())).thenThrow(new DuplicateResourceException("Email already registered"));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("DUPLICATE_RESOURCE"));
    }

    @Test @DisplayName("POST /auth/register - validation error 400")
    void register_ValidationError() throws Exception {
        // Missing required fields
        String invalidJson = "{\"email\":\"\",\"password\":\"\",\"role\":\"\"}";

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test @DisplayName("POST /auth/login - success 200")
    void login_Success() throws Exception {
        LoginRequest req = LoginRequest.builder()
                .email("test@erasm.com").password("Pass@1234").build();
        AuthResponse resp = AuthResponse.builder()
                .accessToken("token").refreshToken("refresh").tokenType("Bearer")
                .expiresIn(900000).email("test@erasm.com").role("ADMIN").build();
        when(authService.login(any())).thenReturn(resp);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").value("token"))
                .andExpect(jsonPath("$.data.role").value("ADMIN"));
    }

    @Test @DisplayName("POST /auth/login - bad credentials 401")
    void login_BadCredentials() throws Exception {
        LoginRequest req = LoginRequest.builder()
                .email("test@erasm.com").password("wrong").build();
        when(authService.login(any())).thenThrow(new BadCredentialsException("Invalid password"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test @DisplayName("POST /auth/refresh-token - success 200")
    void refreshToken_Success() throws Exception {
        AuthResponse resp = AuthResponse.builder()
                .accessToken("new-token").refreshToken("refresh").tokenType("Bearer")
                .expiresIn(900000).email("test@erasm.com").role("ADMIN").build();
        when(authService.refreshToken(any())).thenReturn(resp);

        mockMvc.perform(post("/auth/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"refreshToken\":\"refresh\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").value("new-token"));
    }
}
