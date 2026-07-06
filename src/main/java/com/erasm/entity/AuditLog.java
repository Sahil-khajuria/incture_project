package com.erasm.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "audit_logs") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuditLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "entity_type", nullable = false, length = 100) private String entityType;
    @Column(name = "entity_id") private Long entityId;
    @Column(nullable = false, length = 50) private String action;
    @Column(name = "performed_by", length = 150) private String performedBy;
    @Column(columnDefinition = "TEXT") private String description;
    @Column(name = "ip_address", length = 50) private String ipAddress;
    @Column @Builder.Default private LocalDateTime timestamp = LocalDateTime.now();
}
