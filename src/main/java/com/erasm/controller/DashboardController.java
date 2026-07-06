package com.erasm.controller;
import com.erasm.dto.response.*; import com.erasm.service.AllocationService; import org.springframework.http.ResponseEntity; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*; import java.util.List;

@RestController public class DashboardController {
    private final AllocationService svc; public DashboardController(AllocationService s) { svc=s; }
    @GetMapping("/dashboard/utilization") @PreAuthorize("hasAnyRole('RESOURCE_MANAGER','ADMIN','DELIVERY_MANAGER')") public ResponseEntity<ApiResponse<List<UtilizationResponse>>> allUtil() { return ResponseEntity.ok(ApiResponse.success(svc.getAllUtilization())); }
    @GetMapping("/dashboard/utilization/{eid}") public ResponseEntity<ApiResponse<UtilizationResponse>> empUtil(@PathVariable Long eid) { return ResponseEntity.ok(ApiResponse.success(svc.getEmployeeUtilization(eid))); }
    @GetMapping("/dashboard/bench-summary") @PreAuthorize("hasAnyRole('RESOURCE_MANAGER','ADMIN')") public ResponseEntity<ApiResponse<List<UtilizationResponse>>> benchSummary() { return ResponseEntity.ok(ApiResponse.success(svc.getAllUtilization().stream().filter(UtilizationResponse::isOnBench).toList())); }
    @GetMapping("/employees/{id}/allocations") public ResponseEntity<ApiResponse<List<AllocationResponse>>> empAllocs(@PathVariable Long id) { return ResponseEntity.ok(ApiResponse.success(svc.getEmployeeAllocations(id))); }
}
