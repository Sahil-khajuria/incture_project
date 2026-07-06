package com.erasm.controller;
import com.erasm.dto.request.ProjectRequest; import com.erasm.dto.response.*; import com.erasm.service.ProjectService; import jakarta.validation.Valid; import org.springframework.data.domain.Page; import org.springframework.data.domain.Pageable; import org.springframework.http.*; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.security.core.Authentication; import org.springframework.web.bind.annotation.*; import java.util.List;

@RestController @RequestMapping("/projects") public class ProjectController {
    private final ProjectService svc; public ProjectController(ProjectService s) { svc=s; }
    @GetMapping public ResponseEntity<ApiResponse<Page<ProjectResponse>>> getAll(Pageable p) { return ResponseEntity.ok(ApiResponse.success(svc.getAllProjects(p))); }
    @GetMapping("/{id}") public ResponseEntity<ApiResponse<ProjectResponse>> getById(@PathVariable Long id) { return ResponseEntity.ok(ApiResponse.success(svc.getProjectById(id))); }
    @PostMapping @PreAuthorize("hasAnyRole('DELIVERY_MANAGER','ADMIN')") public ResponseEntity<ApiResponse<ProjectResponse>> create(@Valid @RequestBody ProjectRequest r, Authentication a) { return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(svc.createProject(r,a.getName()))); }
    @PutMapping("/{id}") @PreAuthorize("hasAnyRole('DELIVERY_MANAGER','ADMIN')") public ResponseEntity<ApiResponse<ProjectResponse>> update(@PathVariable Long id, @Valid @RequestBody ProjectRequest r) { return ResponseEntity.ok(ApiResponse.success(svc.updateProject(id,r))); }
    @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) { svc.softDeleteProject(id); return ResponseEntity.ok(ApiResponse.success("Deleted",null)); }
    @PutMapping("/{id}/close") @PreAuthorize("hasAnyRole('DELIVERY_MANAGER','ADMIN')") public ResponseEntity<ApiResponse<ProjectResponse>> close(@PathVariable Long id) { return ResponseEntity.ok(ApiResponse.success(svc.closeProject(id))); }
    @GetMapping("/{id}/allocations") @PreAuthorize("hasAnyRole('DELIVERY_MANAGER','RESOURCE_MANAGER','ADMIN')") public ResponseEntity<ApiResponse<List<AllocationResponse>>> allocs(@PathVariable Long id) { return ResponseEntity.ok(ApiResponse.success(svc.getProjectAllocations(id))); }
}
