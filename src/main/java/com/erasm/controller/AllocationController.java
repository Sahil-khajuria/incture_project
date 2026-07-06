package com.erasm.controller;
import com.erasm.dto.request.AllocationRequest; import com.erasm.dto.response.*; import com.erasm.service.AllocationService; import jakarta.validation.Valid; import org.springframework.data.domain.Page; import org.springframework.data.domain.Pageable; import org.springframework.http.*; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.security.core.Authentication; import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/allocations") public class AllocationController {
    private final AllocationService svc; public AllocationController(AllocationService s) { svc=s; }
    @GetMapping @PreAuthorize("hasAnyRole('RESOURCE_MANAGER','ADMIN')") public ResponseEntity<ApiResponse<Page<AllocationResponse>>> getAll(Pageable p) { return ResponseEntity.ok(ApiResponse.success(svc.getAllAllocations(p))); }
    @GetMapping("/{id}") @PreAuthorize("hasAnyRole('RESOURCE_MANAGER','ADMIN')") public ResponseEntity<ApiResponse<AllocationResponse>> getById(@PathVariable Long id) { return ResponseEntity.ok(ApiResponse.success(svc.getAllocationById(id))); }
    @PostMapping @PreAuthorize("hasRole('RESOURCE_MANAGER')") public ResponseEntity<ApiResponse<AllocationResponse>> allocate(@Valid @RequestBody AllocationRequest r, Authentication a) { return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(svc.allocate(r,a.getName()))); }
    @PutMapping("/{id}") @PreAuthorize("hasRole('RESOURCE_MANAGER')") public ResponseEntity<ApiResponse<AllocationResponse>> reallocate(@PathVariable Long id, @Valid @RequestBody AllocationRequest r) { return ResponseEntity.ok(ApiResponse.success(svc.reallocate(id,r))); }
    @PutMapping("/{id}/release") @PreAuthorize("hasRole('RESOURCE_MANAGER')") public ResponseEntity<ApiResponse<AllocationResponse>> release(@PathVariable Long id) { return ResponseEntity.ok(ApiResponse.success(svc.release(id))); }
}
