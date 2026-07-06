package com.erasm.controller;
import com.erasm.dto.response.*; import com.erasm.service.ReportService; import org.springframework.http.ResponseEntity; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*; import java.util.List;

@RestController @RequestMapping("/reports") public class ReportController {
    private final ReportService svc; public ReportController(ReportService s) { svc=s; }
    @GetMapping("/skills") @PreAuthorize("hasAnyRole('ADMIN','AUDITOR')") public ResponseEntity<ApiResponse<List<ReportResponse>>> skillReport() { return ResponseEntity.ok(ApiResponse.success(svc.getSkillReport())); }
    @GetMapping("/utilization") @PreAuthorize("hasAnyRole('ADMIN','AUDITOR','DELIVERY_MANAGER')") public ResponseEntity<ApiResponse<ReportResponse>> utilizationReport() { return ResponseEntity.ok(ApiResponse.success(svc.getUtilizationReport())); }
    @GetMapping("/project-allocation") @PreAuthorize("hasAnyRole('ADMIN','AUDITOR','DELIVERY_MANAGER')") public ResponseEntity<ApiResponse<List<ReportResponse>>> projectReport() { return ResponseEntity.ok(ApiResponse.success(svc.getProjectAllocationReport())); }
}
