package com.erasm.service;
import com.erasm.entity.AuditLog; import com.erasm.repository.AuditLogRepository; import org.slf4j.Logger; import org.slf4j.LoggerFactory; import org.springframework.data.domain.Page; import org.springframework.data.domain.Pageable; import org.springframework.stereotype.Service; import java.time.LocalDateTime;

@Service public class AuditService {
    private static final Logger logger = LoggerFactory.getLogger(AuditService.class);
    private final AuditLogRepository repo;
    public AuditService(AuditLogRepository repo) { this.repo = repo; }
    public void log(String entityType, Long entityId, String action, String performedBy, String description) { repo.save(AuditLog.builder().entityType(entityType).entityId(entityId).action(action).performedBy(performedBy).description(description).timestamp(LocalDateTime.now()).build()); logger.info("Audit: [{}] {} on {} [ID: {}] by {}", action, description, entityType, entityId, performedBy); }
    public void log(String entityType, Long entityId, String action, String performedBy, String description, String ipAddress) { repo.save(AuditLog.builder().entityType(entityType).entityId(entityId).action(action).performedBy(performedBy).description(description).ipAddress(ipAddress).timestamp(LocalDateTime.now()).build()); }
    public Page<AuditLog> getAllLogs(Pageable p) { return repo.findAll(p); }
    public AuditLog getLogById(Long id) { return repo.findById(id).orElse(null); }
    public Page<AuditLog> getLogsByEntityType(String t, Pageable p) { return repo.findByEntityType(t, p); }
    public Page<AuditLog> getLogsByAction(String a, Pageable p) { return repo.findByAction(a, p); }
    public Page<AuditLog> getLogsByEntityTypeAndAction(String t, String a, Pageable p) { return repo.findByEntityTypeAndAction(t, a, p); }
}
