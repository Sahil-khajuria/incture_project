package com.erasm.dto.response;
import lombok.*; @Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AuthResponse { private String accessToken; private String refreshToken; private String tokenType; private long expiresIn; private String email; private String role; }
