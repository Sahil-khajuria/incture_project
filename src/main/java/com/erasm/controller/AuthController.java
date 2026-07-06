package com.erasm.controller;
import com.erasm.dto.request.*; import com.erasm.dto.response.*; import com.erasm.service.AuthService; import jakarta.validation.Valid; import org.springframework.http.*; import org.springframework.security.core.Authentication; import org.springframework.web.bind.annotation.*; import java.util.Map;

@RestController @RequestMapping("/auth") public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService s) { this.authService = s; }

    @PostMapping("/register") public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest r) { return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Registered", authService.register(r))); }
    @PostMapping("/login") public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest r) { return ResponseEntity.ok(ApiResponse.success("Login successful", authService.login(r))); }
    @PostMapping("/logout") public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader("Authorization") String h, Authentication a) { authService.logout(h.replace("Bearer ",""), a.getName()); return ResponseEntity.ok(ApiResponse.success("Logged out", null)); }
    @PostMapping("/change-password") public ResponseEntity<ApiResponse<Void>> changePw(@Valid @RequestBody ChangePasswordRequest r, Authentication a) { authService.changePassword(a.getName(), r); return ResponseEntity.ok(ApiResponse.success("Password changed", null)); }
    @PostMapping("/refresh-token") public ResponseEntity<ApiResponse<AuthResponse>> refresh(@RequestBody Map<String,String> b) { return ResponseEntity.ok(ApiResponse.success("Refreshed", authService.refreshToken(b.get("refreshToken")))); }
}
