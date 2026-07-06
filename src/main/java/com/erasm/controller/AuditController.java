package com.erasm.controller;
import com.erasm.dto.response.ApiResponse; import com.erasm.entity.AuditLog; import com.erasm.service.AuditService; import org.springframework.data.domain.Page; import org.springframework.data.domain.Pageable; import org.springframework.http.ResponseEntity; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/audit-logs") @PreAuthorize("hasAnyRole('ADMIN','AUDITOR')") public class AuditController {
    private final AuditService svc; public AuditController(AuditService s) { svc=s; }
    @GetMapping public ResponseEntity<ApiResponse<Page<AuditLog>>> getAll(@RequestParam(required=false) String entityType, @RequestParam(required=false) String action, Pageable p) {
        Page<AuditLog> logs; if(entityType!=null&&action!=null) logs=svc.getLogsByEntityTypeAndAction(entityType,action,p); else if(entityType!=null) logs=svc.getLogsByEntityType(entityType,p); else if(action!=null) logs=svc.getLogsByAction(action,p); else logs=svc.getAllLogs(p);
        return ResponseEntity.ok(ApiResponse.success(logs));
    }
    @GetMapping("/{id}") public ResponseEntity<ApiResponse<AuditLog>> getById(@PathVariable Long id) { return ResponseEntity.ok(ApiResponse.success(svc.getLogById(id))); }
}
