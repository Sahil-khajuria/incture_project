package com.erasm.controller;
import com.erasm.dto.response.*; import com.erasm.service.UserService; import org.springframework.data.domain.Page; import org.springframework.data.domain.Pageable; import org.springframework.http.ResponseEntity; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*; import java.util.Map;

@RestController @RequestMapping("/users") @PreAuthorize("hasRole('ADMIN')") public class UserController {
    private final UserService svc; public UserController(UserService s) { svc=s; }
    @GetMapping public ResponseEntity<ApiResponse<Page<UserResponse>>> getAll(Pageable p) { return ResponseEntity.ok(ApiResponse.success(svc.getAllUsers(p))); }
    @GetMapping("/{id}") public ResponseEntity<ApiResponse<UserResponse>> getById(@PathVariable Long id) { return ResponseEntity.ok(ApiResponse.success(svc.getUserById(id))); }
    @PutMapping("/{id}") public ResponseEntity<ApiResponse<UserResponse>> update(@PathVariable Long id, @RequestBody Map<String,String> b) { return ResponseEntity.ok(ApiResponse.success(svc.updateUser(id,b.get("email"),b.get("role")))); }
    @DeleteMapping("/{id}") public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) { svc.softDeleteUser(id); return ResponseEntity.ok(ApiResponse.success("Deleted",null)); }
    @PutMapping("/{id}/assign-role") public ResponseEntity<ApiResponse<UserResponse>> assignRole(@PathVariable Long id, @RequestBody Map<String,String> b) { return ResponseEntity.ok(ApiResponse.success(svc.assignRole(id,b.get("role")))); }
    @PutMapping("/{id}/activate") public ResponseEntity<ApiResponse<UserResponse>> activate(@PathVariable Long id) { return ResponseEntity.ok(ApiResponse.success(svc.activateUser(id))); }
    @PutMapping("/{id}/deactivate") public ResponseEntity<ApiResponse<UserResponse>> deactivate(@PathVariable Long id) { return ResponseEntity.ok(ApiResponse.success(svc.deactivateUser(id))); }
}
