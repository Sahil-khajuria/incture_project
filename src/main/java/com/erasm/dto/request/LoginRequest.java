package com.erasm.dto.request;
import jakarta.validation.constraints.*; import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class LoginRequest { @NotBlank @Email private String email; @NotBlank private String password; }
