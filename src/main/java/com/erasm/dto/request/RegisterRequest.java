package com.erasm.dto.request;
import jakarta.validation.constraints.*; import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class RegisterRequest {
    @NotBlank(message = "Email is required") @Email(message = "Invalid email format") private String email;
    @NotBlank(message = "Password is required") @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Password must be min 8 chars with uppercase, lowercase, digit, special char") private String password;
    @NotBlank(message = "Role is required") private String role;
}
