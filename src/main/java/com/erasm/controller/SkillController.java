package com.erasm.controller;
import com.erasm.dto.request.SkillRequest; import com.erasm.dto.response.*; import com.erasm.service.SkillService; import jakarta.validation.Valid; import org.springframework.http.*; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*; import java.util.List;

@RestController @RequestMapping("/skills") public class SkillController {
    private final SkillService svc; public SkillController(SkillService s) { svc=s; }
    @GetMapping public ResponseEntity<ApiResponse<List<SkillResponse>>> getAll() { return ResponseEntity.ok(ApiResponse.success(svc.getAllSkills())); }
    @GetMapping("/{id}") public ResponseEntity<ApiResponse<SkillResponse>> getById(@PathVariable Long id) { return ResponseEntity.ok(ApiResponse.success(svc.getSkillById(id))); }
    @PostMapping @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<ApiResponse<SkillResponse>> create(@Valid @RequestBody SkillRequest r) { return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(svc.createSkill(r))); }
    @PutMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<ApiResponse<SkillResponse>> update(@PathVariable Long id, @Valid @RequestBody SkillRequest r) { return ResponseEntity.ok(ApiResponse.success(svc.updateSkill(id,r))); }
    @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) { svc.softDeleteSkill(id); return ResponseEntity.ok(ApiResponse.success("Deleted",null)); }
}
